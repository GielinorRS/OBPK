package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.GameServer;
import com.ferox.db.transactions.CollectVotes;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.util.Color;
import org.w3c.dom.Attr;

import static com.ferox.game.world.entity.AttributeKey.BOSS_POINTS;
import static com.ferox.game.world.entity.AttributeKey.VOTE_POINS;

/**
 * @author Patrick van Elderen | May, 29, 2021, 11:13
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class ClaimVoteCommand implements Command {

    private long lastCommandUsed;

    @Override
    public void execute(Player player, String command, String[] parts) {
        var current_mac = player.<String>getAttribOr(AttributeKey.MAC_ADDRESS, "invalid");
        if(current_mac.equalsIgnoreCase("invalid") || current_mac.isEmpty()) {
            player.message("You're not connected to a real machine and there for cannot claim votes.");
            return;
        }
                String[] args = command.split(" ");
                if (args.length == 1) {
                    player.message("Please use [::reward id], [::reward id amount], or [::reward id all].");
                    return;
                }
                final String playerName = player.getUsername();
                final String id = args[1];
                final String amount = args.length == 3 ? args[2] : "1";

                com.everythingrs.vote.Vote.service.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            com.everythingrs.vote.Vote[] reward = com.everythingrs.vote.Vote.reward("LFKiZ9zlsbHpUS6XzFbWaXxODdSovzZYVzMr1bNellehH08vpFK1tDV2mOThCI9ZrQnF0pPc",
                                playerName, id, amount);
                            if (reward[0].message != null) {
                                player.message(Color.RED.wrap(reward[0].message));
                                return;
                            }
                            player.inventory().add(reward[0].reward_id, reward[0].give_amount);
             //               player.putAttrib(VOTE_POINS, 1);
                            player.message(
                                "Thank you for voting! You now have " + reward[0].vote_points + " vote points.");
                        } catch (Exception e) {
                            player.message("Api Services are currently offline. Please check back shortly");
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
