package com.ferox.game.content.items;

import com.ferox.game.content.packet_actions.interactions.items.ItemOnItem;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.util.CustomItemIdentifiers.CORRUPTING_STONE;
import static com.ferox.util.ItemIdentifiers.CORRUPTED_YOUNGLLEF;
import static com.ferox.util.ItemIdentifiers.YOUNGLLEF;

public class CorruptingStone extends PacketInteraction {

    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        if ((use.getId() == CORRUPTING_STONE || usedWith.getId() == CORRUPTING_STONE) && (use.getId() == YOUNGLLEF || usedWith.getId() == YOUNGLLEF)) {
            player.optionsTitled("Would you like to combine the stone with your pet?", "Yes", "No", () -> {
                if (!player.inventory().containsAll(CORRUPTING_STONE, YOUNGLLEF)) {
                    return;
                }
                player.inventory().remove(new Item(CORRUPTING_STONE), true);
                player.inventory().remove(new Item(YOUNGLLEF), true);
                player.inventory().add(new Item(CORRUPTED_YOUNGLLEF), true);
            });
            return true;
        }
        return false;
    }
}

