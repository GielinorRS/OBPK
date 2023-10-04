package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.GameConstants;
import com.ferox.game.content.consumables.potions.impl.OverloadPotion;
import com.ferox.game.content.raids.chamber_of_xeric.ChamberOfXericReward;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.entity.mob.player.save.PlayerSave;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.position.areas.impl.WildernessArea;
import com.ferox.net.packet.incoming_packets.AttackPlayerPacketListener;

import static com.ferox.game.content.collection_logs.CollectionLog.RAIDS_KEY;
import static com.ferox.game.content.collection_logs.LogType.OTHER;
import static com.ferox.util.CustomItemIdentifiers.FENRIR_GREYBACK_JR;
import static com.ferox.util.CustomItemIdentifiers.SCYTHE_OF_VITUR_KIT;

public class DiscordCommand implements Command {

    @Override
    public void execute(Player player, String command, String[] parts) {

      player.getPacketSender().sendURL("https://discord.com/invite/dEUZcGVjmr/");


    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }

}
