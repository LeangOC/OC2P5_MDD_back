package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    private User user;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setEmail("john@test.com");
        user.setPassword("password");

        customUserDetails = new CustomUserDetails(user);
    }

    @Test
    void getAuthorities_ShouldReturnEmptyCollection() {

        Collection<? extends GrantedAuthority> authorities =
                customUserDetails.getAuthorities();

        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void getEmail_ShouldReturnUserEmail() {

        assertEquals(
                "john@test.com",
                customUserDetails.getEmail()
        );
    }

    @Test
    void getPassword_ShouldReturnUserPassword() {

        assertEquals(
                "password",
                customUserDetails.getPassword()
        );
    }

    @Test
    void getUsername_ShouldReturnUsername() {

        assertEquals(
                "john",
                customUserDetails.getUsername()
        );
    }

    @Test
    void isAccountNonExpired_ShouldReturnTrue() {

        assertTrue(customUserDetails.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked_ShouldReturnTrue() {

        assertTrue(customUserDetails.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired_ShouldReturnTrue() {

        assertTrue(customUserDetails.isCredentialsNonExpired());
    }

    @Test
    void isEnabled_ShouldReturnTrue() {

        assertTrue(customUserDetails.isEnabled());
    }
}