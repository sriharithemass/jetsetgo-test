package com.training.controllers;

import com.training.repositories.RoleRepository;
import com.training.repositories.UserRepository;
import com.training.security.jwt.JwtUtils;
import com.training.security.request.LoginRequest;
import com.training.security.request.SignupRequest;
import com.training.security.response.LoginResponse;
import com.training.security.response.MessageResponse;
import com.training.security.response.UserInfoResponse;
import com.training.services.UserService;
import com.training.services.impl.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserService userService;

    @PostMapping("/public/signin")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/public/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        MessageResponse response = authService.registerUser(signUpRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<UserInfoResponse> getUserDetails(@RequestHeader("username") String username) {
        UserInfoResponse response = authService.getUserDetails(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/username")
    public ResponseEntity<String> currentUserName(@RequestHeader("username") String username) {
        String response = authService.currentUserName(username);
        return ResponseEntity.ok(response);
    }
}
