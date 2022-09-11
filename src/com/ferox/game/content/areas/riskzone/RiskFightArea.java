package com.ferox.game.content.areas.riskzone;

import com.ferox.game.world.World;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.combat.skull.SkullType;
import com.ferox.game.world.entity.combat.skull.Skulling;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.dialogue.Expression;
import com.ferox.game.world.entity.mob.movement.MovementQueue;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.GameObject;
import com.ferox.game.world.position.Area;
import com.ferox.game.world.position.Tile;
import com.ferox.game.world.route.StepType;
import com.ferox.net.packet.interaction.PacketInteraction;
import com.ferox.util.Color;
import com.ferox.util.Utils;
import com.ferox.util.chainedwork.Chain;
import com.ferox.util.timers.TimerKey;

import static com.ferox.util.ObjectIdentifiers.*;

public class RiskFightArea extends PacketInteraction {

    public static final Area NH_AREA = new Area(3071, 3466, 3080, 3473);
    //public static final Area ONE_V_ONE_1 = new Area(3111, 3505, 3116, 3509);
    //public static final Area ONE_V_ONE_2 = new Area(3103, 3513, 3110, 3517);
    //public static final Area ONE_V_ONE_3 = new Area(3111, 3513, 3116, 3517);

    private void walk(Player player, GameObject obj) {
        player.runFn(1, () -> {
            int x = player.getAbsX();
            int y = player.getAbsY();

            switch (obj.getRotation()) {
                case 1:
                case 3:
                    if(y < obj.tile().y) {
                        System.out.println("we here 3");
                        y += 1;
                    } else {
                        System.out.println("we here 4");
                        y -= 1;
                    }
                default: break;
            }
            player.lock();
            player.stepAbs(x, y, StepType.FORCE_WALK);
            final int finalY = y;
            player.waitForTile(new Tile(x, finalY), player::unlock);
        });
    }

    private void walkX(Player player, boolean xUp) {
        Tile targTile = player.tile().transform(xUp ? +1 : -1, 0, 0);
        player.getMovementQueue().interpolate(targTile, MovementQueue.StepType.FORCED_WALK);
    }

    private void walkY(Player player, boolean yUp) {
        Tile targTile = player.tile().transform(0, yUp ? +1 : -1, 0);
        player.getMovementQueue().interpolate(targTile, MovementQueue.StepType.FORCED_WALK);
    }

