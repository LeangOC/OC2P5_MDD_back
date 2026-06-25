package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
/**
 * Implémentation du chargement des utilisateurs utilisé
 * par Spring Security lors de l'authentification.
 *
 * <p>
 * Cette classe fait le lien entre le système d'authentification
 * Spring Security et la base de données applicative.
 * </p>
 *
 * @author LCH
 * @since 1.0
 */
@Service
public class JwtUserDetailsService
        implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    public CustomUserDetails loadUserByLogin(String login) throws UsernameNotFoundException {

        Optional<User> userByEmail = userRepository.findByEmail(login);
        if (userByEmail.isPresent()) {
            return new CustomUserDetails(userByEmail.get());
        }

        // puis, si encore là, on essaye avec le userName
        Optional<User> userByUsername = userRepository.findByUsername(login);
        if (userByUsername.isPresent()) {
            return new CustomUserDetails(userByUsername.get());
        }

        throw new UsernameNotFoundException("User not found with login: " + login);
    }



    /**
     * Recherche un utilisateur à partir de son identifiant.
     *
     * <p>
     * Cette méthode est appelée automatiquement par Spring Security
     * lors d'une tentative d'authentification.
     * </p>
     *
     * @param userName identifiant utilisateur (email)
     *
     * @return informations nécessaires à Spring Security
     *
     * @throws UsernameNotFoundException
     * si aucun utilisateur n'existe
     */
    @Override
    public CustomUserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return loadUserByLogin(userName);
    }


    public User save(User user) {
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


}
