package com.ferox.game.world.entity.combat.formula;

import com.ferox.fs.NpcDefinition;
import com.ferox.game.content.skill.impl.slayer.Slayer;
import com.ferox.game.content.skill.impl.slayer.SlayerConstants;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.ferox.game.world.entity.combat.weapon.AttackType;
import com.ferox.game.world.entity.combat.weapon.FightStyle;
import com.ferox.game.world.entity.combat.weapon.WeaponType;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.EquipSlot;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.container.equipment.EquipmentInfo;
import com.ferox.util.CustomItemIdentifiers;
import com.ferox.util.NpcIdentifiers;

import java.util.ArrayList;
import java.util.List;

import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.CustomItemIdentifiers.TOTEMIC_PLATELEGS;
import static com.ferox.util.ItemIdentifiers.*;

/**
 * @author Origin
 * @Since January 16 2022
 */
public class MeleeMaxHit {

    /**
     * The max hit
     * @param player The player performing the hit
     * @param includeNpcMax The npc is a PvP combat dummy
     * @return return the max hit based on the given calculations
     */
    public static int maxHit(Player player, boolean includeNpcMax) {
        EquipmentInfo.Bonuses bonuses = EquipmentInfo.totalBonuses(player, World.getWorld().equipmentInfo());
        FightStyle style = player.getCombat().getFightType().getStyle();
        Mob target = player.getCombat().getTarget();

        int maxHit;
        double prayerBonus = 1;
        double specialMultiplier = 1;
        double otherBonus = 1;
        double petBonus = 1;
        int strengthLevel = player.skills().level(Skills.STRENGTH);
        int styleBonus = 0;

        /**
         * Style Bonus
         *
         */

        switch (style) {
            case AGGRESSIVE -> styleBonus = 3;
            case CONTROLLED -> styleBonus = 1;
            default -> {
            }
        }

        styleBonus += 8;

        /**
         * Prayer Bonus
         *
         */

        if (Prayers.usingPrayer(player, Prayers.BURST_OF_STRENGTH)) {
            prayerBonus = 1.05;
        } else if (Prayers.usingPrayer(player, Prayers.SUPERHUMAN_STRENGTH)) {
            prayerBonus = 1.10;
        } else if (Prayers.usingPrayer(player, Prayers.ULTIMATE_STRENGTH)) {
            prayerBonus = 1.15;
        } else if (Prayers.usingPrayer(player, Prayers.CHIVALRY)) {
            prayerBonus = 1.18;
        } else if (Prayers.usingPrayer(player, Prayers.PIETY)) {
            prayerBonus = 1.23;
        }

        /**
         * Other bonuses
         *
         */
        if (FormulaUtils.voidMelee(player) || FormulaUtils.wearingEliteVoid(player) || FormulaUtils.voidCustomMelee(player)) {
            otherBonus *= 1.10;
        }

        var wearingAnyBlackMask = FormulaUtils.wearingBlackMask(player) || FormulaUtils.wearingBlackMaskImbued(player) || player.getEquipment().wearingSlayerHelm();

        if(wearingAnyBlackMask && target != null && target.isNpc() && includeNpcMax) {
            Npc npc = target.getAsNpc();
            if(npc.id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.1667;
            }

            if(Slayer.creatureMatches(player, npc.id())) {
                otherBonus *= 1.1667;
            }
        }

        if(player.getEquipment().hasAt(EquipSlot.AMULET, SALVE_AMULET) && !wearingAnyBlackMask && target != null && includeNpcMax) {
            if(target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.16;
            }

            if(FormulaUtils.isUndead(target)) {
                otherBonus *= 1.16;
            }
        }

        if(player.getEquipment().hasAt(EquipSlot.AMULET, SALVE_AMULETI) && !wearingAnyBlackMask && target != null && includeNpcMax) {
            if(target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.20;
            }

            if(FormulaUtils.isUndead(target)) {
                otherBonus *= 1.20;
            }
        }

        if(player.getEquipment().hasAt(EquipSlot.AMULET, SALVE_AMULET_E) && !wearingAnyBlackMask && target != null && includeNpcMax) {
            if(target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.20;
            }

            if(FormulaUtils.isUndead(target)) {
                otherBonus *= 1.20;
            }
        }

        if(player.getEquipment().hasAt(EquipSlot.AMULET, SALVE_AMULETEI) && !wearingAnyBlackMask && target != null && includeNpcMax) {
            if(target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.20;
            }

            if(FormulaUtils.isUndead(target)) {
                otherBonus *= 1.20;
            }
        }

        if (player.getEquipment().hasAt(EquipSlot.HEAD, RED_SLAYER_HELMET_I)) {
            otherBonus *= 1.10;
        }

        if (FormulaUtils.wearingTwistedSlayerHelmetI(player) && target != null && target.isNpc() && includeNpcMax) {
            otherBonus *= 1.10;
        }

        if(player.getEquipment().hasAt(EquipSlot.WEAPON, ARCLIGHT) && target != null && includeNpcMax) {
            if(target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.70;
            }

            if(FormulaUtils.isDemon(target)) {
                otherBonus *= 1.70;
            }
        }

        if(player.getEquipment().hasAt(EquipSlot.WEAPON, DARKLIGHT) && target != null && includeNpcMax) {
            if(target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.60;
            }

            if(FormulaUtils.isDemon(target)) {
                otherBonus *= 1.60;
            }
        }

        if(player.getEquipment().hasAt(EquipSlot.WEAPON, DARKLIGHT) && target != null && includeNpcMax) {
            if(target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.175;
            }

            if(target.isNpc() && target.getAsNpc().def() != null && (target.getAsNpc().def().name.equalsIgnoreCase("Kurask") || target.getAsNpc().def().name.equalsIgnoreCase("Turoth"))) {
                otherBonus *= 1.175;
            }
        }

        if (FormulaUtils.obbyArmour(player) && FormulaUtils.hasObbyWeapon(player)) {
            otherBonus *= 1.10;
        }

        if(FormulaUtils.berserkerNecklace(player) && FormulaUtils.hasObbyWeapon(player)) {
            otherBonus *= 1.10;
        }

        if (player.getCombat().getFightType().getAttackType() == AttackType.CRUSH) {
            if (player.getEquipment().hasAt(EquipSlot.HEAD, INQUISITORS_GREAT_HELM)) {
                otherBonus += 0.02;
            }

            if (player.getEquipment().hasAt(EquipSlot.BODY, INQUISITORS_HAUBERK)) {
                otherBonus += 0.01;
            }

            if (player.getEquipment().hasAt(EquipSlot.LEGS, INQUISITORS_PLATESKIRT)) {
                otherBonus += 0.01;
            }

            if (player.getEquipment().hasAt(EquipSlot.HEAD, INQUISITORS_GREAT_HELM) || player.getEquipment().hasAt(EquipSlot.BODY, INQUISITORS_HAUBERK) || player.getEquipment().hasAt(EquipSlot.LEGS, INQUISITORS_PLATESKIRT)) {
                otherBonus += 0.01;
            }
        }

        if(player.getEquipment().hasAt(EquipSlot.WEAPON, DRAGON_HUNTER_LANCE) && target != null && includeNpcMax) {
            if(target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                otherBonus *= 1.20;
            }

            if(FormulaUtils.isDragon(target)) {
                otherBonus *= 1.20;
            }
        }

        //• Gadderhammer: 1.25 or 2.0 vs shades
        if(player.getEquipment().hasAt(EquipSlot.WEAPON, GADDERHAMMER)) {
            if(target != null && target.isNpc()) {
                Npc npc = target.getAsNpc();
                NpcDefinition def = npc.def();
                var isShade = def != null && def.name.equalsIgnoreCase("Shade");
                otherBonus *= isShade ? 1.25 : 2.00;
            }
        }

        if (FormulaUtils.hasViggorasChainMace(player) && target != null && target.isNpc() && includeNpcMax) {
            otherBonus *= 1.50;
        }

        if (player.getEquipment().hasAt(EquipSlot.WEAPON, VIGGORAS_CHAINMACE_C) && target != null && target.isNpc() && includeNpcMax) {
            otherBonus *= 1.50;
        }

        if(player.getEquipment().hasAt(EquipSlot.WEAPON, CustomItemIdentifiers.SWORD_OF_GRYFFINDOR) && target != null && target.isNpc() && includeNpcMax) {
            otherBonus *= 1.25;
        }

        if (player.getEquipment().hasAt(EquipSlot.HEAD, TOTEMIC_HELMET) && target != null && target.isNpc() && includeNpcMax) {
            otherBonus *= 1.10;//10% damage boost
        }

        if (player.getEquipment().hasAt(EquipSlot.BODY, TOTEMIC_PLATEBODY) && target != null && target.isNpc() && includeNpcMax) {
            otherBonus *= 1.20;//20% damage boost
        }

        if (player.getEquipment().hasAt(EquipSlot.LEGS, TOTEMIC_PLATELEGS) && target != null && target.isNpc() && includeNpcMax) {
            otherBonus *= 1.15;//15.0% damage boost
        }

        /**
         * Pet bonuses
         *
         */

        boolean ancientKingBlackDragonPet = player.hasPetOut("Ancient king black dragon");
        if(ancientKingBlackDragonPet && target != null && target.isNpc() && includeNpcMax) {
            Npc npc = (Npc) target;
            if (npc.def() != null && npc.def().name != null && FormulaUtils.isDragon(npc)) {
                petBonus *= 1.25;
            }
        }

        if (player.hasPetOut("Skeleton hellhound pet") && target != null && target.isNpc() && includeNpcMax) {
            petBonus *= 1.05;
        }

        if(player.hasPetOut("Olmlet") && target != null && target.isNpc() && includeNpcMax) {
            if(target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                petBonus *= 1.10;
            }

            if (player.getRaids() != null && player.getRaids().raiding(player))
                petBonus *= 1.10;
        }

        if(player.hasPetOut("Pet zombies champion") && target != null && target.isNpc() && target.getAsNpc().isWorldBoss() && includeNpcMax) {
            petBonus *= 1.10;
        }

        /**
         * Effective Strength
         *
         */

        int effectiveStrengthDamage = (int) ((strengthLevel * prayerBonus * otherBonus * petBonus) + styleBonus);

        /**
         * Base Damage
         *
         */

        double baseDamage = (int) (0.5 + ((effectiveStrengthDamage * (bonuses.str + 64)) / 640));

        /**
         *
         * Donator perk modifiers
         *
         */
        double slayerPerkBonus = 1.0;

        var weakSpot = player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.WEAK_SPOT);
        if(weakSpot && target != null && target.isNpc()) {
            if(Slayer.creatureMatches(player, target.getAsNpc().id())) {
                slayerPerkBonus *= 1.10;
            }
        }

