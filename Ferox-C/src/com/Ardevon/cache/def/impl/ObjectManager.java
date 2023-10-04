package com.Ardevon.cache.def.impl;

import com.Ardevon.cache.def.ObjectDefinition;

public class ObjectManager {

    public static void get(int id) {
        ObjectDefinition definition = ObjectDefinition.get(id);

        if(id == 27095) {
            definition.name = "Portal";
        }

        if(id == 32629) {
            definition.interactions = new String[]{"Loot", null, null, null, null};
        }

        if (id == 2341 || id == 2342 || id == 17977) {
            definition.interactions = new String[]{null, null, null, null, null};
        }

        if (id == 6437) {
            definition.name = "Tom Riddle’s gravestone";
            definition.interactions = new String[]{"Reward", null, null, null, null};
        }

        if (id == 33456) {
            definition.name = "Portkey";
            definition.interactions = new String[]{"Apparate", null, null, null, null};
        }

        if (id == 13503) {
            definition.interactions = new String[]{"Leave", null, null, null, null};
        }

        if (id == 50004) {
            definition.name = "Dark rejuvenation pool";
            definition.interactions = new String[]{"Drink", null, null, null, null};
            definition.ambientLighting = 40;
            definition.animation = 7304;
            definition.solid = true;
            definition.isInteractive = 1;
            definition.objectSizeY = 2;
            definition.modelIds = new int[]{58959};
            definition.delayShading = true;
            definition.supportItems = 1;
            definition.objectSizeX = 2;
        }

        if (id == 31621) {
            definition.name = "50s";
        }

        if (id == 31622) {
            definition.name = "Member cave";
        }

        if (id == 31618) {
            definition.name = "gdz";
        }

        if(id == 2515) {
            definition.interactions = new String[] {"Travel", null, null, null, null};
        }

        if(id == 10060 || id == 7127 || id == 31626 || id == 4652 || id == 4653) {
            definition.interactions = new String[] {null, null, null, null, null};
        }

        if(id == 562 || id == 3192) {
            definition.interactions = new String[] {"Live scoreboard", "Todays top pkers", null, null, null};
        }

        if(id == 6552 || id == 31923) {
            definition.interactions = new String[] {"Change spellbook", null, null, null, null};
        }

        if (id == 29165) {
            definition.name = "Pile Of Coins";
            definition.interactions[0] = null;
            definition.interactions[1] = null;
            definition.interactions[2] = null;
            definition.interactions[3] = null;
            definition.interactions[4] = null;
        }

        if(id == 33020) {
            definition.name = "Forging table";
            definition.interactions = new String[] {"Forge", null, null, null, null};
        }

        if(id == 8878) {
            definition.name = "Item dispenser";
            definition.interactions = new String[] {"Dispense", "Exchange coins", null, null, null};
        }

        if(id == 637) {
            definition.name = "Item cart";
            definition.interactions = new String[] {"Check cart", "Item list", "Clear cart", null, null};
        }

        if (id == 13291) {
            definition.interactions = new String[] {"Open", null, null, null, null};
        }

        if (id == 23709) {
            definition.interactions[0] = "Use";
        }

        if (id == 2156) {
            definition.name = "World Boss Portal";
        }

        if (id == 27780) {
            definition.name = "Scoreboard";
        }

        if (id == 14986) {
            definition.name = "Key Chest";

            ObjectDefinition deadmanChest = ObjectDefinition.get(27269);

            definition.modelIds = deadmanChest.modelIds;
            definition.modifiedModelColors = deadmanChest.modifiedModelColors;
            definition.interactions = deadmanChest.interactions;
            definition.originalModelColors = deadmanChest.originalModelColors;
        }

        if (id == 7811) {
            definition.name = "Supplies";
            definition.interactions[0] = "Blood money supplies";
            definition.interactions[1] = "Vote-rewards";
            definition.interactions[2] = "Donator-store";
        }
        if (id == 31944) {
            definition.name = "Food/Potions/Gear";
            definition.interactions[0] = "Food";
            definition.interactions[1] = "Potions";
            definition.interactions[2] = "Gear";
        }

        if(id == 2654) {
            definition.name = "Blood fountain";
            definition.objectSizeX = 3;
            definition.objectSizeY = 3;
            definition.interactions[0] = "Rewards";
            definition.interactions[1] = null;
            definition.modifiedModelColors = new int[]{10266, 10270, 10279, 10275, 10283, 33325, 33222};
            definition.originalModelColors = new int[]{10266, 10270, 10279, 10275, 10283, 926, 926};
        }
    }
}
