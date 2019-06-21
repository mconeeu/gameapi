/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.game.manager.team;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.ecxeptions.GameSystemException;
import eu.mcone.gamesystem.api.game.Team;
import eu.mcone.gamesystem.api.game.event.GameWinEvent;
import eu.mcone.gamesystem.api.game.player.IGamePlayer;
import eu.mcone.gamesystem.game.inventory.inventories.TeamInventory;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TeamManager implements eu.mcone.gamesystem.api.game.manager.team.TeamManager {

    private Logger log;

    @Getter
    private TeamStageHandler teamStageHandler;

    public TeamManager() {
        log = GameSystemAPI.getInstance().getLogger();

        try {
            if (GameTemplate.getInstance() != null) {
                if (GameTemplate.getInstance().getOptions().contains(GameTemplate.Options.USE_TEAM_MANAGER)) {
                    if (GameTemplate.getInstance().getOptions().contains(GameTemplate.Options.USE_TEAM_STAGE)) {
                        teamStageHandler = new TeamStageHandler();
                    } else {
                        GameTemplate.getInstance().sendConsoleMessage("§cTeamStageHandler deaktiviert!");
                    }

                    for (Team team : Team.values()) {
                        GameTemplate.getInstance().getChats().put(team, new ArrayList<>());
                    }
                } else {
                    throw new GameSystemException("The option 'USE_TEAM_MANAGER' was not activated");
                }
            } else {
                throw new GameSystemException("GameTemplate was not initialized");
            }
        } catch (GameSystemException e) {
            e.printStackTrace();
            log.log(Level.SEVERE, "Exception in TeamManager", e);
        }
    }

    public ItemStack getTeamSelectionItem() {
        return new ItemBuilder(Material.BED, 1, 0).displayName("§c§lTeams §8» §7§oHier kannst du dein Team auswählen.").create();
    }

    public Team getTeam(String name) {
        for (Team team : Team.values()) {
            if (team.getString().equalsIgnoreCase(name)) {
                return team;
            }
        }
        return null;
    }

    public void setupTeam() {
        log.info("Check if all players have a team");
        for (Player p : GameTemplate.getInstance().getPlaying()) {
            IGamePlayer gp = GameTemplate.getInstance().getGamePlayer(p.getUniqueId());
            if (gp.getTeam() == Team.ERROR) {
                int i = 1;
                for (Team team : Team.values()) {
                    if (i < GameTemplate.getInstance().getGameSettingsConfig().getTeams()) {
                        if (GameTemplate.getInstance().getGameSettingsConfig().getPlayersPreTeam() >= 2) {
                            if (GameTemplate.getInstance().getPlaying().size() < GameTemplate.getInstance().getGameSettingsConfig().getTeams()) {
                                if (!GameTemplate.getInstance().getTeams().containsKey(p.getUniqueId())) {
                                    if (team.getValue() == 0) {
                                        gp.setTeam(team);
                                        team.setBedAlive(true);
                                    }
                                    i++;
                                }
                            } else if (team.getValue() < GameTemplate.getInstance().getGameSettingsConfig().getPlayersPreTeam()) {
                                gp.setTeam(team);
                                team.setBedAlive(true);
                                i++;
                            }
                        } else {
                            if (!GameTemplate.getInstance().getTeams().containsKey(p.getUniqueId())) {
                                if (team.getValue() == 0 || team.getValue() < GameTemplate.getInstance().getGameSettingsConfig().getPlayersPreTeam()) {
                                    gp.setTeam(team);
                                    team.setBedAlive(true);
                                }
                            }
                            i++;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    }

    public boolean checkChanceToWin() {
        int playingSize = GameTemplate.getInstance().getPlaying().size();
        if (playingSize >= GameTemplate.getInstance().getGameSettingsConfig().getPlayersPreTeam()) {
            Team team = GameTemplate.getInstance().getGamePlayer(GameTemplate.getInstance().getPlaying().get(0)).getTeam();
            if (team.getValue() == playingSize) {
                List<IGamePlayer> winners = new ArrayList<>();
                GameTemplate.getInstance().getGamePlayers().forEach((key, value) -> {
                    if (value.getTeam().getString().equalsIgnoreCase(team.getString())) {
                        winners.add(value);
                    }
                });

                GameSystemAPI.getInstance().getServer().getPluginManager().callEvent(new GameWinEvent(team, winners));
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void sendKillMessage(Player receiver, final Player victim, final Player killer) {
        if (killer == null) {
            GameTemplate.getInstance().getMessager().sendTransl(receiver, "game.death.normal"
                    .replaceAll("%victim_team%", "§" + GameTemplate.getInstance().getGamePlayer(victim.getUniqueId()).getTeam().getChatColor().getChar())
                    .replaceAll("%victim%", victim.getName()));
        } else {
            GameTemplate.getInstance().getMessager().sendTransl(receiver, "game.death.bykiller"
                    .replaceAll("%victim_team%", "§" + GameTemplate.getInstance().getGamePlayer(victim.getUniqueId()).getTeam().getChatColor().getChar())
                    .replaceAll("%victim%", victim.getName())
                    .replaceAll("%killer_team%", "§" + GameTemplate.getInstance().getGamePlayer(victim.getUniqueId()).getTeam().getChatColor().getChar())
                    .replaceAll("%killer%", killer.getName()));
        }
    }

    public void createTeamInventory(Player p) {
        new TeamInventory().createInventory(p);
    }
}