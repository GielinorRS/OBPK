package com.ferox.game.content.items;

import com.ferox.game.content.packet_actions.interactions.items.ItemOnItem;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.util.CustomItemIdentifiers.CORRUPTING_STONE;
import static com.ferox.util.CustomItemIdentifiers.CORRUPTED_CRYSTAL_LEGS;
import static com.ferox.util.ItemIdentifiers.CRYSTAL_LEGS;

public class CorruptedCrystalLegs extends PacketInteraction{
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        if ((use.getId() == CORRUPTING_STONE || usedWith.getId() == CORRUPTING_STONE) && (use.getId() == CRYSTAL_LEGS || usedWith.getId() == CRYSTAL_LEGS)) {
            player.optionsTitled("Would you like to combine the stone with your legs?", "Yes", "No", () -> {
                if (!player.inventory().containsAll(CORRUPTING_STONE, CRYSTAL_LEGS)) {
                    return;
                }
                player.inventory().remove(new Item(CORRUPTING_STONE), true);
                player.inventory().remove(new Item(CRYSTAL_LEGS), true);
                player.inventory().add(new Item(CORRUPTED_CRYSTAL_LEGS), true);
            });
            return true;
        }
        return false;
    }
}
