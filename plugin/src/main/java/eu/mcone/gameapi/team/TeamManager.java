package eu.mcone.gameapi.team;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.event.team.TeamWonEvent;
import eu.mcone.gameapi.api.team.Team;
import eu.mcone.gameapi.api.team.TeamDefinition;
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

    private HashMap<TeamDefinition, Team> teams;
    private final GamePlugin gamePlugin;

    @Getter
    private Team wonTeam;

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
        for (TeamDefinition team : TeamDefinition.values()) {
            if (i < teamSize) {
                this.teams.put(team, new Team(team));
                i++;
            }
        }
    }

    public Team getTeam(final TeamDefinition teamEnum) {
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
            System.out.println("TEAM: " + gp.getTeam());
            if (gp.getTeam() == null || gp.getTeam().getTeam() == TeamDefinition.ERROR) {
                System.out.println("NO TEAM");
                int i = 1;
                for (TeamDefinition teamEnum : TeamDefinition.values()) {
                    if (i <= teams.size()) {
                        Team team = teams.get(teamEnum);
                        if (playersPerTeam >= 2) {
                            if (gamePlugin.getPlayerManager().getPlaying().size() < teams.size()) {
                                if (!team.containsPlayer(p)) {
                                    if (team.getSize() == 0) {
                                        System.out.println("Set player " + p.getName() + " in team " + team.getTeam());
                                        gp.setTeam(team);
                                    }
                                    break;
                                }
                            } else if (team.getSize() < playersPerTeam) {
                                System.out.println("Set player " + p.getName() + " in team " + team.getTeam());
                                gp.setTeam(team);
                                break;
                            }
                        } else {
                            if (!team.containsPlayer(p)) {
                                if (team.getSize() == 0 || team.getSize() < playersPerTeam) {
                                    System.out.println("Set player " + p.getName() + " in team " + team.getTeam());
                                    gp.setTeam(team);
                                    break;
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
        Team team = null;

//        if (playingSize == playersPerTeam) {
//            for (Team lastTeam : teams.values()) {
//                if (lastTeam.getPlayers().size() == playersPerTeam) {
//                    team = lastTeam;
//                    break;
//                }
//            }
//        } else {
        List<Team> lastTeams = new ArrayList<>();
        for (Team lastTeam : teams.values()) {
            if (lastTeam.getPlayers().size() > 0) {
                System.out.println("TEAM: " + lastTeam.getTeam());
                System.out.println("TEAM: " + lastTeam.getPlayers());
                lastTeams.add(lastTeam);
            }
        }

        if (lastTeams.size() == 1) {
            team = lastTeams.get(0);
        }
//        }

        if (team != null) {
            wonTeam = team;
            gamePlugin.getServer().getPluginManager().callEvent(new TeamWonEvent(team, team.getPlayers()));
            return true;
        } else {
            return false;
        }
    }

    public void sendKillMessage(Player receiver, final Player victim, final Player killer) {
        CorePlayer receiverCP = CoreSystem.getInstance().getCorePlayer(receiver);
        String message;

        if (killer == null) {
            message = CoreSystem.getInstance().getTranslationManager().get("game.death.normal", receiverCP)
                    .replaceAll("%victim_team%", "§" + gamePlugin.getGamePlayer(victim.getUniqueId()).getTeam().getTeam().getChatColor().getChar())
                    .replaceAll("%victim%", victim.getName());
        } else {
            message = CoreSystem.getInstance().getTranslationManager().get("game.death.bykiller", receiverCP)
                    .replaceAll("%victim_team%", "§" + gamePlugin.getGamePlayer(victim.getUniqueId()).getTeam().getTeam().getChatColor().getChar())
                    .replaceAll("%victim%", victim.getName())
                    .replaceAll("%killer_team%", "§" + gamePlugin.getGamePlayer(killer.getUniqueId()).getTeam().getTeam().getChatColor().getChar())
                    .replaceAll("%killer%", killer.getName());
        }

        gamePlugin.getMessager().send(receiver, message);
    }

    public Collection<Team> getTeams() {
        return teams.values();
    }

    public void createTeamInventory(Player p) {
        new TeamInventory(p, this);
    }
}
