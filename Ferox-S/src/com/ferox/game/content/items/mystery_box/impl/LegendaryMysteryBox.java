package com.ferox.game.content.items.mystery_box.impl;

import com.ferox.GameServer;
import com.ferox.game.content.items.mystery_box.MboxItem;
import com.ferox.game.content.items.mystery_box.MysteryBox;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.util.CustomItemIdentifiers;
import com.ferox.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;

public class LegendaryMysteryBox extends MysteryBox {

    public static final int LEGENDARY_MYSTERY_BOX = 16454;

    @Override
    protected String name() {
        return "Legendary mystery box";
    }

    @Override
    public int mysteryBoxId() {
        return LEGENDARY_MYSTERY_BOX;
    }

    private static final int EXTREME_RARE_ROLL = 12;
    private static final int RARE_ROLL = 8;
    private static final int UNCOMMON_ROLL = 4;

    private static final MboxItem[] EXTREMELY_RARE = new MboxItem[]{
        new MboxItem(ARCANE_SPIRIT_SHIELD).broadcastWorldMessage(true),
        new MboxItem(AVERNIC_DEFENDER).broadcastWorldMessage(true),
        new MboxItem(INFERNAL_CAPE).broadcastWorldMessage(true),
        new MboxItem(FEROCIOUS_GLOVES).broadcastWorldMessage(true),
        new MboxItem(HARMONISED_NIGHTMARE_STAFF).broadcastWorldMessage(true),
        new MboxItem(VOLATILE_NIGHTMARE_STAFF).broadcastWorldMessage(true),
        new MboxItem(SANGUINESTI_STAFF).broadcastWorldMessage(true),
        new MboxItem(DEXTEROUS_PRAYER_SCROLL).broadcastWorldMessage(true),
        new MboxItem(ARCANE_PRAYER_SCROLL).broadcastWorldMessage(true),
        new MboxItem(DONATOR_TICKET,1000).broadcastWorldMessage(true),
        new MboxItem(MYSTERY_TICKET).broadcastWorldMessage(true),
        new MboxItem(LARRANS_KEY_TIER_III).broadcastWorldMessage(true)
    };

    private static final MboxItem[] RARE = new MboxItem[]{
        new MboxItem(ELDER_MAUL).broadcastWorldMessage(true),
        new MboxItem(INQUISITORS_GREAT_HELM).broadcastWorldMessage(true),
        new MboxItem(INQUISITORS_HAUBERK).broadcastWorldMessage(true),
        new MboxItem(INQUISITORS_PLATESKIRT).broadcastWorldMessage(true),
        new MboxItem(INQUISITORS_MACE).broadcastWorldMessage(true),
        new MboxItem(MORRIGANS_LEATHER_BODY),
        new MboxItem(MORRIGANS_LEATHER_CHAPS),
        new MboxItem(ZURIELS_ROBE_TOP),
        new MboxItem(ZURIELS_ROBE_BOTTOM),
        new MboxItem(VESTAS_CHAINBODY),
        new MboxItem(VESTAS_PLATESKIRT),
        new MboxItem(STATIUSS_PLATEBODY),
        new MboxItem(STATIUSS_PLATELEGS),
        new MboxItem(LARRANS_KEY_TIER_II)
    };

    private static final MboxItem[] UNCOMMON = new MboxItem[]{
        new MboxItem(STATIUSS_FULL_HELM),
        new MboxItem(MORRIGANS_COIF),
        new MboxItem(ZURIELS_HOOD),
        new MboxItem(AMULET_OF_TORTURE),
        new MboxItem(NECKLACE_OF_ANGUISH),
        new MboxItem(TORMENTED_BRACELET),
        new MboxItem(RING_OF_SUFFERING),
        new MboxItem(SPECTRAL_SPIRIT_SHIELD),
        new MboxItem(PRIMORDIAL_BOOTS),
        new MboxItem(PEGASIAN_BOOTS),
        new MboxItem(ETERNAL_BOOTS),
        new MboxItem(LARRANS_KEY_TIER_I)
    };

    private static final MboxItem[] COMMON = new MboxItem[]{
        new MboxItem(BLOOD_MONEY,100000),
        new MboxItem(DONATOR_TICKET,250),
        new MboxItem(VOTE_TICKET,10),
        new MboxItem(DONATOR_MYSTERY_BOX,3),
        new MboxItem(WEAPON_MYSTERY_BOX,3),
        new MboxItem(ARMOUR_MYSTERY_BOX,3),
        new MboxItem(AMULET_OF_FURY,10),
        new MboxItem(OCCULT_NECKLACE,10),
        new MboxItem(DRAGON_BOOTS,10)
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
        return AttributeKey.LEGENDARY_MYSTERY_BOXES_OPENED;
    }

    @Override
    public MboxItem rollReward() {
        if (Utils.rollDie(EXTREME_RARE_ROLL, 1)) {
            return Utils.randomElement(EXTREMELY_RARE);
        } else if (Utils.rollDie(RARE_ROLL, 1)) {
            return Utils.randomElement(RARE);
        } else if (Utils.rollDie(UNCOMMON_ROLL, 1)) {
            return Utils.randomElement(UNCOMMON);
        } else {
            return Utils.randomElement(COMMON);
        }
    }
}
