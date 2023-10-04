package com.ferox.game.content.items.combine;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;

import static com.ferox.util.CustomItemIdentifiers.*;

/**
 * @author Patrick van Elderen | July, 12, 2021, 21:08
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class ImbuedCapes extends PacketInteraction {

    @Override
    public boolean handleItemOnItemInteraction(Player player, Item use, Item usedWith) {
        if ((use.getId() == 21797 || usedWith.getId() == 2412)) {
            player.inventory().remove(new Item(21797), true);
            player.inventory().remove(new Item(2412), true);
            player.inventory().add(new Item(24248), true);
            return true;
        }
        if ((use.getId() == 2412 || usedWith.getId() == 21797)) {
            player.inventory().remove(new Item(21797), true);
            player.inventory().remove(new Item(2412), true);
            player.inventory().add(new Item(24248), true);
            return true;
        }
        if ((use.getId() == 2414 || usedWith.getId() == 21799)) {
            player.inventory().remove(new Item(21799), true);
            player.inventory().remove(new Item(2414), true);
            player.inventory().add(new Item(23605), true);
            return true;
        }
        if ((use.getId() == 21799 || usedWith.getId() == 2414)) {
            player.inventory().remove(new Item(21799), true);
            player.inventory().remove(new Item(2414), true);
            player.inventory().add(new Item(23605), true);
            return true;
        }
        if ((use.getId() == 21798 || usedWith.getId() == 2413)) {
            player.inventory().remove(new Item(2413), true);
            player.inventory().remove(new Item(21798), true);
            player.inventory().add(new Item(24249), true);
            return true;
        }
        if ((use.getId() == 2413 || usedWith.getId() == 21798)) {
            player.inventory().remove(new Item(2413), true);
            player.inventory().remove(new Item(21798), true);
            player.inventory().add(new Item(24249), true);
            return true;
        }
        return false;
    }
}
