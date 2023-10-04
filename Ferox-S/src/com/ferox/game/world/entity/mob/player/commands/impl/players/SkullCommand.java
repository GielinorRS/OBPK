package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.world.entity.combat.skull.SkullType;
import com.ferox.game.world.entity.combat.skull.Skulling;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.util.Color;

public class SkullCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if(player.getSkullType() == SkullType.RED_SKULL) {
            return;
        }

        if (parts[0].startsWith("red")) {
            Skulling.assignSkullState(player, SkullType.RED_SKULL);
            player.message(Color.RED.tag()+"Be careful whilst being redskulled all items are lost on death, this includes pets!");
        } else {
            Skulling.assignSkullState(player, SkullType.WHITE_SKULL);
        }
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
