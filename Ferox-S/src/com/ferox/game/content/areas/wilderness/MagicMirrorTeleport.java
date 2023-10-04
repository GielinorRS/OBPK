package com.ferox.game.content.areas.wilderness;

import com.ferox.game.content.areas.wilderness.content.key.WildernessKeyPlugin;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.position.areas.impl.WildernessArea;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.chainedwork.Chain;
import com.ferox.util.timers.TimerKey;

public class MagicMirrorTeleport extends PacketInteraction {
    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if (option == 1) {
            //Into the arena
            if (obj.getId() == 34683) {
                player.faceObj(obj);
                //Check to see if the player is teleblocked
                if (player.getTimers().has(TimerKey.TELEBLOCK) || player.getTimers().has(TimerKey.SPECIAL_TELEBLOCK)) {
                    player.teleblockMessage();
                    return true;
                }

                if (WildernessKeyPlugin.hasKey(player) && WildernessArea.inWilderness(player.tile())) {
                    player.message("You cannot teleport outside the Wilderness with the Wilderness key.");
                    return true;
                }

                player.lockDelayDamage();
                Chain.bound(null).runFn(1, () -> {
                    player.animate(2710);
                    player.message("You grab onto the mirror...");
                }).then(2, () -> {
                    player.animate(714);
                    player.graphic(111, 110, 0);
                }).then(4, () -> {
                    player.teleport(new Tile(3094, 3503, 0));
                    player.animate(-1);
                    player.unlock();
                    player.message("...and get taken by a magical force to home!");
                });
                return true;
            }
        }
        return false;
    }
}
