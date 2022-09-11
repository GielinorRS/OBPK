package com.ferox.game.world.entity.mob.npc.droptables.impl.raids;

import com.ferox.fs.NpcDefinition;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.method.impl.npcs.raids.cox.Muttadile;
import com.ferox.game.world.entity.mob.movement.MovementQueue;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.npc.droptables.Droptable;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.Tile;
import com.ferox.util.chainedwork.Chain;

import java.lang.ref.WeakReference;

import static com.ferox.game.world.entity.AttributeKey.MUTTADILE_HEAL_COUNT;
import static com.ferox.util.NpcIdentifiers.KALPHITE_QUEEN_6501;
import static com.ferox.util.NpcIdentifiers.MUTTADILE_7563;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 30, 2021
 */
public class BabyMuttadileDroptable implements Droptable {

    @Override
    public void reward(Npc npc, Player killer) {
        var party = killer.raidsParty;

        if (party != null) {
            var currentKills = party.getKills();
            party.setKills(currentKills + 1);
            party.teamMessage("<col=ef20ff>"+npc.def().name+" has been defeated!");

            Npc mommaMuttadile = party.getMommaMuttadile();
            mommaMuttadile.putAttrib(MUTTADILE_HEAL_COUNT,0);
            mommaMuttadile.getCombat().reset();
            mommaMuttadile.respawns(false);
            mommaMuttadile.lockNoDamage();
            var targ = mommaMuttadile.<WeakReference<Mob>>getAttribOr(AttributeKey.TARGET, new WeakReference<Mob>(null)).get();
            Chain.bound(null).runFn(1, () -> {
                mommaMuttadile.transmog(MUTTADILE_7563);
                mommaMuttadile.combatInfo(World.getWorld().combatInfo(MUTTADILE_7563));
                mommaMuttadile.def(World.getWorld().definitions().get(NpcDefinition.class, MUTTADILE_7563));
                mommaMuttadile.setCombatMethod(new Muttadile());
                mommaMuttadile.animate(7423);
                mommaMuttadile.getMovement().reset();
                mommaMuttadile.getMovement().interpolate(new Tile(3311, 5329, mommaMuttadile.tile().level));
            }).then(3, () -> {
                mommaMuttadile.heal(mommaMuttadile.maxHp());
                mommaMuttadile.unlock();
                if (targ != null) {
                    mommaMuttadile.face(targ.tile());
                    mommaMuttadile.getCombat().attack(targ);
                    mommaMuttadile.cloneDamage(mommaMuttadile);
                }
            });
        }
    }
}
