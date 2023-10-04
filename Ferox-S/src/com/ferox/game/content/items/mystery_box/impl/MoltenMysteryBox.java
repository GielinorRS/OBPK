package com.ferox.game.content.items.mystery_box.impl;

import com.ferox.game.content.items.mystery_box.MboxItem;
import com.ferox.game.content.items.mystery_box.MysteryBox;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.util.Utils;
import com.ferox.util.CustomItemIdentifiers;

import java.util.ArrayList;
import java.util.Arrays;

import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;

public class MoltenMysteryBox extends MysteryBox {

    @Override
    protected String name() {
        return "Molten mystery box";
    }

    @Override
    public int mysteryBoxId() {
        return MOLTEN_MYSTERY_BOX;
    }

    //TODO fill in rolls
    private static final int EXTREME_ROLL = 20;
    private static final int RARE_ROLL = 10;
    private static final int UNCOMMON_ROLL = 5;

    //TODO fill in rewards
    private static final MboxItem[] EXTREMELY_RARE = new MboxItem[]{
        new MboxItem(CustomItemIdentifiers.MOLTEN_DEFENDER).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.LAVA_DHIDE_COIF).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.LAVA_DHIDE_BODY).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.LAVA_DHIDE_CHAPS).broadcastWorldMessage(true),
    };

    private static final MboxItem[] RARE = new MboxItem[]{
        new MboxItem(INFERNAL_CAPE).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.MOLTEN_PARTYHAT).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.LAVA_PARTYHAT).broadcastWorldMessage(true),
    };

    private static final MboxItem[] UNCOMMON = new MboxItem[]{
        new MboxItem(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.MYSTERY_TICKET).broadcastWorldMessage(true),
    };

    private static final MboxItem[] COMMON = new MboxItem[]{
        new MboxItem(CustomItemIdentifiers.LAVA_WHIP),
        new MboxItem(LAVA_DRAGON_MASK),
    };

    private MboxItem[] allRewardsCached;

    public MboxItem[] allPossibleRewards() {
        if (allRewardsCached == null) {
            ArrayList<MboxItem> mboxItems = new ArrayList<>();
            mboxItems.addAll(Arrays.asList(EXTREMELY_RARE));
            mboxItems.addAll(Arrays.asList(RARE));
            mboxItems.addAll(Arrays.asList(UNCOMMON));
            mboxItems.addAll(Arrays.asList(COMMON));
            allRewardsCached = mboxItems.toArray(new MboxItem[0]);
        }
        return allRewardsCached;
    }

    @Override
    public AttributeKey key() {
        return AttributeKey.MOLTEN_MYSTERY_BOXES_OPENED;
    }

    @Override
    public MboxItem rollReward() {
        if (World.getWorld().rollDie(EXTREME_ROLL, 1)) {
            return Utils.randomElement(EXTREMELY_RARE);
        } else if (World.getWorld().rollDie(RARE_ROLL, 1)) {
            return Utils.randomElement(RARE);
        } else if (World.getWorld().rollDie(UNCOMMON_ROLL, 1)) {
            return Utils.randomElement(UNCOMMON);
        } else {
            return Utils.randomElement(COMMON);
        }
    }
}
