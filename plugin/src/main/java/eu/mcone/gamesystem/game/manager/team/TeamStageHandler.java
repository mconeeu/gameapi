/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.game.manager.team;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.event.NpcInteractEvent;
import eu.mcone.coresystem.api.bukkit.npc.NPC;
import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.Team;
import eu.mcone.gamesystem.api.game.manager.team.TeamStage;
import eu.mcone.gamesystem.api.game.player.GamePlayer;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class TeamStageHandler implements eu.mcone.gamesystem.api.game.manager.team.TeamStageHandler {

    @Getter
    private Map<Team, TeamStage> stages;

    public TeamStageHandler() {
        if (GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_TEAM_STAGE)
                || GameTemplate.getInstance().getOptions().contains(GameTemplate.GameSystemOptions.USE_ALL)) {
            stages = new HashMap<>();
            registerInteract();
        } else {
            GameTemplate.getInstance().sendConsoleMessage("§cEs konnte kein TeamStageHandler erstellt werden, da dieser deaktiviert ist!");
        }
    }

    private void registerInteract() {
        CoreSystem.getInstance().registerEvents(new Listener() {
            @EventHandler
            public void on(NpcInteractEvent e) {
                Player player = e.getPlayer();
                PlayerNpc playerNpc = (PlayerNpc) e.getNpc();

                if (e.getAction().equals(PacketPlayInUseEntity.EnumEntityUseAction.INTERACT)) {
                    for (TeamStage teamStage : stages.values()) {
                        if (teamStage.getNpcs().containsValue(playerNpc)) {
                            for (Map.Entry<GamePlayer, PlayerNpc> entry : teamStage.getNpcs().entrySet()) {
                                if (entry.getValue().getData().getName().equalsIgnoreCase(playerNpc.getData().getName())) {
                                    new StageNpcInteractInventory(player, entry.getKey());
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            }
        });
    }

    public TeamStage createTeamStage(final Team team) {
        if (!stages.containsKey(team)) {
            stages.put(team, new TeamStage(team));
        } else {
            return getTeamStage(team);
        }

        return stages.get(team);
    }

    public TeamStage getTeamStage(final Team team) {
        if (stages.containsKey(team)) {
            return stages.get(team);
        } else {
            return createTeamStage(team);
        }
    }

    public void removePlayerFromStage(final GamePlayer gamePlayer) {
        if (gamePlayer.getTeam() != Team.ERROR) {
            if (stages.containsKey(gamePlayer.getTeam())) {
                stages.get(gamePlayer.getTeam()).removePlayerFromStage(gamePlayer);
            } else {
                GameTemplate.getInstance().sendConsoleMessage("§cTeamStage for team " + gamePlayer.getTeam().getString() + " not exists!");
            }
        } else {
            GameTemplate.getInstance().sendConsoleMessage("§cNot team for Player " + gamePlayer.getName() + " set!");
        }
    }

    public void addPlayerToStage(final GamePlayer gamePlayer) {
        if (gamePlayer.getTeam() == Team.ERROR) {
            if (stages.containsKey(gamePlayer.getTeam())) {
                stages.get(gamePlayer.getTeam()).addPlayerToStage(gamePlayer);
            } else {
                GameTemplate.getInstance().sendConsoleMessage("§cTeamStage for team " + gamePlayer.getTeam().getString() + " not exists!");
            }
        }
    }

    public void deSpawnAllNPCs() {
        for (TeamStage teamStage : stages.values()) {
            for (NPC npc : teamStage.getNpcs().values()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
//                    npc.canBeSeenBy(null);
                }
            }
        }
    }

    public void spawnAllNPCs() {
        for (TeamStage teamStage : stages.values()) {
            for (NPC npc : teamStage.getNpcs().values()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
//                    npc.spawn(player);
                }
            }
        }
    }
}
