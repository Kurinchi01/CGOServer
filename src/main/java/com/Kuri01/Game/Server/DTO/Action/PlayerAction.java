package com.Kuri01.Game.Server.DTO.Action;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PlayerAction {
    public final String actionType;

    public PlayerAction(String actionType) {
        this.actionType = actionType;
    }
}
