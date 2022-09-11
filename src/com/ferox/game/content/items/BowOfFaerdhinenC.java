package com.ferox.game.content.items;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.util.CustomItemIdentifiers.BOW_OF_FAERDHINEN;
import static com.ferox.util.CustomItemIdentifiers.CORRUPTING_STONE;
import static com.ferox.util.CustomItemIdentifiers.BOW_OF_FAERDHINEN_3;

public class BowOfFaerdhinenC extends PacketInteraction {
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        if ((use.getId() == CORRUPTING_STONE || usedWith.getId() == CORRUPTING_STONE) && (use.getId() == BOW_OF_FAERDHINEN_3 || usedWith.getId() == BOW_OF_FAERDHINEN_3)) {
            player.optionsTitled("Would you like to combine the stone with your bow?", "Yes", "No", () -> {
                if (!player.inventory().containsAll(CORRUPTING_STONE, BOW_OF_FAERDHINEN_3)) {
                    return;
                }
                player.inventory().remove(new Item(CORRUPTING_STONE), true);
                player.inventory().remove(new Item(BOW_OF_FAERDHINEN_3), true);
                player.inventory().add(new Item(BOW_OF_FAERDHINEN), true);
            });
            return true;
        }
        return false;
    }
}
