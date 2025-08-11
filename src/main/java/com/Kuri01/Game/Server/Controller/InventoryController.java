package com.Kuri01.Game.Server.Controller;

// Oder später ein eigener InventoryService

import com.Kuri01.Game.Server.DTO.PlayerActionQueueDTO;
import com.Kuri01.Game.Server.DTOMapper.InventoryMapper;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Action.PlayerInventoryAction;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Item;
import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Repository.EquipmentRepository;
import com.Kuri01.Game.Server.Repository.InventoryRepository;
import com.Kuri01.Game.Server.Repository.PlayerRepository;
import com.Kuri01.Game.Server.Service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * REST-Controller für alle Aktionen, die das Inventar und das Equipment betreffen eines Spielers betreffen.
 */
@Slf4j
/// <--- erzeugt ein Logger der mit log angesprochen werden kann
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    // Für den Anfang kann der GameService diese Logik noch enthalten.
    // Später könnte man sie in einen eigenen InventoryService auslagern.
    private final InventoryService inventoryService;


    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Endpunkt zum Öffnen einer Loot-Truhe aus dem Inventar eines Spielers.
     * Reagiert auf POST-Anfragen an z.B. /api/inventory/open-chest/55
     *
     * @param inventoryChestId Die ID der Truhe im Inventar des Spielers.
     * @return Ein ResponseEntity, das eine Liste der erhaltenen Items enthält.
     */
    @PostMapping("/open-chest/{inventoryChestId}")
    public ResponseEntity<List<Item>> openChest(@PathVariable Long inventoryChestId) {
        // TODO: Spieler-ID aus der Authentifizierung holen
        Long playerId = 1L; // Platzhalter

        List<Item> finalLoot = new ArrayList<>();
        return ResponseEntity.ok(finalLoot);
    }


    @PostMapping("/action/inventory")
    public ResponseEntity<?> receiveInventoryActions(@RequestBody PlayerActionQueueDTO actions, Authentication authentication) {

        try {
            Player loggedInPlayer = (Player) authentication.getPrincipal();

            inventoryService.processPlayerActions(actions, loggedInPlayer);

            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Der Spieler wurde nicht gefunden!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
