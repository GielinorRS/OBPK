package com.ferox.game.content.items;

import com.ferox.game.content.packet_actions.interactions.items.ItemOnItem;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.util.CustomItemIdentifiers.CORRUPTING_STONE;
import static com.ferox.util.CustomItemIdentifiers.CORRUPTED_CRYSTAL_BODY;
import static com.ferox.util.ItemIdentifiers.CRYSTAL_BODY;

public class CorruptedCrystalBody extends PacketInteraction{
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        if ((use.getId() == CORRUPTING_STONE || usedWith.getId() == CORRUPTING_STONE) && (use.getId() == CRYSTAL_BODY || usedWith.getId() == CRYSTAL_BODY)) {
            player.optionsTitled("Would you like to combine the stone with your body?", "Yes", "No", () -> {
                if (!player.inventory().containsAll(CORRUPTING_STONE, CRYSTAL_BODY)) {
                    return;
                }
                player.inventory().remove(new Item(CORRUPTING_STONE), true);
                player.inventory().remove(new Item(CRYSTAL_BODY), true);
                player.inventory().add(new Item(CORRUPTED_CRYSTAL_BODY), true);
            });
            return true;
        }
        return false;
    }
}
