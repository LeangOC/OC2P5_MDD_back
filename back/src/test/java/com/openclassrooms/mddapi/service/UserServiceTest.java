package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.exception.UserAlreadyExistsException;
import com.openclassrooms.mddapi.exception.UserNotFoundException;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_ShouldCreateUser_WhenEmailAndUsernameAreAvailable() {

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("john@test.com");
        savedUser.setUsername("john");
        savedUser.setPassword("password");

        when(userRepository.findByEmail("john@test.com"))
                .thenReturn(Optional.empty());

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.empty());

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        UserDTO result = userService.createUser(
                "john@test.com",
                "john",
                "password"
        );

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("john@test.com", result.getEmail());
        assertEquals("john", result.getUsername());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailAlreadyExists() {

        User existingUser = new User();

        when(userRepository.findByEmail("john@test.com"))
                .thenReturn(Optional.of(existingUser));

        assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.createUser(
                        "john@test.com",
                        "john",
                        "password"
                )
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_ShouldThrowException_WhenUsernameAlreadyExists() {

        User existingUser = new User();

        when(userRepository.findByEmail("john@test.com"))
                .thenReturn(Optional.empty());

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(existingUser));

        assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.createUser(
                        "john@test.com",
                        "john",
                        "password"
                )
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserById_ShouldReturnUserDTO_WhenUserExists() {

        User user = new User();
        user.setId(1L);
        user.setEmail("john@test.com");
        user.setUsername("john");
        user.setPassword("password");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("john@test.com", result.getEmail());
    }

    @Test
    void getUserById_ShouldReturnNull_WhenUserDoesNotExist() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        UserDTO result = userService.getUserById(1L);

        assertNull(result);
    }

    @Test
    void getUserByName_ShouldReturnUserDTO_WhenUserExists() {

        User user = new User();
        user.setId(1L);
        user.setEmail("john@test.com");
        user.setUsername("john");
        user.setPassword("password");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        UserDTO result = userService.getUserByName("john");

        assertNotNull(result);
        assertEquals("john", result.getUsername());
    }

    @Test
    void getUserByName_ShouldReturnNull_WhenUserDoesNotExist() {

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.empty());

        UserDTO result = userService.getUserByName("john");

        assertNull(result);
    }

    @Test
    void updateUser_ShouldUpdateUser_WhenUserExists() {

        User user = new User();
        user.setId(1L);
        user.setEmail("old@test.com");
        user.setUsername("oldName");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserDTO result = userService.updateUser(
                1L,
                "new@test.com",
                "newName"
        );

        assertNotNull(result);
        assertEquals("new@test.com", result.getEmail());
        assertEquals("newName", result.getUsername());

        verify(userRepository).save(user);
    }

    @Test
    void updateUser_ShouldThrowException_WhenUserNotFound() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser(
                        1L,
                        "new@test.com",
                        "newName"
                )
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_ShouldCallRepository() {

        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
}