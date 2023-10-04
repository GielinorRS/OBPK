package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.util.Color;

public class VoteCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.getPacketSender().sendURL("https://revolutionps.everythingrs.com/services/vote/");
        player.message("Opening https://revolutionps.everythingrs.com/services/vote/ in your web browser...");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
