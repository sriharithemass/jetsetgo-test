package com.training.controllers;

import com.training.security.request.LoginRequest;
import com.training.security.request.SignupRequest;
import com.training.security.response.LoginResponse;
import com.training.security.response.MessageResponse;
import com.training.security.response.UserInfoResponse;
import com.training.services.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateUser() {
        LoginRequest loginRequest = new LoginRequest();
        LoginResponse loginResponse = new LoginResponse();
        ResponseEntity<LoginResponse> expectedResponse = ResponseEntity.ok(loginResponse);

        when(authService.authenticateUser(loginRequest)).thenReturn(loginResponse);

        ResponseEntity<LoginResponse> response = authController.authenticateUser(loginRequest);

        assertEquals(expectedResponse, response);
    }

    @Test
    void testRegisterUser() {
        SignupRequest signupRequest = new SignupRequest();
        MessageResponse messageResponse = new MessageResponse("User registered successfully!");
        ResponseEntity<MessageResponse> expectedResponse = ResponseEntity.ok(messageResponse);

        when(authService.registerUser(signupRequest)).thenReturn(messageResponse);

        ResponseEntity<MessageResponse> response = authController.registerUser(signupRequest);

        assertEquals(expectedResponse, response);
    }

    @Test
    void testGetUserDetails() {
        String username = "testUser";
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        ResponseEntity<UserInfoResponse> expectedResponse = ResponseEntity.ok(userInfoResponse);

        when(authService.getUserDetails(username)).thenReturn(userInfoResponse);

        ResponseEntity<UserInfoResponse> response = authController.getUserDetails(username);

        assertEquals(expectedResponse, response);
    }

    @Test
    void testCurrentUserName() {
        String username = "testUser";
        ResponseEntity<String> expectedResponse = ResponseEntity.ok(username);

        when(authService.currentUserName(username)).thenReturn(username);

        ResponseEntity<String> response = authController.currentUserName(username);

        assertEquals(expectedResponse, response);
    }
}