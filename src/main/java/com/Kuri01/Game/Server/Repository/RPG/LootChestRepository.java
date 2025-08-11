package com.Kuri01.Game.Server.Repository.RPG;

import com.Kuri01.Game.Server.Model.RPG.ItemSystem.LootChest;
import com.Kuri01.Game.Server.Model.RPG.Rarity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LootChestRepository extends JpaRepository<LootChest, Long> {

    Optional<LootChest> findByRarity(Rarity rarity);

}
