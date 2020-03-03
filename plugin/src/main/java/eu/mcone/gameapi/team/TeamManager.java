package eu.mcone.gameapi.team;

import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.event.team.TeamWonEvent;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.team.Team;
import eu.mcone.gameapi.api.team.TeamEnum;
import eu.mcone.gameapi.api.utils.GameConfig;
import eu.mcone.gameapi.inventory.TeamInventory;
import eu.mcone.gameapi.player.GameAPIPlayer;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class TeamManager implements eu.mcone.gameapi.api.team.TeamManager {

    @Getter
    private final int playersPerTeam;

    private HashMap<TeamEnum, Team> teams;
    private final GamePlugin gamePlugin;

    public TeamManager(GamePlugin plugin, GameAPIPlugin system) {
        this.gamePlugin = plugin;
        GameConfig config = plugin.getGameConfig().parseConfig();
        playersPerTeam = config.getPlayersPerTeam();
        teams = new HashMap<>();

        system.sendConsoleMessage("§aLoading TeamManager...");

        int teamSize = 0;
        if (config.getMaxPlayers() != 0 && config.getPlayersPerTeam() != 0) {
            teamSize = config.getMaxPlayers() / config.getPlayersPerTeam();
        }

        if (GamePlugin.getGamePlugin().hasModule(Module.REPLAY_SESSION_MANAGER)) {
            GamePlugin.getGamePlugin().getReplaySession().getInfo().setTeams(teamSize);
        }

        int i = 0;
        for (TeamEnum team : TeamEnum.values()) {
            if (i < teamSize) {
                this.teams.put(team, new Team(team, team.getTeam() + ".spawn", team.getTeam() + ".npc", team.getTeam() + ".respawn"));
                i++;
            }
        }
    }

    public Team getTeam(final TeamEnum teamEnum) {
        return teams.getOrDefault(teamEnum, null);
    }

    public boolean hasPlayerTeam(final Player player) {
        for (Team template : teams.values()) {
            if (template.containsPlayer(player)) {
                return true;
            }
        }

        return false;
    }

    public void setupTeam() {
        for (Player p : gamePlugin.getPlayerManager().getPlaying()) {
            GameAPIPlayer gp = GameAPIPlugin.getSystem().getGamePlayer(p.getUniqueId());
            if (gp.getTeam() == TeamEnum.ERROR) {
                int i = 1;
                for (TeamEnum teamEnum : TeamEnum.values()) {
                    if (i < teams.size()) {
                        Team team = teams.get(teamEnum);
                        if (playersPerTeam >= 2) {
                            if (gamePlugin.getPlayerManager().getPlaying().size() < teams.size()) {
                                if (!team.containsPlayer(p)) {
                                    if (team.getSize() == 0) {
                                        gp.setTeam(team);
                                    }

                                    i++;
                                }
                            } else if (team.getSize() < playersPerTeam) {
                                gp.setTeam(team);
                                i++;
                            }
                        } else {
                            if (!team.containsPlayer(p)) {
                                if (team.getSize() == 0 || team.getSize() < playersPerTeam) {
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

    public boolean checkChanceToWin() {
        int playingSize = gamePlugin.getPlayerManager().getPlaying().size();
        if (playingSize >= playersPerTeam) {
            Team team = null;

            for (Team var : teams.values()) {
                if (var.containsPlayer(gamePlugin.getPlayerManager().getPlaying().get(0))) {
                    team = var;
                    break;
                }
            }

            if (team != null) {
                if (team.getSize() == playingSize) {
                    List<GamePlayer> winners = new ArrayList<>();
                    for (GamePlayer player : gamePlugin.getOnlineGamePlayers()) {
                        if (player.getTeam().equals(team.getTeamEnum())) {
                            winners.add(player);
                        }
                    }

                    gamePlugin.getServer().getPluginManager().callEvent(new TeamWonEvent(team, winners));
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public void sendKillMessage(Player receiver, final Player victim, final Player killer) {
        if (killer == null) {
            gamePlugin.getMessager().sendTransl(receiver, "game.death.normal"
                    .replaceAll("%victim_team%", "§" + gamePlugin.getGamePlayer(victim.getUniqueId()).getTeam().getChatColor().getChar())
                    .replaceAll("%victim%", victim.getName()));
        } else {
            gamePlugin.getMessager().sendTransl(receiver, "game.death.bykiller"
                    .replaceAll("%victim_team%", "§" + gamePlugin.getGamePlayer(victim.getUniqueId()).getTeam().getChatColor().getChar())
                    .replaceAll("%victim%", victim.getName())
                    .replaceAll("%killer_team%", "§" + gamePlugin.getGamePlayer(victim.getUniqueId()).getTeam().getChatColor().getChar())
                    .replaceAll("%killer%", killer.getName()));
        }
    }

    public Collection<Team> getTeams() {
        return teams.values();
    }

    public void createTeamInventory(Player p) {
        new TeamInventory(p, this);
    }
}
