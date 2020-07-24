package eu.mcone.gameapi.api;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.config.CoreJsonConfig;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.gameapi.api.achievement.AchievementManager;
import eu.mcone.gameapi.api.backpack.BackpackManager;
import eu.mcone.gameapi.api.damage.DamageLogger;
import eu.mcone.gameapi.api.gamestate.GameStateManager;
import eu.mcone.gameapi.api.kit.KitManager;
import eu.mcone.gameapi.api.map.MapManager;
import eu.mcone.gameapi.api.onepass.OnePassManager;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.player.PlayerManager;
import eu.mcone.gameapi.api.replay.exception.GameModuleNotActiveException;
import eu.mcone.gameapi.api.replay.session.ReplayRecord;
import eu.mcone.gameapi.api.replay.session.ReplayManager;
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

    private final List<Module> modules;
    private final Option[] options;

    private MapManager mapManager;
    private BackpackManager backpackManager;
    private AchievementManager achievementManager;
    private KitManager kitManager;
    private ReplayManager replayManager;
    private GameStateManager gameStateManager;
    private TeamManager teamManager;
    private PlayerManager playerManager;
    private ReplayRecord replay;
    private DamageLogger damageLogger;
    private OnePassManager onePassManager;

    @Getter
    private CoreJsonConfig<GameConfig> gameConfig;

    protected GamePlugin(String pluginName, ChatColor pluginColor, String prefixTranslation, Option... options) {
        this(pluginName, pluginColor, prefixTranslation, null, options);
    }

    protected GamePlugin(String pluginName, ChatColor pluginColor, String prefixTranslation, String sentryDsn, Option... options) {
        super(pluginName, pluginColor, prefixTranslation, sentryDsn);
        gamePlugin = this;

        this.modules = new ArrayList<>();
        this.options = options;
    }

    protected GamePlugin(Gamemode pluginGamemode, String prefixTranslation, Option... options) {
        this(pluginGamemode, prefixTranslation, null, options);
    }

    protected GamePlugin(Gamemode pluginGamemode, String prefixTranslation, String sentryDsn, Option... options) {
        super(pluginGamemode, prefixTranslation, sentryDsn);
        gamePlugin = this;

        this.modules = new ArrayList<>();
        this.options = options;
    }

    @Override
    public void onEnable() {
        GameAPI.getInstance().withErrorLogging(() -> {
            super.onEnable();
            this.gameConfig = new CoreJsonConfig<>(this, GameConfig.class, "gameConfig.json");
        });

        withErrorLogging(this::onGameEnable);
    }

    @Override
    public void onDisable() {
        GameAPI.getInstance().withErrorLogging(() -> {
            super.onDisable();

            if (modules.contains(Module.REPLAY_MANAGER)
                    && modules.contains(Module.TEAM_MANAGER)
                    && modules.contains(Module.PLAYER_MANAGER)) {
                //Stop date
                getReplay().getRecorder().stop();
                getReplayManager().saveReplay(getReplay());
//            getSessionManager().getChannelHandler().createUnregisterRequest();
            }

            if (modules.contains(Module.REPLAY_MANAGER)) {
                if (getReplayManager().getWorldDownloader() != null) {
                    getReplayManager().getWorldDownloader().stop();
                }
            }
        });

        withErrorLogging(this::onGameEnable);
    }

    public abstract void onGameEnable();

    public abstract void onGameDisable();

    public MapManager getMapManager() {
        modules.add(Module.MAP_MANAGER);
        return mapManager != null ? mapManager : (mapManager = GameAPI.getInstance().constructMapManager());
    }

    public BackpackManager getBackpackManager() {
        modules.add(Module.BACKPACK_MANAGER);
        return backpackManager != null ? backpackManager : (backpackManager = GameAPI.getInstance().constructBackpackManager(this, options));
    }

    public KitManager getKitManager() {
        modules.add(Module.KIT_MANAGER);
        return kitManager != null ? kitManager : (kitManager = GameAPI.getInstance().constructKitManager(this, options));
    }

    public AchievementManager getAchievementManager() {
        modules.add(Module.ACHIEVEMENT_MANAGER);
        return achievementManager != null ? achievementManager : (achievementManager = GameAPI.getInstance().constructAchievementManager(this, options));
    }

    public ReplayManager getReplayManager() {
        modules.add(Module.REPLAY_MANAGER);
        return replayManager != null ? replayManager : (replayManager = GameAPI.getInstance().constructReplayManager(options));
    }

    public GameStateManager getGameStateManager() {
        modules.add(Module.GAME_STATE_MANAGER);
        return gameStateManager != null ? gameStateManager : (gameStateManager = GameAPI.getInstance().constructGameStateManager(this));
    }

    public ReplayRecord getReplay() {
        try {
            if (modules.contains(Module.REPLAY_MANAGER)
                    && modules.contains(Module.TEAM_MANAGER)
                    && modules.contains(Module.PLAYER_MANAGER)) {
                modules.add(Module.REPLAY);
                return replay != null ? replay : (replay = replayManager.createReplay(getGamemode(), options));
            } else {
                throw new GameModuleNotActiveException("The game module ReplaysessionManager isn`t active!");
            }
        } catch (GameModuleNotActiveException e) {
            e.printStackTrace();
        }

        return null;
    }

    public TeamManager getTeamManager() {
        modules.add(Module.TEAM_MANAGER);
        return teamManager != null ? teamManager : (teamManager = GameAPI.getInstance().constructTeamManager(this, options));
    }

    public PlayerManager getPlayerManager() {
        modules.add(Module.PLAYER_MANAGER);
        return playerManager != null ? playerManager : (playerManager = GameAPI.getInstance().constructPlayerManager(this));
    }

    public DamageLogger getDamageLogger() {
        return damageLogger != null ? damageLogger : (damageLogger = GameAPI.getInstance().constructDamageLogger());
    }

    public OnePassManager getOnePassManager() {
        return onePassManager != null ? onePassManager : (onePassManager = GameAPI.getInstance().constructOnePassManager());
    }

    public boolean hasModule(Module module) {
        return modules.contains(module);
    }

    public GamePlayer getGamePlayer(final UUID uuid) {
        return GameAPI.getInstance().getGamePlayer(uuid);
    }

    public GamePlayer getGamePlayer(final Player player) {
        return getGamePlayer(player.getUniqueId());
    }

    public GamePlayer getGamePlayer(String name) {
        return GameAPI.getInstance().getGamePlayer(name);
    }

    public Collection<GamePlayer> getOnlineGamePlayers() {
        return GameAPI.getInstance().getOnlineGamePlayers();
    }
}
