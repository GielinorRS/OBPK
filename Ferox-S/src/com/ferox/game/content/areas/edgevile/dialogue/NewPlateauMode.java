package com.ferox.game.content.areas.edgevile.dialogue;

import com.ferox.game.content.mechanics.break_items.BreakItemsOnDeath;
import com.ferox.game.world.entity.AttributeKey;
import com.ferox.game.world.entity.dialogue.Dialogue;
import com.ferox.game.world.entity.dialogue.DialogueType;
import com.ferox.game.world.entity.dialogue.Expression;
import com.ferox.game.world.entity.mob.player.GameMode;
import com.ferox.game.world.entity.mob.player.IronMode;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.entity.mob.player.rights.PlayerRights;
import com.ferox.util.Color;
import com.ferox.util.Utils;

/**
 * Auth the plateau
 */

public class NewPlateauMode extends Dialogue {

    @Override
    protected void start(Object... parameters) {
        send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Change My Mode.", "Nevermind.");
        setPhase(0);
    }//check updates bro okay!

    @Override
    public void next() {
        if (isPhase(2)) {
            send(DialogueType.OPTION, DEFAULT_OPTION_TITLE, "Yes Change it.", "No thanks.");
            setPhase(3);
        } else if (isPhase(4)) {
            stop();
        }
    }

    @Override
    public void select(int option) {
        if(isPhase(0)) {
            if (option == 1) {
                if (player.ironMode() == IronMode.NONE && player.mode() != GameMode.INSTANT_PKER) {
                    player.getPacketSender().sendMessage("You need to be an iron man or instant pker to to use this ");
                    player.getPacketSender().closeDialogue();
                    return;

                }else if  (player.mode() == GameMode.INSTANT_PKER) {
                    GameMode accountType = player.mode(GameMode.TRAINED_ACCOUNT);
                    player.mode(accountType);
                    player.resetSkills();
                    player.ironMode(IronMode.NONE);
                    player.getPacketSender().closeDialogue();
                    player.getPacketSender().sendMessage("Please relog to take the effect ");
                } else {
                    player.setPlayerRights(PlayerRights.PLAYER);
                    player.ironMode(IronMode.NONE);
                    player.mode(GameMode.TRAINED_ACCOUNT);
                    player.getPacketSender().closeDialogue();
                    player.getPacketSender().sendMessage("You're not ironman anymore and can't change it again ");
                    player.getPacketSender().sendMessage("Please relog to take the effect ");

                }


            } else if (option == 2) {
                stop();
            }

        }
    }

}
