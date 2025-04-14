package com.jwt.example.security;

import org.springframework.stereotype.Component;

@Component
public class JWTHelper {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    

}
