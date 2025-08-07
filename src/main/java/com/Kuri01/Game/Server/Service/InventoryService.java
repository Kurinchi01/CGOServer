package com.Kuri01.Game.Server.Service;

import com.Kuri01.Game.Server.DTO.Action.EquipAction;
import com.Kuri01.Game.Server.DTO.Action.PlayerAction;
import com.Kuri01.Game.Server.DTO.Action.SwapInvAction;
import com.Kuri01.Game.Server.DTO.Action.UnequipAction;
import com.Kuri01.Game.Server.Exceptions.SwapFailException;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.*;
import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Repository.EquipmentRepository;
import com.Kuri01.Game.Server.Repository.InventoryRepository;
import com.Kuri01.Game.Server.Repository.PlayerRepository;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Getter
public class InventoryService {
    private final PlayerRepository playerRepository;
    private final InventoryRepository inventoryRepository;
    private final EquipmentRepository equipmentRepository;
    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    public InventoryService(PlayerRepository playerRepository, InventoryRepository inventoryRepository, EquipmentRepository equipmentRepository) {
        this.inventoryRepository = inventoryRepository;
        this.playerRepository = playerRepository;
        this.equipmentRepository = equipmentRepository;
    }

    @Transactional
    public void reciveInv(List<PlayerAction> actions, Player player) {


        Inventory inventoryCopy = inventoryRepository.findByPlayer(player).orElseThrow();
        Equipment equipmentCopy = equipmentRepository.findByPlayer(player).orElseThrow();

        for (PlayerAction a : actions) {

            //Aus Inv ins Inv Verschieben
            if (a instanceof SwapInvAction b) {

                InventorySlot sourceSlot = b.getSourceSlot();
                InventorySlot targetSlot = b.getTargetSlot();

                InventorySlot serverSourceInventorySlot = inventoryCopy.getSlots().get(sourceSlot.getSlotIndex());
                InventorySlot serverTargetInventorySlot = inventoryCopy.getSlots().get(targetSlot.getSlotIndex());

                boolean sourceItemsMatch = Objects.equals(
                        (serverSourceInventorySlot.getItem() != null ? serverSourceInventorySlot.getItem().getId() : null),
                        (sourceSlot.getItem() != null ? sourceSlot.getItem().getId() : null)
                );
                boolean targetItemsMatch = Objects.equals(
                        (serverTargetInventorySlot.getItem() != null ? serverTargetInventorySlot.getItem().getId() : null),
                        (targetSlot.getItem() != null ? targetSlot.getItem().getId() : null)
                );

                if (sourceItemsMatch && targetItemsMatch) {
                    // ERFOLGSFALL: Beide Slots auf dem Server entsprechen dem, was der Client erwartet hat.
                    swapItems(inventoryCopy, serverSourceInventorySlot, serverTargetInventorySlot);
                    logger.info("Swap-Validierung erfolgreich und durchgeführt.");
                } else {
                    // FEHLERFALL: Der Zustand hat sich geändert, seit der Client die Aktion gestartet hat.
                    logger.warn("Swap-Validierung fehlgeschlagen: Client- und Server-Zustand stimmen nicht überein.");
                    throw new SwapFailException("Aktion konnte nicht ausgeführt werden, da sich das Inventar geändert hat.");
                }
            }

            if (a instanceof EquipAction b) {

                InventorySlot sourceInventorySlot = b.getSourceInventorySlot();
                EquipmentSlot targetEquipmentSlot = b.getTargetEquipmentSlot();

                InventorySlot serverSourceInventorySlot = inventoryCopy.getSlots().get(sourceInventorySlot.getSlotIndex());
                EquipmentSlot serverTargetEquipmentSlot = equipmentCopy.getEquipmentSlots().get(targetEquipmentSlot.getSlotEnum());

                boolean sourceItemsMatch = Objects.equals(
                        (serverSourceInventorySlot.getItem() != null ? serverSourceInventorySlot.getItem().getId() : null),
                        (sourceInventorySlot.getItem() != null ? sourceInventorySlot.getItem().getId() : null)
                );
                boolean targetItemsMatch = Objects.equals(
                        (serverTargetEquipmentSlot.getItem() != null ? serverTargetEquipmentSlot.getItem().getId() : null),
                        (targetEquipmentSlot.getItem() != null ? targetEquipmentSlot.getItem().getId() : null)
                );

                if (sourceItemsMatch && targetItemsMatch) {
                    // ERFOLGSFALL: Beide Slots auf dem Server entsprechen dem, was der Client erwartet hat.
                    equipItem(inventoryCopy,serverSourceInventorySlot, serverTargetEquipmentSlot);
                    logger.info("Equip-Validierung erfolgreich und durchgeführt.");
                } else {
                    // FEHLERFALL: Der Zustand hat sich geändert, seit der Client die Aktion gestartet hat.
                    logger.warn("Equip-Validierung fehlgeschlagen: Client- und Server-Zustand stimmen nicht überein.");
                    throw new SwapFailException("Aktion konnte nicht ausgeführt werden, da sich das Inventar geändert hat.");
                }

            }

            if (a instanceof UnequipAction b) {
                EquipmentSlot sourceEquipmentSlot = b.getSourceEquipmentSlot();
                InventorySlot targetInventorySlot = b.getTargetInventorySlot();

                EquipmentSlot serverSourceEquipmentSlot = equipmentCopy.getEquipmentSlots().get(sourceEquipmentSlot.getSlotEnum());
                InventorySlot servertargetInventorySlot = inventoryCopy.getSlots().get(targetInventorySlot.getSlotIndex());

                boolean sourceItemsMatch = Objects.equals(
                        (serverSourceEquipmentSlot.getItem() != null ? serverSourceEquipmentSlot.getItem().getId() : null),
                        (sourceEquipmentSlot.getItem() != null ? sourceEquipmentSlot.getItem().getId() : null)
                );
                boolean targetItemsMatch = Objects.equals(
                        (servertargetInventorySlot.getItem() != null ? servertargetInventorySlot.getItem().getId() : null),
                        (targetInventorySlot.getItem() != null ? targetInventorySlot.getItem().getId() : null)
                );

                if (sourceItemsMatch && targetItemsMatch) {
                    // ERFOLGSFALL: Beide Slots auf dem Server entsprechen dem, was der Client erwartet hat.
                    unqeuipItem(serverSourceEquipmentSlot, servertargetInventorySlot);
                    logger.info("Unequip-Validierung erfolgreich und durchgeführt.");
                } else {
                    // FEHLERFALL: Der Zustand hat sich geändert, seit der Client die Aktion gestartet hat.
                    logger.warn("Unequip-Validierung fehlgeschlagen: Client- und Server-Zustand stimmen nicht überein.");
                    throw new SwapFailException("Aktion konnte nicht ausgeführt werden, da sich das Inventar geändert hat.");
                }
            }


        }

    }


