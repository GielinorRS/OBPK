package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.util.Color;

/**
 * @author Patrick van Elderen | June, 23, 2021, 09:58
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class RaidsHealCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (!player.isInsideRaids() && player.hp() != 0) {
            player.message(Color.RED.wrap("You can only do this when you have 0 HP in raids."));
            return;
        }
        player.healPlayer();
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
