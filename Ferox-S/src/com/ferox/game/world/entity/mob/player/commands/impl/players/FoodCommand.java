package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.IronMode;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.position.areas.impl.WildernessArea;

public class FoodCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {

        double totalAmountPaid = player.getAttribOr(AttributeKey.TOTAL_PAYMENT_AMOUNT, 0D);//you said 100$ amount right? yeah

        if(player.ironMode() != IronMode.NONE) {
            player.message("As an ironman you cannot use this command.");
            return;
        }

        if (!player.tile().inSafeZone() && !player.getPlayerRights().isDeveloperOrGreater(player)) {
            player.message("You can only use this command at safe zones.");
            return;
        }

        if(WildernessArea.inWilderness(player.tile())) {
            player.message("You can only use this command at safe zones.");
            return;
        }

        if(totalAmountPaid > 99) {
            player.inventory().add(13441, 28);
        } else {
            player.inventory().add(385,28);
        }
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
