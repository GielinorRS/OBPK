
package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.GameServer;
import com.ferox.db.NewStore;
import com.ferox.db.Vote;
import com.ferox.db.transactions.CollectVotes;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;


/**
 * @author Patrick van Elderen | May, 29, 2021, 11:13
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */


public class ClaimCommand implements Command {

    private long lastCommandUsed;

    @Override
    public void execute(Player c, String command, String[] parts) {

        new java.lang.Thread() {
            public void run() {
                try {
                    com.everythingrs.donate.Donation[] donations = com.everythingrs.donate.Donation.donations("Lmnehg4nxbaRoh5it1AvrfdP4eAFEoTVMKiLo1Q3dRWqaUS7lrIeZPvsZTUcgnSWaKKZsMzy",
                        c.getUsername());
                    if (donations.length == 0) {
                        c.message("You currently don't have any items waiting. You must donate first!");
                        return;
                    }
                    if (donations[0].message != null) {
                        c.message(donations[0].message);
                        return;
                    }
                    for (com.everythingrs.donate.Donation donate: donations) {
                        c.getInventory().add(donate.product_id, donate.product_amount);
                    }
                    c.message("Thank you for donating!");
                } catch (Exception e) {
                    c.message("Api Services are currently offline. Please check back shortly");
                    e.printStackTrace();
                }
            }
        }.start();
    }


    @Override
    public boolean canUse(Player player) {
        return true;
    }
}

