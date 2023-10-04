package com.ferox.game.content.areas.dungeons.godwars;

import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.timers.TimerKey;

import static com.ferox.util.ObjectIdentifiers.*;

public class GodwarsAltars extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(obj.getId() == ZAMORAK_ALTAR || obj.getId() == SARADOMIN_ALTAR || obj.getId() == ARMADYL_ALTAR || obj.getId() == BANDOS_ALTAR) {
            if (option == 1) {
                if (CombatFactory.inCombat(player)) {
                    if (!player.getTimers().has(TimerKey.GODWARS_ALTAR_LOCK)) {
                        player.message("You pray to the gods...");
                        player.getTimers().addOrSet(TimerKey.GODWARS_ALTAR_LOCK, 1000);
                        player.skills().replenishSkill(5, player.skills().xpLevel(5));
                        player.animate(645);
                        player.message("...and recharged your prayer.");
                    } else {
                        player.message("You cannot use this altar yet!");
                    }
                } else {
                    player.message("You cannot do this while under attack.");
                }
            } else {
                Tile teleportTile = null;
                if (obj.getId() == 26363)
                    teleportTile = new Tile(2925, 5333, 2);
                if (obj.getId() == 26364)
                    teleportTile = new Tile(2909, 5265, 0);
                if (obj.getId() == 26365)
                    teleportTile = new Tile(2839, 5294, 2);
                if (obj.getId() == 26366)
                    teleportTile = new Tile(2862, 5354, 2);

                player.teleport(teleportTile);
            }
            return true;
        }
        return false;
    }
}
