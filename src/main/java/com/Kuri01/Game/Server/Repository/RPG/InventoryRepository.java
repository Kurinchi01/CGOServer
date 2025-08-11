package com.Kuri01.Game.Server.Repository.RPG;

import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Inventory;
import com.Kuri01.Game.Server.Model.RPG.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByPlayer(Player player);
}
