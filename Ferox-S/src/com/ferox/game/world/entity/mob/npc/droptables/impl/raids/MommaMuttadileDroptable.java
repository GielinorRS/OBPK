package com.ferox.game.world.entity.mob.npc.droptables.impl.raids;

import com.ferox.game.content.raids.chamber_of_xeric.ChamberOfXerics;
import com.ferox.game.world.entity.mob.npc.Npc;
import com.ferox.game.world.entity.mob.npc.droptables.Droptable;
import com.ferox.game.world.entity.mob.player.Player;
import com.ferox.game.world.object.ObjectManager;

/**
 * @author Patrick van Elderen <https://github.com/PVE95>
 * @Since October 30, 2021
 */
public class MommaMuttadileDroptable implements Droptable {

    @Override
    public void reward(Npc npc, Player killer) {
        var party = killer.raidsParty;

        if (party != null) {
            var currentKills = party.getKills();
            party.setKills(currentKills + 1);
            party.teamMessage("<col=ef20ff>"+npc.def().name+" has been defeated!");

            //Progress to the next stage
            if (party.getKills() == 2) {
                party.setRaidStage(5);
                party.teamMessage("<col=ef20ff>You may now progress to the next room!");
                party.setKills(0);//Reset kills back to 0

                //Delete the meat tree
                ObjectManager.removeObj(party.getMeatTree());
            }
        }
    }
}
