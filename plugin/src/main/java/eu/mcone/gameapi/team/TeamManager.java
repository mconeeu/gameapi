package eu.mcone.gameapi.team;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreScoreboard;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.team.Team;
import eu.mcone.gameapi.api.team.TeamChat;
import eu.mcone.gameapi.api.team.TeamDefinition;
import eu.mcone.gameapi.api.utils.GameConfig;
import eu.mcone.gameapi.inventory.TeamInventory;
import eu.mcone.gameapi.player.GameAPIPlayer;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamManager implements eu.mcone.gameapi.api.team.TeamManager {

    @Getter
    private final int playersPerTeam;

    private final HashMap<String, Team> teams;
    private final GamePlugin gamePlugin;

    @Getter
    private TeamChat teamChat;
    @Getter
    private CoreScoreboard teamTablist;
    @Getter
    private Team wonTeam;
    @Getter
    private final List<Option> options;

    @Getter
    private final boolean exitBySingleDeath;
    @Getter
    private final boolean useCustomTeams;
    @Getter
    private final boolean useDefaultTeams;

    public TeamManager(GamePlugin plugin, GameAPIPlugin system, Option[] options) {
        this.gamePlugin = plugin;

        //Chat System
        CoreSystem.getInstance().setPlayerChatEnabled(false);
        GamePlugin.getGamePlugin().registerEvents(new GeneralTeamChatManager(this));
        teamChat = new DefaultTeamChat();
        teamTablist = new GeneralTeamTablist();

        GameConfig config = plugin.getGameConfig().parseConfig();
        playersPerTeam = config.getPlayersPerTeam();
        teams = new HashMap<>();
        this.options = Arrays.asList(options);

        exitBySingleDeath = this.options.contains(Option.TEAM_MANAGER_EXIT_BY_SINGLE_DEATH);
        useCustomTeams = this.options.contains(Option.USE_CUSTOM_TEAMS);
        useDefaultTeams = this.options.contains(Option.USE_DEFAULT_TEAMS);

        system.sendConsoleMessage("§aLoading TeamManager...");

        int teamSize = 0;
        if (config.getMaxPlayers() != 0 && config.getPlayersPerTeam() != 0) {
            teamSize = config.getMaxPlayers() / config.getPlayersPerTeam();
        }

        if (GamePlugin.getGamePlugin().hasModule(Module.REPLAY_SESSION_MANAGER)) {
            GamePlugin.getGamePlugin().getReplaySession().getInfo().setTeams(teamSize);
        }

        if (useDefaultTeams) {
            int i = 1;
            for (TeamDefinition team : TeamDefinition.values()) {
                if (i <= teamSize) {
                    this.teams.put(team.getName(), new Team(team));
                    i++;
                }
            }
        }
    }

    public void addCustomTeam(Team team) {
        options.add(Option.USE_CUSTOM_TEAMS);

        if (!teams.containsKey(team.getName())) {
            teams.put(team.getName(), team);
        }
    }

    public void addTeamTablist(CoreScoreboard scoreboard) {
        this.teamTablist = scoreboard;
    }

    public void addTeamChat(TeamChat teamChat) {
        this.teamChat = teamChat;
    }

    public Team getTeam(final String team) {
        return teams.getOrDefault(team, null);
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
            if (gp.getTeam() == null || gp.getTeam().getName().equalsIgnoreCase(TeamDefinition.ERROR.getName())) {
                int i = 1;
                for (TeamDefinition teamEnum : TeamDefinition.values()) {
                    if (i <= teams.size()) {
                        Team team = teams.get(teamEnum.getName());
                        if (playersPerTeam >= 2) {
                            if (gamePlugin.getPlayerManager().getPlaying().size() < teams.size()) {
                                if (!team.containsPlayer(p)) {
                                    if (team.getSize() == 0) {
                                        gp.setTeam(team);
                                    }
                                    break;
                                }
                            } else if (team.getSize() < playersPerTeam) {
                                gp.setTeam(team);
                                break;
                            }
                        } else {
                            if (!team.containsPlayer(p)) {
                                if (team.getSize() == 0 || team.getSize() < playersPerTeam) {
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

    public Team checkChanceToWin() {
        Team team = null;
        List<Team> lastTeams = new ArrayList<>();
        for (Team lastTeam : teams.values()) {
            if (lastTeam.getPlayers().size() > 0) {
                lastTeams.add(lastTeam);
            }
        }

        if (lastTeams.size() == 1) {
            team = lastTeams.get(0);
        }

        if (team != null) {
            wonTeam = team;
            return wonTeam;
        } else {
            return null;
        }
    }

    public Team getPrematureWinner() {
        Map<Team, Integer> potentialWinners = new HashMap<>();
        Team winner = null;
        if (this.options.contains(Option.SORT_WHERE_GOALS)) {
            for (Team team : teams.values()) {
                for (Player player : team.getPlayers()) {
                    GamePlayer gamePlayer = GamePlugin.getGamePlugin().getGamePlayer(player);
                    if (potentialWinners.containsKey(team)) {
                        potentialWinners.merge(team, gamePlayer.getRoundGoals(), Integer::sum);
                    } else {
                        potentialWinners.put(team, gamePlayer.getRoundGoals());
                    }
                }
            }
        } else {
            for (Team team : teams.values()) {
                for (Player player : team.getPlayers()) {
                    GamePlayer gamePlayer = GamePlugin.getGamePlugin().getGamePlayer(player);
                    if (potentialWinners.containsKey(team)) {
                        potentialWinners.merge(team, gamePlayer.getRoundKills(), Integer::sum);
                    } else {
                        potentialWinners.put(team, gamePlayer.getRoundKills());
                    }
                }
            }
        }

        int max = 0;
        for (Map.Entry<Team, Integer> entry : potentialWinners.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                winner = entry.getKey();
            }
        }

        return winner;
    }

    public void sendKillMessage(Player receiver, final Player victim, final Player killer) {
        CorePlayer receiverCP = CoreSystem.getInstance().getCorePlayer(receiver);
        String message = "";

        if (receiver != victim) {
            if (killer == null) {
                message = CoreSystem.getInstance().getTranslationManager().get("game.death.normal", receiverCP, "§" + gamePlugin.getGamePlayer(victim.getUniqueId()).getTeam().getChatColor().getChar() + victim.getName());
            } else {
                if (receiver != killer) {
                    message = CoreSystem.getInstance().getTranslationManager().get("game.death.msg", receiverCP,
                            "§" + gamePlugin.getGamePlayer(victim.getUniqueId()).getTeam().getChatColor().getChar() + victim.getName(),
                            "§" + gamePlugin.getGamePlayer(killer.getUniqueId()).getTeam().getChatColor().getChar() + killer.getName());
                } else {
                    GamePlugin.getGamePlugin().getMessenger().send(killer, "§7Du hast " + GamePlugin.getGamePlugin().getGamePlayer(victim).getTeam().getChatColor().toString() + victim.getName() + " §7getötet.");
                }
            }

            if (!message.isEmpty())
                gamePlugin.getMessenger().send(receiver, message);
        } else {
            GamePlayer gamePlayer = GamePlugin.getGamePlugin().getGamePlayer(victim);

            if (gamePlayer.getTeam() != null) {
                if (gamePlayer.getTeam().isAlive()) {
                    if (killer == null) {
                        message = "§7Du bist gestorben";
                    } else {
                        message = CoreSystem.getInstance().getTranslationManager().get("game.death.msg.killed", receiverCP, gamePlugin.getGamePlayer(killer.getUniqueId()).getTeam().getChatColor().toString() + killer.getName());
                    }
                } else {
                    message = "§7Du bist gestorben und somit aus dem Spiel ausgeschieden!";
                }
            }

            if (!message.isEmpty())
                gamePlugin.getMessenger().send(victim, message);
        }
    }

    public Collection<Team> getTeams() {
        return teams.values();
    }

    public Team getTeam(int index) {
        if (teams.size() > index) {
            return new ArrayList<>(teams.values()).get(index);
        } else {
            return null;
        }
    }

    public Collection<Team> getAliveTeams() {
        List<Team> teams = new ArrayList<>();

        for (Team team : this.teams.values()) {
            if (team.isAlive()) {
                teams.add(team);
            }
        }

        return teams;
    }

    public void openTeamInventory(Player p) {
        new TeamInventory(p, this);
    }
}
