package com.jwt.example.security;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@Component
public class JWTHelper {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    private String secretKey = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";
     SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

    //retrive username from jwt token
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    //get expirate date from token
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // claims from token
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // to reteive all infor from toke we need secret key
    private Claims getAllClaimsFromToken(String token){
        JwtParser parser = Jwts.parser().verifyWith(key).build();
        return parser.parseSignedClaims(token).getPayload();
    }

    //to check if token is expired
    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject){
       
        return Jwts.builder()
            .claims(claims)
            .subject(subject)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
            .signWith(key, Jwts.SIG.HS512)
            .compact();
    }

    public Boolean validToken(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    } 

}
