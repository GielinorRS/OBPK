package com.ferox.game.content.title.req.impl.other;

import com.ferox.game.content.title.req.TitleRequirement;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.Skill;

/**
 * Created by Kaleem on 25/03/2018.
 */
public class MasteryNonCombat extends TitleRequirement {

    public MasteryNonCombat() {
        super("Reach level 99 in 1 <br>non combat skill");
    }

    @Override
    public boolean satisfies(Player player) {
        return Skill.ALL.stream().filter(skill -> !skill.isCombatSkill()).map(player.skills()::getMaxLevel).anyMatch(skill -> skill >= 99);
    }

}
