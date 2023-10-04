package com.ferox.game.content.title.req.impl.other;

import com.ferox.game.content.title.req.TitleRequirement;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.mob.player.Player;

/**
 * Created by Kaleem on 25/03/2018.
 */
public class MysteryBoxRequirement extends TitleRequirement {

    private final int amount;

    public MysteryBoxRequirement(int amount) {
        super("Open " + amount + " Mystery <br>boxes");
        this.amount = amount;
    }

    @Override
    public boolean satisfies(Player player) {
        int mysteryBoxesOpened = player.getAttribOr(AttributeKey.REGULAR_MYSTERY_BOXES_OPENED, 0);
        return mysteryBoxesOpened >= amount;
    }

}
