package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.GameServer;
import com.ferox.game.content.teleport.TeleportType;
import com.ferox.game.content.teleport.Teleports;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.position.Tile;

public class EdgeCommand implements Command {

    public void execute(Player player, String command, String[] parts) {
        Tile tile = GameServer.properties().defaultTile;

        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
            return;
        }

        Teleports.basicTeleport(player, (new Tile(3092, 3512)));
        player.message("You have been teleported to the edgeville pking area.");
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
