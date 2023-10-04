package com.ferox.game.world.entity.combat.formula;

import com.ferox.game.content.skill.impl.slayer.Slayer;
import com.ferox.game.content.skill.impl.slayer.SlayerConstants;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.ferox.game.world.entity.combat.weapon.FightStyle;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.EquipSlot;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.Item;
import com.ferox.game.world.items.container.equipment.Equipment;
import com.ferox.game.world.items.container.equipment.EquipmentInfo;
import com.ferox.util.CustomItemIdentifiers;
import com.ferox.util.ItemIdentifiers;
import com.ferox.util.NpcIdentifiers;

import java.util.Arrays;
import java.util.HashSet;

import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since November 06, 2021
 */
public class RangeMaxHit {

    private static final HashSet<Integer> TWISTED_BOWS = new HashSet<>(Arrays.asList(TWISTED_BOW, TWISTED_BOW_I, TWISTED_BOW_ORANGE, TWISTED_BOW_PURPLE, SANGUINE_TWISTED_BOW));

    public static int maxHit(Mob mob, Mob target, boolean factorInAmmoRangeStr, boolean includeNpcMax) {
        int maxHit;
        Player player = (Player) mob;

        EquipmentInfo.Bonuses bonuses = EquipmentInfo.totalBonuses(player, World.getWorld().equipmentInfo(), !factorInAmmoRangeStr);

        double prayerBonus = 1;
        double extraBonus = 1;
        double specialMultiplier = 1;

        // Prayer additions
        if (Prayers.usingPrayer(player, Prayers.SHARP_EYE)) {
            prayerBonus = 1.05;
        } else if (Prayers.usingPrayer(player, Prayers.HAWK_EYE)) {
            prayerBonus = 1.10;
        } else if (Prayers.usingPrayer(player, Prayers.EAGLE_EYE)) {
            prayerBonus = 1.15;
        } else if (Prayers.usingPrayer(player, Prayers.RIGOUR)) {
            prayerBonus = 1.23;
        }

        Item weapon = player.getEquipment().get(EquipSlot.WEAPON);
        Item helm = player.getEquipment().get(EquipSlot.HEAD);
        Item amulet = player.getEquipment().get(EquipSlot.AMULET);

        var wearingAnyBlackMask = FormulaUtils.wearingBlackMask(player) || FormulaUtils.wearingBlackMaskImbued(player) || player.getEquipment().wearingSlayerHelm();

        // The magic shortbow spec ignores bonus like prayers, void etc...
        var usingSpecialAttack = player.isSpecialActivated();
        var isMSB = player.getEquipment().hasAt(EquipSlot.WEAPON, MAGIC_SHORTBOW) || player.getEquipment().hasAt(EquipSlot.WEAPON, MAGIC_SHORTBOW_I);
        var ignoreMSBBonus = isMSB && usingSpecialAttack;
        //Magic short bow ignores prayer bonus on special attacks
        if (ignoreMSBBonus) {
            prayerBonus = 1.0;
        }

        int effectiveLevel = ignoreMSBBonus ? player.skills().level(Skills.RANGED) : (int) Math.floor(player.skills().level(Skills.RANGED) * prayerBonus * extraBonus);

        // Accurate mode gives you 3 extra levels in the algorithm.
        if (player.getCombat().getFightType().getStyle().equals(FightStyle.ACCURATE)) {
            effectiveLevel += 3;
        }

        effectiveLevel += 8;

        // Void effect adds 10%.
        if (FormulaUtils.voidRanger(player)) {
            effectiveLevel += effectiveLevel / 10;
        }

        // Elite Void effect adds extra 2.5%.
        if (FormulaUtils.wearingEliteVoid(player) || FormulaUtils.voidCustomRanger(player)) {
            effectiveLevel += effectiveLevel * 0.025;
        }

        if (FormulaUtils.wearingTwistedSlayerHelmetI(player) && target != null && target.isNpc() && includeNpcMax) {
            effectiveLevel += effectiveLevel / 10; //+10%
        }

        if (player.getEquipment().contains(ItemIdentifiers.SALVE_AMULET)) {
            effectiveLevel += effectiveLevel / 15;
        }

        if (player.getEquipment().contains(ItemIdentifiers.SALVE_AMULETI) || player.getEquipment().contains(SALVE_AMULET_E) || player.getEquipment().contains(ItemIdentifiers.SALVE_AMULETEI)) {
            effectiveLevel += effectiveLevel / 20;
        }

        int baseDamage = (int) (1.3 + effectiveLevel / 10 + (bonuses.rangestr / 80) + (effectiveLevel * bonuses.rangestr) / 640);
        var off_additional_bonus = 1.0;

        if (player.hasPetOut("Baby Aragog")) {
            var percentage = target != null && target.isNpc() && includeNpcMax ? 0.10 : 0.05;
            off_additional_bonus += percentage;
        }

        if (player.hasPetOut("Olmlet") && target != null && target.isNpc()) {
            if (target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY && includeNpcMax) {
                off_additional_bonus *= 1.10;
            }
            if (player.getRaids() != null && player.getRaids().raiding(player))
                off_additional_bonus *= 1.10;
        }

        if (player.hasPetOut("Skeleton hellhound pet")) {
            off_additional_bonus += 0.05;
        }

        var crystalArmourMultiplier = 1.0;

        if (player.getEquipment().hasAt(EquipSlot.HEAD, CRYSTAL_HELM)) {
            crystalArmourMultiplier += 0.025;//2.5% damage boost
        }

        if(player.getEquipment().hasAt(EquipSlot.HEAD, CORRUPTED_CRYSTAL_HELM)) {
            crystalArmourMultiplier += 0.030;//3% damage boost
        }

        if(player.getEquipment().hasAt(EquipSlot.BODY, CORRUPTED_CRYSTAL_BODY)) {
            crystalArmourMultiplier += 0.10;//10% damage boost
        }
        if (player.getEquipment().hasAt(EquipSlot.HEAD, GREEN_SLAYER_HELMET_I)) {//G1
            crystalArmourMultiplier += 0.10;//10% damage boost
        }
        if(player.getEquipment().hasAt(EquipSlot.LEGS, CORRUPTED_CRYSTAL_LEGS)) {
            crystalArmourMultiplier += 0.070;//7% damage boost
        }

        if (player.getEquipment().hasAt(EquipSlot.BODY, CRYSTAL_BODY)) {
            crystalArmourMultiplier += 0.075;//7.5% damage boost
        }

        if (player.getEquipment().hasAt(EquipSlot.LEGS, CRYSTAL_LEGS)) {
            crystalArmourMultiplier += 0.05;//5.0% damage boost
        }

        // Append the Twisted bow computation, if we have enough data..
        if (weapon != null && (weapon.getId() == TWISTED_BOW || weapon.getId() == TWISTED_BOW_I) && target != null && target.isNpc() && includeNpcMax) {
            int magicLevel = 0;

            if (((Npc) target).combatInfo() != null && ((Npc) target).combatInfo().stats != null)
                magicLevel = ((Npc) target).combatInfo().stats.magic;

            double damage = 265D + ((3 * magicLevel - 14D) / 100D) - (Math.pow(3 * magicLevel / 10.0 - 140.0, 2) / 100D);
            damage = Math.min(265D, damage);
            baseDamage *= Math.min(2D, 1D + damage);
            return baseDamage;
        }

        // Append the Twisted bow computation, if we have enough data..
        if (weapon != null && weapon.getId() == SANGUINE_TWISTED_BOW && target != null && target.isNpc() && includeNpcMax) {
            int magicLevel = 0;

            if (((Npc) target).combatInfo() != null && ((Npc) target).combatInfo().stats != null)
                magicLevel = ((Npc) target).combatInfo().stats.magic;

            double damage = 280D + ((3 * magicLevel - 14D) / 100D) - (Math.pow(3 * magicLevel / 10.0 - 140.0, 2) / 100D);
            damage = Math.min(280, damage);
            baseDamage *= Math.min(2D, 1D + damage);
            return baseDamage;
        }

        //• Dragon hunter crossbow: 1.25
        if ((player.getEquipment().hasAt(EquipSlot.WEAPON, DRAGON_HUNTER_CROSSBOW) || player.getEquipment().hasAt(EquipSlot.WEAPON, DRAGON_HUNTER_CROSSBOW_T)) && target != null && includeNpcMax) {
            if (target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                off_additional_bonus *= 1.25;
            }

            if (FormulaUtils.isDragon(target)) {
                off_additional_bonus *= 1.25;
            }
        }

        var weakSpot = player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.WEAK_SPOT);

