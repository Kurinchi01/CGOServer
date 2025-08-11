package com.Kuri01.Game.Server.Controller;

import com.Kuri01.Game.Server.DTO.RPG.PlayerDTO;
import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST-Controller für alle Aktionen, die den Spielercharakter direkt betreffen,
 * wie das Abrufen von Profildaten oder das Managen der Ausrüstung.
 */
@RestController
@Slf4j /// <--- erzeugt ein Logger der mit log angesprochen werden kann
@RequestMapping("/api/character")
public class PlayerCharacterController   {


    private final PlayerService playerService;

    @Autowired
    public PlayerCharacterController( PlayerService playerService) {

        this.playerService = playerService;
    }

    /**
     * Gibt das vollständige Profil des aktuell eingeloggten Spielers zurück.
     * Ideal für den Charakter-Bildschirm im Client.
     *
     * @param authentication Wird von Spring Security automatisch mit den Daten des eingeloggten Benutzers befüllt.
     * @return Ein ResponseEntity mit dem vollständigen Player-Objekt.
     */
    @GetMapping("/me")
    public ResponseEntity<PlayerDTO> getMyPlayerData(Authentication authentication) {
        // Da wir unsere Player-Klasse UserDetails implementieren lassen haben,
        // können wir sie direkt aus dem "Principal"-Objekt der Authentifizierung casten.
        Player loggedInPlayer = (Player) authentication.getPrincipal();

        log.info("Spielerprofil für '{}' angefragt.", loggedInPlayer.getName());


        // Kein extra Datenbank-Aufruf nötig! Wir haben bereits das vollständige Spieler-Objekt.
        return ResponseEntity.ok(playerService.getPlayerProfile(loggedInPlayer.getGoogleId()));
    }


}
