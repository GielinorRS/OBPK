package com.ferox.game.world.entity.combat.formula;

import com.ferox.game.content.skill.impl.slayer.Slayer;
import com.ferox.game.content.skill.impl.slayer.SlayerConstants;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.magic.CombatSpell;
import com.ferox.game.world.entity.mob.player.EquipSlot;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skills;
import com.ferox.game.world.items.container.equipment.EquipmentInfo;
import com.ferox.util.NpcIdentifiers;
import com.ferox.util.timers.TimerKey;

import static com.ferox.util.CustomItemIdentifiers.*;
import static com.ferox.util.ItemIdentifiers.*;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since November 06, 2021
 */
public class MagicMaxHit {

    public static int maxHit(Player player, boolean includeNpcMax) {
        int baseMaxHit = 0;
        CombatSpell spell = player.getCombat().getCastSpell() != null ? player.getCombat().getCastSpell() : player.getCombat().getAutoCastSpell();
        if (spell != null) {
            EquipmentInfo.Bonuses b = EquipmentInfo.totalBonuses(player, World.getWorld().equipmentInfo());
            boolean hasTomeOfFire = player.getEquipment().hasAt(EquipSlot.SHIELD, TOME_OF_FIRE);
            Mob target = player.getCombat().getTarget();
            String spell_name = spell.name();
            int level = player.skills().level(Skills.MAGIC);

            //Find the base maximum damage a spell can deal.
            int spell_maxhit = spell.baseMaxHit();

            //• Slayer dart
            if (spell_name.equals("Magic Dart")) {
                spell_maxhit = (int) (10 + Math.floor(level/10D));
            }

            //• Trident of the seas
            if (spell_name.equals("Trident of the seas")) {
                spell_maxhit = 20;
                spell_maxhit = (int) Math.round((Math.max(spell_maxhit, spell_maxhit + (Math.max(0, level - 75)) / 3)) * (1 + (b.magestr / 100.0)));
            }

            //• Trident of the swamp
            if (spell_name.equals("Trident of the swamp")) {
                spell_maxhit = 23;
                spell_maxhit = (int) Math.round((Math.max(spell_maxhit, spell_maxhit + (Math.max(0, level - 75)) / 3)) * (1 + (b.magestr / 100.0)));
            }

            //• God spells (level 60) in combination with Charge (level 80): the base max hit is 30.
            if (spell_name.equals("Saradomin Strike") || spell_name.equals("Claws of Guthix") || spell_name.equals("Flames of Zamorak")) {
                if (player.getTimers().has(TimerKey.CHARGE_SPELL)) {
                    spell_maxhit = 30;
                }
            }

            if (spell_name.toLowerCase().contains("fire") && hasTomeOfFire) {
                spell_maxhit *= 1.50;
            }

            double multiplier = 1 + ((b.magestr > 0 ? b.magestr : 1.0) / 100);

            if (FormulaUtils.hasThammaronSceptre(player) && target != null && target.isNpc() && includeNpcMax) {
                multiplier += 0.25;
            }

            if (player.getEquipment().hasAt(EquipSlot.WEAPON, THAMMARONS_STAFF_C) && target != null && target.isNpc() && includeNpcMax) {
                multiplier += 0.25;
            }

            if (player.getEquipment().hasAt(EquipSlot.HEAD, TURQUOISE_SLAYER_HELMET_I) ) {
                multiplier += 0.10;//OK ALL OF 3 SLAYER HELM FIXED

                //i plus for pkpp normal slayer i it works already right? um im not 100%
                //i saw it worked already with pkp bonus/

            }

            if (FormulaUtils.wearingTwistedSlayerHelmetI(player) && target != null && target.isNpc() && includeNpcMax) {
                multiplier += 0.10;
            }

            /**
             * When wearing the clock of invisibility with an elder wand you get a 10% damage boost vs npcs
             */
            boolean elderWand = player.getEquipment().hasAt(EquipSlot.WEAPON, ELDER_WAND);
            boolean cloakOfInvisibility = player.getEquipment().hasAt(EquipSlot.CAPE, CLOAK_OF_INVISIBILITY);
            boolean wearingBoth = elderWand && cloakOfInvisibility;
            if (target != null && target.isNpc() && wearingBoth) {
                multiplier += 0.10;
            }

            // #Custom slayer effects
            var weakSpot = player.getSlayerRewards().getUnlocks().containsKey(SlayerConstants.WEAK_SPOT);
            if (weakSpot && target != null && target.isNpc()) {
                if (Slayer.creatureMatches(player, target.getAsNpc().id())) {
                    multiplier += 0.10;
                }
            }

            int weapon = player.getEquipment().get(3) == null ? -1 : player.getEquipment().get(3).getId();
            if (spell_name.equals("Volatile spell")) {
                int baseLevel = level;
                if (baseLevel > 99)
                    baseLevel = 99;
                double levelTimes = 0.67;
                multiplier -= 0.15;
                spell_maxhit = (int) (baseLevel * levelTimes);
            }

            // #Custom armour multipliers
            if (player.getEquipment().hasAt(EquipSlot.HEAD, DARK_SAGE_HAT) && target != null && target.isNpc() && includeNpcMax) {
                multiplier += 0.05;//5% damage boost
            }

            if (player.getEquipment().hasAt(EquipSlot.BODY, DARK_SAGE_ROBE_TOP) && target != null && target.isNpc() && includeNpcMax) {
                multiplier += 0.10;//10% damage boost
            }

            if (player.getEquipment().hasAt(EquipSlot.LEGS, DARK_SAGE_ROBE_BOTTOM) && target != null && target.isNpc() && includeNpcMax) {
                multiplier += 0.10;//10.0% damage boost
            }

            int maxHit = (int) Math.round(spell_maxhit * multiplier);

            // #Custom Armour effects
            if (player.getEquipment().hasAt(EquipSlot.AMULET, OCCULT_NECKLACE_OR) || player.getEquipment().hasAt(EquipSlot.HANDS, TORMENTED_BRACELET_OR)) {
                maxHit += 1;
            }

            if (player.getEquipment().hasAt(EquipSlot.RING, MARVOLO_GAUNTS_RING) && target != null && target.isNpc()) {
                maxHit += 5;
            }

            //# Pet effects
            if (player.hasPetOut("Olmlet") && target != null && target.isNpc() && includeNpcMax) {
                var increaseBy = 5;
                if (target.isNpc() && target.getAsNpc().id() == NpcIdentifiers.COMBAT_DUMMY) {
                    maxHit += increaseBy;
                }
                if (player.getRaids() != null && player.getRaids().raiding(player))
                    maxHit += increaseBy;
            }

            if (player.hasPetOut("Skeleton hellhound pet")) {
                var increaseBy = target != null && target.isNpc() && includeNpcMax ? 2 : 1;
                maxHit += increaseBy;
            }

            if (player.hasPetOut("Baby lava dragon pet")) {
                var increaseBy = target != null && target.isNpc() && includeNpcMax ? 5 : 1;
                maxHit += increaseBy;
            }

            if (player.hasPetOut("Mini necromancer pet")) {
                var increaseBy = target != null && target.isNpc() && includeNpcMax ? 3 : 1;
                maxHit += increaseBy;
            }

            if (player.hasPetOut("Nagini pet")) {
                var increaseBy = target != null && target.isNpc() && includeNpcMax ? 10 : 1;
                maxHit += increaseBy;
            }
            if (player.hasPetOut("Youngllef pet")) {
                maxHit += 1;
            }

            if (player.hasPetOut("Corrupted Youngllef pet")) {
                maxHit += 2;
            }

            if (player.hasPetOut("Little Nightmare")) {
                maxHit += 1;
            }
            if (player.hasPetOut("Lava beast pet")) {
                maxHit += 1;
            }
            if (player.hasPetOut("Snowbird") && spell_name.equals("Ice barrage")) {
                maxHit += 2;
            }
            if (player.hasPetOut("Baby Elvarg") && spell_name.equals("Fire surge")) {
                maxHit += 2;
            }
            //After all modifiers spell max hits
            if (spell_name.equals("Petrificus Totalus") && target != null && target.isPlayer()) {
                maxHit = 40;
            }

            if (spell_name.equals("Cruciatus Curse") && target != null && target.isPlayer()) {
                maxHit = 41;
            }

            if (spell_name.equals("Expelliarmus") && target != null && target.isPlayer()) {
                maxHit = 50;
            }

            if (spell_name.equals("Sectumsempra") && target != null && target.isPlayer()) {
                maxHit = 55;
            }

            if (spell_name.equals("Avada Kedavra") && target != null && target.isPlayer()) {
                if(target.isNpc()){
                    maxHit = 82;

                }
            }

            if (spell_name.equals("Sanguinesti spell")) {
                boolean holy_staff = weapon == HOLY_SANGUINESTI_STAFF;
                if (holy_staff) {
                    maxHit += 10;
                }
            }
            return maxHit;
        }
        return baseMaxHit;
    }
}
