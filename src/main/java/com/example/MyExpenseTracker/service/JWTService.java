package com.example.MyExpenseTracker.service;

import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTService {

    private static final String SECRET =
            "mySuperSecretKeyForJwtAuthentication123456789";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                SECRET.getBytes(StandardCharsets.UTF_8)
        );
    }


    /**
     * Generates a JWT token for the authenticated user.
     *
     * JWT Structure:
     * Header.Payload.Signature
     *
     * We store the user's email as the subject (sub claim).
     * The token is signed using our secret key so that nobody
     * can modify it and create fake tokens.
     */
    public String generateToken(String email) {

        return Jwts.builder()

                // Stores the identity of the logged-in user.
                // Example:
                // "sub": "prat@gmail.com"
                .subject(email)

                // Time when the token was generated.
                // Example:
                // "iat": 1751234567
                .issuedAt(new Date())

                // Token expiry time.
                // Current time + 1 hour.
                //
                // 1000 = 1 second in milliseconds
                // 60 = 1 minute
                // 60 = 1 hour
                //
                // After expiry, user must login again.
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 60
                        )
                )

                // Signs the token using our Secret Key.
                //
                // Purpose:
                // - Prevent token tampering
                // - Verify token was created by our server
                // - Reject fake JWTs created by attackers
                .signWith(getSigningKey())

                // Converts JWT Builder into a compact String token.
                //
                // Example output:
                // eyJhbGciOiJIUzI1NiJ9...
                .compact();
    }

    public String extractEmail(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}
