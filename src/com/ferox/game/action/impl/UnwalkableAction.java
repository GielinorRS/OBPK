package com.ferox.game.action.impl;

import com.ferox.game.action.Action;
import com.ferox.game.action.policy.WalkablePolicy;
import com.ferox.game.world.entity.Mob;

/**
 * @author PVE
 * @Since september 04, 2020
 */
public abstract class UnwalkableAction extends Action<Mob> {
    public UnwalkableAction(Mob mob, int delay, boolean instant) {
        super(mob, delay, instant);
    }

    public UnwalkableAction(Mob mob, int delay) {
        super(mob, delay);
    }

    @Override
    public boolean prioritized() {
        return super.prioritized();
    }

    @Override
    public WalkablePolicy getWalkablePolicy() {
        return WalkablePolicy.NON_WALKABLE;
    }

    @Override
    public String getName() {
        return "";
    }

}
