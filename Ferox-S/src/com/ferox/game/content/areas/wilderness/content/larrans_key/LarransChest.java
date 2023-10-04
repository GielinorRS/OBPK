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

/**
 * @author Patrick van Elderen | February, 17, 2021, 14:17
 * @see <a href="https://www.rune-server.ee/members/Zerikoth/">Rune-Server profile</a>
 */
public class LarransChest extends PacketInteraction {

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if (obj.getId() == 34832) {
            if (player.skills().combatLevel() < 3 && !player.ironMode().ironman()) {
                player.message(Color.RED.wrap("You need to be at least level 126 to open this chest."));
                return true;
            }
            if (player.inventory().contains(LARRANS_KEY_TIER_I)) {
                open(player, LARRANS_KEY_TIER_I);
            } else if (player.inventory().contains(LARRANS_KEY_TIER_II)) {
                open(player, LARRANS_KEY_TIER_II);
            } else if (player.inventory().contains(LARRANS_KEY_TIER_III)) {
                player.confirmDialogue(new String[]{"Are you sure you wish to open the chest?", "You will be " + Color.RED.wrap("red") + " skulled if you proceed."}, "", "proceed to open the chest.", "Nevermind.", () -> {
                    if(!player.inventory().contains(LARRANS_KEY_TIER_III)) {
                        return;
                    }
                    open(player, LARRANS_KEY_TIER_III);
                });
            } else {
                player.message("This Larran's big chest wont budge, I think I need to find a key that fits.");
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
                Item reward = LarransKeyLootTable.rewardTables(key);

                if (reward == null)
                    return;

                //Collection logs
                KEYS.log(player, key, reward);

                //Send a world message that someone opened the Larran's chest
                World.getWorld().sendWorldMessage("<img=505>[<col=" + Color.MEDRED.getColorValue() + ">Larran's chest</col>]: " + "<col=1e44b3>" + player.getUsername() + " has just looted the Larran's chest with a Larran's key!");

                //When we receive a rare loot send a world message
                if (reward.getValue() >= 30_000) {
                    boolean amOverOne = reward.getAmount() > 1;
                    String amtString = amOverOne ? "x " + Utils.format(reward.getAmount()) + "" : Utils.getAOrAn(reward.name());
                    String msg = "<img=505>[<col=" + Color.MEDRED.getColorValue() + ">Larran's chest</col>]: " + "<col=1e44b3>" + player.getUsername() + " has received " + amtString + " " + reward.unnote().name() + "!";
                    World.getWorld().sendWorldMessage(msg);
                }
                player.inventory().addOrDrop(reward);
            }

            //Give half a teleblock for tier I and a full for tier II and III when opening the Larran's chest.
            player.teleblock(key == LARRANS_KEY_TIER_I ? 250 : 500, true);

            if (key == LARRANS_KEY_TIER_I) {
                int keysUsed = (Integer) player.getAttribOr(AttributeKey.LARRANS_KEYS_TIER_ONE_USED, 0) + 1;
                player.putAttrib(AttributeKey.LARRANS_KEYS_TIER_ONE_USED, keysUsed);
            }

            if (key == LARRANS_KEY_TIER_II) {
                int keysUsed = (Integer) player.getAttribOr(AttributeKey.LARRANS_KEYS_TIER_TWO_USED, 0) + 1;
                player.putAttrib(AttributeKey.LARRANS_KEYS_TIER_TWO_USED, keysUsed);
            }

            //Tier III also gives out a redskull, high risk high reward!
            if (key == LARRANS_KEY_TIER_III) {
                Skulling.assignSkullState(player, SkullType.RED_SKULL);
                if (player.hasPetOut("Deranged archaeologist")) {
                    Skulling.assignSkullState(player, SkullType.NO_SKULL);
                }

                int keysUsed = (Integer) player.getAttribOr(AttributeKey.LARRANS_KEYS_TIER_THREE_USED, 0) + 1;
                player.putAttrib(AttributeKey.LARRANS_KEYS_TIER_THREE_USED, keysUsed);
            }

            //Update achievements
            AchievementsManager.activate(player, Achievements.LARRANS_LOOTER_I, 1);
            AchievementsManager.activate(player, Achievements.LARRANS_LOOTER_II, 1);
            AchievementsManager.activate(player, Achievements.LARRANS_LOOTER_III, 1);
            player.unlock();
        });
    }
}
