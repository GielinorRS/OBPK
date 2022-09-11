package com.ferox.game.world.entity.combat.method.impl.npcs.bosses;

import com.google.common.collect.Lists;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.route.routes.ProjectileRoute;
import com.ferox.util.TickDelay;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Patrick van Elderen | May, 03, 2021, 16:19
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class CorruptedNechryarch extends CommonCombatMethod {

    private final TickDelay acidAttackCooldown = new TickDelay();
    private final List<Tile> acidPools = Lists.newArrayList();

    @Override
    public void prepareAttack(Mob mob, Mob target) {
       if (!acidAttackCooldown.isDelayed()) {
            acid_attack(mob, target);
        }
        boolean close = target.tile().isWithinDistance(mob.tile(),2);
        if (close && World.getWorld().rollDie(3))
            melee_attack(mob, target);
        else
            magic_attack(mob, target);
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 8;
    }

    private void acid_attack(Mob mob, Mob target) {
        acidAttackCooldown.delay(50);
        Tile lastAcidPos = mob.tile();

        ArrayList<Player> targets = new ArrayList<>();
        World.getWorld().getPlayers().forEach(p -> {
            if (p != null && p.tile().inSqRadius(mob.tile(), 8)) {
                targets.add(p);
            }
        });
        for (int cycle = 0; cycle < 1; cycle++) {
            Player random = Utils.randomElement(targets);
            if(random == null) {
                return;
            }
            // so this start delay needs to increase per target so the attack appears
            // in sequence..
            Chain.bound(null).runFn(2, () -> {
                Tile lockonTile = random.tile();
                var tileDist = mob.tile().transform(3, 3, 0).distance(random.tile());
                var delay = Math.max(1, (20 + (tileDist * 12)) / 30);

                new Projectile(lastAcidPos, lockonTile, -1, 5005, 12 * tileDist, 10, 35, 35, 0, 16, 64).sendProjectile();
                World.getWorld().tileGraphic(5001, lastAcidPos, 0, 0);
                World.getWorld().tileGraphic(5004, lastAcidPos, 0, 0);
                acidPools.add(lastAcidPos);
                for (Player player : targets) {
                    Chain.bound(null).runFn(delay, () -> {
                        if (player.tile().equals(lockonTile)) {
                            int damage = World.getWorld().random(1, 30);
                            player.hit(mob, damage);
                            mob.heal(damage);
                        }
                    }).then(20, acidPools::clear);
                }
                // after fixed delay of 2s
            });
        }
    }

    private void melee_attack(Mob mob, Mob target) {
        mob.animate(4672);
        World.getWorld().getPlayers().forEach(p -> {
            if (p != null && p.tile().isWithinDistance(mob.tile(), 2)) {
                p.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), CombatType.MELEE).checkAccuracy().submit();
            }
        });
    }

    private void magic_attack(Mob mob, Mob target) {
        mob.animate(7550); // there
        Arrays.stream(mob.closePlayers(16, 16)).forEach(p -> {
            if (p != null && ProjectileRoute.allow(mob, p)) {
                var tileDist = mob.tile().transform(3, 3, 0).distance(p.tile());
                var delay = Math.max(1, (20 + (tileDist * 12)) / 30);
                new Projectile(mob, target, 5000, 30, 12 * tileDist, 120, 43, 0, 16, 64).sendProjectile();
                p.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
                if (World.getWorld().rollDie(10)) {
                    Chain.bound(null).runFn(delay + 2, () -> {
                        //after hit effects
                        for (int i = 0; i < 5; i++) {
                            p.hit(mob,3);
                            p.graphic(5002);
                        }
                    });
                }
            }
        });
    }
}
