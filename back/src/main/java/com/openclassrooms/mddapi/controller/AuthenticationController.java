package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.configuration.JwtTokenUtil;
import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.model.JwtRequest;
import com.openclassrooms.mddapi.model.JwtResponse;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.CustomUserDetails;
import com.openclassrooms.mddapi.service.JwtUserDetailsService;
import com.openclassrooms.mddapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


/**
 * Contrôleur responsable de l'authentification
 * et de la gestion du compte utilisateur.
 *
 * <p>
 * Il permet :
 * </p>
 * <ul>
 *     <li>l'inscription d'un utilisateur ;</li>
 *     <li>la connexion ;</li>
 *     <li>la génération de jetons JWT ;</li>
 *     <li>la mise à jour du profil utilisateur.</li>
 * </ul>
 *
 * @author LCH
 * @since 1.0
 */
@RestController
@CrossOrigin
@Tag(name = "Authentification", description = "Authentification et gestion des comptes")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Inscrit un nouvel utilisateur.
     *
     * @param userDTO informations du compte
     * @return jeton JWT valide
     */
    @Operation(summary = "Inscription utilisateur")
    @PostMapping("/api/auth/register")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDTO userDTO) {


        User user = User.createNewUser(userDTO.getEmail(), userDTO.getUsername(), userDTO.getPassword());


        userDetailsService.save(user);


        final CustomUserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());


        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));

    }

    /**
     * Authentifie un utilisateur à partir de son identifiant
     * et de son mot de passe.
     *
     * @param authenticationRequest informations de connexion
     * @return jeton JWT valide
     * @throws Exception si l'authentification échoue
     */
    @Operation(summary = "Connexion utilisateur")
    @PostMapping("/api/auth/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getLogin(), authenticationRequest.getPassword());


        final CustomUserDetails userDetails = userDetailsService
                .loadUserByLogin(authenticationRequest.getLogin());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }


    private void authenticate(String login, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }


    /**
     * Met à jour les informations d'un utilisateur.
     *
     * @param id identifiant utilisateur
     * @param userDTO nouvelles informations
     * @return nouveau jeton JWT
     */
    @Operation(summary = "Modifier un profil utilisateur")
    @PutMapping("/api/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUserDTO = userService.updateUser(id, userDTO.getEmail(), userDTO.getUsername());


        final CustomUserDetails userDetails = userDetailsService.loadUserByLogin(updatedUserDTO.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));

    }
}
