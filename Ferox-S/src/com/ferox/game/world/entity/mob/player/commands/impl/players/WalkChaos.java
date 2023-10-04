package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.util.FileUtil;

import java.util.HashSet;
import java.util.Set;

import static com.ferox.game.world.entity.AttributeKey.GAME_TIME;
import static com.ferox.game.world.entity.AttributeKey.MAC_ADDRESS;

public class WalkChaos implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        boolean walkchaos = player.getAttribOr(AttributeKey.WALKCHAOS, false);
        var IP = player.getHostAddress();
        var MAC = player.<String>getAttribOr(AttributeKey.MAC_ADDRESS,"invalid");
        Set<String> claimedIP = new HashSet<>(), claimedMac = new HashSet<>();

        if (walkchaos) {
            player.message("You have already used this command!");
        } else {
            player.putAttrib(AttributeKey.WALKCHAOS, true);
            player.getPacketSender().sendURL("https://www.youtube.com/watch?v=cW03JGTnhOM");
            player.inventory().add(6199, 1);
            player.inventory().add(13307, 10000);
            FileUtil.addAddressToClaimedList(IP, MAC, claimedIP, claimedMac, "./data/youtubers/walkchaos.txt");
            player.message("Opening WalkChaos's channel in your web browser...");
        }



    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
