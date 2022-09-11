package com.ferox.game.content.syntax.impl;

import com.ferox.game.content.syntax.EnterSyntax;
import com.ferox.game.world.entity.mob.player.Player;

public class BankX implements EnterSyntax {

    private boolean deposit;
    private int item_id;
    private int slot_id;

    public BankX(int item_id, int slot_id, boolean deposit) {
        this.item_id = item_id;
        this.slot_id = slot_id;
        this.deposit = deposit;
    }

    @Override
    public void handleSyntax(Player player, String input) {

    }

    @Override
    public void handleSyntax(Player player, long input) {
        if (item_id < 0 || slot_id < 0 || input <= 0) {
            return;
        }
        if (deposit) {
            player.getBank().deposit(slot_id, (int) input);
        } else {
            if (player.getBank().quantityX) {
                player.getBank().currentQuantityX = (int) input;
            }
           player.getBank().withdraw(item_id, slot_id, (int) input);
        }
    }

}
