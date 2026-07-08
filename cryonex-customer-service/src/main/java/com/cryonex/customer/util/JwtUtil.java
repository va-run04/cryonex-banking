package com.cryonex.customer.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "cryonexbankingsecretkeymustbeatleast256bitslong123456";
    private static final long EXPIRATION_TIME = 86400000;
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

   public String generateToken(String username, String role){

       return Jwts.builder()
               .subject(username)
               .claim("role", role)
               .issuedAt(new Date())
               .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
               .signWith(key)
               .compact();
   }

   private Claims extractAllClaims(String token){
       return Jwts.parser()
               .verifyWith(key)
               .build()
               .parseSignedClaims(token)
               .getPayload();
   }

   private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
       Claims claims = extractAllClaims(token);
       return claimsResolver.apply(claims);
   }

   public String extractUserName(String token){
       return extractClaim(token, Claims::getSubject);
   }

   public String extractRole(String token){
       return extractClaim(token, claims -> claims.get("role", String.class));
   }

   public Date extractExpiration(String token){
       return extractClaim(token, Claims::getExpiration);
   }

   public boolean isTokenExpired(String token) {
       return extractExpiration(token).before(new Date());
   }

   public boolean validateToken(String token){
       try{
           extractAllClaims(token);
           return !isTokenExpired(token);
       }catch (Exception e){
           return false;
       }
   }




}
