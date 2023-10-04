package com.ferox.net.packet.incoming_packets;

import com.ferox.GameServer;
import com.ferox.game.content.items.RottenPotato;
import com.ferox.game.content.packet_actions.interactions.items.ItemActionThree;
import com.ferox.game.world.InterfaceConstants;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.Packet;
import com.ferox.net.packet.PacketListener;

import static com.ferox.util.CustomItemIdentifiers.HOLY_SCYTHE_OF_VITUR;
import static com.ferox.util.CustomItemIdentifiers.TWISTED_BOW_I;
import static com.ferox.util.ItemIdentifiers.ROTTEN_POTATO;
import static com.ferox.util.ItemIdentifiers.SCYTHE_OF_VITUR;

/**
 * @author PVE
 * @Since augustus 27, 2020
 */
public class ItemActionThreePacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        final int itemId = packet.readShortA();
        final int slot = packet.readLEShortA();
        final int interfaceId = packet.readLEShortA();

        player.debugMessage(String.format("Third item action, itemId: %d slot: %d interfaceId: %d", itemId, slot, interfaceId));

        if (slot < 0 || slot > 27)
            return;
        Item item = player.inventory().get(slot);
        if (item != null && item.getId() == itemId) {

            if(item.getId() == ROTTEN_POTATO) {
                RottenPotato.onItemOption3(player);
                return;
            }
            if(itemId == 30183){//updatet3
                player.getDialogueManager().start(new Dialogue() {
                    @Override
                    protected void start(Object... parameters) {
                        send(DialogueType.OPTION, "Un-Attach?", "Yes, Un-Attach Twisted bow i.", "No, not right now.");
                        setPhase(0);
                    }

                    @Override
                    protected void select(int option) {
                        if(isPhase(0)) {
                            if(option == 1) {
                                if(!player.inventory().containsAll(TWISTED_BOW_I)) {
                                    stop();
                                    return;
                                }
                                player.inventory().remove(new Item(TWISTED_BOW_I), true);
                                player.inventory().add(new Item(29103), true);
                                player.inventory().add(new Item(20997), true);

                                player.message("You Un-attached twisted bow i and received Twisted bow, Twisted bow Kit I");
                                stop();
                            } else if(option == 2) {
                                stop();
                            }
                        }
                    }
                });
            }
            if(itemId == 25736){
                player.getDialogueManager().start(new Dialogue() {
                    @Override
                    protected void start(Object... parameters) {
                        send(DialogueType.OPTION, "Un-Attach?", "Yes, Un-Attach Holy Scythe.", "No, not right now.");
                        setPhase(0);
                    }

                    @Override
                    protected void select(int option) {
                        if(isPhase(0)) {
                            if(option == 1) {
                                if(!player.inventory().containsAll(HOLY_SCYTHE_OF_VITUR)) {
                                    stop();
                                    return;
                                }
                                player.inventory().remove(new Item(HOLY_SCYTHE_OF_VITUR), true);
                                player.inventory().add(new Item(SCYTHE_OF_VITUR), true);
                                player.inventory().add(new Item(29102), true);

                                player.message("You Un-attached Holy Scythe and received Scythe of Vitur , Scythe Kit ");
                                stop();
                            } else if(option == 2) {
                                stop();
                            }
                        }
                    }
                });
            }
            if (player.locked() || player.dead()) {
                return;
            }

            if (player.busy()) {
                return;
            }

            if (!player.getBankPin().hasEnteredPin() && GameServer.properties().requireBankPinOnLogin) {
                player.getBankPin().openIfNot();
                return;
            }

            if(player.askForAccountPin()) {
                player.sendAccountPinMessage();
                return;
            }

            player.afkTimer.reset();

            player.stopActions(false);
            player.putAttrib(AttributeKey.ITEM_SLOT, slot);
            player.putAttrib(AttributeKey.FROM_ITEM, player.inventory().get(slot));
            player.putAttrib(AttributeKey.ITEM_ID, item.getId());

            if (interfaceId == InterfaceConstants.INVENTORY_INTERFACE) {
                ItemActionThree.click(player, item);
            }
        }
    }
}
