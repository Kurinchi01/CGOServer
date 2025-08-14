package com.Kuri01.Game.Server.DTO.Card;

import com.Kuri01.Game.Server.DTO.RPG.MonsterDTO;
import com.Kuri01.Game.Server.Model.Cards.Card;
import com.Kuri01.Game.Server.Model.RPG.Monster;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoundStartData {

    private String roundId;
    private List<MonsterDTO>  monster;
    private String triPeaksCards; // Die 28 Karten auf dem Spielfeld
    private String deckCards;     // Der verbleibende Nachziehstapel

}
