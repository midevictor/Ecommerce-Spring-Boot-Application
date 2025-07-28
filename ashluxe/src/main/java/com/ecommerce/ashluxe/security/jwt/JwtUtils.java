package com.ecommerce.ashluxe.security.jwt;

import com.ecommerce.ashluxe.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs ;
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret; // secret key for signing JWT

    @Value("${spring.ecom.app.jwtCookieName}")
    private String jwtCookie;
    //Getting JWT from the request header
//    public String getJJwtFromHeader(HttpServletRequest request){
//        String bearerToken = request.getHeader("Authorization");
//        logger.debug("Authorization Header: {}", bearerToken);
//        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }

    //Generating JWT from Cookies
    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
//            System.out.println("COOKIE:" + cookie.getValue());
            return  cookie.getValue();
        } else{
            return null;
        }

    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal){
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt)
                .path("/api")
                .maxAge(24 * 60 * 60)
                .httpOnly(false)
                .build();

        return cookie;
    }

    public ResponseCookie getCleanJwtCookie(){
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null)
                .path("/api")
                .build();

        return cookie;
    }

    //Generating Token from Username
    public String generateTokenFromUsername(String username){
//        String username = userDetails.getUsername();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime() + jwtExpirationMs)))
                .signWith(key())
                .compact();

    }

    //Getting Username form JWTToken
    public String getUserNameFromJWTToken(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }
    //Generate Signing Key
    public Key key() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }
    //Validating JWT Token
    public boolean validateJwtToken(String authToken) {
        try {
            System.out.println("Validate");
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("JWT Token is invalid: {}", e.getMessage());
        } catch (ExpiredJwtException e){
            logger.error("JWT Token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT Token validation failed: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("JWT validation failed: {}", e.getMessage());
        }
        return false;
    }
}
