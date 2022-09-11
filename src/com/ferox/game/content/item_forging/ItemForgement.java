package com.ferox.game.content.item_forging;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.items.Item;
import com.ferox.util.CustomItemIdentifiers;
import com.ferox.util.ItemIdentifiers;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.ferox.game.content.item_forging.ItemForgingTable.START_OF_FORGE_LIST;
import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.CustomItemIdentifiers.ZAWEKS_PET;
import static com.ferox.util.ItemIdentifiers.*;

/**
 * utility enum which writes all data to interface.
 *
 * @author Patrick van Elderen | Zerikoth (PVE) | 16 okt. 2019 : 10:04
 * @see <a href="https://github.com/Patrick9-10-1995">Github profile</a>
 */
public enum ItemForgement {

    //Weapons
    ARMADYL_GODSWORD("Armadyl godsword (or)", ItemForgingCategory.WEAPON, 10, new Item(ARMADYL_GODSWORD_OR), new Item[]{new Item(ItemIdentifiers.ARMADYL_GODSWORD + 1, 5)}, AttributeKey.ARMADYL_GODSWORD_OR_ATTEMPTS),
    BANDOS_GODSWORD("Bandos godsword (or)", ItemForgingCategory.WEAPON, 10, new Item(BANDOS_GODSWORD_OR), new Item[]{new Item(ItemIdentifiers.BANDOS_GODSWORD + 1, 10)}, AttributeKey.BANDOS_GODSWORD_OR_ATTEMPTS),
    SARADOMIN_GODSWORD("Saradomin godsword (or)", ItemForgingCategory.WEAPON, 10, new Item(SARADOMIN_GODSWORD_OR), new Item[]{new Item(ItemIdentifiers.SARADOMIN_GODSWORD + 1, 10)}, AttributeKey.SARADOMIN_GODSWORD_OR_ATTEMPTS),
    ZAMORAK_GODSWORD("Zamorak godsword (or)", ItemForgingCategory.WEAPON, 10, new Item(ZAMORAK_GODSWORD_OR), new Item[]{new Item(ItemIdentifiers.ZAMORAK_GODSWORD + 1, 10)}, AttributeKey.ZAMORAK_GODSWORD_OR_ATTEMPTS),
    GRANITE_MAUL("Granite maul (or)", ItemForgingCategory.WEAPON, 5, new Item(GRANITE_MAUL_12848), new Item[]{new Item(GRANITE_MAUL_24225, 5)}, AttributeKey.GRANITE_MAUL_OR_ATTEMPTS),
    DRAGON_CLAWS("Dragon claws (or)", ItemForgingCategory.WEAPON, 10, new Item(DRAGON_CLAWS_OR), new Item[]{new Item(ItemIdentifiers.DRAGON_CLAWS, 5)}, AttributeKey.DRAGON_CLAWS_OR_ATTEMPTS),
    DRAGON_HCB("Dragon hunter crossbow (t)", ItemForgingCategory.WEAPON, 25, new Item(DRAGON_HUNTER_CROSSBOW_T), new Item[]{new Item(DRAGON_HUNTER_CROSSBOW, 4)}, AttributeKey.DRAGON_HCB_ATTEMPTS),
    ELDER_MAUL("Enchanted maul", ItemForgingCategory.WEAPON, 10, new Item(DARK_ELDER_MAUL), new Item[]{new Item(ItemIdentifiers.ELDER_MAUL, 5)}, AttributeKey.DARK_ELDER_MAUL_ATTEMPTS),
    ELDER_MAUL2("Elder ice maul", ItemForgingCategory.WEAPON, 10, new Item(CustomItemIdentifiers.ELDER_ICE_MAUL), new Item[]{new Item(ItemIdentifiers.ELDER_MAUL, 3)}, AttributeKey.ELDER_ICE_MAUL_ATTEMPTS),
    MAGMA_BLOWPIPE("Magma blowpipe", ItemForgingCategory.WEAPON, 35, new Item(CustomItemIdentifiers.MAGMA_BLOWPIPE), new Item[]{new Item(TOXIC_BLOWPIPE,4)}, AttributeKey.MAGMA_BLOWPIPE_ATTEMPTS),
    HOLY_SANGUINESTI_STAFF("Holy sanguinesti staff", ItemForgingCategory.WEAPON, 25, new Item(CustomItemIdentifiers.HOLY_SANGUINESTI_STAFF), new Item[]{new Item(SANGUINESTI_STAFF,2)}, AttributeKey.HOLY_SANGUINESTI_STAFF_ATTEMPTS),
    HOLY_SCYHTE_OF_VITUR("Holy scythe of vitur", ItemForgingCategory.WEAPON, 25, new Item(HOLY_SCYTHE_OF_VITUR), new Item[]{new Item(SCYTHE_OF_VITUR,1)}, AttributeKey.HOLY_SCYTHE_OF_VITUR_ATTEMPTS),
    HOLY_GHRAZI_RAPIER("Holy ghrazi rapier", ItemForgingCategory.WEAPON, 25, new Item(CustomItemIdentifiers.HOLY_GHRAZI_RAPIER), new Item[]{new Item(GHRAZI_RAPIER,2)}, AttributeKey.HOLY_GHRAZI_RAPIER_ATTEMPTS),
    SANGUINE_SCYTHE_OF_VITUR("Sanguine scythe of vitur", ItemForgingCategory.WEAPON, 30, new Item(CustomItemIdentifiers.SANGUINE_SCYTHE_OF_VITUR), new Item[]{new Item(ItemIdentifiers.SCYTHE_OF_VITUR,2)}, AttributeKey.SANGUINE_SCYTHE_OF_VITUR_ATTEMPTS),
    VIGGORAS_CHAINMACE_C("Viggora's chainmace (c)", ItemForgingCategory.WEAPON, 25, new Item(CustomItemIdentifiers.VIGGORAS_CHAINMACE_C), new Item[]{new Item(VIGGORAS_CHAINMACE, 4)}, AttributeKey.CORRUPTED_VIGGORAS_CHAINMACE_ATTEMPTS),
    CRAWS_BOW_C("Craw's bow (c)", ItemForgingCategory.WEAPON, 25, new Item(CustomItemIdentifiers.CRAWS_BOW_C), new Item[]{new Item(CRAWS_BOW, 3)}, AttributeKey.CORRUPTED_CRAWS_BOW_ATTEMPTS),
    THAMMARONS_SCEPTRE_C("Thammaron's sceptre (c)", ItemForgingCategory.WEAPON, 25, new Item(CustomItemIdentifiers.THAMMARONS_STAFF_C), new Item[]{new Item(THAMMARONS_SCEPTRE,5)}, AttributeKey.CORRUPTED_THAMMARONS_STAFF_ATTEMPTS),
    SANGUINE_TWISTED_BOW("Sanguine twisted bow", ItemForgingCategory.WEAPON, 30, new Item(CustomItemIdentifiers.SANGUINE_TWISTED_BOW), new Item[]{new Item(ItemIdentifiers.TWISTED_BOW, 2)}, AttributeKey.SANGUINE_TWISTED_BOW_ATTEMTPS),
    TOXIC_STAFF_C("Toxic staff of the dead (i)", ItemForgingCategory.WEAPON, 25, new Item(TOXIC_STAFF_OF_THE_DEAD_C), new Item[]{new Item(TOXIC_STAFF_OF_THE_DEAD,5)}, AttributeKey.TOXIC_STAFF_OF_THE_DEAD_C_ATTEMPTS),

