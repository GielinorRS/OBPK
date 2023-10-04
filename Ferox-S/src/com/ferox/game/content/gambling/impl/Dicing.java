package com.ferox.game.content.gambling.impl;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.util.Color;
import com.ferox.util.Utils;

public class Dicing {

    public static final int DICE_BAG = 12020;

    public static boolean rollDice(Player player, Item dice) {
        double totalAmountPaid = player.getAttribOr(AttributeKey.TOTAL_PAYMENT_AMOUNT, 0D);
        if (dice.getId() == DICE_BAG && totalAmountPaid >= 499.00) {
            player.animate(11900);
            player.forceChat("<img=505>I've just rolled a " + Utils.getRandom(100) + "/100.");
            return true;
        }
        if (dice.getId() == DICE_BAG && totalAmountPaid <= 499.00) {
            player.message(Color.PURPLE.wrap("You must've donated $500+ to use the dice."));
            return false;
        }
        return false;
    }
}
