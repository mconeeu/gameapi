package eu.mcone.gameapi.api;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.config.CoreJsonConfig;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.gameapi.api.achievement.AchievementManager;
import eu.mcone.gameapi.api.backpack.BackpackManager;
import eu.mcone.gameapi.api.gamestate.GameStateManager;
import eu.mcone.gameapi.api.kit.KitManager;
import eu.mcone.gameapi.api.map.MapManager;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.player.PlayerManager;
import eu.mcone.gameapi.api.replay.exception.GameModuleNotActiveException;
import eu.mcone.gameapi.api.replay.session.ReplaySession;
import eu.mcone.gameapi.api.replay.session.ReplaySessionManager;
import eu.mcone.gameapi.api.team.TeamManager;
import eu.mcone.gameapi.api.utils.GameConfig;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public abstract class GamePlugin extends CorePlugin {

    @Getter
    private static GamePlugin gamePlugin;

    @Getter
    private List<Modules> modules;
    private final Option[] options;

    private MapManager mapManager;
    private BackpackManager backpackManager;
    private AchievementManager achievementManager;
    private KitManager kitManager;
    private ReplaySessionManager sessionManager;
    private GameStateManager gameStateManager;
    private TeamManager teamManager;
    private PlayerManager playerManager;
    private ReplaySession replaySession;

    @Getter
    private CoreJsonConfig<GameConfig> gameConfig;

    protected GamePlugin(String pluginName, ChatColor pluginColor, String prefixTranslation, Option... options) {
        super(pluginName, pluginColor, prefixTranslation);
        gamePlugin = this;

        this.modules = new ArrayList<>();
        this.options = options;
    }

    protected GamePlugin(Gamemode pluginGamemode, String prefixTranslation, Option... options) {
        super(pluginGamemode, prefixTranslation);
        gamePlugin = this;

        this.options = options;
    }

    @Override
    public void onEnable() {
        this.gameConfig = new CoreJsonConfig<>(this, GameConfig.class, "gameConfig.json");
        onGameEnable();
    }

    @Override
    public void onDisable() {
        if (modules.contains(Modules.REPLAY_SESSION_MANAGER)
                && modules.contains(Modules.TEAM_MANAGER)
                && modules.contains(Modules.PLAYER_MANAGER)) {
            //Stop date
            getReplaySession().getInfo().setStopped(System.currentTimeMillis() / 1000);
//            getSessionManager().getChannelHandler().createUnregisterRequest();
        }

        onGameDisable();
    }

    public abstract void onGameEnable();

    public abstract void onGameDisable();

    public MapManager getMapManager() {
        return mapManager != null ? mapManager : (mapManager = GameAPI.getInstance().constructMapManager());
    }

    public BackpackManager getBackpackManager() {
        return backpackManager != null ? backpackManager : (backpackManager = GameAPI.getInstance().constructBackpackManager(this, options));
    }

    public KitManager getKitManager() {
        return kitManager != null ? kitManager : (kitManager = GameAPI.getInstance().constructKitManager(this, options));
    }

    public AchievementManager getAchievementManager() {
        return achievementManager != null ? achievementManager : (achievementManager = GameAPI.getInstance().constructAchievementManager(this, options));
    }

    public ReplaySessionManager getSessionManager() {
        modules.add(Modules.REPLAY_SESSION_MANAGER);
        return sessionManager != null ? sessionManager : (sessionManager = GameAPI.getInstance().constructReplaySessionManager(this, options));
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager != null ? gameStateManager : (gameStateManager = GameAPI.getInstance().constructGameStatsManager(this));
    }

    public ReplaySession getReplaySession() {
        try {
            if (modules.contains(Modules.REPLAY_SESSION_MANAGER)
                    && modules.contains(Modules.TEAM_MANAGER)
                    && modules.contains(Modules.PLAYER_MANAGER)) {
                modules.add(Modules.REPLAY);
                return replaySession != null ? replaySession : (replaySession = GameAPI.getInstance().createReplaySession(getSessionManager()));
            } else {
                throw new GameModuleNotActiveException("The game module ReplaysessionManager isn`t active!");
            }
        } catch (GameModuleNotActiveException e) {
            e.printStackTrace();
        }

        return null;
    }

    public TeamManager getTeamManager() {
        return teamManager != null ? teamManager : (teamManager = GameAPI.getInstance().constructTeamManager(this));
    }

    public PlayerManager getPlayerManager() {
        return playerManager != null ? playerManager : (playerManager = GameAPI.getInstance().constructPlayerManager(this));
    }

    public GamePlayer getGamePlayer(final UUID uuid) {
        return GameAPI.getInstance().getGamePlayer(uuid);
    }

    public GamePlayer getGamePlayer(final Player player) {
        return getGamePlayer(player.getUniqueId());
    }

    public Collection<GamePlayer> getOnlineGamePlayers() {
        return GameAPI.getInstance().getOnlineGamePlayers();
    }
}
