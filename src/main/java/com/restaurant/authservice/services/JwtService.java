package com.restaurant.authservice.services;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    public String secretKey;



    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claims != null ? claimsResolver.apply(claims) : null ;
    }

    public SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }



    private Claims extractAllClaims(String token){
        try{
            return Jwts
                    .parser()
                    .verifyWith(getSignInKey())
                    .setAllowedClockSkewSeconds(30)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        }catch (ExpiredJwtException e){
            return e.getClaims();
        }catch(JwtException e){
            return null;
        }
    }


    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username != null && username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }


    private boolean isTokenExpired(String token){
        Date expiration = extraExpiration(token);
        return expiration != null && expiration.before(new Date());
    }


    private Date extraExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }


}
