package com.ferox.game.world.entity.combat.method.impl.npcs.raids;

import com.ferox.game.content.raids.party.Party;
import com.ferox.game.world.World;
import com.ferox.game.world.entity.Mob;
import com.ferox.game.world.entity.combat.CombatFactory;
import com.ferox.game.world.entity.combat.CombatType;
import com.ferox.game.world.entity.combat.method.impl.CommonCombatMethod;
import com.ferox.game.world.entity.masks.Projectile;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.position.Area;
import com.ferox.game.world.position.Boundary;
import com.ferox.game.world.position.Tile;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;
import com.ferox.util.timers.TimerKey;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Patrick van Elderen | May, 12, 2021, 13:06
 * @see <a href="https://github.com/PVE95">Github profile</a>
 */
public class Fluffy extends CommonCombatMethod {

    private void lighting(Player player) {
        mob.animate(4494);
        Party party = player.raidsParty;
        if(party == null) {
            return;
        }
        for (Player member : party.getMembers()) {
            if (member != null && member.isInsideRaids() && member.tile().inArea(new Area(3306, 5315, 3320, 5340, member.raidsParty.getHeight()))) {
                final var tile1 = member.tile().copy();
                final var tile2 = member.tile().copy().transform(1, 0);
                final var tile3 = member.tile().copy().transform(1, 1);

                new Projectile(mob.getCentrePosition(), tile1, 1, 731, 125, 40, 25, 0, 0, 16, 96).sendProjectile();
                new Projectile(mob.getCentrePosition(), tile2, 1, 731, 125, 40, 25, 0, 0, 16, 96).sendProjectile();
                new Projectile(mob.getCentrePosition(), tile3, 1, 731, 125, 40, 25, 0, 0, 16, 96).sendProjectile();
                Chain.bound(null).runFn(5, () -> {
                    World.getWorld().tileGraphic(83, tile1, 0, 0);
                    World.getWorld().tileGraphic(83, tile2, 0, 0);
                    World.getWorld().tileGraphic(83, tile3, 0, 0);
                    if (member.tile().equals(tile1) || member.tile().equals(tile2) || member.tile().equals(tile3)) {
                        member.hit(mob, World.getWorld().random(1, 23), CombatType.MAGIC).submit();
                    }
                });
            }
        }
        mob.getTimers().register(TimerKey.COMBAT_ATTACK, 6);
    }

    private void ventAttack(Player player) {
        mob.forceChat("GRRRRRRRRRRRRRRRRRRRRRRRR");
        mob.animate(4493);
        Party party = player.raidsParty;
        Boundary npcBounds = mob.boundaryBounds();
        Boundary ventBounds = mob.boundaryBounds(6);
        List<Tile> ventTiles = new ArrayList<>();
        for (int x = ventBounds.getMinimumX(); x < ventBounds.getMaximumX(); x++) {
            for (int y = ventBounds.getMinimumY(); y < ventBounds.getMaximumY(); y++) {
                if (Utils.random(3) == 2) {
                    Tile tile = new Tile(x, y, mob.tile().getLevel());
                    if (!npcBounds.inside(tile)) {
                        ventTiles.add(tile);
                    }
                }
            }
        }

        for (Tile ventTile : ventTiles) {
            World.getWorld().tileGraphic(1411, ventTile, 0, 0);
        }

        for (Player member : party.getMembers()) {
            if (member != null && member.isInsideRaids() && member.tile().inArea(new Area(3306, 5315, 3320, 5340, member.raidsParty.getHeight()))) {
                if (ventTiles.stream().anyMatch(ventTile -> ventTile.equals(member.tile()))) {
                    member.hit(mob, World.getWorld().random(1, 30), 5);
                }
            }
        }

        Chain.bound(null).runFn(3, ventTiles::clear);
        mob.getTimers().register(TimerKey.COMBAT_ATTACK, 6);
    }

