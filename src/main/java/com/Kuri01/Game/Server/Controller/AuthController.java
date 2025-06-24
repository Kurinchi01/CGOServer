package com.Kuri01.Game.Server.Controller;

import com.Kuri01.Game.Server.Model.RPG.Repository.PlayerRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PlayerRepository playerRepository;
    private final GoogleTokenValidator googleTokenValidator;
    private final JwtService jwtService;

    // Konstruktor zum Injizieren der Services...

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
