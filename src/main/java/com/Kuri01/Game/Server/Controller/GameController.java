package com.Kuri01.Game.Server.Controller;

import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Item;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.LootResult;
import com.Kuri01.Game.Server.Model.RoundEndRequest;
import com.Kuri01.Game.Server.Model.RoundStartData;
import com.Kuri01.Game.Server.Service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-Controller, der alle API-Endpunkte für die Kern-Spielmechanik (Runden) verwaltet.
 * Alle Endpunkte unter diesem Controller haben das Basis-Präfix "/api/rounds".
 */
@RestController
@RequestMapping("/api/rounds")
public class GameController {

    // Die einzige Abhängigkeit des Controllers ist die Service-Schicht.
    private final GameService gameService;

    /**
     * Konstruktor-basierte Dependency Injection. Dies ist der empfohlene Weg,
     * um Abhängigkeiten in Spring zu injizieren.
     * @param gameService Der GameService wird vom Spring-Kontext automatisch bereitgestellt.
     */
    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Endpunkt zum Starten einer neuen Spielrunde für ein bestimmtes Kapitel.
     * Reagiert auf POST-Anfragen an z.B. /api/rounds/start/1
     * @param chapterId Die ID des Kapitels, die aus dem URL-Pfad gelesen wird.
     * @return Ein ResponseEntity, das im Erfolgsfall die Startdaten der Runde (RoundStartData)
     * und den HTTP-Status 200 (OK) enthält. Bei Fehlern werden entsprechende
     * HTTP-Fehlercodes zurückgegeben.
     */
    @PostMapping("/start/{chapterId}")
    public ResponseEntity<RoundStartData> startNewRound(@PathVariable Long chapterId) {
        try {
            RoundStartData roundData = gameService.createNewRound(chapterId);
            return ResponseEntity.ok(roundData);
        } catch (IllegalArgumentException e) {
            // Dieser Fehler wird geworfen, wenn die chapterId ungültig ist.
            // HTTP 404 Not Found ist hier die semantisch korrekte Antwort.
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            // Dieser Fehler wird geworfen, wenn z.B. der Monster-Pool leer ist.
            // Das ist ein serverseitiges Konfigurationsproblem.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpunkt zum Beenden einer Spielrunde.
     * Reagiert auf POST-Anfragen an z.B. /api/rounds/abc-123-def-456/end
     * @param roundId Die einzigartige ID der Runde.
     * @param request Der Request-Body, der das Ergebnis der Runde enthält.
     * @return Ein ResponseEntity, das im Falle eines Sieges eine Liste der erhaltenen Truhen (als Items) enthält.
     */
    @PostMapping("/{roundId}/end")
    public ResponseEntity<?> endRound(@PathVariable String roundId, @RequestBody RoundEndRequest request) {
        try {
            // TODO: Spieler-ID aus der Authentifizierung holen
            Long playerId = 1L; // Platzhalter
            List<Item> rewardedChests = gameService.processRoundEnd(playerId, roundId, request);
            return ResponseEntity.ok(rewardedChests);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}