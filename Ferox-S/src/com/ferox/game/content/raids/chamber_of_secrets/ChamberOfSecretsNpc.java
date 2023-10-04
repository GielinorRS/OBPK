package com.ferox.game.content.raids.chamber_of_secrets;

import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.Tile;

/**
 * @author Patrick van Elderen | May, 10, 2021, 16:16
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class ChamberOfSecretsNpc extends Npc {

    public static final double BONUS_HP_PER_PLAYER = 0.75; // %increased hp for each players beyond the first

    public ChamberOfSecretsNpc(int id, Tile tile, int partySize) {
        super(id, tile);
        this.respawns(false);
        this.combatInfo().aggroradius = 15;
        this.walkRadius(15);
        this.setHitpoints((int) (this.hp() * (1 + (BONUS_HP_PER_PLAYER * (partySize - 1)))));
    }
}
