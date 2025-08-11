package com.Kuri01.Game.Server.Model.RPG.Currency;

import com.Kuri01.Game.Server.Model.RPG.Player;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private Player player;

    private Long gold = 2000L; // Spielwährung
    private Long candy = 500L; // Echtgeld-Währung

}