    //Armour
    AMULET_OF_FURY("Amulet of fury (or)", ItemForgingCategory.ARMOUR, 25, new Item(AMULET_OF_FURY_OR), new Item[]{new Item(ItemIdentifiers.AMULET_OF_FURY + 1, 10)}, AttributeKey.FURY_OR_ATTEMPTS),
    OCCULT_NECKLACE("Occult necklace (or)", ItemForgingCategory.ARMOUR, 25, new Item(OCCULT_NECKLACE_OR), new Item[]{new Item(ItemIdentifiers.OCCULT_NECKLACE + 1, 10)}, AttributeKey.OCCULT_OR_ATTEMPTS),
    AMULET_OF_TORTURE("Amulet of torture (or)", ItemForgingCategory.ARMOUR, 25, new Item(AMULET_OF_TORTURE_OR), new Item[]{new Item(ItemIdentifiers.AMULET_OF_TORTURE + 1, 3)}, AttributeKey.TORTURE_OR_ATTEMPTS),
    NECKLACE_OF_ANGUISH("Necklace of anguish (or)", ItemForgingCategory.ARMOUR, 25, new Item(NECKLACE_OF_ANGUISH_OR), new Item[]{new Item(ItemIdentifiers.NECKLACE_OF_ANGUISH + 1, 3)}, AttributeKey.ANGUISH_OR_ATTEMPTS),
    BERSERKER_NECKLACE("Berserker necklace (or)", ItemForgingCategory.ARMOUR, 25, new Item(BERSERKER_NECKLACE_OR), new Item[]{new Item(ItemIdentifiers.BERSERKER_NECKLACE + 1, 10)}, AttributeKey.BERSERKER_NECKLACE_OR_ATTEMPTS),
    TORMENTED_BRACELET("Tormented bracelet (or)", ItemForgingCategory.ARMOUR, 25, new Item(TORMENTED_BRACELET_OR), new Item[]{new Item(ItemIdentifiers.TORMENTED_BRACELET + 1, 3)}, AttributeKey.TORMENTED_BRACELET_OR_ATTEMPTS),
    DRAGON_BOOTS("Dragon boots (g)", ItemForgingCategory.ARMOUR, 2, new Item(DRAGON_BOOTS_G), new Item[]{new Item(ItemIdentifiers.DRAGON_BOOTS + 1, 5)}, AttributeKey.DRAGON_BOOTS_G_ATTEMPTS),
    RING_OF_CONFLICTION("Ring of confliction", ItemForgingCategory.ARMOUR, 25, new Item(CustomItemIdentifiers.RING_OF_MANHUNTING), new Item[]{new Item(RING_OF_SUFFERING + 1, 1), new Item(BERSERKER_RING_I, 5)}, AttributeKey.RING_OF_MANHUNTING_ATTEMPTS),
    ALCHEMISTS_RING("Alchemist's ring", ItemForgingCategory.ARMOUR, 25, new Item(CustomItemIdentifiers.RING_OF_SORCERY), new Item[]{new Item(RING_OF_SUFFERING + 1, 1), new Item(SEERS_RING_I, 5)}, AttributeKey.RING_OF_SORCERY_ATTEMPTS),
    DEADEYES_RING("Deadeye's ring", ItemForgingCategory.ARMOUR, 25, new Item(CustomItemIdentifiers.RING_OF_PRECISION), new Item[]{new Item(RING_OF_SUFFERING + 1, 1), new Item(ARCHERS_RING_I, 5)}, AttributeKey.RING_OF_PRECISION_ATTEMPTS),
    RING_OF_TRINITY("Ring of perfection", ItemForgingCategory.ARMOUR, 50, new Item(CustomItemIdentifiers.RING_OF_TRINITY), new Item[]{new Item(CustomItemIdentifiers.RING_OF_MANHUNTING), new Item(CustomItemIdentifiers.RING_OF_SORCERY), new Item(CustomItemIdentifiers.RING_OF_PRECISION)}, AttributeKey.RING_OF_TRINITY_ATTEMPTS),
    SLAYER_HELM("Slayer helmet (i)", ItemForgingCategory.ARMOUR, 100, new Item(SLAYER_HELMET_I), new Item[]{new Item(SLAYER_HELMET)}, AttributeKey.SLAYER_HELMET_I_ATTEMPTS),
    GREEN_SLAYER_HELM("Green slayer helmet (i)", ItemForgingCategory.ARMOUR, 100, new Item(GREEN_SLAYER_HELMET_I), new Item[]{new Item(GREEN_SLAYER_HELMET)}, AttributeKey.GREEN_SLAYER_HELMET_I_ATTEMPTS),
    TURQUOISE_SLAYER_HELM("Turquoise slayer helmet (i)", ItemForgingCategory.ARMOUR, 100, new Item(TURQUOISE_SLAYER_HELMET_I), new Item[]{new Item(TURQUOISE_SLAYER_HELMET)}, AttributeKey.TURQUOISE_SLAYER_HELMET_I_ATTEMPTS),
    RED_SLAYER_HELM("Red slayer helmet (i)", ItemForgingCategory.ARMOUR, 100, new Item(RED_SLAYER_HELMET_I), new Item[]{new Item(RED_SLAYER_HELMET)}, AttributeKey.RED_SLAYER_HELMET_I_ATTEMPTS),
    BLACK_SLAYER_HELM("Black slayer helmet (i)", ItemForgingCategory.ARMOUR, 100, new Item(BLACK_SLAYER_HELMET_I), new Item[]{new Item(BLACK_SLAYER_HELMET)}, AttributeKey.BLACK_SLAYER_HELMET_I_ATTEMPTS),
    TWISTED_SLAYER_HELM("Twisted slayer helmet (i)", ItemForgingCategory.ARMOUR, 100, new Item(TWISTED_SLAYER_HELMET_I), new Item[]{new Item(SLAYER_HELMET_I), new Item(BLACK_SLAYER_HELMET_I), new Item(GREEN_SLAYER_HELMET_I), new Item(TURQUOISE_SLAYER_HELMET_I), new Item(RED_SLAYER_HELMET_I)}, AttributeKey.TWISTED_SLAYER_HELMET_I_ATTEMPTS),
    ANCESTRAL_HAT_I("Ancestral hat (I)", ItemForgingCategory.ARMOUR, 50, new Item(CustomItemIdentifiers.ANCESTRAL_HAT_I), new Item[]{new Item(ANCESTRAL_HAT + 1, 5)}, AttributeKey.ANCESTRAL_HAT_I_ATTEMPTS),
    ANCESTRAL_ROBE_TOP_I("Ancestral robe top (I)", ItemForgingCategory.ARMOUR, 50, new Item(CustomItemIdentifiers.ANCESTRAL_ROBE_TOP_I), new Item[]{new Item(ANCESTRAL_ROBE_TOP + 1, 5)}, AttributeKey.ANCESTRAL_ROBE_TOP_I_ATTEMPTS),
    ANCESTRAL_ROBE_BOTTOM_I("Ancestral robe bottom (I)", ItemForgingCategory.ARMOUR, 50, new Item(CustomItemIdentifiers.ANCESTRAL_ROBE_BOTTOM_I), new Item[]{new Item(ANCESTRAL_ROBE_BOTTOM + 1, 5)}, AttributeKey.ANCESTRAL_ROBE_BOTTOM_I_ATTEMPTS),
    PRIMORDIAL_BOOTS_OR("Primordial boots (or)", ItemForgingCategory.ARMOUR, 25, new Item(CustomItemIdentifiers.PRIMORDIAL_BOOTS_OR), new Item[]{new Item(PRIMORDIAL_BOOTS + 1, 4)}, AttributeKey.PRIMORDIAL_BOOTS_OR_ATTEMPTS),
    PEGASIAN_BOOTS_OR("Pegasian boots (or)", ItemForgingCategory.ARMOUR, 25, new Item(CustomItemIdentifiers.PEGASIAN_BOOTS_OR), new Item[]{new Item(PEGASIAN_BOOTS + 1, 4)}, AttributeKey.PEGASIAN_BOOTS_OR_ATTEMPTS),
    ETERNAL_BOOTS_OR("Eternal boots (or)", ItemForgingCategory.ARMOUR, 25, new Item(CustomItemIdentifiers.ETERNAL_BOOTS_OR), new Item[]{new Item(ETERNAL_BOOTS + 1, 4)}, AttributeKey.ETERNAL_BOOTS_OR_ATTEMPTS),
    MYTHICAL_BOOTS("Mythical boots", ItemForgingCategory.ARMOUR, 50, new Item(CustomItemIdentifiers.CORRUPTED_BOOTS), new Item[]{new Item(CustomItemIdentifiers.PRIMORDIAL_BOOTS_OR), new Item(CustomItemIdentifiers.PEGASIAN_BOOTS_OR), new Item(CustomItemIdentifiers.ETERNAL_BOOTS_OR)}, AttributeKey.CORRUPTED_BOOTS_ATTEMTPS),
    ENCHANTED_FACEGUARD("Enchanted faceguard", ItemForgingCategory.ARMOUR, 25, new Item(CustomItemIdentifiers.ANCIENT_FACEGAURD), new Item[]{new Item(NEITIZNOT_FACEGUARD,5)}, AttributeKey.ANCIENT_FACEGUARD_ATTEMPTS),

