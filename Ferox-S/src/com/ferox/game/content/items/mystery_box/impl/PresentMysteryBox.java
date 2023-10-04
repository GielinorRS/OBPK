package com.ferox.game.content.items.mystery_box.impl;

import com.ferox.game.content.items.mystery_box.MboxItem;
import com.ferox.game.content.items.mystery_box.MysteryBox;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.util.CustomItemIdentifiers;
import com.ferox.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import static com.ferox.util.ItemIdentifiers.*;

public class PresentMysteryBox extends MysteryBox {

    @Override
    protected String name() {
        return "Present";
    }

    @Override
    public int mysteryBoxId() {
        return PRESENT_13346;
    }

    //TODO fill in rolls
    private static final int EXTREME_ROLL = 25;
    private static final int RARE_ROLL = 10;
    private static final int UNCOMMON_ROLL = 5;

    //TODO fill in rewards
    private static final MboxItem[] EXTREMELY_RARE = new MboxItem[]{
        new MboxItem(CustomItemIdentifiers.SNOWBIRD).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.ELDER_ICE_MAUL).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.DRAGON_HUNTER_CROSSBOW_T).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.FROST_IMBUED_CAPE).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.INFINITY_WINTER_BOOTS).broadcastWorldMessage(true),
    };

    private static final MboxItem[] RARE = new MboxItem[]{
        new MboxItem(CustomItemIdentifiers.MYSTERY_TICKET).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.SNOWY_SLED).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.UGLY_SANTA_HAT).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.ICED_SANTA_HAT).broadcastWorldMessage(true),
        new MboxItem(WISE_OLD_MANS_SANTA_HAT).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.FROST_CLAWS).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.ARMADYL_FROSTSWORD).broadcastWorldMessage(true),
        new MboxItem(ELDER_MAUL).broadcastWorldMessage(true),
        new MboxItem(BLACK_SANTA_HAT).broadcastWorldMessage(true),
        new MboxItem(INVERTED_SANTA_HAT).broadcastWorldMessage(true),
    };

    private static final MboxItem[] UNCOMMON = new MboxItem[]{
        new MboxItem(CustomItemIdentifiers.LEGENDARY_MYSTERY_BOX).broadcastWorldMessage(true),
        new MboxItem(SANTA_HAT).broadcastWorldMessage(true),
        new MboxItem(CHRISTMAS_CRACKER).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.ABYSSAL_TENTACLE_24948).broadcastWorldMessage(true),
        new MboxItem(CustomItemIdentifiers.FROST_WHIP),
        new MboxItem(CustomItemIdentifiers.DONATOR_MYSTERY_BOX),
        new MboxItem(CustomItemIdentifiers.WINTER_TOKEN, 5000),
        new MboxItem(BLOOD_MONEY, 250000),
    };

    private static final MboxItem[] COMMON = new MboxItem[]{
        new MboxItem(CustomItemIdentifiers.DONATOR_MYSTERY_BOX),
        new MboxItem(ANTISANTA_MASK),
        new MboxItem(ANTISANTA_JACKET),
        new MboxItem(ANTISANTA_PANTALOONS),
        new MboxItem(ANTISANTA_GLOVES),
        new MboxItem(ANTISANTA_BOOTS),
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
        return AttributeKey.PRESENTS_OPENED;
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
