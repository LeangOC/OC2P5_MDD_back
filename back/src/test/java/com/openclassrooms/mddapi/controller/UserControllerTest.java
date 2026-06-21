package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.exception.UserNotFoundException;
import com.openclassrooms.mddapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void createUser_ShouldReturnCreated() {

        UserDTO request = new UserDTO();
        request.setEmail("john@test.com");
        request.setUsername("john");
        request.setPassword("password");

        UserDTO created = new UserDTO();
        created.setId(1L);
        created.setEmail("john@test.com");
        created.setUsername("john");

        when(userService.createUser(
                "john@test.com",
                "john",
                "password"))
                .thenReturn(created);

        ResponseEntity<String> response =
                userController.createUser(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("New User created", response.getBody());
    }

    @Test
    void createUser_ShouldReturnInternalServerError() {

        UserDTO request = new UserDTO();
        request.setEmail("john@test.com");
        request.setUsername("john");
        request.setPassword("password");

        when(userService.createUser(
                anyString(),
                anyString(),
                anyString()))
                .thenReturn(null);

        ResponseEntity<String> response =
                userController.createUser(request);

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode());

        assertEquals(
                "Failed to create User",
                response.getBody());
    }

    @Test
    void getUserById_ShouldReturnUser() {

        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setUsername("john");

        when(userService.getUserById(1L))
                .thenReturn(user);

        ResponseEntity<UserDTO> response =
                userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void getUserById_ShouldThrowUserNotFoundException() {

        when(userService.getUserById(1L))
                .thenReturn(null);

        assertThrows(
                UserNotFoundException.class,
                () -> userController.getUserById(1L)
        );
    }

    @Test
    void deleteUser_ShouldReturnNoContent() {

        ResponseEntity<Void> response =
                userController.deleteUser(1L);

        assertEquals(
                HttpStatus.NO_CONTENT,
                response.getStatusCode());

        verify(userService)
                .deleteUser(1L);
    }

    @Test
    void getCurrentUser_ShouldReturnUser() {

        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setUsername("john");

        Authentication authentication =
                mock(Authentication.class);

        SecurityContext securityContext =
                mock(SecurityContext.class);

        when(authentication.getName())
                .thenReturn("john");

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        try (MockedStatic<SecurityContextHolder> mocked =
                     mockStatic(SecurityContextHolder.class)) {

            mocked.when(SecurityContextHolder::getContext)
                    .thenReturn(securityContext);

            when(userService.getUserByName("john"))
                    .thenReturn(user);

            ResponseEntity<UserDTO> response =
                    userController.getCurrentUser();

            assertEquals(
                    HttpStatus.OK,
                    response.getStatusCode());

            assertEquals(user, response.getBody());
        }
    }

    @Test
    void getCurrentUser_ShouldThrowUserNotFoundException() {

        Authentication authentication =
                mock(Authentication.class);

        SecurityContext securityContext =
                mock(SecurityContext.class);

        when(authentication.getName())
                .thenReturn("john");

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        try (MockedStatic<SecurityContextHolder> mocked =
                     mockStatic(SecurityContextHolder.class)) {

            mocked.when(SecurityContextHolder::getContext)
                    .thenReturn(securityContext);

            when(userService.getUserByName("john"))
                    .thenReturn(null);

            assertThrows(
                    UserNotFoundException.class,
                    () -> userController.getCurrentUser()
            );
        }
    }
}