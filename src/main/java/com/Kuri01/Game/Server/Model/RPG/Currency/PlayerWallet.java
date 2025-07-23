package com.Kuri01.Game.Server.Model.RPG.Currency;

import com.Kuri01.Game.Server.Model.RPG.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class PlayerWallet {
    @Id
    private Long id; // Teilt sich die ID mit dem Spieler

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")// Verknüpft diese ID mit der des Spielers
    private Player player;

    private long gold = 2000; // Spielwährung
    private long candy = 500; // Echtgeld-Währung

    public PlayerWallet(Player player) {
        this.player = player;
    }


}