    //Misc
    MYTHICAL_MAX_CAPE("Mythical Max cape", ItemForgingCategory.MISC, 100, new Item(24855), new Item[]{new Item(21285),new Item(30023), new Item(21898)}, AttributeKey.MYTHICAL_MAX_CAPE_ATTEMPTS),//updatet2
    RUNE_POUCH("Rune pouch (i)", ItemForgingCategory.MISC, 25, new Item(RUNE_POUCH_23650), new Item[]{new Item(ItemIdentifiers.RUNE_POUCH, 5)}, AttributeKey.RUNE_POUCH_I_ATTEMPTS),
    LARRANS_KEY_TIER_II("Larran's key tier II", ItemForgingCategory.MISC, 30, new Item(CustomItemIdentifiers.LARRANS_KEY_TIER_II), new Item[]{new Item(LARRANS_KEY_TIER_I)}, AttributeKey.LARRANS_KEY_II_ATTEMPTS),
    LARRANS_KEY_TIER_III("Larran's key tier III", ItemForgingCategory.MISC, 40, new Item(CustomItemIdentifiers.LARRANS_KEY_TIER_III), new Item[]{new Item(CustomItemIdentifiers.LARRANS_KEY_TIER_II)}, AttributeKey.LARRANS_KEY_III_ATTEMPTS),
    ZAWEKS_RECOLOR("Zaweks Pet", ItemForgingCategory.MISC, 45, new Item(ZAWEKS_PET), new Item[]{new Item(FAWKES),new Item(ZRIAWK)}, AttributeKey.FAWKEYS_ATTEMPTS),
    MENDING_LIFE_BIRD("Mending Life Bird", ItemForgingCategory.MISC, 45, new Item(CustomItemIdentifiers.MENDING_LIFE_BIRD), new Item[]{new Item(BLOOD_FIREBIRD),new Item(FAWKES)}, AttributeKey.MENDING_ATTEMPTS),

