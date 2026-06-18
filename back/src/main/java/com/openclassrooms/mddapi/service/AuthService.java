package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(User user) {
        // Encodage du mot de passe avant sauvegarde
       user.setPassword( passwordEncoder.encode(user.getPassword()) );
       userRepository.save(user);

    }

    public User login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return null;
        }

        if (!passwordEncoder.matches( password, user.getPassword())) {
            return null;
        }

        return user;
    }
}
