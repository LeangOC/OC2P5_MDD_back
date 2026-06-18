package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.service.AuthService;
import com.openclassrooms.mddapi.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.mddapi.security.JwtService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @RequestBody User user) {

        User createdUser = authService.register(user);

        String token =
                jwtService.generateToken(
                        createdUser.getUsername());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {

        User authenticatedUser =
                authService.login(user.getEmail(), user.getPassword());

        if (authenticatedUser == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Email ou mot de passe incorrect");
        }
        String token = jwtService.generateToken( authenticatedUser.getUsername());
       // return ResponseEntity.ok(Connexion réussie : " + authenticatedUser.getUsername() );
        return ResponseEntity.ok(token);

    }
}