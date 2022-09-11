package com.ferox.game.world.entity.combat.formula;

import com.ferox.GameServer;
import com.ferox.game.content.skill.impl.slayer.Slayer;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatConstants;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.magic.CombatSpell;
import com.ferox.game.world.entity.combat.magic.CombatSpells;
import com.ferox.game.world.entity.combat.prayer.default_prayer.Prayers;
import com.ferox.game.world.entity.combat.weapon.AttackType;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

import static com.ferox.game.world.entity.combat.prayer.default_prayer.Prayers.*;
import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;
import static com.ferox.util.NpcIdentifiers.CORPOREAL_BEAST;

/**
 * Created by Bart on 8/22/2015.
 */
public class AccuracyFormula {

    public static final SecureRandom srand = new SecureRandom();
    private static final Logger logger = LogManager.getLogger(AccuracyFormula.class);

    public static boolean doesHit(Mob entity, Mob enemy, CombatType style) {
        return doesHit(entity, enemy, style, 1);
    }

    public static boolean doesHit(Mob entity, Mob enemy, CombatType style, int spellLevel) {
        EquipmentInfo.Bonuses playerBonuses = EquipmentInfo.totalBonuses(entity, World.getWorld().equipmentInfo());
        EquipmentInfo.Bonuses targetBonuses = EquipmentInfo.totalBonuses(enemy, World.getWorld().equipmentInfo());

		/*
			S E T T I N G S

			S T A R T
		*/

        //attack stances
        int off_stance_bonus = 0;
        int def_stance_bonus = 0;
        if (entity.isPlayer()) {
            final FightStyle attackStyle = entity.getCombat().getFightType().getStyle();
            off_stance_bonus = attackStyle.equals(FightStyle.ACCURATE) ? 3 : attackStyle.equals(FightStyle.CONTROLLED) ? 1 : 0; //accurate, aggressive, controlled, defensive
            def_stance_bonus = attackStyle.equals(FightStyle.DEFENSIVE) ? 3 : attackStyle.equals(FightStyle.CONTROLLED) ? 1 : 0; //accurate, aggressive, controlled, defensive
        }

        //requirements
        int off_weapon_requirement = 1; //weapon attack level requirement
        int off_spell_requirement = spellLevel; //spell magic level requirement

        //base levels
        int off_base_attack_level = 1;
        int off_base_ranged_level = 1;
        int off_base_magic_level = 1;
        int attackerWeaponId = -1;
        double twistedBowMultiplier = 1.0; // Defaults to no change (x1)

        if (entity.isPlayer()) {
            Player player = ((Player) entity);
            final Item weapon = player.getEquipment().get(EquipSlot.WEAPON);

            if (weapon != null) {
                attackerWeaponId = weapon.getId(); // Used below in Twisted bow computation.

                //melee
                final Map<Integer, Integer> requiredLevels = World.getWorld().equipmentInfo().requirementsFor(weapon.getId());
                if (requiredLevels != null) {
                    final Integer requiredLevel = requiredLevels.get(Skills.ATTACK);
                    if (requiredLevel != null)
                        off_weapon_requirement = requiredLevel;
                }
                if (requiredLevels != null) {
                    final Integer requiredLevel = requiredLevels.get(Skills.RANGED);
                    if (requiredLevel != null)
                        off_weapon_requirement = requiredLevel;
                }
            }

            off_base_attack_level = (int) (player.skills().xpLevel(Skills.ATTACK)) + ((player.skills().xpLevel(Skills.ATTACK)) / 3);
            off_base_ranged_level = player.skills().xpLevel(Skills.RANGED);
            off_base_magic_level = player.skills().xpLevel(Skills.MAGIC);
        } else {
            Npc npc = ((Npc) entity);
            if (npc.combatInfo() != null && npc.combatInfo().stats != null) {
                off_base_attack_level = (int) (npc.combatInfo().stats.attack);
                off_base_ranged_level = npc.combatInfo().stats.ranged;
                off_base_magic_level = npc.combatInfo().stats.magic;
            } else {
                logger.error("Npc id {} index {} name {} missing combat info or stats.", npc.id(), npc.getIndex(), npc.def().name);
            }
        }

        //current levels
        double off_current_attack_level = 1;
        double off_current_ranged_level = 1;
        double off_current_magic_level = 1;

        if (entity.isPlayer()) {
            assert entity instanceof Player;
            Player player = ((Player) entity);
            off_current_attack_level = player.skills().level(Skills.ATTACK);
            off_current_ranged_level = player.skills().level(Skills.RANGED);
            off_current_magic_level = player.skills().level(Skills.MAGIC);
        } else {
            assert entity instanceof Npc;
            Npc npc = ((Npc) entity);
            if (npc.combatInfo() != null && npc.combatInfo().stats != null) {
                off_current_attack_level = (int) (npc.combatInfo().stats.attack);
                off_current_ranged_level = npc.combatInfo().stats.ranged;
                off_current_magic_level = npc.combatInfo().stats.magic;
            }
        }



        double def_current_defence_level = 1;
        double def_current_magic_level = 1;

        if (enemy.isPlayer()) {
            Player opp = (Player) enemy;
            def_current_defence_level = opp.skills().level(Skills.DEFENCE);
            def_current_magic_level = opp.skills().level(Skills.MAGIC);

            int hpmissing = opp.maxHp() - opp.hp();
            if (hpmissing > 0 && Equipment.hasAmmyOfDamned(opp) && Equipment.fullTorag(opp)) {
                // Calc % increase. 1% per 1hp missing.
                double multi = 0.01D * hpmissing;
                def_current_defence_level += def_current_defence_level * multi;
            }
        } else {
            Npc npc = ((Npc) enemy);
            if (npc.combatInfo() != null && npc.combatInfo().stats != null) {
                def_current_defence_level = npc.combatInfo().stats.defence;
                def_current_magic_level = npc.combatInfo().stats.magic;
            }
        }

        off_current_attack_level *= 1.1;
        off_current_ranged_level *= 1.15;
        off_current_magic_level *= 1.15;

        //prayer bonuses
        double off_attack_prayer_bonus = 1.0;
        double off_ranged_prayer_bonus = 1.0;
        double off_magic_prayer_bonus = 1.0;
        double def_defence_prayer_bonus = 1.0;

        // Prayers
        if (entity.isPlayer()) {
            Player p = (Player) entity;
            if (Prayers.usingPrayer(p, CLARITY_OF_THOUGHT))
                off_attack_prayer_bonus += 0.05; // 5% attack level boost
            else if (Prayers.usingPrayer(p, IMPROVED_REFLEXES))
                off_attack_prayer_bonus += 0.10; // 10% attack level boost
            else if (Prayers.usingPrayer(p, INCREDIBLE_REFLEXES))
                off_attack_prayer_bonus += 0.15; // 15% attack level boost
            else if (Prayers.usingPrayer(p, CHIVALRY))
                off_attack_prayer_bonus += 0.15; // 15% attack level boost
            else if (Prayers.usingPrayer(p, PIETY))
                off_attack_prayer_bonus += 0.20; // 20% attack level boost

            // System.out.println("attk bonus "+off_attack_prayer_bonus);

            if (Prayers.usingPrayer(p, SHARP_EYE))
                off_ranged_prayer_bonus += 0.05; // 5% range level boost
            else if (Prayers.usingPrayer(p, HAWK_EYE))
                off_ranged_prayer_bonus += 0.10; // 10% range level boost
            else if (Prayers.usingPrayer(p, EAGLE_EYE))
                off_ranged_prayer_bonus += 0.15; // 15% range level boost
            else if (Prayers.usingPrayer(p, RIGOUR))
                off_ranged_prayer_bonus += 0.20; // 20% range level boost

            if (Prayers.usingPrayer(p, MYSTIC_WILL))
                off_magic_prayer_bonus += 0.05; // 5% magic level boost
            else if (Prayers.usingPrayer(p, MYSTIC_LORE))
                off_magic_prayer_bonus += 0.10; // 10% magic level boost
            else if (Prayers.usingPrayer(p, MYSTIC_MIGHT))
                off_magic_prayer_bonus += 0.15; // 15% magic level boost
            else if (Prayers.usingPrayer(p, AUGURY))
                off_magic_prayer_bonus += 0.25; // 25% magic level boost
        }

        if (enemy.isPlayer()) {
            Player p = (Player) enemy;

            if (Prayers.usingPrayer(p, THICK_SKIN))
                def_defence_prayer_bonus += 0.05; // 5% def level boost
            else if (Prayers.usingPrayer(p, ROCK_SKIN))
                def_defence_prayer_bonus += 0.10; // 10% def level boost
            else if (Prayers.usingPrayer(p, STEEL_SKIN))
                def_defence_prayer_bonus += 0.15; // 15% def level boost
            if (Prayers.usingPrayer(p, CHIVALRY))
                def_defence_prayer_bonus += 0.20; // 20% def level boost
            else if (Prayers.usingPrayer(p, PIETY))
                def_defence_prayer_bonus += 0.25; // 25% def level boost
            else if (Prayers.usingPrayer(p, RIGOUR))
                def_defence_prayer_bonus += 0.25; // 25% def level boost
            else if (Prayers.usingPrayer(p, AUGURY))
                def_defence_prayer_bonus += 0.25; // 25% def level boost
        }

        //additional bonus
        double off_additional_bonus = 1.0;

        //OS-Scape has diff hit calculations they increase the multiplier in the Special attacks themselves we use an enum for that
        if (entity.isPlayer()) {
            final Player player = (Player) entity;
            if (player.isSpecialActivated()) {
                if (player.getCombatSpecial() != null) {
                    double bonus = 0.0;
                    if (player.hasPetOut("Artio pet")) {
                        bonus += 0.20;
                    }
                    if (player.hasPetOut("Jal-Nib-Rek pet")) {
                        bonus += 0.10;
                    }
                    off_additional_bonus = player.getCombatSpecial().getAccuracyMultiplier() + bonus;
                }
            }
        }

        if (entity.isPlayer()) {
            assert entity instanceof Player;
            final Player player = (Player) entity;
            final Item helm = player.getEquipment().get(EquipSlot.HEAD);
            final Item weapon = player.getEquipment().get(EquipSlot.WEAPON);

            if (enemy.isNpc()) {
                final Npc npc = (Npc) enemy;

                AttackType attackType = player.getCombat().getFightType().getAttackType();
                if (npc.id() == CORPOREAL_BEAST) {
                    if (weapon != null && player.getEquipment().corpbeastArmour(weapon) && attackType != null && attackType.equals(AttackType.STAB)) {
                        off_additional_bonus -= 0.5;
                    }
                }

                if (helm != null && Slayer.creatureMatches(player, npc.id())) {
                    if (player.getEquipment().wearingSlayerHelm() || (IntStream.range(8901, 8921).anyMatch(id -> id == helm.getId()))) {
                        off_additional_bonus += 0.125;
                    }
                }

                //Arclight
                if (player.getEquipment().hasAt(EquipSlot.WEAPON, ARCLIGHT)) {
                    if (npc.def() != null && npc.def().name != null && FormulaUtils.isDemon(npc)) {
                        off_additional_bonus += 0.7;
                    }
                }

                //Dragon hunter crossbow and lance
                if (player.getEquipment().hasAt(EquipSlot.WEAPON, DRAGON_HUNTER_CROSSBOW) || player.getEquipment().hasAt(EquipSlot.WEAPON, DRAGON_HUNTER_CROSSBOW_T) || player.getEquipment().hasAt(EquipSlot.WEAPON, DRAGON_HUNTER_LANCE)) {
                    if (npc.def() != null && npc.def().name != null && FormulaUtils.isDragon(npc)) {
                        off_additional_bonus += 0.3;
                    }
                }

                boolean ancientKingBlackDragonPet = player.hasPetOut("Ancient king black dragon");
                if (ancientKingBlackDragonPet) {
                    if (npc.def() != null && npc.def().name != null && FormulaUtils.isDragon(npc)) {
                        off_additional_bonus += 0.25;
                    }
                }
                if (player.getEquipment().hasAt(EquipSlot.SHIELD, MOLTEN_DEFENDER) && style.equals(CombatType.MELEE)) {
                    off_additional_bonus += 0.1;
                }

                //Magic on lava dragons
                if (npc.id() == 6593 && style.equals(CombatType.MAGIC)) {
                    off_additional_bonus += 0.5;
                }

                if (player.getEquipment().contains(ItemIdentifiers.SALVE_AMULET)) {
                    off_additional_bonus += 0.15;
                }

                if (player.getEquipment().contains(ItemIdentifiers.SALVE_AMULETI) || player.getEquipment().contains(SALVE_AMULET_E) || player.getEquipment().contains(ItemIdentifiers.SALVE_AMULETEI)) {
                    off_additional_bonus += 0.2;
                }

                //  if (weapon != null && (weapon.getId() == TWISTED_BOW || weapon.getId() == TWISTED_BOW_ORANGE || weapon.getId() == TWISTED_BOW_PURPLE || weapon.getId() == TWISTED_BOW_I || weapon.getId() == SANGUINE_TWISTED_BOW)) {
                //      twistedBowMultiplier = twistedBowAccuracyMultiplier((int) def_current_magic_level) + 0.10;
                //  }

                if (weapon != null && weapon.getId() == SANGUINE_TWISTED_BOW) {
                    twistedBowMultiplier += 0.25;
                }

                //The sword of gryffindor gives a 25% accuracy boost vs monsters.
                if (player.getEquipment().hasAt(EquipSlot.WEAPON, CustomItemIdentifiers.SWORD_OF_GRYFFINDOR)) {
                    off_additional_bonus += 0.25;
                }

                if (player.getEquipment().hasAt(EquipSlot.WEAPON, CustomItemIdentifiers.MAGMA_BLOWPIPE)) {
                    off_additional_bonus += 0.15;
                }

                //Craws Bow and viggora's chainmace
                if (weapon != null && (FormulaUtils.hasCrawsBow(player) || FormulaUtils.hasViggorasChainMace(player))) {
                    off_additional_bonus += 0.5;
                }

                if (player.getEquipment().hasAt(EquipSlot.WEAPON, VIGGORAS_CHAINMACE_C) || player.getEquipment().hasAt(EquipSlot.WEAPON, CRAWS_BOW_C)) {
                    off_additional_bonus += 0.5;
                }

                //Thammaron Sceptre
                if (weapon != null && FormulaUtils.hasThammaronSceptre(player)) {
                    off_additional_bonus += 1.00;
                }

                if (player.getEquipment().hasAt(EquipSlot.WEAPON, THAMMARONS_STAFF_C)) {
                    off_additional_bonus += 1.00;
                }

                if (player.pet() != null && player.hasPetOut("Pet zombies champion") && npc.isWorldBoss()) {
                    off_additional_bonus += 0.10;
                }

                if (player.pet() != null && player.hasPetOut("Olmlet") && player.getRaids() != null && player.getRaids().raiding(player)) {
                    off_additional_bonus += 0.10;
                }

                if (player.hasPetOut("Nagini pet")) {
                    off_additional_bonus += 0.10;
                }

                if (player.hasPetOut("Baby Elvarg") && style == CombatType.MAGIC) {
                    off_additional_bonus += 0.05;
                }

                if (player.hasPetOut("Snowbird") && style == CombatType.MAGIC) {
                    off_additional_bonus += 0.05;
                }
                if (player.hasPetOut("Deranged archaeologist") && style == CombatType.MAGIC) {
                    off_additional_bonus += 0.10;
                }

                if (player.tile().memberCave()) {
                    off_additional_bonus += 0.10;
                }

                if (player.getEquipment().hasAt(EquipSlot.HEAD, TOTEMIC_HELMET) || player.getEquipment().hasAt(EquipSlot.HEAD, DARK_SAGE_HAT) || player.getEquipment().hasAt(EquipSlot.HEAD, SARKIS_DARK_COIF)) {
                    off_additional_bonus += 0.05;//5% accuracy boost
                }

                if (player.getEquipment().hasAt(EquipSlot.BODY, TOTEMIC_PLATEBODY) || player.getEquipment().hasAt(EquipSlot.BODY, DARK_SAGE_ROBE_TOP) || player.getEquipment().hasAt(EquipSlot.BODY, SARKIS_DARK_BODY)) {
                    off_additional_bonus += 0.10;//10% damage boost
                }

                if (player.getEquipment().hasAt(EquipSlot.BODY, CORRUPTED_CRYSTAL_HELM) || player.getEquipment().hasAt(EquipSlot.BODY, CORRUPTED_CRYSTAL_BODY) || player.getEquipment().hasAt(EquipSlot.BODY, CORRUPTED_CRYSTAL_LEGS)) {
                    off_additional_bonus += 0.05;//10% damage boost
                }


                if (player.getEquipment().hasAt(EquipSlot.LEGS, TOTEMIC_PLATELEGS) || player.getEquipment().hasAt(EquipSlot.LEGS, DARK_SAGE_ROBE_BOTTOM) || player.getEquipment().hasAt(EquipSlot.LEGS, SARKIS_DARK_LEGS)) {
                    off_additional_bonus += 10;//10.0% damage boost
                }
            }

            if (FormulaUtils.obbyArmour(player) && FormulaUtils.hasObbyWeapon(player)) {
                off_additional_bonus += 0.1;
            }

            //Pet accuracy bonus

            final boolean anyCombatStyle = style.equals(CombatType.MELEE) || style.equals(CombatType.RANGED) || style.equals(CombatType.MAGIC);
            if (player.hasPetOut("Kerberos pet") && anyCombatStyle) {
                off_additional_bonus += 0.05;
            }

            if (player.hasPetOut("Skeleton hellhound pet") && anyCombatStyle) {
                off_additional_bonus += 0.05;
            }

            if (player.hasPetOut("Youngllef pet") && anyCombatStyle) {
                off_additional_bonus += 0.075;
            }

            if (player.hasPetOut("Corrupted Youngllef pet") && anyCombatStyle) {
                off_additional_bonus += 0.125;
            }

            if (player.hasPetOut("Little Nightmare") && style.equals(CombatType.MAGIC)) {
                off_additional_bonus += 0.05;
            }

            if (player.hasPetOut("Little Nightmare") && style.equals(CombatType.RANGED)) {
                off_additional_bonus += 0.05;
            }

            if (player.hasPetOut("Baby Dark Beast") && weapon != null && Equipment.darkbow(weapon.getId())) {
                off_additional_bonus += 0.10;
            }

            if (player.hasPetOut("Baby Barrelchest") && style == CombatType.MELEE) {
                off_additional_bonus += 0.10;
            }

            if (player.hasPetOut("Ancient barrelchest") && style == CombatType.MELEE) {
                off_additional_bonus += 0.20;
            }

            if (player.hasPetOut("Memory of seren") && style == CombatType.RANGED) {
                off_additional_bonus += 0.15;
            }

            if (player.hasPetOut("Baby Squirt")) {
                off_additional_bonus += 0.10;
            }

            if (player.hasPetOut("Corrupted nechryarch pet") && style == CombatType.MELEE) {
                off_additional_bonus += 0.15;
            }

            if (player.hasPetOut("Dharok the Wretched") && player.getEquipment().hasAt(EquipSlot.WEAPON, DHAROKS_GREATAXE)) {
                off_additional_bonus += 0.10;
            }

            if (player.hasPetOut("Baby Aragog") && style == CombatType.RANGED) {
                off_additional_bonus += 0.15;
            }

            if (player.hasPetOut("Kaal-Ket-Jor jr")) {
                off_additional_bonus += 0.15;
            }

            if (player.hasPetOut("Baby Lava Dragon") && style == CombatType.MAGIC) {
                off_additional_bonus += 0.10;
            }

            //Custom item accuracy

            if (player.getEquipment().hasAt(EquipSlot.WEAPON, ELDER_MAUL_21205)) {
                off_additional_bonus += 0.10;
            }

            if (player.getEquipment().hasAt(EquipSlot.HEAD, ANCESTRAL_HAT_I)) {
                off_additional_bonus += 0.10;
            }

            if (player.getEquipment().hasAt(EquipSlot.BODY, ANCESTRAL_ROBE_TOP_I)) {
                off_additional_bonus += 0.10;
            }

            if (player.getEquipment().hasAt(EquipSlot.LEGS, ANCESTRAL_ROBE_BOTTOM_I)) {
                off_additional_bonus += 0.10;
            }

            if (player.getEquipment().hasAt(EquipSlot.HEAD, CRYSTAL_HELM)) {
                off_additional_bonus += 0.5;//5% accuracy boost
            }

            if (player.getEquipment().hasAt(EquipSlot.BODY, CRYSTAL_BODY)) {
                off_additional_bonus += 0.15;//15% damage boost
            }

            if (player.getEquipment().hasAt(EquipSlot.LEGS, CRYSTAL_LEGS)) {
                off_additional_bonus += 0.10;//10.0% damage boost
            }

            //Custom effect not from OSRS, OSRS is 2.5% this is 5%
            if (player.getCombat().getFightType().getAttackType() == AttackType.CRUSH) {
                if (player.getEquipment().hasAt(EquipSlot.HEAD, INQUISITORS_GREAT_HELM)) {
                    off_additional_bonus += 0.01;//1% accuracy boost
                }

                if (player.getEquipment().hasAt(EquipSlot.BODY, INQUISITORS_HAUBERK)) {
                    off_additional_bonus += 0.01;//1% accuracy boost
                }

                if (player.getEquipment().hasAt(EquipSlot.LEGS, INQUISITORS_PLATESKIRT)) {
                    off_additional_bonus += 0.01;//1% accuracy boost
                }

                if (player.getEquipment().hasAt(EquipSlot.HEAD, INQUISITORS_GREAT_HELM) || player.getEquipment().hasAt(EquipSlot.BODY, INQUISITORS_HAUBERK) || player.getEquipment().hasAt(EquipSlot.LEGS, INQUISITORS_PLATESKIRT)) {
                    off_additional_bonus += 0.02;//2% accuracy boost
                }
            }
        }

        //if the player is using a smoke battlestaff
        if (entity.isPlayer()) {
            if (entity.getAsPlayer().getEquipment().containsAny(11998, 12000) && style.equals(CombatType.MAGIC)) {
                off_additional_bonus += 0.10;
            }
        }

        //equipment bonuses
        int off_equipment_stab_attack = playerBonuses.stab;
        int off_equipment_slash_attack = playerBonuses.slash;
        int off_equipment_crush_attack = playerBonuses.crush;
        int off_equipment_ranged_attack = playerBonuses.range;
        int off_equipment_magic_attack = playerBonuses.mage;

        int def_equipment_stab_defence = targetBonuses.stabdef;
        int def_equipment_slash_defence = targetBonuses.slashdef;
        int def_equipment_crush_defence = targetBonuses.crushdef;
        int def_equipment_ranged_defence = targetBonuses.rangedef;
        int def_equipment_magic_defence = targetBonuses.magedef;

        // if (enemy.isNpc()) {
        //    assert enemy instanceof Npc;
        //     Npc npc = (Npc) enemy;
        //    if (npc.combatInfo() != null && npc.combatInfo().stats != null && npc.combatInfo().boss) {
        //        def_equipment_ranged_defence -= (def_current_defence_level * 0.50); //I don't like this solution but this formula is fucked.
        //    }
        // }

        //protect from * prayers
        boolean def_protect_from_melee = false;
        boolean def_protect_from_ranged = false;
        boolean def_protect_from_magic = false;

        if (entity.isNpc() && enemy.isPlayer()) {
            assert enemy instanceof Player;
            Player player = ((Player) enemy);
            def_protect_from_melee = Prayers.usingPrayer(player, PROTECT_FROM_MELEE);
            def_protect_from_ranged = Prayers.usingPrayer(player, PROTECT_FROM_MISSILES);
            def_protect_from_magic = Prayers.usingPrayer(player, PROTECT_FROM_MAGIC);
        }

        //chance bonuses
        double off_special_attack_bonus = 1.0;
        double off_void_bonus = 1.0;

        if (entity.isPlayer()) {
            assert entity instanceof Player;
            final Player player = (Player) entity;
            if (style.equals(CombatType.MELEE) && (FormulaUtils.voidMelee(player) || FormulaUtils.voidCustomMelee(player)))
                off_void_bonus = 1.10;
            else if (style.equals(CombatType.RANGED) && (FormulaUtils.voidRanger(player) || FormulaUtils.voidCustomRanger(player)))
                off_void_bonus = 1.10;
            else if (style.equals(CombatType.MAGIC) && (FormulaUtils.voidMagic(player) || FormulaUtils.voidCustomMagic(player)))
                off_void_bonus = 1.30;
        }

		/*
			S E T T I N G S

			E N D
		*/



		/*
			C A L C U L A T E D
			V A R I A B L E S

			S T A R T
		*/

        //experience bonuses
        double off_spell_bonus = 0;
        double off_weapon_bonus = 0;

        //effective levels
        double effective_attack = 0;
        double effective_magic = 0;
        double effective_defence = 0;

        //relevent equipment bonuses
        int off_equipment_bonus = 0;
        int def_equipment_bonus = 0;

        //augmented levels
        double augmented_attack = 0;
        double augmented_defence = 0;

        //hit chances
        double hit_chance = 0;
        double off_hit_chance = 0;
        double def_block_chance = 0;

		/*
			C A L C U L A T E D
			V A R I A B L E S

			E N D
		*/

        AttackType off_style = null;
        if (entity.isPlayer()) {
            off_style = entity.getCombat().getFightType().getAttackType();
        }

        //determine effective attack
        switch (style) {
            case MELEE -> {
                if (off_base_attack_level > off_weapon_requirement) {
                    off_weapon_bonus = (off_base_attack_level - off_weapon_requirement) * .3;
                }
                effective_attack = Math.floor((((off_current_attack_level * off_attack_prayer_bonus) * off_additional_bonus) + off_stance_bonus + off_weapon_bonus));
                effective_defence = Math.floor((def_current_defence_level * def_defence_prayer_bonus) + def_stance_bonus);
                //System.out.println("effective Def  " + effective_defence);
                if (off_style != null) {
                    switch (off_style) {
                        case STAB -> {
                            off_equipment_bonus = off_equipment_stab_attack;
                            def_equipment_bonus = def_equipment_stab_defence;
                        }
                        case SLASH -> {
                            off_equipment_bonus = off_equipment_slash_attack;
                            def_equipment_bonus = def_equipment_slash_defence;
                        }
                        case CRUSH -> {
                            off_equipment_bonus = off_equipment_crush_attack;
                            def_equipment_bonus = def_equipment_crush_defence;
                        }
                        default -> {
                            off_equipment_bonus = Math.max(Math.max(off_equipment_stab_attack, off_equipment_slash_attack), off_equipment_crush_attack);
                            def_equipment_bonus = Math.max(Math.max(def_equipment_stab_defence, def_equipment_slash_defence), def_equipment_crush_defence);
                        }
                    }
                } else {
                    off_equipment_bonus = Math.max(Math.max(off_equipment_stab_attack, off_equipment_slash_attack), off_equipment_crush_attack);
                    def_equipment_bonus = Math.max(Math.max(def_equipment_stab_defence, def_equipment_slash_defence), def_equipment_crush_defence);
                }
            }
            case RANGED -> {
                if (off_base_ranged_level > off_weapon_requirement) {
                    off_weapon_bonus = (off_base_ranged_level - off_weapon_requirement) * .3;
                }
                effective_attack = Math.floor((((off_current_ranged_level * off_ranged_prayer_bonus) * off_additional_bonus) + off_stance_bonus + off_weapon_bonus) * twistedBowMultiplier);
                effective_defence = Math.floor((def_current_defence_level * def_defence_prayer_bonus) + def_stance_bonus);
                off_equipment_bonus = off_equipment_ranged_attack;
                def_equipment_bonus = def_equipment_ranged_defence;
            }
            case MAGIC -> {
                if (off_base_magic_level > off_spell_requirement) {
                    off_spell_bonus = (off_base_magic_level - off_spell_requirement) * .4;
                }
                effective_attack = Math.floor(((off_current_magic_level * off_magic_prayer_bonus) * off_additional_bonus) + off_spell_bonus);
                effective_magic = Math.floor(def_current_magic_level * .7);
                effective_defence = Math.floor((def_current_defence_level * def_defence_prayer_bonus) * .3);
                effective_defence = effective_defence + effective_magic;
                off_equipment_bonus = off_equipment_magic_attack;
                def_equipment_bonus = def_equipment_magic_defence;
            }
        }

        //augmented_attack = Math.floor(((effective_attack+8) * (off_equipment_bonus + 64.)));
        //augmented_defence = Math.floor(((effective_defence+8) * (def_equipment_bonus + 64.)));
        augmented_attack = srand.nextInt((int) ((effective_attack + 8) * (off_equipment_bonus + 64.)));
        augmented_defence = srand.nextInt((int) ((effective_defence + 8) * (def_equipment_bonus + 64.)));

        augmented_attack = Math.floor(augmented_attack);
        augmented_defence = Math.floor(augmented_defence);

        //System.out.println(augmented_attack);
        //  System.out.println(augmented_defence);

        if (augmented_attack < augmented_defence) {
            hit_chance = augmented_attack / ((augmented_defence + 1.) * 2.);
            // hit_chance = (augmented_attack - 1.) / (2. * augmented_defence);
        } else {
            hit_chance = 1. - ((augmented_defence + 2.) / ((augmented_attack + 1.) * 2.));
            // hit_chance = 1. - (augmented_defence + 1) / (2. * augmented_attack);
        }

        //System.out.println("hit chance +  " + hit_chance);

        switch (style) {
            case MELEE:
                if (Prayers.usingPrayer(enemy, PROTECT_FROM_MELEE)) {
                    off_hit_chance = Math.floor((((hit_chance) * off_void_bonus) * .6) * 100.);
                    def_block_chance = Math.floor(101 -(((hit_chance) * off_void_bonus) * .6) * 350.);
                } else {
                    off_hit_chance = Math.floor(((hit_chance) * off_void_bonus) * 100.);
                    def_block_chance = Math.floor(101 -(((hit_chance) * off_void_bonus) * 350.));
                }
                break;
            case RANGED:
                if (Prayers.usingPrayer(enemy, PROTECT_FROM_MISSILES)) {
                    off_hit_chance = Math.floor((((hit_chance) * off_void_bonus) * .6) * 100.);
                    def_block_chance = Math.floor(101 -(((hit_chance) * off_void_bonus) * .6) * 350.);
                } else {
                    off_hit_chance = Math.floor(((hit_chance) * off_void_bonus) * 100.);
                    def_block_chance = Math.floor(101 -(((hit_chance) * off_void_bonus) * 350.));
                }
                break;
            case MAGIC:
                if (Prayers.usingPrayer(enemy, PROTECT_FROM_MAGIC)) {
                    off_hit_chance = Math.floor(((hit_chance * off_void_bonus) * .6) * 100.);
                    def_block_chance = Math.floor(101 -(((hit_chance) * off_void_bonus) * .6) * 350.);
                } else {
                    off_hit_chance = Math.floor((hit_chance * off_void_bonus) * 100.);
                    def_block_chance = Math.floor(101 -((hit_chance * off_void_bonus) * 350.));
                }
                break;
        }

        if (entity.isPlayer() && (boolean) GameServer.properties().logAccuracyChances) {
            String msg = String.format("Atk %d v def %d. Bonus %d vs %d. Level %d vs %d. Relative %d%% hit > %d%% block%n",
                (int) augmented_attack, (int) augmented_defence,
                off_equipment_bonus, def_equipment_bonus, (int) effective_attack, (int) effective_defence, (int) off_hit_chance, (int) def_block_chance);
            System.out.println(msg);
            entity.message(msg);
            //System.out.println(targetBonuses);
        }

        /*
         * Brimstone ring effect.
         */
        if (style == CombatType.MAGIC && entity.isPlayer() && entity.getAsPlayer().getEquipment().contains(BRIMSTONE_RING)) {
            if (World.getWorld().rollDie(4, 1)) {
                entity.message("Your attack ignored 10% of your opponent's magic defence.");
                def_block_chance = def_block_chance * 0.9D;
            }
        }

        off_hit_chance = (int) (srand.nextFloat() * off_hit_chance);
        def_block_chance = (int) (srand.nextFloat() * def_block_chance);

        //print roll
        //System.out.println("\nYou rolled: " + (int) off_hit_chance);
        //System.out.println("Your opponent rolled: " + (int) def_block_chance);

        //determine hit
        if (entity.isPlayer()) {
            //System.out.println("Success =  " + off_hit_chance + " > " + def_block_chance);
        }
        return off_hit_chance > def_block_chance;
    } //end main

    private static double twistedBowAccuracyMultiplier(double magicLevel) {
        return Math.min(140, (140 + Math.floor(3*magicLevel - 10)/100) - Math.pow((3*magicLevel/10 - 100), 2) / 100) / 100;
    }

}
