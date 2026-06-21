package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder bcryptEncoder;

    @InjectMocks
    private JwtUserDetailsService jwtUserDetailsService;

    @Test
    void loadUserByLogin_ShouldReturnUser_WhenEmailExists() {

        User user = new User();
        user.setId(1L);
        user.setEmail("john@test.com");
        user.setUsername("john");
        user.setPassword("password");

        when(userRepository.findByEmail("john@test.com"))
                .thenReturn(Optional.of(user));

        CustomUserDetails result =
                jwtUserDetailsService.loadUserByLogin("john@test.com");

        assertNotNull(result);
        assertEquals("john@test.com", result.getEmail());
        assertEquals("john", result.getUsername());

        verify(userRepository).findByEmail("john@test.com");
        verify(userRepository, never()).findByUsername(anyString());
    }

    @Test
    void loadUserByLogin_ShouldReturnUser_WhenUsernameExists() {

        User user = new User();
        user.setId(1L);
        user.setEmail("john@test.com");
        user.setUsername("john");
        user.setPassword("password");

        when(userRepository.findByEmail("john"))
                .thenReturn(Optional.empty());

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        CustomUserDetails result =
                jwtUserDetailsService.loadUserByLogin("john");

        assertNotNull(result);
        assertEquals("john", result.getUsername());

        verify(userRepository).findByEmail("john");
        verify(userRepository).findByUsername("john");
    }

    @Test
    void loadUserByLogin_ShouldThrowException_WhenUserNotFound() {

        when(userRepository.findByEmail("unknown"))
                .thenReturn(Optional.empty());

        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> jwtUserDetailsService.loadUserByLogin("unknown")
        );

        verify(userRepository).findByEmail("unknown");
        verify(userRepository).findByUsername("unknown");
    }

    @Test
    void loadUserByUsername_ShouldDelegateToLoadUserByLogin() {

        User user = new User();
        user.setId(1L);
        user.setEmail("john@test.com");
        user.setUsername("john");
        user.setPassword("password");

        when(userRepository.findByEmail("john"))
                .thenReturn(Optional.empty());

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        CustomUserDetails result =
                jwtUserDetailsService.loadUserByUsername("john");

        assertNotNull(result);
        assertEquals("john", result.getUsername());
    }

    @Test
    void save_ShouldEncodePasswordAndSaveUser() {

        User user = new User();
        user.setUsername("john");
        user.setPassword("plainPassword");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("john");
        savedUser.setPassword("encodedPassword");

        when(bcryptEncoder.encode("plainPassword"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        User result = jwtUserDetailsService.save(user);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("encodedPassword", user.getPassword());

        verify(bcryptEncoder).encode("plainPassword");
        verify(userRepository).save(user);
    }
}