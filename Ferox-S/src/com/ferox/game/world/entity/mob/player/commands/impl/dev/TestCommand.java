
package com.ferox.game.world.entity.mob.player.commands.impl.dev;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.items.Item;
import com.ferox.util.Varbit;

import static com.ferox.util.ItemIdentifiers.GIANT_PRESENT;

public class TestCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        //player.varps().varbit(Varbit.FALADOR_COMPOST_BIN_FULLNESS,player.varps().varbit(Varbit.FALADOR_COMPOST_BIN_FULLNESS) + 6);
        player.getSackOfPresents().pickup(new Item(GIANT_PRESENT), new Npc(2005, player.tile()));
        //System.out.println(player.pet().def().name);
        player.message("Test command has been activated.");
    }

    @Override
    public boolean canUse(Player player) {
        return (player.getPlayerRights().isDeveloperOrGreater(player));
    }

}
