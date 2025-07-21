package com.Kuri01.Game.Server.Model.RPG.ItemSystem;

import com.Kuri01.Game.Server.Model.RPG.Rarity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Erlaubt uns später, spezielle Item-Typen zu haben
@DiscriminatorColumn(name = "item_type")
@Getter
@Setter
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private Rarity rarity;

    @Column(nullable = false)
    private int quantity=1;

    @Column(nullable = false)
    private String iconName;


}
