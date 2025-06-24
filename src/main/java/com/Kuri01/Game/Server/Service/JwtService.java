package com.Kuri01.Game.Server.Service;

import com.Kuri01.Game.Server.Model.RPG.Player;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secretKey;

    private final long validityInMilliseconds = 3600000 * 24; // 24 Stunden

    public String generateToken(Player player) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(player.getGoogleId()) // Wir speichern die Google ID im Token
                .claim("name", player.getName())
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    // ... Methoden zum Validieren des Tokens, die Spring Security später braucht ...
}