    ;

    /**
     * forged item name.
     */
    public final String name;

    /**
     * item difficulty
     */
    public final ItemForgingCategory tier;

    /**
     * chance of successfully enchanting the item.
     */
    public final int successRate;

    /**
     * enchanted item reward.
     */
    public final Item enchantedItem;

    /**
     * items required to have a chance at upgrading.
     */
    public final Item[] requiredItems;

    /**
     * This key determines how many times we're tried to enchant this item.
     */
    public final AttributeKey attempts;

    ItemForgement(String name, ItemForgingCategory tier, int successRate, Item enchantedItem, Item[] requiredItems, AttributeKey attempts) {
        this.name = name;
        this.tier = tier;
        this.successRate = successRate;
        this.enchantedItem = enchantedItem;
        this.requiredItems = requiredItems;
        this.attempts = attempts;
    }

    public int button() {
        return START_OF_FORGE_LIST + ordinal();
    }

    public static List<ItemForgement> sortByTier(ItemForgingCategory tier) {
        return Arrays.stream(values()).filter(a -> a.tier == tier).sorted(Comparator.comparing(Enum::name)).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "ItemForgement{" +
            "name='" + name + '\'' +
            ", tier=" + tier +
            ", successRate=" + successRate +
            ", enchantedItem=" + enchantedItem +
            ", requiredItems=" + Arrays.toString(requiredItems) +
            ", attempts=" + attempts +
            '}';
    }
}
