package com.ferox.game.content.areas.wilderness.content.larrans_key;

import com.ferox.game.content.achievements.Achievements;
import com.ferox.game.content.achievements.AchievementsManager;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.combat.skull.SkullType;
import com.ferox.game.world.entity.combat.skull.Skulling;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.object.GameObject;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;
import static com.ferox.game.content.collection_logs.LogType.KEYS;
import static com.ferox.util.CustomItemIdentifiers.*;

public class EnchantedChestP extends PacketInteraction {
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if (obj.getId() == 4125) {
            player.teleblock( 500, true);
            Skulling.assignSkullState(player, SkullType.RED_SKULL);
            if (player.hasPetOut("Deranged archaeologist")) {
                Skulling.assignSkullState(player, SkullType.NO_SKULL);
            }
            if (player.skills().combatLevel() < 126) {
                player.message(Color.RED.wrap("You need to be at least level 126 to open this chest."));
                return true;
            }
            if (player.inventory().contains(ENCHANTED_KEY_II)) {
                open(player, ENCHANTED_KEY_II);
            } else {
                player.message("This enchanted chest wont budge, I think I need to find a key that fits.");
            }
            return true;
        }
        return false;
    }
    private static void open(Player player, int key) {
        if(!player.inventory().contains(key)) {
            return;
        }
        player.animate(536);
        player.lock();
        Chain.bound(player).runFn(1, () -> {
            player.inventory().remove(new Item(key, 1), true);
            int roll = Utils.percentageChance(player.extraItemRollChance()) ? 2 : 1;
            for (int i = 0; i < roll; i++) {
                Item reward = EnchantedChestPLootTable.rewardTables(key);

                if (reward == null)
                    return;

                //Collection logs
                KEYS.log(player, key, reward);
                //Send a world message that someone opened the enchanted chest
                World.getWorld().sendWorldMessage("<img=505>[<col=" + Color.MEDRED.getColorValue() + ">Enchanted chest</col>]: " + "<col=1e44b3>" + player.getUsername() + " has just looted the purple enchanted chest!");

                //When we receive a rare loot send a world message
                if (reward.getValue() >= 5000) {
                    boolean amOverOne = reward.getAmount() > 1;
                    String amtString = amOverOne ? "x " + Utils.format(reward.getAmount()) + "" : Utils.getAOrAn(reward.name());
                    String msg = "<img=505>[<col=" + Color.MEDRED.getColorValue() + ">Enchanted chest</col>]: " + "<col=1e44b3>" + player.getUsername() + " has received " + amtString + " " + reward.unnote().name() + "!";
                    World.getWorld().sendWorldMessage(msg);
                }
                player.inventory().addOrDrop(reward);
            }

            if (key == ENCHANTED_KEY_II) {
                int keysUsed = (Integer) player.getAttribOr(AttributeKey.ENCHANTED_KEYS_P_OPENED, 0) + 1;
                player.putAttrib(AttributeKey.ENCHANTED_KEYS_P_OPENED, keysUsed);
            }

            //Update achievements
            AchievementsManager.activate(player, Achievements.ENCHANTED_LOOTER_P_I, 1);
            AchievementsManager.activate(player, Achievements.ENCHANTED_LOOTER_P_II, 1);
            AchievementsManager.activate(player, Achievements.ENCHANTED_LOOTER_P_III, 1);
            player.unlock();
        });
    }
}

