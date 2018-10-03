package eu.mcone.gamesystem.game.manager.team;

import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.gamesystem.GameSystem;
import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.ecxeptions.gamesystem.GameSystemException;
import eu.mcone.gamesystem.api.game.Team;
import eu.mcone.gamesystem.api.game.event.GameWinEvent;
import eu.mcone.gamesystem.api.game.player.GamePlayer;
import eu.mcone.gamesystem.game.inventory.TeamInventory;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TeamManager implements eu.mcone.gamesystem.api.game.manager.team.TeamManager {

    private Logger log;

    @Getter
    private TeamStage teamStage;

    public TeamManager() {
        log = GameSystemAPI.getInstance().getLogger();

        try {
            if (GameTemplate.getInstance() != null) {
                teamStage = new TeamStage();

                for (Team team : Team.values()) {
                    GameTemplate.getInstance().getChats().put(team, new ArrayList<>());
                }
            } else {
                throw new GameSystemException("GameTemplate was not initialized");
            }
        } catch (GameSystemException e) {
            e.printStackTrace();
            log.log(Level.SEVERE, "Exception in TeamManager", e);
        }
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
            GamePlayer gp = GameTemplate.getInstance().getGamePlayer(p.getUniqueId());
            if (gp.getTeam() == Team.ERROR) {
                int i = 1;
                for (Team team : Team.values()) {
                    if (i < GameTemplate.getInstance().getNumberOfTeams()) {
                        if (GameTemplate.getInstance().getPlayerPreTeam() >= 2) {
                            if (GameTemplate.getInstance().getPlaying().size() < GameTemplate.getInstance().getNumberOfTeams()) {
                                if (!GameTemplate.getInstance().getTeams().containsKey(p.getUniqueId())) {
                                    if (team.getValue() == 0) {
                                        gp.setTeam(team);
                                    }
                                    i++;
                                }
                            } else if (team.getValue() < GameTemplate.getInstance().getPlayerPreTeam()) {
                                gp.setTeam(team);
                                i++;
                            }
                        } else {
                            if (!GameTemplate.getInstance().getTeams().containsKey(p.getUniqueId())) {
                                if (team.getValue() == 0 || team.getValue() < GameTemplate.getInstance().getPlayerPreTeam()) {
                                    gp.setTeam(team);
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

    public void chekWinntection() {
        int i = 0;
        for (Team winner_team : Team.values()) {
            if (i == Team.values().length - 1) {
                GameSystemAPI.getInstance().getServer().getPluginManager().callEvent(new GameWinEvent(winner_team));
            } else {
                i = 0;
                for (Team lose_teams : Team.values()) {
                    if (winner_team.getValue() < 0
                            && lose_teams.getValue() == 0) {
                        i++;
                    }
                }
            }
        }
    }

    public void sendKillMessage(Player receiver, final Player victim, final Player killer) {
        if (killer == null) {
            GameTemplate.getInstance().getMessager().sendTransl(receiver, "game.death.normal"
                    .replace("%victim_team%", GameTemplate.getInstance().getGamePlayer(victim.getUniqueId()).getTeam().getColor())
                    .replace("%victim%", victim.getName()));
        } else {
            GameTemplate.getInstance().getMessager().sendTransl(receiver, "game.death.bykiller"
                    .replace("%victim_team%", GameTemplate.getInstance().getGamePlayer(victim.getUniqueId()).getTeam().getColor())
                    .replace("%victim%", victim.getName())
                    .replace("%killer_team%", GameTemplate.getInstance().getGamePlayer(killer.getUniqueId()).getTeam().getColor())
                    .replace("%killer%", killer.getName()));

        }
    }

    public void createTeamInventory(Player p) {
        if (GameTemplate.getInstance().getNumberOfTeams() == 2
                || GameTemplate.getInstance().getNumberOfTeams() == 4
                || GameTemplate.getInstance().getNumberOfTeams() == 8) {
            log.info("Open TeamInventory for player `" + p.getName() + "`");
            new TeamInventory(p, teamStage, InventorySlot.ROW_3);
        } else if (GameTemplate.getInstance().getNumberOfTeams() == 16) {
            log.info("Open TeamInventory for player `" + p.getName() + "`");
            new TeamInventory(p, teamStage, InventorySlot.ROW_4);
        }
    }
}
