package com.ferox.game.world.entity.mob.player.commands.impl.VIP;
import com.ferox.game.content.teleport.TeleportType;
import com.ferox.game.content.teleport.Teleports;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.position.areas.impl.WildernessArea;
import com.ferox.util.Color;

public class wDie implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {
        if (WildernessArea.inWilderness(player.tile()) && !player.getPlayerRights().isDeveloperOrGreater(player)) {
            player.message("You can't use this command in the wilderness.");
            return;
        }
        if (Teleports.canTeleport(player,true, TeleportType.GENERIC)) {
            Teleports.basicTeleport(player, new Tile(2526, 3859));
            player.message(Color.BLUE.wrap("Welcome to the west donator island Expansion!"));
        }
    }

    @Override
    public boolean canUse(Player player) {
        return player.getMemberRights().isSponsorOrGreater(player) || player.getPlayerRights().isStaffMember(player);
    }
}
