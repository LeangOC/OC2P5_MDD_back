package com.openclassrooms.mddapi.configuration;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.CustomUserDetails;
import com.openclassrooms.mddapi.service.JwtUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {

    @Mock
    private JwtUserDetailsService jwtUserDetailsService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private DecodedJWT decodedJWT;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {

        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setEmail("john@test.com");
        user.setPassword("password");

        userDetails = new CustomUserDetails(user);

        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_ShouldContinue_WhenAuthorizationHeaderIsMissing()
            throws Exception {

        when(request.getHeader("Authorization"))
                .thenReturn(null);

        jwtRequestFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        assertNull(
                SecurityContextHolder.getContext().getAuthentication()
        );
    }

    @Test
    void doFilterInternal_ShouldContinue_WhenHeaderDoesNotStartWithBearer()
            throws Exception {

        when(request.getHeader("Authorization"))
                .thenReturn("Basic test");

        when(request.getMethod())
                .thenReturn("GET");

        when(request.getRequestURI())
                .thenReturn("/api/test");

        jwtRequestFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        assertNull(
                SecurityContextHolder.getContext().getAuthentication()
        );
    }

    @Test
    void doFilterInternal_ShouldAuthenticateUser_WhenTokenIsValid()
            throws Exception {

        String token = "validToken";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        when(jwtTokenUtil.verifyToken(token))
                .thenReturn(decodedJWT);

        when(decodedJWT.getSubject())
                .thenReturn("john@test.com");

        when(jwtUserDetailsService.loadUserByLogin("john@test.com"))
                .thenReturn(userDetails);

        when(jwtTokenUtil.validateToken(
                token,
                userDetails,
                "john@test.com"))
                .thenReturn(true);

        jwtRequestFilter.doFilter(request, response, filterChain);

        assertNotNull(
                SecurityContextHolder.getContext().getAuthentication()
        );

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ShouldNotAuthenticate_WhenTokenValidationFails()
            throws Exception {

        String token = "invalidToken";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        when(jwtTokenUtil.verifyToken(token))
                .thenReturn(decodedJWT);

        when(decodedJWT.getSubject())
                .thenReturn("john@test.com");

        when(jwtUserDetailsService.loadUserByLogin("john@test.com"))
                .thenReturn(userDetails);

        when(jwtTokenUtil.validateToken(
                token,
                userDetails,
                "john@test.com"))
                .thenReturn(false);

        jwtRequestFilter.doFilter(request, response, filterChain);

        assertNull(
                SecurityContextHolder.getContext().getAuthentication()
        );

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ShouldHandleExceptionFromVerifyToken()
            throws Exception {

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer invalid");

        when(jwtTokenUtil.verifyToken("invalid"))
                .thenThrow(new RuntimeException("JWT error"));

        assertDoesNotThrow(() ->
                jwtRequestFilter.doFilter(request, response, filterChain)
        );

        verify(filterChain).doFilter(request, response);

        assertNull(
                SecurityContextHolder.getContext().getAuthentication()
        );
    }

    @Test
    void doFilterInternal_ShouldSkipAuthentication_WhenSubjectIsNull()
            throws Exception {

        String token = "token";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        when(jwtTokenUtil.verifyToken(token))
                .thenReturn(decodedJWT);

        when(decodedJWT.getSubject())
                .thenReturn(null);

        jwtRequestFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        assertNull(
                SecurityContextHolder.getContext().getAuthentication()
        );
    }

    @Test
    void doFilterInternal_ShouldSkipAuthentication_WhenAlreadyAuthenticated()
            throws Exception {

        SecurityContextHolder.getContext().setAuthentication(
                mock(org.springframework.security.core.Authentication.class)
        );

        String token = "token";

        when(request.getHeader("Authorization"))
                .thenReturn("Bearer " + token);

        when(jwtTokenUtil.verifyToken(token))
                .thenReturn(decodedJWT);

        when(decodedJWT.getSubject())
                .thenReturn("john@test.com");

        jwtRequestFilter.doFilter(request, response, filterChain);

        verify(jwtUserDetailsService, never())
                .loadUserByLogin(anyString());

        verify(filterChain).doFilter(request, response);
    }
}