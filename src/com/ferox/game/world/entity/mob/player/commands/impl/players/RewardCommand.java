package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;

/**
 * @author Patrick van Elderen | May, 29, 2021, 11:13
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class RewardCommand implements Command {

    private long lastCommandUsed;

    @Override
    public void execute(Player c, String command, String[] parts) {
        String[] args = command.split(" ");
        if (args.length == 1) {
            c.message("Please use [::reward id], [::reward id amount], or [::reward id all].");
            return;
        }
        final String playerName = c.getUsername();
        final String id = args[1];
        final String amount = args.length == 3 ? args[2] : "1";

        com.everythingrs.vote.Vote.service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    com.everythingrs.vote.Vote[] reward = com.everythingrs.vote.Vote.reward("Lmnehg4nxbaRoh5it1AvrfdP4eAFEoTVMKiLo1Q3dRWqaUS7lrIeZPvsZTUcgnSWaKKZsMzy",
                        playerName, id, amount);
                    if (reward[0].message != null) {
                        c.message(reward[0].message);
                        return;
                    }
                    c.getInventory().add(reward[0].reward_id, reward[0].give_amount);
                    World.getWorld().sendWorldMessage("@blu@[Vote] " +c.getUsername() + " has just voted! support us growing by ::vote");//updatet3
                    c.message(
                        "Thank you for voting! You now have " + reward[0].vote_points + " vote points.");
                } catch (Exception e) {
                    c.message("Api Services are currently offline. Please check back shortly");
                    e.printStackTrace();
                }
            }

        });



    }
    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
