package com.Kuri01.Game.Server.Model.RPG.Repository;

import com.Kuri01.Game.Server.Model.RPG.Currency.CurrencyTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyTransactionRepository extends JpaRepository<CurrencyTransaction,Long> {
}
