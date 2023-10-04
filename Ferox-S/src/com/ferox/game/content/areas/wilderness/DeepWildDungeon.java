package com.ferox.game.content.areas.wilderness;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.util.ObjectIdentifiers.CREVICE_19043;

/**
 * @author Patrick van Elderen | January, 17, 2021, 19:16
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class DeepWildDungeon extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject object, int option) {
        if(option == 1) {
            if (object.getId() == CREVICE_19043) {
                if(player.tile().equals(3046, 10326)) {
                    player.teleport(new Tile(3048, 10336));
                } else if(player.tile().equals(3048, 10336)) {
                    player.teleport(new Tile(3046, 10326));
                }
                return true;
            }
        }
        return false;
    }
}