    @Transactional
    public void swapItems(Inventory inventory, InventorySlot sourceSlot, InventorySlot targetSlot) {
        Item tmpItem = targetSlot.getItem();


        inventory.setItemToSlot(targetSlot.getSlotIndex(), sourceSlot.getItem());
        if (tmpItem != null)
            inventory.setItemToSlot(sourceSlot.getSlotIndex(), tmpItem);
        else
            inventory.setItemToSlot(sourceSlot.getSlotIndex(), null);

    }

    public void equipItem(Inventory inventory, InventorySlot sourceSlot, EquipmentSlot targetSlot) {
        Item tmpItem = targetSlot.getItem();

        targetSlot.setItem(sourceSlot.getItem());

        //wenn vorher was ausgerüstet ersetze, sonst setze item auf null
        if (tmpItem != null)
            inventory.setItemToSlot(sourceSlot.getSlotIndex(), tmpItem);
        else
            inventory.setItemToSlot(sourceSlot.getSlotIndex(), null);

    }

    public void unqeuipItem(EquipmentSlot sourceSlot, InventorySlot targetSlot) {
        Item tmpItem = targetSlot.getItem();

        targetSlot.setItem(sourceSlot.getItem());

        //wenn vorher was ausgerüstet ersetze, sonst setze item auf null
        if (tmpItem != null)
            sourceSlot.setItem(tmpItem);
        else
            sourceSlot.setItem(null);
    }
}
