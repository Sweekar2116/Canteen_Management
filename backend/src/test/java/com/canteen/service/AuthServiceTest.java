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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private AuthService authService;

    private User sampleUser;
    private Role customerRole;

    @BeforeEach
    void setUp() {
        customerRole = new Role("CUSTOMER");
        customerRole.setId(1L);

        sampleUser = new User("Rahul Sharma", "rahul@example.com", "9876543210", "encodedPass");
        sampleUser.setId(3L);
        sampleUser.setRoles(Set.of(customerRole));
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest req = new RegisterRequest();
        req.setName("Rahul Sharma");
        req.setEmail("rahul@example.com");
        req.setPassword("customer123");
        req.setPhone("9876543210");

        when(userRepository.existsByEmail("rahul@example.com")).thenReturn(false);
        when(passwordEncoder.encode("customer123")).thenReturn("encodedPass");
        when(roleRepository.findByName("CUSTOMER")).thenReturn(Optional.of(customerRole));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        when(cartRepository.save(any(Cart.class))).thenReturn(new Cart(sampleUser));
        when(tokenProvider.generateToken(any())).thenReturn("sample-jwt-token");

        AuthResponse res = authService.register(req);

        assertNotNull(res);
        assertEquals("rahul@example.com", res.getEmail());
        assertEquals("sample-jwt-token", res.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterDuplicateEmailThrows() {
        RegisterRequest req = new RegisterRequest();
        req.setEmail("rahul@example.com");
        when(userRepository.existsByEmail("rahul@example.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.register(req));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginSuccess() {
        LoginRequest req = new LoginRequest("rahul@example.com", "customer123");
        Authentication auth = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(tokenProvider.generateToken(auth)).thenReturn("sample-jwt-token");
        when(userRepository.findByEmail("rahul@example.com")).thenReturn(Optional.of(sampleUser));

        AuthResponse res = authService.login(req);

        assertNotNull(res);
        assertEquals("sample-jwt-token", res.getToken());
        assertEquals("Rahul Sharma", res.getName());
    }
}
