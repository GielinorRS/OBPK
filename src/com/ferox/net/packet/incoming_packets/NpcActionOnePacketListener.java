package com.ferox.net.packet.incoming_packets;

import com.ferox.game.content.bank_pin.BankTeller;
import com.ferox.game.content.packet_actions.interactions.npcs.NPCActions;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.route.routes.TargetRoute;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;

import java.lang.ref.WeakReference;

import static com.ferox.util.NpcIdentifiers.IRON_MAN_TUTOR;

/**
 * @author PVE
 * @Since augustus 27, 2020
 */
public class NpcActionOnePacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        int index = packet.readLEShort();
        Npc npc = World.getWorld().getNpcs().get(index);

        handleNpcClicks(player, npc, 1);
    }

    public static void handleNpcClicks(Player player, Npc npc, int option) {
        if (npc == null || player.locked() || player.dead()) {
            return;
        }
        player.stopActions(false);
        player.face(npc.tile());
        player.debugMessage("Option "+option+" click npc id: " + npc.id());
        if (npc.dead())
            return;

        player.putAttrib(AttributeKey.TARGET, new WeakReference<Mob>(npc));
        player.putAttrib(AttributeKey.INTERACTION_OPTION, option);
        player.setEntityInteraction(npc);

        //Do actions
        npc.getMovementQueue().setBlockMovement(true);
        int size = npc.getSize();
        Runnable bankerAction = BankTeller.bankerDialogue(player, npc);
        if (bankerAction != null) {
            size++;
        }

        // override tile to walk to for shops at edge
        Tile dest = npc.tile();
        if (dest.y == 3513 && dest.x >= 3075 && dest.x <= 3085)
            dest = dest.transform(0, -1);
        else if (dest.x == 3077 && dest.y == 3507)
            dest = dest.transform(0, 1);
        else if (dest.x == 3076 && dest.y >= 3507 && dest.y <= 3514)
            dest = dest.transform(1, 0);

        // recalc path, overriding the one from the client in MovementPacketHandler
        // MovementPacket is sent when clicking edge general store npcs, but Not for krystilia in the edgevill eslayer hut. wtf.
        if (dest != npc.tile() || (!player.getMovementQueue().movementPacketThisCycle() && !npc.getMobName().toLowerCase().equals("krystilia"))) {
            npc.walkTo = dest;
            player.debug(npc.getMobName()+" walkTo overridden to "+npc.walkTo);
        }

        TargetRoute.set(player, npc, () -> {
            if (bankerAction != null) {
                bankerAction.run();
                return;
            }

            player.setInteractingNpcId(npc.id());
            NPCActions.handleAction(player, npc, option);
            player.face(npc.tile());
            npc.getMovementQueue().setBlockMovement(false);
        });
    }
}
