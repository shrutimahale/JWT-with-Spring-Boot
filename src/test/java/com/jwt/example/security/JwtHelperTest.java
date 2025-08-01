package com.jwt.example.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

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
    void testGetUsernameFromToken(){
        String token = jwtHelper.generateToken(userDetails);
        String userName = jwtHelper.getUsernameFromToken(token);

        assertEquals("testuser", userName);
    }

    @Test
    void testGetExpirationDateFromToken(){
        String token = jwtHelper.generateToken(userDetails);
        Date exprirationdate = jwtHelper.getExpirationDateFromToken(token);

        assertNotNull(exprirationdate);
        assertTrue(exprirationdate.after(new Date()));
    }

    @Test
    void testIsTokenExpired() throws Exception{
        String token = jwtHelper.generateToken(userDetails);
       Method isExpiredMethod = JWTHelper.class.getDeclaredMethod("isTokenExpired", String.class);
       isExpiredMethod.setAccessible(true);

       Boolean isExpired = (Boolean) isExpiredMethod.invoke(jwtHelper, token);

       assertNotNull(isExpired);
       assertFalse(isExpired , "Token should not be expired immediately after generation");
    }

    @Test
    void testValidationToken(){
        String token = jwtHelper.generateToken(userDetails);
        Boolean isValid = jwtHelper.validToken(token, userDetails);

        assertTrue(isValid);
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
