package com.Kuri01.Game.Server.Controller;

import com.Kuri01.Game.Server.Auth.GoogleLoginRequest;
import com.Kuri01.Game.Server.Auth.LoginResponse;
import com.Kuri01.Game.Server.Model.GoogleUserProfile;
import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Model.RPG.Repository.PlayerRepository;
import com.Kuri01.Game.Server.Service.GoogleTokenValidator;
import com.Kuri01.Game.Server.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PlayerRepository playerRepository;
    private final GoogleTokenValidator googleTokenValidator;
    private final JwtService jwtService;

    @Autowired
    public AuthController(PlayerRepository playerRepository,
                          GoogleTokenValidator googleTokenValidator,
                          JwtService jwtService) {
        this.playerRepository = playerRepository;
        this.googleTokenValidator = googleTokenValidator;
        this.jwtService = jwtService;
    }
    /**
     * Verarbeitet einen Login-Versuch mit einem Google ID Token.
     * Validiert das Token, findet oder erstellt einen Spielerdatensatz
     * und gibt ein internes JWT für die Sitzung zurück.
     *
     * @param request Das Anfrageobjekt, das das Google-Token enthält.
     * @return Ein ResponseEntity, das bei Erfolg ein JWT enthält oder bei einem Fehler
     * einen entsprechenden HTTP-Statuscode.
     */
    @PostMapping("/login/google")
    public ResponseEntity<LoginResponse> loginWithGoogle(@RequestBody GoogleLoginRequest request) {
        // 1. Validiere das Google Token
        Optional<GoogleUserProfile> profileOpt = googleTokenValidator.validate(request.googleToken());
        if (profileOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        GoogleUserProfile profile = profileOpt.get();

        // 2. Finde oder erstelle den Spieler in der Datenbank
        Player player = playerRepository.findByGoogleId(profile.googleId())
                .orElseGet(() -> {
                    // Spieler existiert nicht -> erstelle einen neuen
                    Player newPlayer = new Player();
                    newPlayer.setGoogleId(profile.googleId());
                    newPlayer.setName(profile.name());
                    // Setze Startwerte
                    newPlayer.setLevel(1);
                    newPlayer.setExperiencePoints(0);
                    return playerRepository.save(newPlayer);
                });

        // 3. Erstelle unser eigenes JWT für den Spieler
        String jwt = jwtService.generateToken(player);

        // 4. Sende das JWT an den Client zurück
        return ResponseEntity.ok(new LoginResponse(jwt));
    }
}
