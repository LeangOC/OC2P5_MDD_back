package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.configuration.JwtTokenUtil;
import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.model.JwtRequest;
import com.openclassrooms.mddapi.model.JwtResponse;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.CustomUserDetails;
import com.openclassrooms.mddapi.service.JwtUserDetailsService;
import com.openclassrooms.mddapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtUserDetailsService userDetailsService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private AuthenticationController controller;

    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(
                controller,
                "userService",
                userService);

        ReflectionTestUtils.setField(
                controller,
                "userDetailsService",
                userDetailsService);

        ReflectionTestUtils.setField(
                controller,
                "jwtTokenUtil",
                jwtTokenUtil);

        User user = new User();
        user.setId(1L);
        user.setEmail("john@test.com");
        user.setUsername("john");
        user.setPassword("password");

        userDetails = new CustomUserDetails(user);
    }

    @Test
    void createUser_ShouldReturnJwtToken() {

        UserDTO dto = new UserDTO();
        dto.setEmail("john@test.com");
        dto.setUsername("john");
        dto.setPassword("password");

        when(userDetailsService.loadUserByUsername("john@test.com"))
                .thenReturn(userDetails);

        when(jwtTokenUtil.generateToken(userDetails))
                .thenReturn("jwt-token");

        ResponseEntity<?> response = controller.createUser(dto);

        assertEquals(200, response.getStatusCode().value());

        JwtResponse jwtResponse = (JwtResponse) response.getBody();

        assertNotNull(jwtResponse);
        assertEquals("jwt-token", jwtResponse.getToken());

        verify(userDetailsService).save(any(User.class));
    }

    @Test
    void createAuthenticationToken_ShouldReturnJwtToken() throws Exception {

        JwtRequest request = new JwtRequest();

        ReflectionTestUtils.setField(request, "login", "john");
        ReflectionTestUtils.setField(request, "password", "password");

        when(userDetailsService.loadUserByLogin("john"))
                .thenReturn(userDetails);

        when(jwtTokenUtil.generateToken(userDetails))
                .thenReturn("jwt-token");

        ResponseEntity<?> response =
                controller.createAuthenticationToken(request);

        assertEquals(200, response.getStatusCode().value());

        JwtResponse jwtResponse = (JwtResponse) response.getBody();

        assertNotNull(jwtResponse);
        assertEquals("jwt-token", jwtResponse.getToken());

        verify(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void createAuthenticationToken_ShouldThrowInvalidCredentialsException() {

        JwtRequest request = new JwtRequest();

        ReflectionTestUtils.setField(request, "login", "john");
        ReflectionTestUtils.setField(request, "password", "wrong");

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        Exception exception = assertThrows(
                Exception.class,
                () -> controller.createAuthenticationToken(request)
        );

        assertTrue(
                exception.getMessage().contains("INVALID_CREDENTIALS")
        );
    }

    @Test
    void createAuthenticationToken_ShouldThrowUserDisabledException() {

        JwtRequest request = new JwtRequest();

        ReflectionTestUtils.setField(request, "login", "john");
        ReflectionTestUtils.setField(request, "password", "password");

        doThrow(new DisabledException("User disabled"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        Exception exception = assertThrows(
                Exception.class,
                () -> controller.createAuthenticationToken(request)
        );

        assertTrue(
                exception.getMessage().contains("USER_DISABLED")
        );
    }

    @Test
    void updateUser_ShouldReturnNewJwtToken() {

        UserDTO dto = new UserDTO();
        dto.setId(1L);
        dto.setEmail("new@test.com");
        dto.setUsername("newUser");

        when(userService.updateUser(
                1L,
                "new@test.com",
                "newUser"))
                .thenReturn(dto);

        when(userDetailsService.loadUserByLogin("new@test.com"))
                .thenReturn(userDetails);

        when(jwtTokenUtil.generateToken(userDetails))
                .thenReturn("new-jwt-token");

        ResponseEntity<?> response =
                controller.updateUser(1L, dto);

        assertEquals(200, response.getStatusCode().value());

        JwtResponse jwtResponse = (JwtResponse) response.getBody();

        assertNotNull(jwtResponse);
        assertEquals("new-jwt-token", jwtResponse.getToken());
    }
}