package com.Kuri01.Game.Server.Service;

import com.Kuri01.Game.Server.Model.RPG.Player;
import com.Kuri01.Game.Server.Model.RPG.Repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PlayerRepository playerRepository;

    @Autowired
    public UserDetailsServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 'username' ist in unserem Fall die Google ID
        Player player = playerRepository.findByGoogleId(username)
                .orElseThrow(() -> new UsernameNotFoundException("Spieler nicht gefunden mit Google ID: " + username));

        // Wir geben ein Spring Security User-Objekt zurück.
        // Das Passwort wird hier nicht benötigt, da wir JWT verwenden.
        return new User(player.getGoogleId(), "", new ArrayList<>());
    }
}
