package com.ferox.game.world.entity.combat.method.impl;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.combat.weapon.WeaponType;
import com.ferox.game.world.entity.mob.player.EquipSlot;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.util.Utils;

import static com.ferox.util.CustomItemIdentifiers.HOLY_SCYTHE_OF_VITUR;
import static com.ferox.util.CustomItemIdentifiers.SANGUINE_SCYTHE_OF_VITUR;
import static com.ferox.util.ItemIdentifiers.SCYTHE_OF_VITUR;

/**
 * Represents the combat method for melee attacks.
 *
 * @author Professor Oak
 */
public class MeleeCombatMethod extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        if (target.isNpc() && mob.isPlayer()) {
            Player player = (Player) mob;
            if (player.getEquipment().hasAt(EquipSlot.WEAPON, SCYTHE_OF_VITUR) || player.getEquipment().hasAt(EquipSlot.WEAPON, HOLY_SCYTHE_OF_VITUR) || player.getEquipment().hasAt(EquipSlot.WEAPON, SANGUINE_SCYTHE_OF_VITUR)) {
                mob.animate(mob.attackAnimation());
                mob.graphic(1231, 100, 0);
                if(target.getAsNpc().getSize() > 2 || target.getAsNpc().isCombatDummy()) {
                    target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
                    target.hit(mob, (int) (CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE) * .66), CombatType.MELEE).checkAccuracy().submit();
                    target.hit(mob, (int) (CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE) * .33), 1, CombatType.MELEE).checkAccuracy().submit();
                } else {
                    target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
                }
                return;
            }
        }
        if(mob.isPlayer()) {
            Player player = (Player) mob;
            boolean fenrirGreybackJrPet = player.hasPetOut("Fenrir greyback Jr pet");
            if (fenrirGreybackJrPet) {
                if (Utils.percentageChance(15)) {
                    target.hit(mob, World.getWorld().random(1, 15), CombatType.MELEE).checkAccuracy().submit();
                }
            }
        }

        final Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 1, CombatType.MELEE).checkAccuracy();
        hit.submit();

        mob.animate(mob.attackAnimation());
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        if (mob.getCombat().getWeaponType() == WeaponType.HALBERD) {
            return 2;
        }
        return 1;
    }

}
