package com.ferox.game.world.entity.mob.npc;

import com.ferox.game.world.entity.Mob;

public interface AggressionCheck {

    boolean shouldAgro(Mob mob, Mob victim);

}
