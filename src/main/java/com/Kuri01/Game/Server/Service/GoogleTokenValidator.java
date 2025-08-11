package com.Kuri01.Game.Server.Service;

import com.Kuri01.Game.Server.DTO.GoogleUserProfile;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // Dies ist die "echte" Service-Bean für die Produktion
public class GoogleTokenValidator {

    // Deine Google Client ID wird aus der application.properties geladen
    @Value("${app.google.client-id}")
    private String googleClientId;

    // Der Verifier von Google, der die eigentliche Arbeit macht
    private GoogleIdTokenVerifier verifier;

    // Diese Methode wird nach der Initialisierung aufgerufen, um den Verifier vorzubereiten
    @PostConstruct
    public void init() {
        // ... Code zum Initialisieren des GoogleIdTokenVerifier mit der Client ID ...
        // verifier = new GoogleIdTokenVerifier.Builder(...).build();
    }

    /**
     * Validiert ein Token, indem es eine ECHTE Anfrage an Google sendet.
     * @param tokenString Das Token vom Client.
     * @return Ein Optional mit den Profildaten bei Erfolg, sonst ein leeres Optional.
     */
    public Optional<GoogleUserProfile> validate(String tokenString) {
        try {
            // ECHTER NETZWERKAUFRUF an Google-Server!
            GoogleIdToken idToken = verifier.verify(tokenString);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String userId = payload.getSubject();
                String email = payload.getEmail();
                String name = (String) payload.get("name");

                // Erstelle und gib unser eigenes User-Profil-Objekt zurück
                return Optional.of(new GoogleUserProfile(userId, email, name));
            }
        } catch (Exception e) {
            // Logge den Fehler (z.B. bei Netzwerkproblemen oder ungültigem Token)
            return Optional.empty();
        }
        return Optional.empty();
    }
}
