package com.Kuri01.Game.Server.Model.RPG.ItemSystem.Action;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PlayerInventoryAction {
    public final String actionType;

    public PlayerInventoryAction(String actionType) {
        this.actionType = actionType;
    }
}
