package com.openclassrooms.mddapi.configuration;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;

    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {

        jwtTokenUtil = new JwtTokenUtil();

        ReflectionTestUtils.setField(
                jwtTokenUtil,
                "jwtSecret",
                "mySecretKeyForUnitTests"
        );

        User user = new User();
        user.setId(1L);
        user.setUsername("john");
        user.setEmail("john@test.com");
        user.setPassword("password");

        userDetails = new CustomUserDetails(user);
    }

    @Test
    void generateToken_ShouldGenerateToken() {

        String token =
                jwtTokenUtil.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void verifyToken_ShouldDecodeToken() {

        String token =
                jwtTokenUtil.generateToken(userDetails);

        DecodedJWT decodedJWT =
                jwtTokenUtil.verifyToken(token);

        assertNotNull(decodedJWT);

        assertEquals(
                "john@test.com",
                decodedJWT.getSubject()
        );
    }

    @Test
    void getExpirationDateFromToken_ShouldReturnExpirationDate() {

        String token =
                jwtTokenUtil.generateToken(userDetails);

        Date expiration =
                jwtTokenUtil.getExpirationDateFromToken(token);

        assertNotNull(expiration);

        assertTrue(
                expiration.after(new Date())
        );
    }

    @Test
    void validateToken_ShouldReturnTrue_WhenTokenIsValid() {

        String token =
                jwtTokenUtil.generateToken(userDetails);

        boolean result =
                jwtTokenUtil.validateToken(
                        token,
                        userDetails,
                        "john@test.com"
                );

        assertTrue(result);
    }

    @Test
    void validateToken_ShouldReturnFalse_WhenLoginDoesNotMatch() {

        String token =
                jwtTokenUtil.generateToken(userDetails);

        boolean result =
                jwtTokenUtil.validateToken(
                        token,
                        userDetails,
                        "wrong@test.com"
                );

        assertFalse(result);
    }

    @Test
    void verifyToken_ShouldThrowException_WhenTokenIsInvalid() {

        assertThrows(
                Exception.class,
                () -> jwtTokenUtil.verifyToken("invalid-token")
        );
    }
}