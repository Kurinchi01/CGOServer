package com.Kuri01.Game.Server.Model.RPG.Repository;

import com.Kuri01.Game.Server.Model.RPG.Currency.PlayerWallet;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerWalletRepository extends JpaRepository<PlayerWallet, Long> {
}
