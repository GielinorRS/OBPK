package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.GameServer;
import com.ferox.game.content.teleport.TeleportType;
import com.ferox.game.content.teleport.Teleports;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.position.Tile;

public class SkillerCommand implements Command {

    public void execute(Player player, String command, String[] parts) {
        Tile tile = GameServer.properties().defaultTile;

        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
            return;
        }

        Teleports.basicTeleport(player, (new Tile(3125, 3473)));
        player.message("You have been teleported to the skilling area.");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
