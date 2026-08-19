package com.canteen.controller;

import com.canteen.dto.UserProfileResponse;
import com.canteen.security.UserPrincipal;
import com.canteen.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@Tag(name = "Admin User Management", description = "Admin User viewing, role management and activation APIs")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Search users with role filter and pagination")
    public ResponseEntity<Page<UserProfileResponse>> getAllUsers(
        @RequestParam(required = false) String query,
        @RequestParam(required = false) String role,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "15") int size,
        @RequestParam(defaultValue = "id,desc") String[] sort
    ) {
        String sortField = sort[0];
        Sort.Direction direction = sort.length > 1 && sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));

        return ResponseEntity.ok(userService.getAllUsers(query, role, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user details by ID")
    public ResponseEntity<UserProfileResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Toggle active/inactive status of a user account")
    public ResponseEntity<UserProfileResponse> toggleUserStatus(
        @AuthenticationPrincipal UserPrincipal currentUser,
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.toggleUserStatus(currentUser.getId(), id));
    }

    @PatchMapping("/{id}/role")
    @Operation(summary = "Update user role (CUSTOMER, ADMIN, STAFF)")
    public ResponseEntity<UserProfileResponse> updateUserRole(
        @AuthenticationPrincipal UserPrincipal currentUser,
        @PathVariable Long id,
        @RequestParam String role
    ) {
        return ResponseEntity.ok(userService.updateUserRole(currentUser.getId(), id, role));
    }
}
