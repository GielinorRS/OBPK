package com.ferox.game.world.entity.combat.method.impl.npcs.raids.cox;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.combat.skull.SkullType;
import com.ferox.game.world.entity.combat.skull.Skulling;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Tuple;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;
import com.ferox.util.timers.TimerKey;

import static com.ferox.util.NpcIdentifiers.TEKTON_7542;
import static com.ferox.util.NpcIdentifiers.TEKTON_ENRAGED_7544;

/**
 * @author Patrick van Elderen <patrick.vanelderen@live.nl>
 * april 11, 2020
 */
public class Tekton extends CommonCombatMethod {

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        if(mob.isNpc()) {
            Npc npc = mob.getAsNpc();

            //World boss
            if (npc.id() == TEKTON_7542) {
                npc.animate(npc.attackAnimation());
                //10% chance that the wold boss skulls you!
                if (World.getWorld().rollDie(10, 1)) {
                    Skulling.assignSkullState(((Player) target), SkullType.WHITE_SKULL);
                    target.message("The " + mob.getMobName() + " has skulled you, be careful!");
                }

                mob.face(null); // Stop facing the target
                World.getWorld().getPlayers().forEach(p -> {
                    if (p != null && target.tile().inSqRadius(p.tile(), 12)) {
                        p.hit(mob, CombatFactory.calcDamageFromType(mob, p, CombatType.MELEE), 1, CombatType.MELEE).checkAccuracy().submit();
                    }
                });

                mob.face(target.tile()); // Go back to facing the target.
                mob.getTimers().register(TimerKey.COMBAT_ATTACK, 5);
            } else if(npc.id() == TEKTON_ENRAGED_7544) {
                npc.getMovement().setBlockMovement(true); // Lock movement when we found a target
                doMeleePhaseInner(npc, target);
            }
        }
    }

    private static boolean instanceFinished(Mob mob) {
        if (mob instanceof Npc) {
            Npc npc = (Npc) mob;
            if (npc.dead() || !npc.isRegistered()) {
                return true;
            }
        }
        return false;
    }

    private static void doMeleePhaseInner(Npc npc, Mob target) {
        npc.setEntityInteraction(null);
        npc.animate(7493);
        Tile p1 = target.tile().copy();
        npc.face(p1);
        Chain.bound(null).cancelWhen(() -> instanceFinished(npc)).runFn(1, () -> {
            npc.face(p1);
        }).then(4, () -> {
            if (p1.area(1).contains(target)) {
                target.hit(npc, Utils.random(41), 0, CombatType.MELEE).checkAccuracy().submit();
            }
        }).then(2, () -> {
            npc.setEntityInteraction(target);
        }).then(2, () -> {
            npc.setEntityInteraction(null);
        });
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return mob.getAsNpc().id() == TEKTON_ENRAGED_7544 ? 1 : 7;
    }
}
