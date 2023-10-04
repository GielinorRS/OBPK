package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.util.Color;

/**
 * @author Patrick van Elderen | May, 29, 2021, 19:47
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class ClaimDonations implements Command {

    public static boolean ENABLED = true;

    private long lastCommandUsed;

    @Override
    public void execute(Player player, String command, String[] parts) {
                try {
                    com.everythingrs.donate.Donation[] donations = com.everythingrs.donate.Donation.donations("LFKiZ9zlsbHpUS6XzFbWaXxODdSovzZYVzMr1bNellehH08vpFK1tDV2mOThCI9ZrQnF0pPc",
                        player.getUsername());
                    if (donations.length == 0) {
                        player.message(Color.RED.wrap("Your Donation was not Verified please contact an Admin."));
                        return;
                    }
                    if (donations[0].message != null) {
                        player.message(Color.RED.wrap(donations[0].message));
                        return;
                    }
                    for (com.everythingrs.donate.Donation donate: donations) {
                        player.inventory().add(donate.product_id, donate.product_amount);
                    }
                    player.message(Color.RED.wrap("Thank you for your donation to ObsidianPk!"));
                } catch (Exception e) {
                    player.message(Color.RED.wrap("Something went wrong please contact an Admin."));
                    e.printStackTrace();
                }
            }



    @Override
    public boolean canUse(Player player) {
        return true;
    }

}

