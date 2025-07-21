package com.Kuri01.Game.Server.Controller;

import com.Kuri01.Game.Server.Auth.LoginResponse;
import com.Kuri01.Game.Server.Config.JwtAuthenticationFilter;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Inventory;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Item;
import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Model.RPG.Repository.ItemRepository;
import com.Kuri01.Game.Server.Model.RPG.Repository.PlayerRepository;
import com.Kuri01.Game.Server.Service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

// WICHTIG: Diese Annotation sorgt dafür, dass dieser Controller NUR im 'dev'-Profil existiert.
// In der Produktionsumgebung wird er einfach ignoriert und ist nicht erreichbar.
@Profile("dev")
@RestController
@RequestMapping("/api/auth/dev") // Eigener Pfad, um Verwechslungen zu vermeiden
public class DevAuthController {

    private final PlayerRepository playerRepository;
    private final JwtService jwtService;
    private final ItemRepository itemRepository;

    // Ein einfaches DTO für die Anfrage, kann als innere Klasse hier definiert werden.
    public record DevLoginRequest(String username) {}
    private static final Logger logger = LoggerFactory.getLogger(DevAuthController.class);

    @Autowired
    public DevAuthController(PlayerRepository playerRepository, JwtService jwtService, ItemRepository itemRepository) {
        this.playerRepository = playerRepository;
        this.jwtService = jwtService;
        this.itemRepository=itemRepository;
    }

    /**
     * Ein unsicherer Login-Endpunkt NUR FÜR ENTWICKLUNGSZWECKE.
     * Erlaubt das Einloggen mit einem beliebigen Benutzernamen.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> devLogin(@RequestBody DevLoginRequest request) {
        // Finde oder erstelle einen Spieler mit dem angegebenen Namen.
        Player player = playerRepository.findByName(request.username()) // Du musst findByName im Repo deklarieren
                .orElseGet(() -> {
                    logger.info("User '{}' nicht gefunden. erstelle User!", request.username());

                    Player newPlayer = new Player();
                    newPlayer.setName(request.username());
                    // Setze eine Fake-Google-ID, damit das Feld nicht leer ist
                    newPlayer.setGoogleId("dev-user-"+request.username());
                    newPlayer.setLevel(1);
                    newPlayer.getRoles().add("ROLE_USER");
                    newPlayer.getInventory().setCapacity(20);
                    newPlayer.getInventory().fillSlots();
                    Item tmpItem = itemRepository.findById(1L).orElse(null);
                    newPlayer.getInventory().setItemToSlot(0,tmpItem);
                    return playerRepository.save(newPlayer);
                });

        // Erstelle ein JWT für diesen Spieler.
        String jwt = jwtService.generateToken(player);

        return ResponseEntity.ok(new LoginResponse(jwt));
    }
}