    private void fallingRocks(Player player) {
        mob.forceChat("SQUASH");
        mob.animate(4493);
        Party party = player.raidsParty;
        Boundary npcBounds = mob.boundaryBounds();
        Boundary rockBounds = mob.boundaryBounds(11);
        List<Tile> rockTiles = new ArrayList<>();
        for (int x = rockBounds.getMinimumX(); x < rockBounds.getMaximumX(); x++) {
            for (int y = rockBounds.getMinimumY(); y < rockBounds.getMaximumY(); y++) {
                if (Utils.random(3) == 2) {
                    Tile tile = new Tile(x, y, mob.tile().getLevel());
                    if (!npcBounds.inside(tile)) {
                        rockTiles.add(tile);
                    }
                }
            }
        }

        for (Tile rockTile : rockTiles) {
            new Projectile(mob.getCentrePosition(), rockTile, 1, 1406, 150, 20, 30, 0, 25).sendFor(target.getAsPlayer());
        }

        for (Player member : party.getMembers()) {
            if (member != null && member.isInsideRaids() && member.tile().inArea(new Area(3306, 5315, 3320, 5340, member.raidsParty.getHeight()))) {
                if (rockTiles.stream().anyMatch(rockTile -> rockTile.equals(member.tile()))) {
                    member.hit(mob, World.getWorld().random(1, 30), 5);
                }
            }
        }

        Chain.bound(null).runFn(3, rockTiles::clear);
        mob.getTimers().register(TimerKey.COMBAT_ATTACK, 6);
    }

    private void rangeAttack(Player player) {
        mob.face(null); // Stop facing the target
        mob.animate(4492);
        Party party = player.raidsParty;
        for (Player member : party.getMembers()) {
            if (member != null && member.isInsideRaids() && member.tile().inArea(new Area(3306, 5315, 3320, 5340, member.raidsParty.getHeight()))) {
                var tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());
                var delay = Math.max(1, (20 + (tileDist * 12)) / 30);

                new Projectile(mob, member, 182, 25, 12 * tileDist, 65, 31, 0, 15, 220).sendProjectile();
                member.hit(mob, CombatFactory.calcDamageFromType(mob, member, CombatType.RANGED), delay, CombatType.RANGED).checkAccuracy().submit();
                member.delayedGraphics(183, 50, delay);
            }
        }

        mob.face(target.tile()); // Go back to facing the target.
    }

    private void magicAttack(Player player) {
        mob.face(null); // Stop facing the target
        mob.animate(4492);
        Party party = player.raidsParty;
        for (Player member : party.getMembers()) {
            if (member != null && member.isInsideRaids() && member.tile().inArea(new Area(3306, 5315, 3320, 5340, member.raidsParty.getHeight()))) {
                var tileDist = mob.tile().transform(3, 3, 0).distance(target.tile());
                var delay = Math.max(1, (20 + (tileDist * 12)) / 30);
                new Projectile(mob, member, 1403, 25, 12 * tileDist, 65, 31, 0, 15, 220).sendProjectile();
                member.hit(mob, CombatFactory.calcDamageFromType(mob, member, CombatType.MAGIC), delay, CombatType.MAGIC).checkAccuracy().submit();
            }
        }
        mob.face(target.tile()); // Go back to facing the target.
    }

    private void meleeAttack() {
        target.hit(mob, CombatFactory.calcDamageFromType(mob, target, CombatType.MELEE), 0, CombatType.MELEE).checkAccuracy().submit();
        mob.animate(mob.attackAnimation());
    }

    private boolean specialAttack = false;

    @Override
    public void prepareAttack(Mob mob, Mob target) {
        if (!mob.isNpc() || !target.isPlayer())
            return;

        Player player = (Player) target;

        if (World.getWorld().rollDie(20, 1)) { //5% chance the npc sends lightning
            lighting(player);
            specialAttack = true;
        }

        if (World.getWorld().rollDie(20, 1)) { //5% chance the npc sends vents
            ventAttack(player);
            specialAttack = true;
        }

        if (World.getWorld().rollDie(20, 1)) { //5% chance the npc sends rocks
            fallingRocks(player);
            specialAttack = true;
        }

        // Determine if we're going to melee or mage
        if (CombatFactory.canReach(mob, CombatFactory.MELEE_COMBAT, target)) {
            int chance = World.getWorld().random(6);
            if (chance == 1) {
                rangeAttack(player);
            } else if (chance == 2) {
                magicAttack(player);
            } else {
                meleeAttack();
            }
        } else {
            int chance = World.getWorld().random(3);
            if (chance == 1) {
                rangeAttack(player);
            } else {
                magicAttack(player);
            }
        }
    }

    @Override
    public int getAttackSpeed(Mob mob) {
        return specialAttack ? 10 : mob.getBaseAttackSpeed();
    }

    @Override
    public int getAttackDistance(Mob mob) {
        return 6;
    }
}
