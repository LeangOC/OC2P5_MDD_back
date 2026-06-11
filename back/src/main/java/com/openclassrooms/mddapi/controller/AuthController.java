package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.service.AuthService;
import com.openclassrooms.mddapi.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
    authService.register(user);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body("Utilisateur créé avec succès");
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

        return ResponseEntity.ok(
                "Connexion réussie : " + authenticatedUser.getUsername()
        );
    }
}