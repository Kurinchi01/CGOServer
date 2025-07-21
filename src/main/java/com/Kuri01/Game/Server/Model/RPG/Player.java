package com.Kuri01.Game.Server.Model.RPG;

import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Equipment;
import com.Kuri01.Game.Server.Model.RPG.ItemSystem.Inventory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
public class Player extends Character implements UserDetails {

    @Column(unique = true)
    private String googleId; // Eindeutige ID vom Google Play Login

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "equipment_id", referencedColumnName = "id")
    private Equipment equipment;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "player_roles", joinColumns = @JoinColumn(name = "player_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    private int experiencePoints;
    private int level;

    // Jeder Spieler hat genau ein Inventar.
    // cascade = CascadeType.ALL: Wenn ein Spieler gelöscht wird, wird auch sein Inventar gelöscht.
    // orphanRemoval = true: Wenn die Verknüpfung zum Inventar entfernt wird, wird es gelöscht.
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "inventory_id", referencedColumnName = "id")
    private Inventory inventory;

    public Player() {
        // Beim Erstellen eines neuen Spielers erstellen wir auch direkt ein leeres Inventar.
        super();
        this.inventory = new Inventory();
        this.equipment = new Equipment();
    }

    /// ===================================================================
    ///  IMPLEMENTIERUNG DER USERDETAILS-METHODEN
    /// ===================================================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.googleId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}