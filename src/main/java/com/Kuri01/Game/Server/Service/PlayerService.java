package com.Kuri01.Game.Server.Service;

import com.Kuri01.Game.Server.DTO.*;
import com.Kuri01.Game.Server.DTO.Action.EquipAction;
import com.Kuri01.Game.Server.DTO.Action.PlayerAction;
import com.Kuri01.Game.Server.DTO.Action.SwapInvAction;
import com.Kuri01.Game.Server.DTO.Action.UnequipAction;
import com.Kuri01.Game.Server.Model.RPG.Currency.PlayerWallet;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.*;
import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional(readOnly = true)
    public PlayerDTO getPlayerProfile(String googleId) {
        Player player = playerRepository.findByGoogleId(googleId).orElseThrow(() -> new IllegalArgumentException("Spieler nicht gefunden: " + googleId));
        ;

        // Hier, innerhalb der Transaktion, mappen wir die Entity auf ein DTO.
        return createDTOFromPlayer(player);
    }

    public PlayerDTO getPlayerProfileJUnitTest(Player player) {
        return createDTOFromPlayer(player);
    }





    /// _______________________ Reverse Methoden __________________________________
    ///                 Ertelle Models aus DTO



}
