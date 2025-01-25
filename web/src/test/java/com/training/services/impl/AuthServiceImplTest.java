package com.training.services.impl;

import com.training.config.AppConstants;
import com.training.exception.APIException;
import com.training.models.AppRole;
import com.training.models.Role;
import com.training.models.User;
import com.training.repositories.RoleRepository;
import com.training.repositories.UserRepository;
import com.training.security.jwt.JwtUtils;
import com.training.security.request.LoginRequest;
import com.training.security.request.SignupRequest;
import com.training.security.response.LoginResponse;
import com.training.security.response.MessageResponse;
import com.training.security.response.UserInfoResponse;
import com.training.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateUser() {
        LoginRequest loginRequest = new LoginRequest("testUser", "password");
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());
        when(jwtUtils.generateTokenFromUsername(userDetails)).thenReturn("jwtToken");

        LoginResponse response = authService.authenticateUser(loginRequest);

        assertEquals("testUser", response.getUsername());
        assertEquals("jwtToken", response.getJwtToken());
    }

    @Test
    void testAuthenticateUser_InvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest("testUser", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Invalid credentials") {});

        APIException exception = assertThrows(APIException.class, () -> {
            authService.authenticateUser(loginRequest);
        });

        assertEquals(AppConstants.INVALID_CREDENTIALS, exception.getMessage());
    }

    @Test
    void testRegisterUser() {
        SignupRequest signUpRequest = new SignupRequest("testUser", "test@example.com", Set.of("user"), "password");
        User user = new User("testUser", "test@example.com", "encodedPassword");
        Role role = new Role(AppRole.ROLE_USER);

        when(userRepository.existsByUserName("testUser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(encoder.encode("password")).thenReturn("encodedPassword");
        when(roleRepository.findByRoleName(AppRole.ROLE_USER)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        MessageResponse response = authService.registerUser(signUpRequest);

        assertEquals("User registered successfully!", response.getMessage());
    }

    @Test
    void testRegisterUser_UsernameTaken() {
        SignupRequest signUpRequest = new SignupRequest("testUser", "test@example.com", Set.of("user"), "password");

        when(userRepository.existsByUserName("testUser")).thenReturn(true);

        APIException exception = assertThrows(APIException.class, () -> {
            authService.registerUser(signUpRequest);
        });

        assertEquals("Error: Username is already taken!", exception.getMessage());
    }

    @Test
    void testRegisterUser_EmailTaken() {
        SignupRequest signUpRequest = new SignupRequest("testUser", "test@example.com", Set.of("user"), "password");

        when(userRepository.existsByUserName("testUser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        APIException exception = assertThrows(APIException.class, () -> {
            authService.registerUser(signUpRequest);
        });

        assertEquals("Error: Email is already in use!", exception.getMessage());
    }

    @Test
    void testGetUserDetails() {
        User user = new User("testUser", "test@example.com", "password");
        user.setUserId(1L);
        Role role = new Role(AppRole.ROLE_USER);
        user.setRole(role);

        when(userService.findByUsername("testUser")).thenReturn(user);

        UserInfoResponse response = authService.getUserDetails("testUser");

        assertEquals(1L, response.getId());
        assertEquals("testUser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals(AppRole.ROLE_USER.toString(), response.getRole());
    }

    @Test
    void testCurrentUserName() {
        when(userRepository.existsByUserName("testUser")).thenReturn(true);

        String result = authService.currentUserName("testUser");

        assertEquals("testUser", result);
    }

    @Test
    void testCurrentUserName_UserNotFound() {
        when(userRepository.existsByUserName("testUser")).thenReturn(false);

        APIException exception = assertThrows(APIException.class, () -> {
            authService.currentUserName("testUser");
        });

        assertEquals(AppConstants.USER_NOT_FOUND, exception.getMessage());
    }
}