        if (weakSpot && target != null && target.isNpc()) {
            if (Slayer.creatureMatches(player, target.getAsNpc().id())) {
                off_additional_bonus += 0.10;
            }
        }

        //Craws Bow
        if (FormulaUtils.hasCrawsBow(player) && target != null && target.isNpc() && includeNpcMax) {
            off_additional_bonus += 0.50;
        }

        if (player.getEquipment().hasAt(EquipSlot.WEAPON, CRAWS_BOW_C) && target != null && target.isNpc() && includeNpcMax) {
            off_additional_bonus += 0.50;
        }

        //System.out.println("Regular hit "+baseDamage+" times specialMultiplier "+specialMultiplier);

        maxHit = (int) Math.floor(baseDamage * off_additional_bonus * crystalArmourMultiplier * specialMultiplier);

        if (player.hasPetOut("Little Nightmare")) {
            maxHit += 1;
        }

        if (player.getEquipment().hasAt(EquipSlot.AMULET, NECKLACE_OF_ANGUISH_OR)) {
            maxHit += 1;
        }

        if (player.getEquipment().hasAt(EquipSlot.WEAPON, MAGMA_BLOWPIPE)) {
            maxHit += 3;
        }

        if (player.hasPetOut("Youngllef pet")) {
            maxHit += 1;
        }

        if (player.hasPetOut("Corrupted Youngllef pet")) {
            maxHit += 2;
        }

        //System.out.printf("range max hit %d %n", maxHit);
        return maxHit;
    }
}
