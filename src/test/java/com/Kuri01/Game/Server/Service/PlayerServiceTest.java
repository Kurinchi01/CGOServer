package com.Kuri01.Game.Server.Service;

import com.Kuri01.Game.Server.DTO.InventorySlotDTO;
import com.Kuri01.Game.Server.DTO.PlayerDTO;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.EquipmentSlotEnum;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Inventory;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Item;
import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Repository.ItemRepository;
import com.Kuri01.Game.Server.Repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Aktiviert Mockito für diesen Test
class PlayerServiceTest {

    @Mock // Erstellt einen Mock für das Repository. Es wird keine echte DB verwendet.
    private PlayerRepository playerRepository;
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks // Erstellt eine ECHTE Instanz des PlayerService und injiziert die Mocks (@Mock) hinein.
    private PlayerService playerService;

    private Player testPlayer;
    private Item sword;
    private Item tmp;
    private List<Item> itemList = new ArrayList<>();

    // Diese Methode wird vor JEDEM Test ausgeführt, um saubere Testdaten zu erstellen.
    @BeforeEach
    void setUp() {
        // Erstelle ein komplexes, "lebendiges" Test-Player-Objekt
        testPlayer = new Player();
        testPlayer.setId(1L);
        testPlayer.setGoogleId("google-123");
        testPlayer.setName("TestSpieler");
        testPlayer.setLevel(5);
        testPlayer.setMaxHp(150f);
        testPlayer.setAttack(20f);

        // Füge ein ausgerüstetes Item hinzu
        sword = new Item();
        sword.setId(101L);
        sword.setName("Stahlschwert");
        sword.setIconName("sword_steel");
        sword.setEquipmentSlotEnum(EquipmentSlotEnum.WEAPON);
        sword.getStats().put("ATTACK", 15);

        testPlayer.getEquipment().setItemInSlot(EquipmentSlotEnum.WEAPON, sword);

        // Füge Items zum Inventar hinzu
        Inventory inventory = testPlayer.getInventory();


        tmp = new Item();
        tmp.setId(202L);
        tmp.setName("Basic Helmet");
        tmp.setIconName("BasicHelmet");
        tmp.getStats().put("DEF", 5);
        tmp.setEquipmentSlotEnum(EquipmentSlotEnum.HELMET);

        // Lege den Trank auf Platz 3 mit der Menge 5
        inventory.getSlots().get(5).setItem(tmp);
        itemList.add(tmp);
        itemList.add(sword);
    }

    @Test
    void getPlayerProfile_shouldMapEntityToDtoCorrectly() {
        // ========== ARRANGE (Vorbereiten) ==========

        // Programmiere den Mock: Wenn findByGoogleId aufgerufen wird, gib unseren Test-Spieler zurück.
        when(playerRepository.findByGoogleId("google-123")).thenReturn(Optional.of(testPlayer));
        when(itemRepository.findById(101L)).thenReturn(Optional.of(sword));
        when(itemRepository.findById(202L)).thenReturn(Optional.of(tmp));
        when(itemRepository.findAll()).thenReturn(itemList);

        // ========== ACT (Ausführen) ==========

        // Rufe die zu testende Methode auf.
        PlayerDTO resultDTO = playerService.getPlayerProfile("google-123");

        // ========== ASSERT (Überprüfen) ==========

        // 1. Überprüfe die Basis-Daten des Spielers
        assertNotNull(resultDTO);
        assertEquals(1L, resultDTO.getId());
        assertEquals("TestSpieler", resultDTO.getName());
        assertEquals(5, resultDTO.getLevel());
        assertEquals(150f, resultDTO.getMaxHp());

        // 2. Überprüfe das Equipment
        assertNotNull(resultDTO.getEquipmentDTO());
        assertNotNull(resultDTO.getEquipmentDTO().getEquipmentSlots().get(EquipmentSlotEnum.WEAPON));
        Item searchedItem = itemRepository.findById(resultDTO.getEquipmentDTO().getEquipmentSlots().get(EquipmentSlotEnum.WEAPON).getItemID()).orElseThrow();

        assertEquals("Stahlschwert", searchedItem.getName());
        assertEquals(15, searchedItem.getStats().get("ATTACK"));

        assertNull(resultDTO.getEquipmentDTO().getEquipmentSlots().get(EquipmentSlotEnum.HELMET).getItemID(), "Helm-Slot sollte leer sein.");

        // 3. Überprüfe das Inventar
        assertNotNull(resultDTO.getInventoryDTO().getInventorySlots());
        // Der Mapper sollte nur belegte Slots umwandeln

        // Überprüfe das eine Item im Detail

        InventorySlotDTO inventoryItemDTO = resultDTO.getInventoryDTO().getInventorySlots().get(5);
        searchedItem = itemRepository.findById(resultDTO.getInventoryDTO().getInventorySlots().get(5).getItemID()).orElseThrow();

        assertEquals(202L, searchedItem.getId());
        assertEquals("Basic Helmet", searchedItem.getName());
        assertEquals(1, inventoryItemDTO.getQuantity());
        assertEquals(EquipmentSlotEnum.HELMET, searchedItem.getEquipmentSlotEnum());
        assertEquals(5, searchedItem.getStats().get("DEF"));
        assertEquals(5, inventoryItemDTO.getSlotIndex());

        // Wichtig: Der Slot-Index muss korrekt gemappt werden
        // Annahme: Ihr ItemDTO hat ein slotIndex-Feld, das vom InventorySlotDTO kommt
        // assertEquals(3, inventoryItemDTO.getSlotIndex());
    }
}