package com.Kuri01.Game.Server.Model.RPG.Repository;

import com.Kuri01.Game.Server.Model.RPG.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
