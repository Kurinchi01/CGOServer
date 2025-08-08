package com.Kuri01.Game.Server.DTOMapper;

import com.Kuri01.Game.Server.DTO.EquipmentSlotDTO;
import com.Kuri01.Game.Server.DTO.InventorySlotDTO;
import com.Kuri01.Game.Server.DTO.PlayerActionDTO;
import com.Kuri01.Game.Server.DTO.PlayerActionQueueDTO;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Action.EquipInventoryAction;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Action.PlayerInventoryAction;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Action.SwapInvInventoryAction;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Action.UnequipInventoryAction;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.EquipmentSlot;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.InventorySlot;
import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Repository.EquipmentRepository;
import com.Kuri01.Game.Server.Repository.InventoryRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Slf4j
public class InventoryMapper {
    private final InventoryRepository inventoryRepository;
    private final EquipmentRepository equipmentRepository;

    public InventoryMapper(InventoryRepository inventoryRepository, EquipmentRepository equipmentRepository) {
        this.inventoryRepository = inventoryRepository;
        this.equipmentRepository = equipmentRepository;
    }

    /// _____________________________________ DTO to PlayerInventoryAction Mapper _______________________________________________________

    public List<PlayerInventoryAction> createPlayerInventoryActionListFromDTO(PlayerActionQueueDTO playerActionQueueDTO, Player player) {
        List<PlayerInventoryAction> tmp = new ArrayList<>();
        for (PlayerActionDTO a : playerActionQueueDTO.getPlayerActionDTOList()) {
            tmp.add(createPlayerInventoryAction(a, player));
        }
        return tmp;
    }


    public PlayerInventoryAction createPlayerInventoryAction(PlayerActionDTO playerActionDTO, Player player) {
        PlayerInventoryAction tmp = null;
        switch (playerActionDTO.getActionType()) {
            case "EQUIP": {
                tmp = createEquipInventoryActionFromDTO(playerActionDTO, player);
                break;
            }
            case "UNEQUIP": {
                tmp = createUnequipInventoryActionFromDTO(playerActionDTO, player);
                break;
            }
            case "SWAP_INVENTORY": {
                tmp = createSwapInvInventoryActionFromDTO(playerActionDTO, player);
                break;
            }
        }
        return tmp;
    }

    public UnequipInventoryAction createUnequipInventoryActionFromDTO(PlayerActionDTO playerActionDTO, Player player) {
        EquipmentSlot sourceSlot = findEquipmentSlotFromDTO(playerActionDTO.getSourceEquipmentSlotDTO(), player);
        InventorySlot targetSlot = findInventorySlotFromDTO(playerActionDTO.getTargetInventorySlotDTO(), player);

        return new UnequipInventoryAction(sourceSlot, targetSlot);
    }

    public EquipInventoryAction createEquipInventoryActionFromDTO(PlayerActionDTO playerActionDTO, Player player) {
        InventorySlot sourceSlot = findInventorySlotFromDTO(playerActionDTO.getSourceInventorySlotDTO(), player);
        EquipmentSlot targetSlot = findEquipmentSlotFromDTO(playerActionDTO.getTargetEquipmentSlotDTO(), player);

        return new EquipInventoryAction(sourceSlot, targetSlot);
    }

    public SwapInvInventoryAction createSwapInvInventoryActionFromDTO(PlayerActionDTO playerActionDTO, Player player) {
        InventorySlot sourceSlot = findInventorySlotFromDTO(playerActionDTO.getSourceInventorySlotDTO(), player);
        InventorySlot targetSlot = findInventorySlotFromDTO(playerActionDTO.getTargetInventorySlotDTO(), player);

        return new SwapInvInventoryAction(sourceSlot, targetSlot);
    }


    public InventorySlot findInventorySlotFromDTO(InventorySlotDTO inventorySlotDTO, Player player) {
        return inventoryRepository.findByPlayer(player).orElseThrow().getSlots().get(inventorySlotDTO.getSlotIndex());
    }

    public EquipmentSlot findEquipmentSlotFromDTO(EquipmentSlotDTO equipmentSlotDTO, Player player) {
        return equipmentRepository.findByPlayer(player).orElseThrow().getEquipmentSlots().get(equipmentSlotDTO.getSlotEnum());
    }
}
