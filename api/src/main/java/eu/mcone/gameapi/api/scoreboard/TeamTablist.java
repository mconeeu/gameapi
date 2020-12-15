/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.scoreboard;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreScoreboard;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreScoreboardEntry;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.team.Team;

public class TeamTablist extends CoreScoreboard {

    @Override
    public void modifyTeam(CorePlayer owner, CorePlayer player, CoreScoreboardEntry t) {
        GamePlayer gp = GamePlugin.getGamePlugin().getGamePlayer(player.getUuid());

        if (gp.getTeam() != null) {
            Team team = gp.getTeam();
            t.priority(team.getPriority()).prefix(team.getColor().toString());
        } else {
            t.prefix("§4x §8").priority(9999);
        }
    }

}
