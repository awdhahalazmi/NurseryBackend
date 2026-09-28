package com.iosProject.iosProject.controllers;

import com.iosProject.iosProject.bo.auth.AuthenticationResponse;
import com.iosProject.iosProject.bo.auth.CreateLoginRequest;
import com.iosProject.iosProject.bo.auth.CreateSignupRequest;
import com.iosProject.iosProject.bo.auth.LogoutResponse;
import com.iosProject.iosProject.service.auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/Signup")
    public ResponseEntity<String> createUser(@RequestBody CreateSignupRequest createSignupRequest) {
        log.info("Starting createUser");
        log.info("Calling authService signup");
        authService.signup(createSignupRequest);
        log.info("Completed createUser");
        return ResponseEntity.status(HttpStatus.CREATED).body("User Created");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody CreateLoginRequest createLoginRequest) {
        log.info("Starting login");
        log.info("Calling authService login");
        AuthenticationResponse response = authService.login(createLoginRequest);
        HttpStatus status = HttpStatus.OK;
        if (response == null) {
            status = HttpStatus.BAD_REQUEST;
        }
        log.info("Completed login");
        return new ResponseEntity<>(response, status);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutResponse logoutResponse) {
        authService.logout(logoutResponse);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
