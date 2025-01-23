package com.training.controllers;

import com.training.repositories.RoleRepository;
import com.training.repositories.UserRepository;
import com.training.security.jwt.JwtUtils;
import com.training.security.request.LoginRequest;
import com.training.security.request.SignupRequest;
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
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/public/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@RequestHeader("username") String username) {
        return authService.getUserDetails(username);
    }

    @GetMapping("/username")
    public String currentUserName(@RequestHeader("username") String username) {
        return authService.currentUserName(username);
    }
}
