package com.ferox.game.world.entity.combat.method.impl.npcs.raids;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.combat.skull.SkullType;
import com.ferox.game.world.entity.combat.skull.Skulling;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.util.timers.TimerKey;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 11, 2020
 */
public class Tekton extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        //10% chance that the wold boss skulls you!
        if(World.getWorld().rollDie(10,1)) {
            Skulling.assignSkullState(((Player) target), SkullType.WHITE_SKULL);
            target.message("The "+mob.getMobName()+" has skulled you, be careful!");
        }

        mob.face(null); // Stop facing the target
        World.getWorld().getPlayers().forEach(p -> {
            if(p != null && target.tile().inSqRadius(p.tile(),12)) {
                p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.MELEE), 1, CombatType.MELEE).checkAccuracy().submit();
            }
        });

        mob.face(target.tile()); // Go back to facing the target.

        mob.getTimers().register(TimerKey.COMBAT_ATTACK, 5);
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 7;
    }
}
