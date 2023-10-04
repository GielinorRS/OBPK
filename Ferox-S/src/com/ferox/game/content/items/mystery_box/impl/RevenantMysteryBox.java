package com.ferox.game.content.items.mystery_box.impl;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.Utils;

import static com.ferox.util.CustomItemIdentifiers.REVENANT_MYSTER_BOX;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 15, 2021
 */
public class RevenantMysteryBox extends PacketInteraction {

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if (option == 1) {
            if (item.getId() == REVENANT_MYSTER_BOX) {
                open(player);
                return true;
            }
        }
        return false;
    }

    private void open(Player player) {
        if (player.inventory().contains(REVENANT_MYSTER_BOX)) {
            player.inventory().remove(new Item(REVENANT_MYSTER_BOX), true);
            Item reward = rollReward();
            player.inventory().add(reward, true);
            Utils.sendDiscordInfoLog("Player " + player.getUsername() + " received a "+reward.unnote().name()+" from a revenant mystery box.", "box_and_tickets");
        }
    }

    private static final Item[] COMMON = new Item[]{
        new Item(ItemIdentifiers.VIGGORAS_CHAINMACE),
        new Item(ItemIdentifiers.CRAWS_BOW),
        new Item(ItemIdentifiers.THAMMARONS_SCEPTRE),
        new Item(ItemIdentifiers.AMULET_OF_AVARICE),
        new Item(ItemIdentifiers.STATIUSS_FULL_HELM),
        new Item(ItemIdentifiers.STATIUSS_PLATEBODY),
        new Item(ItemIdentifiers.STATIUSS_PLATELEGS),
        new Item(ItemIdentifiers.STATIUSS_WARHAMMER),
        new Item(ItemIdentifiers.VESTAS_CHAINBODY),
        new Item(ItemIdentifiers.VESTAS_PLATESKIRT),
        new Item(ItemIdentifiers.VESTAS_LONGSWORD),
        new Item(ItemIdentifiers.MORRIGANS_COIF),
        new Item(ItemIdentifiers.MORRIGANS_LEATHER_BODY),
        new Item(ItemIdentifiers.MORRIGANS_LEATHER_CHAPS),
        new Item(ItemIdentifiers.ZURIELS_HOOD),
        new Item(ItemIdentifiers.ZURIELS_ROBE_TOP),
        new Item(ItemIdentifiers.ZURIELS_ROBE_BOTTOM),
        new Item(ItemIdentifiers.ZURIELS_STAFF),


    };

    public Item rollReward() {
        return Utils.randomElement(COMMON);
    }
}
