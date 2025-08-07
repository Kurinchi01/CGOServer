package com.Kuri01.Game.Server.Repository;

import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
