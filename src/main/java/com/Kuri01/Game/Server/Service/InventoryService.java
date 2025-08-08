package com.Kuri01.Game.Server.Service;

import com.Kuri01.Game.Server.DTOMapper.InventoryMapper;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Action.EquipInventoryAction;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Action.PlayerInventoryAction;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Action.SwapInvInventoryAction;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Action.UnequipInventoryAction;
import com.Kuri01.Game.Server.Exceptions.SwapFailException;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.*;
import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Repository.EquipmentRepository;
import com.Kuri01.Game.Server.Repository.InventoryRepository;
import com.Kuri01.Game.Server.Repository.PlayerRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Getter
@Slf4j
public class InventoryService {
    private final PlayerRepository playerRepository;
    private final InventoryRepository inventoryRepository;
    private final EquipmentRepository equipmentRepository;
    private InventoryMapper inventoryMapper;


    @Autowired
    public InventoryService(PlayerRepository playerRepository, InventoryMapper inventoryMapper) {
        this.inventoryRepository = inventoryMapper.getInventoryRepository();
        this.playerRepository = playerRepository;
        this.equipmentRepository = inventoryMapper.getEquipmentRepository();
        this.inventoryMapper = inventoryMapper;
    }

    @Transactional
    public void receiveIn(List<PlayerInventoryAction> actions, Player player) {


        Inventory inventoryCopy = inventoryRepository.findByPlayer(player).orElseThrow();
        Equipment equipmentCopy = equipmentRepository.findByPlayer(player).orElseThrow();

        for (PlayerInventoryAction a : actions) {

            //Aus Inv ins Inv Verschieben
            if (a instanceof SwapInvInventoryAction b) {

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
                    log.info("Swap-Validierung erfolgreich und durchgeführt.");
                } else {
                    // FEHLERFALL: Der Zustand hat sich geändert, seit der Client die Aktion gestartet hat.
                    log.warn("Swap-Validierung fehlgeschlagen: Client- und Server-Zustand stimmen nicht überein.");
                    throw new SwapFailException("Aktion konnte nicht ausgeführt werden, da sich das Inventar geändert hat.");
                }
            }

            if (a instanceof EquipInventoryAction b) {

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
                    equipItem(inventoryCopy, serverSourceInventorySlot, serverTargetEquipmentSlot);
                    log.info("Equip-Validierung erfolgreich und durchgeführt.");
                } else {
                    // FEHLERFALL: Der Zustand hat sich geändert, seit der Client die Aktion gestartet hat.
                    log.warn("Equip-Validierung fehlgeschlagen: Client- und Server-Zustand stimmen nicht überein.");
                    throw new SwapFailException("Aktion konnte nicht ausgeführt werden, da sich das Inventar geändert hat.");
                }

            }

            if (a instanceof UnequipInventoryAction b) {
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
                    unequipItem(serverSourceEquipmentSlot, servertargetInventorySlot);
                    log.info("Unequip-Validierung erfolgreich und durchgeführt.");
                } else {
                    // FEHLERFALL: Der Zustand hat sich geändert, seit der Client die Aktion gestartet hat.
                    log.warn("Unequip-Validierung fehlgeschlagen: Client- und Server-Zustand stimmen nicht überein.");
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

    @Transactional
    public void equipItem(Inventory inventory, InventorySlot sourceSlot, EquipmentSlot targetSlot) {
        Item tmpItem = targetSlot.getItem();

        targetSlot.setItem(sourceSlot.getItem());

        //wenn vorher was ausgerüstet ersetze, sonst setze item auf null
        if (tmpItem != null)
            inventory.setItemToSlot(sourceSlot.getSlotIndex(), tmpItem);
        else
            inventory.setItemToSlot(sourceSlot.getSlotIndex(), null);

    }

    @Transactional
    public void unequipItem(EquipmentSlot sourceSlot, InventorySlot targetSlot) {
        Item tmpItem = targetSlot.getItem();

        targetSlot.setItem(sourceSlot.getItem());

        //wenn vorher was ausgerüstet ersetze, sonst setze item auf null
        if (tmpItem != null)
            sourceSlot.setItem(tmpItem);
        else
            sourceSlot.setItem(null);
    }


}
