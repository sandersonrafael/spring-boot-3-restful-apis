package com.spring3.firstproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring3.firstproject.data.vo.v1.security.AccountCredentialsVO;
import com.spring3.firstproject.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

// Swagger tag
@Tag(name = "Authentication Endpoint")
@RestController()
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Authenticate a user and returns a token") // Swagger
    @PostMapping(value = "/signin")
    public ResponseEntity<?> signin(@RequestBody AccountCredentialsVO data) {
        if (checkIfParamsAreNull(data)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }

        ResponseEntity<?> token = authService.signin(data);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }

        return token;
    }

    @Operation(summary = "Refresh token for authenticated user and returns a token") // Swagger
    @PutMapping(value = "/refresh/{username}")
    public ResponseEntity<?> refreshToken(
        @PathVariable(value = "username") String username,
        @RequestHeader(value = "Authorization") String refreshToken
    ) {
        if (refreshToken == null || refreshToken.isBlank() || username == null || username.isBlank()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }

        ResponseEntity<?> token = authService.refreshToken(username, refreshToken);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");
        }
        return token;
    }

    private boolean checkIfParamsAreNull(AccountCredentialsVO data) {
        return data == null || data.getUsername() == null || data.getUsername().isBlank()
            || data.getPassword() == null || data.getPassword().isBlank();
    }
}
