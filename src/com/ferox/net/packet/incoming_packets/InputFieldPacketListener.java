package com.ferox.net.packet.incoming_packets;

import com.ferox.game.content.clan.ClanManager;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;
import com.ferox.util.Color;

public class InputFieldPacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        final int component = packet.readInt();
        final String context = packet.readString();

        if (component < 0) {
            return;
        }

        player.debugMessage("[InputField] - Text: " + context + " Component: " + component);
        
        switch (component) {
            /* Clan Chat */
            case 47828:
                ClanManager.kickMember(player, context);
                break;
            case 47830:
                if (World.getWorld().getPlayerByName(context).isPresent()) {
                    Player other = World.getWorld().getPlayerByName(context).get();
                    player.setClanPromote(other.getUsername());
                    player.message("You are now promoting "+ Color.RED.wrap(other.getUsername())+"</col>.");
                }
                break;
            case 47843:
                ClanManager.changeSlogan(player, context);
                break;
            case 47845:
                int amount = context.length() == 0 ? 0 : Integer.parseInt(context);
                ClanManager.setMemberLimit(player, amount);
                break;
        }
    }

}
