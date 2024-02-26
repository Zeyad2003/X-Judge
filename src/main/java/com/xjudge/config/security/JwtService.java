package com.xjudge.config.security;

import com.xjudge.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.security-key}")
    String SECRET_KEY;

    public String extractByUserHandle(String token){
        return extractClaim(token , Claims::getSubject);
    }

    public String generateToken(User userDetails){
        return generateToken(new HashMap<>() , userDetails);
    }

    public String generateToken(Map<String , Object> extraClaims , User userDetails){
        return Jwts
                .builder()
                .setClaims(extraClaims) // add claims
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis())) // create time
                .setExpiration(new Date(System.currentTimeMillis() * 1000 * 60 * 24)) // expiration time
                .signWith(getSingingKey() , SignatureAlgorithm.HS256) // sign key to create signature
                .compact();
    }

    public boolean isTokenValid(String token , UserDetails userDetails){
        String userEmail = extractByUserHandle(token);
        return (userEmail.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = extractClaim(token , Claims::getExpiration);
        return expirationDate.before(new Date());
    }


    public <T> T extractClaim (String jwt , Function<Claims , T> extractResolver){
        Claims claims = extractClaims(jwt);
        return extractResolver.apply(claims);
    }

    private Claims extractClaims(String jwt){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSingingKey()) // add singing key
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private Key getSingingKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
