package com.Kuri01.Game.Server.Service;

import com.Kuri01.Game.Server.DTO.RPG.PlayerActionQueueDTO;
import com.Kuri01.Game.Server.DTOMapper.InventoryMapper;
import com.Kuri01.Game.Server.Exceptions.SwapFailException;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.*;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Action.EquipInventoryAction;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Action.PlayerInventoryAction;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Action.SwapSlotInventoryAction;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Action.UnequipInventoryAction;
import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Repository.RPG.EquipmentRepository;
import com.Kuri01.Game.Server.Repository.RPG.InventoryRepository;
import com.Kuri01.Game.Server.Repository.RPG.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    // Erstelle Mocks für alle Abhängigkeiten des InventoryService
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private EquipmentRepository equipmentRepository;
    @Mock
    private InventoryMapper inventoryMapper;

    // Erstelle eine ECHTE Instanz des zu testenden Service und injiziere die Mocks
    @InjectMocks
    private InventoryService inventoryService;

    // Testdaten, die in jedem Test verwendet werden
    private Player testPlayer;
    private Item sword;
    private Item potion;
    private Item helmet;

    @BeforeEach
    void setUp() {
        // Erstelle einen sauberen Zustand für jeden Test
        testPlayer = new Player();
        testPlayer.setId(1L);

        // Erstelle Item-Blaupausen
        sword = new Item();
        sword.setId(101L);
        sword.setName("Schwert");
        sword.setEquipmentSlotEnum(EquipmentSlotEnum.WEAPON); // Wichtig: Typ setzen

        helmet = new Item();
        helmet.setId(102L);
        helmet.setName("Helm");
        helmet.setEquipmentSlotEnum(EquipmentSlotEnum.HELMET); // Wichtig: Typ setzen

        potion = new Item();
        potion.setId(201L);
        potion.setName("Trank");

        // Richte das Inventar des Spielers ein
        Inventory inventory = testPlayer.getInventory();
        inventory.getSlots().get(2).setItem(sword); // Schwert auf Platz 2
        inventory.getSlots().get(7).setItem(potion); // Trank auf Platz 7

    }
    @Test
    void processPlayerActions_withValidEquipAction_shouldMoveItemToEquipment() {
        // ARRANGE: Simuliere das Ausrüsten des Schwertes von Slot 2
        InventorySlot sourceClient = testPlayer.getInventory().getSlots().get(2);
        EquipmentSlot targetClient = testPlayer.getEquipment().getEquipmentSlots().get(EquipmentSlotEnum.WEAPON);
        EquipInventoryAction equipAction = new EquipInventoryAction(sourceClient, targetClient);
        List<PlayerInventoryAction> actions = List.of(equipAction);

        when(inventoryMapper.createPlayerInventoryActionListFromDTO(any(), eq(testPlayer))).thenReturn(actions);
        when(inventoryRepository.findByPlayer(testPlayer)).thenReturn(Optional.of(testPlayer.getInventory()));
        when(equipmentRepository.findByPlayer(testPlayer)).thenReturn(Optional.of(testPlayer.getEquipment()));

        // ACT
        inventoryService.processPlayerActions(new PlayerActionQueueDTO(), testPlayer);

        // ASSERT
        assertNull(testPlayer.getInventory().getSlots().get(2).getItem(), "Inventar-Slot 2 sollte jetzt leer sein.");
        assertEquals(sword, testPlayer.getEquipment().getItemInSlot(EquipmentSlotEnum.WEAPON), "Waffen-Slot sollte jetzt das Schwert enthalten.");
    }

    @Test
    void processPlayerActions_withValidUnequipAction_shouldMoveItemToInventory() {
        // ARRANGE: Simuliere das Ablegen des Helms in den leeren Inventarplatz 5
        // Modifiziere den Start-Zustand: Der Spieler hat den Helm bereits an.
        testPlayer.getEquipment().setItemInSlot(EquipmentSlotEnum.HELMET, helmet);

        EquipmentSlot sourceClient = testPlayer.getEquipment().getEquipmentSlots().get(EquipmentSlotEnum.HELMET);
        InventorySlot targetClient = testPlayer.getInventory().getSlots().get(5);
        UnequipInventoryAction unequipAction = new UnequipInventoryAction(sourceClient, targetClient);
        List<PlayerInventoryAction> actions = List.of(unequipAction);

        when(inventoryMapper.createPlayerInventoryActionListFromDTO(any(), eq(testPlayer))).thenReturn(actions);
        when(inventoryRepository.findByPlayer(testPlayer)).thenReturn(Optional.of(testPlayer.getInventory()));
        when(equipmentRepository.findByPlayer(testPlayer)).thenReturn(Optional.of(testPlayer.getEquipment()));

        // ACT
        inventoryService.processPlayerActions(new PlayerActionQueueDTO(), testPlayer);

        // ASSERT
        assertNull(testPlayer.getEquipment().getItemInSlot(EquipmentSlotEnum.HELMET), "Helm-Slot sollte jetzt leer sein.");
        assertEquals(helmet, testPlayer.getInventory().getSlots().get(5).getItem(), "Inventar-Slot 5 sollte jetzt den Helm enthalten.");
    }

    @Test
    void processPlayerActions_withValidSwapAction_shouldSwapItemsSuccessfully() {
        // ========== ARRANGE (Vorbereiten) ==========

        // 1. Simuliere die DTO-Anfrage vom Client
        PlayerActionQueueDTO actionsDTO = new PlayerActionQueueDTO(); // Angenommen, diese Klasse existiert

        // 2. Simuliere die "übersetzte" Action vom Mapper
        // Der Client will Slot 2 (Schwert) mit dem leeren Slot 10 tauschen
        InventorySlot sourceSlotFromClient = new InventorySlot(2);
        sourceSlotFromClient.setItem(sword);
        InventorySlot targetSlotFromClient = new InventorySlot(10);
        targetSlotFromClient.setItem(null);

        SwapSlotInventoryAction swapAction = new SwapSlotInventoryAction(sourceSlotFromClient, targetSlotFromClient);
        List<PlayerInventoryAction> actionList = List.of(swapAction);

        // 3. Programmiere das Verhalten der Mocks
        when(inventoryMapper.createPlayerInventoryActionListFromDTO(actionsDTO, testPlayer)).thenReturn(actionList);
        when(inventoryRepository.findByPlayer(testPlayer)).thenReturn(Optional.of(testPlayer.getInventory()));
        when(equipmentRepository.findByPlayer(testPlayer)).thenReturn(Optional.of(testPlayer.getEquipment()));

        // ========== ACT (Ausführen) ==========
        // Führe die zu testende Methode aus. Erwarte, dass sie KEINE Exception wirft.
        assertDoesNotThrow(() -> inventoryService.processPlayerActions(actionsDTO, testPlayer));

        // ========== ASSERT (Überprüfen) ==========
        // Überprüfe den finalen Zustand des Inventars
        assertNull(testPlayer.getInventory().getSlots().get(2).getItem(), "Quell-Slot sollte jetzt leer sein.");
        assertEquals(sword, testPlayer.getInventory().getSlots().get(10).getItem(), "Ziel-Slot sollte jetzt das Schwert enthalten.");
    }

    @Test
    void processPlayerActions_withStaleSwapAction_shouldThrowSwapFailException() {
        // ========== ARRANGE (Vorbereiten) ==========
        // Simuliere eine Race Condition: Der Client glaubt, in Slot 2 ist ein Schwert,
        // aber auf dem Server ist dort jetzt ein Trank.

        // 1. Simuliere die DTO-Anfrage vom Client (glaubt, er tauscht das Schwert)
        PlayerActionQueueDTO actionsDTO = new PlayerActionQueueDTO();
        InventorySlot sourceSlotFromClient = new InventorySlot(2);
        sourceSlotFromClient.setItem(sword); // Client denkt, hier ist ein Schwert
        InventorySlot targetSlotFromClient = new InventorySlot(10);
        SwapSlotInventoryAction swapAction = new SwapSlotInventoryAction(sourceSlotFromClient, targetSlotFromClient);
        List<PlayerInventoryAction> actionList = List.of(swapAction);

        // 2. Manipuliere den Server-Zustand, um die Race Condition zu erzeugen
        testPlayer.getInventory().getSlots().get(2).setItem(potion); // In Wahrheit ist hier ein Trank!

        // 3. Programmiere das Verhalten der Mocks
        when(inventoryMapper.createPlayerInventoryActionListFromDTO(actionsDTO, testPlayer)).thenReturn(actionList);
        when(inventoryRepository.findByPlayer(testPlayer)).thenReturn(Optional.of(testPlayer.getInventory()));
        when(equipmentRepository.findByPlayer(testPlayer)).thenReturn(Optional.of(testPlayer.getEquipment()));

        // ========== ACT & ASSERT ==========
        // Erwarte, dass die Methode eine SwapFailException wirft, weil der Zustand nicht übereinstimmt.
        assertThrows(SwapFailException.class, () -> {
            inventoryService.processPlayerActions(actionsDTO, testPlayer);
        });
    }
}