    @Override
    public boolean handleObjectInteraction(Player player, GameObject obj, int option) {
        if(option == 1) {
            var risk = player.<Long>getAttribOr(AttributeKey.RISKED_WEALTH, 0L);
            if(obj.getId() == BELL_21394) {
                if (player.getTimers().has(TimerKey.RISK_FIGHT_BELL)) {
                    player.message(Color.RED.wrap("You can ring the bell again in " + player.getTimers().asMinutesAndSecondsLeft(TimerKey.RISK_FIGHT_BELL) + "."));
                } else {
                    player.getTimers().register(TimerKey.RISK_FIGHT_BELL, 300);
                    World.getWorld().sendWorldMessage("<img=162>" + Color.BLUE.wrap(player.getUsername()) + Color.RAID_PURPLE.wrap(" has just rung the bell at the risk zone and is looking for a fight!"));
                }
                return true;
            }
            if(obj.getId() == MAGICAL_BARRIER_31808) {
                walk(player, obj);
                return true;
            }

            if(obj.getId() == ENERGY_BARRIER_4470) {
                if (obj.tile().equals(3043, 3789, 0) || obj.tile().equals(3044, 3789, 0)) {
                    if (player.tile().y == 3789) {
                        player.message(Color.RED.wrap("You sense that the barrier has a magical force that won't let you back out."));
                        walkY(player, true);
                    }
                    return true;
                }
            }

            if(obj.getId() == ENERGY_BARRIER_4470) {
                //500k risk area
                if(obj.tile().equals(3106,3512,0) || obj.tile().equals(3107,3512,0)) {
                    player.getRisk().update(); // make sure our wealth is up to date.
                    if (risk <= 500_000) {
                        player.message(Color.RED.wrap("You need to risk at least 500K blood money to enter this risk zone."));
                        return true;
                    }
                    if (player.tile().y == 3512) {
                        if (!player.tile().equals(obj.tile().transform(0, 0, 0))) {
                            player.getMovementQueue().walkTo(obj.tile().transform(0, 0, 0));
                        }
                        Chain.bound(player).name("RiskFightArenaTask").waitForTile(obj.tile().transform(0, 0, 0), () -> {
                            Skulling.assignSkullState(player, SkullType.RED_SKULL);
                            player.forceChat("I am currently risking " + Utils.formatNumber(risk) + " BM!");
                            Tile targTile = player.tile().transform(0, +1, 0);
                            player.getMovementQueue().interpolate(targTile, MovementQueue.StepType.FORCED_WALK);
                            Chain.bound(player).name("RiskFightArenaTask2").waitForTile(targTile, player::unlock);
                        });
                    } else if (player.tile().y == 3513) {
                        Skulling.unskull(player);
                        walkY(player, false);
                    }
                    return true;
                }
                //250k risk area
                if(obj.tile().equals(3110,3508,0) || obj.tile().equals(3110,3507,0)) {
                    player.getRisk().update(); // make sure our wealth is up to date.
                    if (risk <= 250_000) {
                        player.message(Color.RED.wrap("You need to risk at least 250K blood money to enter this risk zone."));
                        return true;
                    }
                    if (player.tile().x == 3110 && (player.tile().y == 3507 || player.tile().y == 3508)) {
                        Skulling.assignSkullState(player, SkullType.RED_SKULL);
                        player.forceChat("I am currently risking "+ Utils.formatNumber(risk)+" BM!");
                        walkX(player,true);
                    } else if(player.tile().x == 3111 && (player.tile().y == 3507 || player.tile().y == 3508)) {
                        Skulling.unskull(player);
                        walkX(player,false);
                    }
                    return true;
                }
                //1M risk area
                if(obj.tile().equals(3112,3512,0) || obj.tile().equals(3113,3512,0)) {
                    player.getRisk().update(); // make sure our wealth is up to date.
                    if (risk <= 1_000_000) {
                        player.message(Color.RED.wrap("You need to risk at least 1M blood money to enter this risk zone."));
                        return true;
                    }
                    if (player.tile().y == 3512 && (player.tile().x == 3112 || player.tile().x == 3113)) {
                        Skulling.assignSkullState(player, SkullType.RED_SKULL);
                        player.forceChat("I am currently risking "+ Utils.formatNumber(risk)+" BM!");
                        walkY(player, true);
                    } else if(player.tile().y == 3513 && (player.tile().x == 3112 || player.tile().x == 3113)) {
                        Skulling.unskull(player);
                        walkY(player, false);
                    }
                    return true;
                }
                //No restrictions here
                if(obj.tile().equals(3076,3473,0) || obj.tile().equals(3075,3473,0) || obj.tile().equals(3117,3510,0)) {
                    if (player.tile().y == 3474) {
                        Skulling.assignSkullState(player, SkullType.RED_SKULL);
                        player.forceChat("I am currently risking "+ Utils.formatNumber(risk)+" BM!");
                        walkY(player, false);
                    } else if(player.tile().y == 3473) {
                        if (player.tile().y <= 3473) {
                            if (!player.tile().equals(obj.tile().transform(0, 0, 0))) {
                                player.getMovementQueue().walkTo(obj.tile().transform(0, 0, 0));
                            }
                            Chain.bound(player).name("RiskFightArenaTask").waitForTile(obj.tile().transform(0, 0, 0), () -> {
                                Skulling.unskull(player);
                                Tile targTile = player.tile().transform(0, 1, 0);
                                player.getMovementQueue().interpolate(targTile, MovementQueue.StepType.FORCED_WALK);
                                Chain.bound(player).name("RiskFightArenaTask2").waitForTile(targTile, player::unlock);
                            });
                        }
                    }
                    return true;
                }
                return true;
            }
        }
        return false;
    }
}
