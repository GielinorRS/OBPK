package com.ferox.game.world.entity.combat.method.impl.npcs.bosses.vorkath;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;

public class ZombifiedSpawn extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {

    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return 4;
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 1;
    }
}
