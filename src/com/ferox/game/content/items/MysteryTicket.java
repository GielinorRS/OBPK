package com.ferox.game.content.items;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.items.Item;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;
import com.ferox.util.Utils;

import java.util.Arrays;
import java.util.List;

import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;

public class MysteryTicket extends PacketInteraction {

    private final List<Item> ITEM_FORGES = Arrays.asList(
        new Item(ARMADYL_GODSWORD_OR),
        new Item(DRAGON_CLAWS_OR),
        new Item(TOXIC_STAFF_OF_THE_DEAD_C),
        new Item(RING_OF_PRECISION),
        new Item(RING_OF_SORCERY),
        new Item(RING_OF_MANHUNTING),
        new Item(ANCIENT_FACEGAURD),
        new Item(AMULET_OF_TORTURE_OR),
        new Item(NECKLACE_OF_ANGUISH_OR),
        new Item(OCCULT_NECKLACE_OR),
        new Item(PEGASIAN_BOOTS_OR),
        new Item(PRIMORDIAL_BOOTS_OR),
        new Item(ETERNAL_BOOTS_OR)

    );

    @Override
    public boolean handleItemInteraction(Player player, Item item, int option) {
        if(option == 1) {
            if(item.getId() == MYSTERY_TICKET) {
                String worldMessage = "";
                player.inventory().remove(new Item(MYSTERY_TICKET),true);
                if(World.getWorld().rollDie(40,1)) {
                    player.inventory().addOrBank(new Item(MYSTERY_CHEST));
                    Utils.sendDiscordInfoLog("Player " + player.getUsername() + " received a Mystery chest from a mystery ticket.", "box_and_tickets");
                    worldMessage = "<img=505><shad=0>[<col=" + Color.MEDRED.getColorValue() + ">Mystery ticket</col>]</shad>:<col=AD800F> " + player.getUsername() + " received a <shad=0>Mystery chest</shad>!";
                } else if(World.getWorld().rollDie(25,1)) {
                    Item randomForge = Utils.randomElement(ITEM_FORGES);
                    player.inventory().addOrBank(randomForge);
                    Utils.sendDiscordInfoLog("Player " + player.getUsername() + " received a "+randomForge.name()+" from a mystery ticket.", "box_and_tickets");
                    worldMessage = "<img=505><shad=0>[<col=" + Color.MEDRED.getColorValue() + ">Mystery ticket</col>]</shad>:<col=AD800F> " + player.getUsername() + " received a <shad=0>" + randomForge.name() + "</shad>!";
                } else if(World.getWorld().rollDie(15,1)) {
                    player.inventory().addOrBank(new Item(EPIC_PET_BOX));
                    Utils.sendDiscordInfoLog("Player " + player.getUsername() + " received a Epic pet box from a mystery ticket.", "box_and_tickets");
                    worldMessage = "<img=505><shad=0>[<col=" + Color.MEDRED.getColorValue() + ">Mystery ticket</col>]</shad>:<col=AD800F> " + player.getUsername() + " received a <shad=0>Epic pet mystery box</shad>!";
                }  else {
                    Utils.sendDiscordInfoLog("Player " + player.getUsername() + " received a Legendary mystery box from a mystery ticket.", "box_and_tickets");
                    player.inventory().addOrBank(new Item(LEGENDARY_MYSTERY_BOX));
                }
                if(!worldMessage.isEmpty() && !player.getUsername().equalsIgnoreCase("Box test")) {
                    World.getWorld().sendWorldMessage(worldMessage);
                }
                return true;
            }
        }
        return false;
    }
}
