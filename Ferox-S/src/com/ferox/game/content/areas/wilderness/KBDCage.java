package com.ferox.game.content.areas.wilderness;

import com.ferox.game.content.areas.wilderness.content.key.WildernessKeyPlugin;
import com.ferox.game.content.packet_actions.interactions.objects.Ladders;
import com.ferox.game.task.TaskManager;
import com.ferox.game.task.impl.TickAndStop;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.object.ObjectManager;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.position.areas.impl.WildernessArea;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.timers.TimerKey;

import static com.ferox.util.ObjectIdentifiers.*;

public class KBDCage extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if (option == 1) {
            if (obj.getId() == LADDER_18987) {//KBD ladder down
                Ladders.ladderDown(player, new Tile(3069, 10255), true);
                return true;
            }

            if (obj.getId() == LADDER_18988) {//KBD ladder up
                Ladders.ladderUp(player, new Tile(3016, 3849), true);
                return true;
            }

            if (obj.getId() == LEVER_1816) {//KBD lever
                //Check to see if the player is teleblocked
                if (player.getTimers().has(TimerKey.TELEBLOCK) || player.getTimers().has(TimerKey.SPECIAL_TELEBLOCK)) {
                    player.teleblockMessage();
                    return true;
                }

                if (WildernessKeyPlugin.hasKey(player) && WildernessArea.inWilderness(player.tile())) {
                    player.message("You cannot teleport outside the Wilderness with the Wilderness key.");
                    return true;
                }

                player.lockNoDamage();
                player.animate(2140);
                player.message("You pull the lever...");
                GameObject replacementLever = new GameObject(88, obj.tile(), obj.getType(), obj.getRotation());
                ObjectManager.replace(obj, replacementLever, 5);

                TaskManager.submit(new TickAndStop(1) {
                    @Override
                    public void executeAndStop() {
                        player.animate(714);
                        player.graphic(111);
                    }
                });
                TaskManager.submit(new TickAndStop(3) {
                    @Override
                    public void executeAndStop() {
                        player.teleport(new Tile(2271, 4680));
                        player.animate(-1);
                        player.unlock();
                        player.message("...And teleport into the Dragon's Lair.");
                    }
                });
                return true;
            }

            if (obj.getId() == LEVER_1817) { //KBD cage lever
                //inside kbd area back to the wilderness
                player.lockNoDamage();
                player.animate(2140);
                player.message("You pull the lever...");
                GameObject replacementLever = new GameObject(88, obj.tile(), obj.getType(), obj.getRotation());
                ObjectManager.replace(obj, replacementLever, 5);


                TaskManager.submit(new TickAndStop(1) {
                    @Override
                    public void executeAndStop() {
                        player.animate(714);
                        player.graphic(111);
                    }
                });
                TaskManager.submit(new TickAndStop(3) {
                    @Override
                    public void executeAndStop() {
                        player.teleport(new Tile(3067, 10253));
                        player.animate(-1);
                        player.unlock();
                        player.message("...And teleport out of the Dragon's Lair.");
                    }
                });
                return true;
            }
        }
        return false;
    }
}
