package com.Kuri01.Game.Server.Controller;

 // Oder später ein eigener InventoryService
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Item;
import com.Kuri01.Game.Server.Service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-Controller für alle Aktionen, die das Inventar eines Spielers betreffen.
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    // Für den Anfang kann der GameService diese Logik noch enthalten.
    // Später könnte man sie in einen eigenen InventoryService auslagern.
    private final GameService gameService;

    @Autowired
    public InventoryController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Endpunkt zum Öffnen einer Loot-Truhe aus dem Inventar eines Spielers.
     * Reagiert auf POST-Anfragen an z.B. /api/inventory/open-chest/55
     * @param inventoryChestId Die ID der Truhe im Inventar des Spielers.
     * @return Ein ResponseEntity, das eine Liste der erhaltenen Items enthält.
     */
    @PostMapping("/open-chest/{inventoryChestId}")
    public ResponseEntity<List<Item>> openChest(@PathVariable Long inventoryChestId) {
        // TODO: Spieler-ID aus der Authentifizierung holen
        Long playerId = 1L; // Platzhalter

        List<Item> finalLoot = gameService.openChest(playerId, inventoryChestId);
        return ResponseEntity.ok(finalLoot);
    }
}
