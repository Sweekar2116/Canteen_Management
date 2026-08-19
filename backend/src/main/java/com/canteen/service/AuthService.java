package com.canteen.service;

import com.canteen.dto.AuthResponse;
import com.canteen.dto.LoginRequest;
import com.canteen.dto.RegisterRequest;
import com.canteen.entity.Cart;
import com.canteen.entity.Role;
import com.canteen.entity.User;
import com.canteen.exception.BadRequestException;
import com.canteen.repository.CartRepository;
import com.canteen.repository.RoleRepository;
import com.canteen.repository.UserRepository;
import com.canteen.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuditLogService auditLogService;

    public AuthService(
        AuthenticationManager authenticationManager,
        UserRepository userRepository,
        RoleRepository roleRepository,
        CartRepository cartRepository,
        PasswordEncoder passwordEncoder,
        JwtTokenProvider tokenProvider,
        AuditLogService auditLogService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already in use: " + request.getEmail());
        }

        User user = new User(
            request.getName(),
            request.getEmail(),
            request.getPhone(),
            passwordEncoder.encode(request.getPassword())
        );

        Role customerRole = roleRepository.findByName(Role.RoleName.CUSTOMER)
            .orElseGet(() -> roleRepository.save(new Role(Role.RoleName.CUSTOMER)));

        user.setRoles(Collections.singleton(customerRole));
        User savedUser = userRepository.save(user);

        // Initialize empty shopping cart for user
        Cart cart = new Cart(savedUser);
        cartRepository.save(cart);

        // Authenticate & issue token
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        List<String> roles = savedUser.getRoles().stream()
            .map(r -> r.getName().name())
            .collect(Collectors.toList());

        auditLogService.log(savedUser.getId(), "USER_REGISTER", "User", savedUser.getId(), "Registered new customer account");

        return new AuthResponse(jwt, savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getPhone(), roles);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BadRequestException("User not found"));

        if (!user.isEnabled()) {
            throw new BadRequestException("User account is disabled");
        }

        // Ensure cart exists
        if (cartRepository.findByUserId(user.getId()).isEmpty()) {
            cartRepository.save(new Cart(user));
        }

        List<String> roles = user.getRoles().stream()
            .map(r -> r.getName().name())
            .collect(Collectors.toList());

        auditLogService.log(user.getId(), "USER_LOGIN", "User", user.getId(), "User logged in successfully");

        return new AuthResponse(jwt, user.getId(), user.getName(), user.getEmail(), user.getPhone(), roles);
    }
}
