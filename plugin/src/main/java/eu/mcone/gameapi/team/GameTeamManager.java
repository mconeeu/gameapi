package eu.mcone.gameapi.team;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.event.team.TeamWonEvent;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.player.GamePlayerState;
import eu.mcone.gameapi.api.team.DefaultTeam;
import eu.mcone.gameapi.api.team.Team;
import eu.mcone.gameapi.api.team.TeamChatListener;
import eu.mcone.gameapi.api.team.TeamManager;
import eu.mcone.gameapi.api.utils.GameConfig;
import eu.mcone.gameapi.inventory.TeamInventory;
import eu.mcone.gameapi.listener.team.TeamListener;
import eu.mcone.gameapi.player.GameAPIPlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GameTeamManager implements TeamManager {

    @Getter
    private final int playersPerTeam, teamCount;

    private final Set<GameTeam> teams;
    private final GamePlugin gamePlugin;

    @Getter @Setter
    private TeamChatListener teamChatListener;

    @Getter
    private final boolean disableRespawn, disableWinMethod;
    @Getter
    private boolean teamsFinallySet;

    public GameTeamManager(GamePlugin plugin, GameAPIPlugin system, Option[] options) {
        this.gamePlugin = plugin;

        //Chat System
        teamChatListener = new DefaultTeamChat();

        CoreSystem.getInstance().setPlayerChatEnabled(false);
        GamePlugin.getGamePlugin().registerEvents(
                new eu.mcone.gameapi.listener.team.TeamChatListener(this),
                new TeamListener(this)
        );

        GameConfig config = plugin.getGameConfig().parseConfig();
        playersPerTeam = config.getPlayersPerTeam();
        teams = new HashSet<>();

        List<Option> optionList = Arrays.asList(options);

        disableRespawn = optionList.contains(Option.TEAM_MANAGER_DISABLE_RESPAWN);
        disableWinMethod = optionList.contains(Option.TEAM_MANAGER_DISABLE_WIN_METHOD);

        if (config.getMaxPlayers() != 0 && config.getPlayersPerTeam() != 0) {
            this.teamCount = config.getMaxPlayers() / config.getPlayersPerTeam();
        } else {
            this.teamCount = 0;
        }

        system.sendConsoleMessage("§aLoading TeamManager...");
    }

    @Override
    public void loadDefaultTeams() {
        if (teamCount > DefaultTeam.values().length) {
            throw new IllegalStateException("Could not register Default Teams! More Teams nedded in gameConfig than default teams are available!");
        }

        int i = 0;
        for (DefaultTeam team : DefaultTeam.values()) {
            if (i < teamCount) {
                GameTeam gameTeam = (GameTeam) team.getTeam();
                gameTeam.setSize(playersPerTeam);
                gameTeam.setSpawnLocation("team."+team.name().toLowerCase()+".spawn");
                gameTeam.setNpcLocation("team."+team.name().toLowerCase()+".npc");
                gameTeam.setRespawnBlockLocation("team."+team.name().toLowerCase()+".respawnblock");

                this.teams.add((GameTeam) team.getTeam());
                i++;
            } else {
                return;
            }
        }
    }

    @Override
    public Team registerNewTeam(String name, String label, int priority, ChatColor color, ItemStack item) {
        GameTeam team = new GameTeam(name, label, priority, color, item);
        team.setSize(playersPerTeam);
        teams.add(team);

        GameAPI.getInstance().sendConsoleMessage("§2Registering Team "+name);
        return team;
    }

    @Override
    public Team createTeam(String name, String label, int priority, ChatColor color, ItemStack item) {
        return new GameTeam(name, label, priority, color, item);
    }

    @Override
    public Team getTeam(final String name) {
        for (GameTeam team : teams) {
            if (team.getName().equals(name)) {
                return team;
            }
        }

        return null;
    }

    public void setPlayerTeam(GameAPIPlayer gp, Team team) {
        if (gp.getTeam() != null) {
            ((GameTeam) gp.getTeam()).removePlayer(gp);
        }

        System.out.println("set team "+team.getName()+" for player "+gp.bukkit().getName());
        ((GameTeam) team).addPlayer(gp);
        gp.setTeam(team);
    }

    public void removeFromGame(GameAPIPlayer gp) {
        if (gp.getTeam() != null) {
            GameTeam team = (GameTeam) gp.getTeam();
            team.removePlayer(gp);

            if (team.getPlayers().size() <= 0) {
                team.setAlive(false);
            }

            System.out.println("remove from team, all teams: "+teams);
        } else {
            System.err.println("gp.geTeam = null");
        }

        if (!disableWinMethod) {
            Team team = GamePlugin.getGamePlugin().getTeamManager().getWinnerTeamIfLastSurvived();
            System.out.println("calculated winner: "+team+" current teams "+teams);

            if (team != null) {
                stopGameWithWinner(team, false);
            }
        }
    }

    @Override
    public void stopGameWithWinner(Team team) {
        stopGameWithWinner(team, true);
    }

    private void stopGameWithWinner(Team team, boolean manually) {
        GameAPI.getInstance().sendConsoleMessage("§aTeam §f"+team.getName()+"§a has won! "+ (manually ? "Game has been stopped manually." : "Players from all other teams are dead."));
        Bukkit.getPluginManager().callEvent(new TeamWonEvent(team));
    }

    @Override
    public void setTeamsForRemainingPlayersBalanced() {
        if (!teamsFinallySet) {
            Collection<GamePlayer> players = GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)
                    ? GamePlugin.getGamePlugin().getPlayerManager().getGamePlayers(GamePlayerState.PLAYING)
                    : GameAPI.getInstance().getOnlineGamePlayers();
            players.removeIf(player -> player.getTeam() != null);

            for (GamePlayer p : players) {
                Map<GameTeam, Integer> teamPlayers = new HashMap<>();
                for (GameTeam team : teams) {
                    if (team.getPlayers().size() < team.getSize()) {
                        teamPlayers.put(team, team.getPlayers().size());
                    }
                }

                if (!teamPlayers.isEmpty()) {
                    if (teamPlayers.size() > 1) {
                        p.changeTeamTo(Collections.min(teamPlayers.entrySet(), Map.Entry.comparingByValue()).getKey());
                    } else {
                        p.changeTeamTo(teamPlayers.keySet().iterator().next());
                    }
                } else {
                    throw new IllegalStateException("Could not automatically set team for player " + p.getCorePlayer().getName() + "! No teams with free slots available!");
                }
            }

            teamsFinallySet = true;
        } else throw new IllegalStateException("Could not set Teams for remaining players. Teams already finally set before!");
    }

    @Override
    public void setTeamsForRemainigPlayersByPriority() {
        if (!teamsFinallySet) {
            Collection<GamePlayer> players = GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)
                    ? GamePlugin.getGamePlugin().getPlayerManager().getGamePlayers(GamePlayerState.PLAYING)
                    : GameAPI.getInstance().getOnlineGamePlayers();
            players.removeIf(player -> player.getTeam() != null);

            for (GamePlayer p : players) {
                Map<GameTeam, Integer> teamPlayers = new HashMap<>();
                for (GameTeam team : teams) {
                    if (team.getPlayers().size() < team.getSize()) {
                        teamPlayers.put(team, team.getPriority());
                    }
                }

                if (!teamPlayers.isEmpty()) {
                    if (teamPlayers.size() > 1) {
                        p.changeTeamTo(Collections.max(teamPlayers.entrySet(), Map.Entry.comparingByValue()).getKey());
                    } else {
                        p.changeTeamTo(teamPlayers.keySet().iterator().next());
                    }
                } else {
                    throw new IllegalStateException("Could not automatically set team for player "+p.getCorePlayer().getName()+"! No teams with free slots available!");
                }
            }

            teamsFinallySet = true;
        } else throw new IllegalStateException("Could not set Teams for remaining players. Teams already finally set before!");
    }

    @Override
    public GameTeam getWinnerTeamIfLastSurvived() {
        GameTeam winner = null;

        for (GameTeam team : teams) {
            if (team.isAlive()) {
                if (winner == null) {
                    winner = team;
                } else {
                    return null;
                }
            }
        }

        if (winner != null) {
            return winner;
        } else throw new IllegalStateException("Could not get last survived Team! Not team is alive anymore!");
    }

    @Override
    public GameTeam calculateWinnerByGoals() {
        Map<GameTeam, Integer> teams = new HashMap<>();

        for (GameTeam team : this.teams) {
            int goals = 0;

            for (GamePlayer player : team.getPlayers()) {
                goals += player.getRoundGoals();
            }

            teams.put(team, goals);
        }

        return Collections.max(teams.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    @Override
    public GameTeam calculateWinnerByKills() {
        Map<GameTeam, Integer> teams = new HashMap<>();

        for (GameTeam team : this.teams) {
            int goals = 0;

            for (GamePlayer player : team.getPlayers()) {
                goals += player.getRoundKills();
            }

            teams.put(team, goals);
        }

        return Collections.max(teams.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    @Override
    public void sendKillMessage(Player receiver, Player victim, Player killer) {
        CorePlayer receiverCP = CoreSystem.getInstance().getCorePlayer(receiver);
        String message = "";

        if (receiver != victim) {
            if (killer == null) {
                message = CoreSystem.getInstance().getTranslationManager().get("game.death.normal", receiverCP, "§" + gamePlugin.getGamePlayer(victim.getUniqueId()).getTeam().getColor() + victim.getName());
            } else {
                if (receiver != killer) {
                    message = CoreSystem.getInstance().getTranslationManager().get("game.death.msg", receiverCP,
                            gamePlugin.getGamePlayer(victim.getUniqueId()).getTeam().getColor() + victim.getName(),
                            gamePlugin.getGamePlayer(killer.getUniqueId()).getTeam().getColor() + killer.getName());
                } else {
                    GamePlugin.getGamePlugin().getMessenger().send(killer, "§7Du hast " + GamePlugin.getGamePlugin().getGamePlayer(victim).getTeam().getColor() + victim.getName() + " §7getötet.");
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
                        message = CoreSystem.getInstance().getTranslationManager().get("game.death.msg.killed", receiverCP, gamePlugin.getGamePlayer(killer.getUniqueId()).getTeam().getColor() + killer.getName());
                    }
                } else {
                    message = "§7Du bist gestorben und somit aus dem Spiel ausgeschieden!";
                }
            }

            if (!message.isEmpty())
                gamePlugin.getMessenger().send(victim, message);
        }
    }

    @Override
    public List<Team> getTeams() {
        return new ArrayList<>(teams);
    }

    @Override
    public Collection<Team> getAliveTeams() {
        List<Team> teams = new ArrayList<>();

        for (GameTeam team : this.teams) {
            if (team.isAlive()) {
                teams.add(team);
            }
        }

        return teams;
    }

    @Override
    public void openTeamInventory(Player p) {
        new TeamInventory(p, this);
    }

}
