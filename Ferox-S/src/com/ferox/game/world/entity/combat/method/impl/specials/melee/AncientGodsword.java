package com.ferox.game.world.entity.combat.method.impl.specials.melee;

import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatSpecial;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.hit.Hit;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.util.ItemIdentifiers;

import static com.ferox.util.CustomItemIdentifiers.BEGINNER_AGS;
import static com.ferox.util.CustomItemIdentifiers.HWEEN_ARMADYL_GODSWORD;

public class AncientGodsword extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        final Player player = (Player) mob;
        int animation = 9171;
        player.animate(animation);
        player.graphic(1996);
        //TODO mob.sound(2537);
        //TODO mob.sound(2537); // yes same sound twice on 07

        int h1 = CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE);
        int h2 = CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE);

        if(h1 > 0) {
            Hit hit = target.hit(mob, h1,1, CombatType.MELEE).checkAccuracy();
            hit.submit();
            Hit hit2 = target.hit(mob, h2,target.isNpc() ? 1 : 1, CombatType.MELEE).checkAccuracy();
            hit2.submit();
        } else {
            //Blocked
            Hit hit = target.hit(mob, 0,1, CombatType.MELEE).setAccurate(false);
            hit.submit();
            Hit hit2 = target.hit(mob, 0,target.isNpc() ? 1 : 1, CombatType.MELEE).setAccurate(false);
            hit2.submit();
        }
            CombatSpecial.drain(mob, CombatSpecial.ANCIENT_GODSWORD.getDrainAmount());
    }

   /*
    public void prepareAttack(Mob mob, Mob target) {
        final Player player = (Player) mob;
        int animation = 9171;
        player.animate(animation);
        player.graphic(1996);
        //TODO it.player().world().spawnSound(it.player().tile(), 3869, 0, 10)

        Hit hit = target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE),1, CombatType.MELEE).checkAccuracy();
        hit.submit();
        CombatSpecial.drain(mob, CombatSpecial.ANCIENT_GODSWORD.getDrainAmount());
    }*/

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 1;
    }
}
