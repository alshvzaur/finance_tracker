package com.zaur.finance_tracker.controller;

import com.zaur.finance_tracker.dto.AuthResponse;
import com.zaur.finance_tracker.dto.LoginRequest;
import com.zaur.finance_tracker.dto.RegisterRequest;
import com.zaur.finance_tracker.model.Role;
import com.zaur.finance_tracker.model.User;
import com.zaur.finance_tracker.security.JwtUtil;
import com.zaur.finance_tracker.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/auth")
public class AuthController {

    UserService userService;
    JwtUtil jwtUtil;
    AuthenticationManager authenticationManager;
    PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest registerRequest) {
        User user = User.builder()
                .username(registerRequest.getUsername())
                .name(registerRequest.getName())
                .surname(registerRequest.getSurname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();

        User registeredUser = userService.saveUser(user);
        AuthResponse authResponse = new AuthResponse(jwtUtil.generateToken(registeredUser.getUsername()
                , registeredUser.getId().toString()
                , registeredUser.getRole().name()));

        return authResponse;
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername()
                , loginRequest.getPassword()
        ));

        User user = userService.findUserByUsername(loginRequest.getUsername());
        AuthResponse authResponse = new AuthResponse(jwtUtil.generateToken(user.getUsername()
                , user.getId().toString()
                , user.getRole().toString()));

        return authResponse;
    }

}
