package com.ferox.game.world.entity.combat.method.impl.npcs.misc;

import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.mob.npc.AggressionCheck;

public class DemonAgro implements AggressionCheck {
    @Override
    public boolean shouldAgro(Mob mob, Mob victim) {
        return true;
    }
}
