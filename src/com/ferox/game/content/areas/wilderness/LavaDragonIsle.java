package com.ferox.game.content.areas.wilderness;

import com.ferox.game.task.TaskManager;
import com.ferox.game.task.impl.ForceMovementTask;
import com.ferox.game.world.entity.mob.player.ForceMovement;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Tile;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.chainedwork.Chain;

import static com.ferox.util.ObjectIdentifiers.STEPPING_STONE_14918;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * mei 11, 2020
 */
public class LavaDragonIsle extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            if(obj.getId() == STEPPING_STONE_14918) {
                if (player.tile().y >= 3808) {
                    player.walkAndWait(new Tile(3201, 3810), () -> handle(player, obj));
                }
                else
                    return handle(player, obj);
            }
        }
        return false;
    }

    private boolean handle(Player player, GameObject obj) {
        if(player.skills().level(Skills.AGILITY) < 74) {
            player.message("You need a Agility level of 74 to use this shortcut.");
            return true;
        }
        player.faceObj(obj);

        if(player.tile().y == 3807) {
            Chain.bound(player).name("LavaDragonIsle1Task").runFn(2, () -> {
                player.lockDelayDamage();
                player.animate(741);
                TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(0, +1), 5, 35, 4)));
            }).then(2, () -> {
                player.animate(741);
                TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(0, +2), 5, 35, 4)));
            }).waitForTile(new Tile(3201, 3810), () -> player.unlock());
        } else if(player.tile().x == 3201 && player.tile().y == 3810) {
            Chain.bound(player).name("LavaDragonIsle2Task").runFn(2, () -> {
                player.lockDelayDamage();
                player.animate(741);
                TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(0, -2), 5, 35, 4)));
            }).then(2, () -> {
                player.animate(741);
                TaskManager.submit(new ForceMovementTask(player, 1, new ForceMovement(player.tile().clone(), new Tile(0, -1), 5, 35, 4)));
            }).waitForTile(new Tile(3201, 3807), () -> player.unlock());
        }
        return false;
    }
}
