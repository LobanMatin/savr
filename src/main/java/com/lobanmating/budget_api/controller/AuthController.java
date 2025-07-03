package com.lobanmating.budget_api.controller;

import com.lobanmating.budget_api.dto.UserRequest;
import com.lobanmating.budget_api.security.JwtUtil;
import com.lobanmating.budget_api.service.CustomUserDetailsService;
import com.lobanmating.budget_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody UserRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        return jwtUtil.generateToken(userDetails.getUsername());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest request) {
        userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
