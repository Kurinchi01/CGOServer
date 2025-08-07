package com.Kuri01.Game.Server.Controller;

import com.Kuri01.Game.Server.DTO.PlayerDTO;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Equipment;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.EquipmentSlotEnum;
import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Service.PlayerEquipmentService;
import com.Kuri01.Game.Server.Service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final PlayerEquipmentService equipmentService;
    private final PlayerService playerService;

    @Autowired
    public PlayerCharacterController(PlayerEquipmentService equipmentService, PlayerService playerService) {
        this.equipmentService = equipmentService;
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

    /**
     * Rüstet ein Item aus dem Inventar des Spielers aus.
     *
     * @param itemId         Die ID des auszurüstenden Items.
     * @param authentication Das Objekt des eingeloggten Spielers.
     * @return Das aktualisierte Equipment-Objekt des Spielers.
     */
    @PostMapping("/equip/{itemId}")
    public ResponseEntity<Equipment> equipItem(@PathVariable Long itemId, Authentication authentication) {
        Player loggedInPlayer = (Player) authentication.getPrincipal();
        log.info("Spieler '{}' versucht, Item {} auszurüsten.", loggedInPlayer.getName(), itemId);

        Equipment updatedEquipment = equipmentService.equipItem(loggedInPlayer.getId(), itemId);
        return ResponseEntity.ok(updatedEquipment);
    }

    /**
     * Legt ein Item von einem bestimmten Slot zurück ins Inventar.
     *
     * @param slot           Der Name des Slots, aus dem das Item entfernt werden soll (z.B. "WEAPON").
     * @param authentication Das Objekt des eingeloggten Spielers.
     * @return Das aktualisierte Equipment-Objekt des Spielers.
     */
    @PostMapping("/unequip/{slot}")
    public ResponseEntity<Equipment> unequipItem(@PathVariable String slot, Authentication authentication) {
        Player loggedInPlayer = (Player) authentication.getPrincipal();
        log.info("Spieler '{}' versucht, Item aus Slot {} abzulegen.", loggedInPlayer.getName(), slot);

        // Wandle den String aus der URL in unseren sicheren Enum-Typ um.
        EquipmentSlotEnum slotToUnequip = EquipmentSlotEnum.valueOf(slot.toUpperCase());

        Equipment updatedEquipment = equipmentService.unequipItem(loggedInPlayer.getId(), slotToUnequip);
        return ResponseEntity.ok(updatedEquipment);
    }
}
