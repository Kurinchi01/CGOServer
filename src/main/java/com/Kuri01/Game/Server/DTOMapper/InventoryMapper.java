package com.Kuri01.Game.Server.DTOMapper;

import com.Kuri01.Game.Server.DTO.RPG.*;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.*;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Action.EquipInventoryAction;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Action.PlayerInventoryAction;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Action.SwapSlotInventoryAction;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Action.UnequipInventoryAction;
import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Repository.RPG.EquipmentRepository;
import com.Kuri01.Game.Server.Repository.RPG.InventoryRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Slf4j
@Component
public class InventoryMapper {
    private final InventoryRepository inventoryRepository;
    private final EquipmentRepository equipmentRepository;

    public InventoryMapper(InventoryRepository inventoryRepository, EquipmentRepository equipmentRepository) {
        this.inventoryRepository = inventoryRepository;
        this.equipmentRepository = equipmentRepository;
    }

    /// _______________________________ DTO to PlayerInventoryAction Mapper ____________________________________________

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

    public SwapSlotInventoryAction createSwapInvInventoryActionFromDTO(PlayerActionDTO playerActionDTO, Player player) {
        InventorySlot sourceSlot = findInventorySlotFromDTO(playerActionDTO.getSourceInventorySlotDTO(), player);
        InventorySlot targetSlot = findInventorySlotFromDTO(playerActionDTO.getTargetInventorySlotDTO(), player);

        return new SwapSlotInventoryAction(sourceSlot, targetSlot);
    }


    public InventorySlot findInventorySlotFromDTO(InventorySlotDTO inventorySlotDTO, Player player) {
        return inventoryRepository.findByPlayer(player).orElseThrow().getSlots().get(inventorySlotDTO.getSlotIndex());
    }

    public EquipmentSlot findEquipmentSlotFromDTO(EquipmentSlotDTO equipmentSlotDTO, Player player) {
        return equipmentRepository.findByPlayer(player).orElseThrow().getEquipmentSlots().get(equipmentSlotDTO.getSlotEnum());
    }

    /// ______________________________________ Model to DTO Mapper _____________________________________________________

    public InventoryDTO createDTOFromInventory(Inventory inventory) {
        InventoryDTO inventoryDTO = new InventoryDTO();

        inventoryDTO.setCapacity(inventory.getCapacity());
        inventoryDTO.setInventorySlots(createDTOFromInventorySlot(inventory.getSlots()));

        return inventoryDTO;
    }

    public List<InventorySlotDTO> createDTOFromInventorySlot(List<InventorySlot> slots) {
        List<InventorySlotDTO> list = new ArrayList<>();
        for (InventorySlot a : slots) {
            InventorySlotDTO tmp = new InventorySlotDTO();
            tmp.setSlotIndex(a.getSlotIndex());
            tmp.setQuantity(a.getQuantity());
            if (a.getItem() != null)
                tmp.setItemID(a.getItem().getId());
            else tmp.setItemID(null);
            list.add(tmp);
        }

        return list;
    }

    public EquipmentDTO createDTOFromEquipment(Equipment equipment) {
        if (equipment == null) return null;
        EquipmentDTO equipmentDTO = new EquipmentDTO();

        equipmentDTO.setEquipmentSlots(createDTOFromEquipmentSlot(equipment.getEquipmentSlots()));

        return equipmentDTO;
    }

    public Map<EquipmentSlotEnum, EquipmentSlotDTO> createDTOFromEquipmentSlot(Map<EquipmentSlotEnum, EquipmentSlot> equipmentSlots) {
        Map<EquipmentSlotEnum, EquipmentSlotDTO> map = new HashMap<>();

        for (Map.Entry<EquipmentSlotEnum, EquipmentSlot> a : equipmentSlots.entrySet()) {
            EquipmentSlotDTO tmp = new EquipmentSlotDTO();

            tmp.setSlotEnum(a.getKey());
            if (a.getValue().getItem() != null)
                tmp.setItemID(a.getValue().getItem().getId());
            else tmp.setItemID(null);

            map.put(a.getKey(), tmp);

        }

        return map;
    }

}
