package com.ferox.game.world.entity.mob.player.commands.impl.dev;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.object.doors.Door;
import com.ferox.game.world.object.doors.Doors;
import com.ferox.game.world.position.Tile;

public class DoorCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        int id = Integer.parseInt(parts[1]);
        int relativeId = Integer.parseInt(parts[2]);
        int rot = Integer.parseInt(parts[3]);
        Tile tile = player.tile();
        GameObject spawned = new GameObject(id, player.tile(), 0, rot);
        ObjectManager.addObj(spawned);
        Doors.CACHE.add(new Door(id, relativeId, true, false));
        player.message("Spawned door "+ id +" at "+ tile.toString() +" with rotation " + rot);
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }
}
