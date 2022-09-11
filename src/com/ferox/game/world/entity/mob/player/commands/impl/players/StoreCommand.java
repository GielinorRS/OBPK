package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.GameServer;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

public class StoreCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        player.getPacketSender().sendURL("https://revolutionps.everythingrs.com/services/store/");
        player.message("Opening https://revolutionps.everythingrs.com/services/store/ in your web browser...");
        /*if(GameServer.properties().promoEnabled) {
            player.getPaymentPromo().open();
        }*/
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
