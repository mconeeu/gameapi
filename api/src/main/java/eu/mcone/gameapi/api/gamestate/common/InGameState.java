package eu.mcone.gameapi.api.gamestate.common;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreObjective;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreScoreboard;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.event.game.GameDrawEvent;
import eu.mcone.gameapi.api.event.gamestate.GameStateStartEvent;
import eu.mcone.gameapi.api.event.gamestate.GameStateStopEvent;
import eu.mcone.gameapi.api.gamestate.GameState;
import eu.mcone.gameapi.api.utils.GameConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

public class InGameState extends GameState {

    private final GameConfig config;
    @Getter
    private double timeoutSeconds = 0;

    public InGameState() {
        super("InGame");
        config = GamePlugin.getGamePlugin().getGameConfig().parseConfig();
    }

    public InGameState(int timeout) {
        super("InGame", 0, timeout);
        config = GamePlugin.getGamePlugin().getGameConfig().parseConfig();
    }

    public InGameState(int countdown, int timeout) {
        super("InGame", countdown, timeout);
        config = GamePlugin.getGamePlugin().getGameConfig().parseConfig();
    }

    @Override
    public void onTimeoutSecond(CorePlugin plugin, long second) {
        if (GamePlugin.getGamePlugin().getGameStateManager().getOptions().contains(Option.USE_SEASON_TIMEOUT)) {
            timeoutSeconds = getTimeout() - second;
            double min = timeoutSeconds / 60;

            if (min >= config.getSeason()) {
                if (GamePlugin.getGamePlugin().hasModule(Module.TEAM_MANAGER)) {
                    Bukkit.getPluginManager().callEvent(new GameDrawEvent(GamePlugin.getGamePlugin().getTeamManager().getPrematureWinner()));
                }
            }

            //Update scoreboard automatically
            if (GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)) {
                for (Player player : GamePlugin.getGamePlugin().getPlayerManager().getPlaying()) {
                    CorePlayer corePlayer = CoreSystem.getInstance().getCorePlayer(player);
                    corePlayer.getScoreboard().getObjective(DisplaySlot.SIDEBAR).reload();
                }
            }
        }
    }

    @Override
    public void onCountdownSecond(CorePlugin plugin, int second) {
    }

    @Override
    public void onStart(GameStateStartEvent event) {
        if (GamePlugin.getGamePlugin().hasModule(Module.TEAM_MANAGER) && GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)) {
            for (Player playing : GamePlugin.getGamePlugin().getPlayerManager().getPlaying()) {
                CorePlayer player = CoreSystem.getInstance().getCorePlayer(playing);
                CoreScoreboard coreScoreboard = null;
                try {
                    coreScoreboard = GamePlugin.getGamePlugin().getTeamManager().getTeamTablist().newInstance();
                    CoreObjective objective = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
                    player.setScoreboard(coreScoreboard);

                    if (objective != null) {
                        player.getScoreboard().setNewObjective(objective);
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        if (GamePlugin.getGamePlugin().hasModule(Module.REPLAY)) {
            GamePlugin.getGamePlugin().getReplaySession().recordSession();
        }
    }

    @Override
    public void onStop(GameStateStopEvent event) {
        if (GamePlugin.getGamePlugin().hasModule(Module.REPLAY)) {
            GamePlugin.getGamePlugin().getReplaySession().saveSession();
        }
    }
}
