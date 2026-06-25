package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.exception.UserAlreadyExistsException;
import com.openclassrooms.mddapi.exception.UserNotFoundException;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service métier responsable de la gestion des utilisateurs.
 *
 * <p>
 * Cette classe gère :
 * </p>
 *
 * <ul>
 *     <li>la création des comptes utilisateurs ;</li>
 *     <li>la récupération des profils ;</li>
 *     <li>la modification des informations utilisateur ;</li>
 *     <li>la suppression des comptes.</li>
 * </ul>
 *
 * @author LCH
 * @since 1.0
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /**
     * Crée un nouveau compte utilisateur.
     *
     * <p>
     * Le mot de passe est chiffré avant stockage en base.
     * </p>
     *
     * @return utilisateur créé
     */
    public UserDTO createUser(String email, String username, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("An account with this email already exists");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("An account with this username already exists");
        }

        User user = User.createNewUser(email, username, password);
        User savedUser = userRepository.save(user);
        return toDTO(savedUser);
    }


    public UserDTO getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(this::toDTO).orElse(null);
    }

    public UserDTO getUserByName(String name) {
        Optional<User> user = userRepository.findByUsername(name);
        return user.map(this::toDTO).orElse(null);
    }

    /**
     * Met à jour les informations personnelles d'un utilisateur.
     *
     * @param id identifiant utilisateur
     *
     * @return utilisateur modifié
     */
    public UserDTO updateUser(Long id, String email, String username) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: " + id);
        }

        User user = existingUser.get();
        user.setEmail(email);
        user.setUsername(username);
        userRepository.save(user);
        return toDTO(user);
    }

    /**
     * Supprime un compte utilisateur.
     *
     * @param id identifiant utilisateur
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(user.getId(),user.getEmail(), user.getUsername(), user.getPassword());
    }


}
