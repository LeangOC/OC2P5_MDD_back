package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public UserDTO getUserByName(String name) {
        Optional<User> user = userRepository.findByUsername(name);
        return user.map(this::toDTO).orElse(null);
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(user.getId(),user.getEmail(), user.getUsername());
    }

}
