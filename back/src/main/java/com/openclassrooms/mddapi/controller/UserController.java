package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.exception.UserNotFoundException;
import com.openclassrooms.mddapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST responsable de la gestion
 * des utilisateurs.
 *
 * @author LCH
 * @since 1.0
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "Utilisateurs", description = "Gestion des utilisateurs")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Crée un nouvel utilisateur.
     *
     * @param userDTO informations du compte
     * @return message de confirmation
     */
    @Operation(summary = "Créer un utilisateur")
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUserDTO = userService.createUser(userDTO.getEmail(), userDTO.getUsername(), userDTO.getPassword());

        if (createdUserDTO != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("New User created");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create User");
        }
    }

    /**
     * Recherche un utilisateur par son identifiant.
     *
     * @param id identifiant utilisateur
     * @return utilisateur trouvé
     */
    @Operation(summary = "Consulter un utilisateur")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        if (userDTO == null) {
            throw new UserNotFoundException("User not found with id: " + id); // Exception personnalisée
        } else {
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }
    }



    /**
     * Supprime un utilisateur.
     *
     * @param id identifiant utilisateur
     * @return réponse HTTP vide
     */
    @Operation(summary = "Supprimer un utilisateur")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Retourne les informations de l'utilisateur actuellement authentifié.
     *
     * @return profil de l'utilisateur connecté
     */
    @Operation(summary = "Profil utilisateur connecté")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserDTO userDTO = userService.getUserByName(username);

        if (userDTO == null) {
            throw new UserNotFoundException("User not found with userName: " + username); // Exception personnalisée
        } else {
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }
    }
}
