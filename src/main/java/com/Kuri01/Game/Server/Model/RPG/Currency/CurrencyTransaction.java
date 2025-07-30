package com.Kuri01.Game.Server.Model.RPG.Currency;

import com.Kuri01.Game.Server.Model.RPG.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class CurrencyTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Player player;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CurrencyType currencyType; // z.B. GOLD, GEMS

    @Column(nullable = false)
    private long amountChanged; // z.B. +100 für Einnahme, -50 für Ausgabe

    @Column(nullable = false)
    private String reason; // z.B. "MONSTER_DROP", "PURCHASE_SWORD", "IAP_5_DOLLAR_PACK"

    @CreationTimestamp // Setzt automatisch das Erstellungsdatum
    private Instant transactionTimestamp;


    public CurrencyTransaction(Player player, CurrencyType type, long amount, String reason) {
        this.player=player;
        this.amountChanged=amount;
        this.reason=reason;
        this.currencyType=type;
    }


}
