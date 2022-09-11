package com.ferox.game.world.entity.mob.player.commands.impl.dev;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.position.areas.impl.WildernessArea;

public class POScommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.message(player.tile().toString());
        player.message("Region = "+player.tile().region());
        player.message(player.tile().toString()+" region: "+player.tile().region()+". wild: "+ WildernessArea.inWilderness(player.tile())+". Chunk: "+player.tile().chunk());
    }

    @Override
    public boolean canUse(Player player) {

        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
