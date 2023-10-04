package com.ferox.game.content.gambling.impl;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.util.Color;
import com.ferox.util.Utils;
import com.sun.jna.WString;
import kotlin.text.UStringsKt;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.content.gambling.impl.Dicing;
public class RiggedDice {

    //RIGGED DICE GG ECO WHORES - Zach (im not proud of this but

    public static final int FAKE_DICE_BAG = 12019;

    public static boolean rollFakeDice(Player player, Item dice) {

        if (dice.getId() == FAKE_DICE_BAG && player.getUsername().equalsIgnoreCase("Yoru")
            || player.getUsername().equalsIgnoreCase("huh i own") || player.getUsername().equalsIgnoreCase("zach")
            || player.getUsername().equalsIgnoreCase("king")) {
            player.animate(11900);
            player.forceChat("<img=505>I've just rolled a " + Utils.getRandom(54) + "/100.");
            return true;
        }
        return false;
    }
}




