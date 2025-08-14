package com.Kuri01.Game.Server.DTO.RPG;

import com.Kuri01.Game.Server.Model.RPG.Rarity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MonsterDTO {

    private String name;
    private float maxHP;
    private float attack;
    private float chargeRate;
    private Rarity rarity;
    private String spriteName;
}
