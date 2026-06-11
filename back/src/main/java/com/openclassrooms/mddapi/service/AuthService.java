package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public void register(User user) {
       userRepository.save(user);

    }
    public boolean login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElse(null);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(password);
    }

}