        /**
         * Max Hit
         *
         */

        maxHit = (int) (Math.floor(baseDamage) * specialMultiplier * slayerPerkBonus);

        /**
         * special bonus
         *
         */

        if(player.isSpecialActivated()) {
            specialMultiplier *= player.getCombatSpecial().getSpecialMultiplier();
            maxHit *= specialMultiplier;
        }

        if (CombatFactory.fullDharoks(player)) {
            double lostHp = player.maxHp() - player.hp();
            maxHit *= 1 + (lostHp /100 * player.maxHp() / 100);
        }

        List<Integer> increaseMaxHitbyOne = new ArrayList<>(List.of(GRANITE_MAUL_12848, ARMADYL_GODSWORD_OR, BANDOS_GODSWORD_OR, SARADOMIN_GODSWORD_OR, ZAMORAK_GODSWORD_OR, DRAGON_CLAWS_OR));
        if (increaseMaxHitbyOne.stream().anyMatch(w -> player.getEquipment().hasAt(EquipSlot.WEAPON, w))) {
            maxHit += 1;
        }

        if(player.getEquipment().hasAt(EquipSlot.AMULET, AMULET_OF_TORTURE_OR) || player.getEquipment().hasAt(EquipSlot.AMULET, AMULET_OF_FURY_OR) || player.getEquipment().hasAt(EquipSlot.AMULET, BERSERKER_NECKLACE_OR)) {
            maxHit += 1;
        }

        if(player.hasPetOut("Baby Barrelchest") || player.hasPetOut("Ancient barrelchest")) {
            maxHit += 1;
        }

        if(player.hasPetOut("Corrupted nechryarch pet")) {
            maxHit += 1;
        }

        if(player.hasPetOut("Youngllef pet")) {
            maxHit += 1;
        }

        if(player.hasPetOut("Lava beast pet")) {
            maxHit += 1;
        }

        if(player.hasPetOut("Corrupted Youngllef pet")) {
            maxHit += 2;
        }

        return maxHit;
    }
}
