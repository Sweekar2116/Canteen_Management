package com.canteen.service;

import com.canteen.dto.ChangePasswordRequest;
import com.canteen.dto.UpdateProfileRequest;
import com.canteen.dto.UserProfileResponse;
import com.canteen.entity.Role;
import com.canteen.entity.User;
import com.canteen.exception.BadRequestException;
import com.canteen.exception.ResourceNotFoundException;
import com.canteen.repository.RoleRepository;
import com.canteen.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;

    public UserService(
        UserRepository userRepository,
        RoleRepository roleRepository,
        PasswordEncoder passwordEncoder,
        AuditLogService auditLogService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditLogService = auditLogService;
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(Long userId) {
        User user = getUserById(userId);
        return mapToResponse(user);
    }

    @Transactional
    public UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = getUserById(userId);
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        User updated = userRepository.save(user);

        auditLogService.log(userId, "UPDATE_PROFILE", "User", userId, "User updated profile details");
        return mapToResponse(updated);
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = getUserById(userId);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException("Current password does not match");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        auditLogService.log(userId, "CHANGE_PASSWORD", "User", userId, "User changed password");
    }

    @Transactional(readOnly = true)
    public Page<UserProfileResponse> getAllUsers(String query, String roleName, Pageable pageable) {
        Role role = null;
        if (roleName != null && !roleName.isBlank()) {
            role = roleRepository.findByName(Role.RoleName.valueOf(roleName.toUpperCase()))
                .orElse(null);
        }
        return userRepository.searchUsers(query, role, pageable)
            .map(this::mapToResponse);
    }

    @Transactional
    public UserProfileResponse toggleUserStatus(Long adminId, Long targetUserId) {
        User user = getUserById(targetUserId);
        user.setEnabled(!user.isEnabled());
        User updated = userRepository.save(user);

        auditLogService.log(adminId, "TOGGLE_USER_STATUS", "User", targetUserId,
            "User active status changed to " + updated.isEnabled());

        return mapToResponse(updated);
    }

    @Transactional
    public UserProfileResponse updateUserRole(Long adminId, Long targetUserId, String newRoleName) {
        User user = getUserById(targetUserId);
        Role role = roleRepository.findByName(Role.RoleName.valueOf(newRoleName.toUpperCase()))
            .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + newRoleName));

        user.getRoles().clear();
        user.getRoles().add(role);
        User updated = userRepository.save(user);

        auditLogService.log(adminId, "UPDATE_USER_ROLE", "User", targetUserId,
            "User role updated to " + newRoleName);

        return mapToResponse(updated);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private UserProfileResponse mapToResponse(User user) {
        return new UserProfileResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.isEnabled(),
            user.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toList()),
            user.getCreatedAt()
        );
    }
}
