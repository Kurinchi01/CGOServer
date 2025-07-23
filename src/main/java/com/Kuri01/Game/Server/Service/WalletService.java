package com.Kuri01.Game.Server.Service;

import com.Kuri01.Game.Server.Model.RPG.Currency.CurrencyTransaction;
import com.Kuri01.Game.Server.Model.RPG.Currency.CurrencyType;
import com.Kuri01.Game.Server.Model.RPG.Currency.PlayerWallet;
import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Model.RPG.Repository.CurrencyTransactionRepository;
import com.Kuri01.Game.Server.Model.RPG.Repository.PlayerWalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private final PlayerWalletRepository walletRepository;
    private final CurrencyTransactionRepository transactionRepository;

    @Autowired
    public WalletService(PlayerWalletRepository walletRepository, CurrencyTransactionRepository transactionRepository) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
    }



    /**
     * Fügt einem Spieler Währung hinzu. Nur für positive Beträge.
     */
    @Transactional
    public void addCurrency(Player player, CurrencyType type, long amount, String reason) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Der hinzuzufügende Betrag muss positiv sein.");
        }

        // 1. Log-Eintrag erstellen
        CurrencyTransaction transaction = new CurrencyTransaction(player, type, amount, reason); // amount ist positiv
        transactionRepository.save(transaction);

        // 2. Kontostand aktualisieren
        updateWalletBalance(player.getPlayerWallet(), type, amount);
    }

    /**
     * Zieht einem Spieler Währung ab, nachdem das Guthaben geprüft wurde.
     * @return true bei Erfolg, false bei nicht ausreichendem Guthaben.
     */
    @Transactional
    public boolean spendCurrency(Player player, CurrencyType type, long amount, String reason) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Der auszugebende Betrag muss positiv sein.");
        }

        PlayerWallet wallet = player.getPlayerWallet();
        long currentBalance = (type == CurrencyType.GOLD) ? wallet.getGold() : wallet.getCandy();

        // WICHTIGE PRÜFUNG: Hat der Spieler genug Geld?
        if (currentBalance < amount) {
            return false; // Nicht genug Guthaben, Aktion wird abgebrochen
        }

        // 1. Log-Eintrag erstellen
        CurrencyTransaction transaction = new CurrencyTransaction(player, type, -amount, reason); // amount ist negativ
        transactionRepository.save(transaction);

        // 2. Kontostand aktualisieren
        updateWalletBalance(player.getPlayerWallet(), type, -amount);
        return true; // Erfolg
    }

    // Private Hilfsmethode, um Code-Wiederholung zu vermeiden
    private void updateWalletBalance(PlayerWallet wallet, CurrencyType type, long changeAmount) {
        if (type == CurrencyType.GOLD) {
            wallet.setGold(wallet.getGold() + changeAmount);
        } else {
            wallet.setCandy(wallet.getCandy() + changeAmount);
        }
        walletRepository.save(wallet);
    }

}