package eu.mcone.gamesystem.game.manager.team;

import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.ecxeptions.gamesystem.GameSystemException;
import eu.mcone.gamesystem.api.game.Team;
import eu.mcone.gamesystem.api.game.event.GameWinEvent;
import eu.mcone.gamesystem.api.game.player.GamePlayer;
import eu.mcone.gamesystem.game.inventory.TeamInventory;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class TeamManager implements eu.mcone.gamesystem.api.game.manager.team.TeamManager {

    private TeamStage teamStage;

    public TeamManager() {
        try {
            if (GameTemplate.getInstance() != null) {
                teamStage = new TeamStage();

                for (Team team : Team.values()) {
                    GameTemplate.getInstance().getChats().put(team, new ArrayList<>());
                }
            } else {
                throw new GameSystemException("GameTeamplate was not initialized");
            }
        } catch (GameSystemException e) {
            e.printStackTrace();
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
        GameSystemAPI.getInstance().sendConsoleMessage("check if all players have a team");
        for (Player p : GameTemplate.getInstance().getPlaying()) {
            GamePlayer gp = GameTemplate.getInstance().getGamePlayer(p.getUniqueId());
            if (gp.getTeam() == Team.ERROR) {
                int i = 1;
                for (Team team : Team.values()) {
                    if (i <= GameTemplate.getInstance().getNumberOfTeams()) {
                        if (!GameTemplate.getInstance().getTeams().containsKey(p.getUniqueId())) {
                            if (team.getValue() == 0 || team.getValue() < GameTemplate.getInstance().getPlayerPreTeam()) {
                                gp.setTeam(team);
                                GameSystemAPI.getInstance().sendConsoleMessage("set player " + p.getName() + " to team " + team.getString());
                            }
                        }

                        i++;
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
        new TeamInventory(p, teamStage);
    }
}
