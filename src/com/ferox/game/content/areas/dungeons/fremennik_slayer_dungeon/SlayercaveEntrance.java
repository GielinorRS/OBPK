package com.ferox.game.content.areas.dungeons.fremennik_slayer_dungeon;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.chainedwork.Chain;

import static com.ferox.util.ObjectIdentifiers.CAVE_ENTRANCE_2123;

public class SlayercaveEntrance extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            if(obj.getId() == CAVE_ENTRANCE_2123) {
                Chain.bound(null).runFn(1, () -> player.teleport(new Tile(2808, 10002)));
                return true;
            }
        }
        return false;
    }

}
