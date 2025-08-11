package com.Kuri01.Game.Server.Repository.RPG;

import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Equipment;
import com.Kuri01.Game.Server.Model.RPG.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EquipmentRepository extends JpaRepository<Equipment,Long> {
    Optional<Equipment> findByPlayer(Player player);
}
