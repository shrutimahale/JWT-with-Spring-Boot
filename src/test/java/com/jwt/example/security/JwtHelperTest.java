package com.jwt.example.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.MalformedJwtException;

@ExtendWith(MockitoExtension.class)
public class JwtHelperTest {

    @InjectMocks
    private JWTHelper jwtHelper;

    private UserDetails userDetails;

    @BeforeEach
    void setUp(){
        userDetails = new User("testuser", "password", new ArrayList<>());
    }

    @Test
    void testGenerateToken(){
        String token = jwtHelper.generateToken(userDetails);

        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    void testInvalidToken(){
        String invalidToken = "invalid.token.here";
        assertThrows(MalformedJwtException.class, () -> {
            jwtHelper.getUsernameFromToken(invalidToken);
        });
    }

    @Test
    void testNullToken(){
        assertThrows(IllegalArgumentException.class, () -> {
            jwtHelper.getUsernameFromToken(null);
        });
    }

}
