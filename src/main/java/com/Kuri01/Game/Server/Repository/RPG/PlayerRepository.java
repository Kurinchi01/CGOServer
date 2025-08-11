package com.Kuri01.Game.Server.Repository.RPG;

import com.Kuri01.Game.Server.Model.RPG.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByGoogleId(String s);

    Optional<Player> findByName(String username);
}
