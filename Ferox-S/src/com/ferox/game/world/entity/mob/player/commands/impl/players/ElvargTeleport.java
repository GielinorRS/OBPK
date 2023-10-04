package com.ferox.game.world.entity.mob.player.commands.impl.players;

import com.ferox.game.content.teleport.TeleportType;
import com.ferox.game.content.teleport.Teleports;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.commands.Command;
import com.ferox.game.world.position.Tile;

public class ElvargTeleport implements Command {
    @Override
    public void execute(Player player, String command, String[] parts) {
        Tile tile = new Tile(3338, 3902);

        if (!Teleports.canTeleport(player,true, TeleportType.GENERIC) || !Teleports.pkTeleportOk(player, tile)) {
            return;
        }

        player.getDialogueManager().start(new Dialogue() {
            @Override
            protected void start(Object... parameters) {
                send(DialogueType.STATEMENT, "This teleport will send you to a dangerous area.", "Do you wish to continue?");
                setPhase(1);
            }

            @Override
            protected void next() {
                if (isPhase(1)) {
                    send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Yes.", "No.");
                    setPhase(2);
                }
            }

            @Override
            protected void select(int option) {
                if (option == 1) {
                    if (!Teleports.canTeleport(player, true, TeleportType.GENERIC)) {
                        stop();
                        return;
                    }
                    Teleports.basicTeleport(player, tile);
                    player.message("You have been teleported to Elvarg's Hideout.");
                } else if (option == 2) {
                    stop();
                }
            }
        });
    }

    @Override
    public boolean canUse(Player player) {
        return true;
    }
}
