package eu.mcone.gameapi.api;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.config.CoreJsonConfig;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.gameapi.api.achievement.AchievementManager;
import eu.mcone.gameapi.api.backpack.BackpackManager;
import eu.mcone.gameapi.api.damage.DamageLogger;
import eu.mcone.gameapi.api.game.GameHistoryManager;
import eu.mcone.gameapi.api.gamestate.GameStateManager;
import eu.mcone.gameapi.api.kit.KitManager;
import eu.mcone.gameapi.api.map.MapManager;
import eu.mcone.gameapi.api.onepass.OnePassManager;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.player.PlayerManager;
import eu.mcone.gameapi.api.team.TeamManager;
import eu.mcone.gameapi.api.utils.GameConfig;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class GamePlugin extends CorePlugin {

    @Getter
    private static GamePlugin gamePlugin;

    private final List<Module> modules;
    private final List<Option> options;

    private MapManager mapManager;
    private BackpackManager backpackManager;
    private AchievementManager achievementManager;
    private KitManager kitManager;
    private GameStateManager gameStateManager;
    private TeamManager teamManager;
    private PlayerManager playerManager;
    private DamageLogger damageLogger;
    private OnePassManager onePassManager;
    private GameHistoryManager gameHistoryManager;

    @Getter
    private CoreJsonConfig<GameConfig> gameConfig;

    protected GamePlugin(String pluginName, ChatColor pluginColor, String prefixTranslation, Option... options) {
        this(pluginName, pluginColor, prefixTranslation, null, options);
    }

    protected GamePlugin(String pluginName, ChatColor pluginColor, String prefixTranslation, String sentryDsn, Option... options) {
        super(pluginName, pluginColor, prefixTranslation, sentryDsn);
        gamePlugin = this;

        this.modules = new ArrayList<>();
        this.options = Arrays.asList(options);
    }

    protected GamePlugin(Gamemode pluginGamemode, String prefixTranslation, Option... options) {
        this(pluginGamemode, prefixTranslation, null, options);
    }

    protected GamePlugin(Gamemode pluginGamemode, String prefixTranslation, String sentryDsn, Option... options) {
        super(pluginGamemode, prefixTranslation, sentryDsn);
        gamePlugin = this;

        this.modules = new ArrayList<>();
        this.options = Arrays.asList(options);
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
            if (modules.contains(Module.GAME_HISTORY_MANAGER)) {
                gameHistoryManager.saveHistory();
            }
        });

        withErrorLogging(this::onGameDisable);
    }

    public abstract void onGameEnable();

    public abstract void onGameDisable();

    public MapManager getMapManager() {
        modules.add(Module.MAP_MANAGER);
        return mapManager != null ? mapManager : (mapManager = GameAPI.getInstance().constructMapManager());
    }

    public BackpackManager getBackpackManager() {
        modules.add(Module.BACKPACK_MANAGER);
        return backpackManager != null ? backpackManager : (backpackManager = GameAPI.getInstance().constructBackpackManager(this));
    }

    public KitManager getKitManager() {
        modules.add(Module.KIT_MANAGER);
        return kitManager != null ? kitManager : (kitManager = GameAPI.getInstance().constructKitManager(this));
    }

    public AchievementManager getAchievementManager() {
        modules.add(Module.ACHIEVEMENT_MANAGER);
        return achievementManager != null ? achievementManager : (achievementManager = GameAPI.getInstance().constructAchievementManager(this));
    }

    public GameStateManager getGameStateManager() {
        modules.add(Module.GAME_STATE_MANAGER);
        return gameStateManager != null ? gameStateManager : (gameStateManager = GameAPI.getInstance().constructGameStateManager(this));
    }

    public TeamManager getTeamManager() {
        modules.add(Module.TEAM_MANAGER);
        return teamManager != null ? teamManager : (teamManager = GameAPI.getInstance().constructTeamManager(this));
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

    public GameHistoryManager getGameHistoryManager() {
        modules.add(Module.GAME_HISTORY_MANAGER);

        try {
            return gameHistoryManager != null ? gameHistoryManager : (gameHistoryManager = GameAPI.getInstance().constructGameHistoryManager());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean hasModule(Module module) {
        return modules.contains(module);
    }

    public boolean hasOption(Option option) {
        return this.options.contains(option);
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

    public static boolean isGamePluginInitialized() {
        return gamePlugin != null;
    }

    public String getGameName() {
        return !getGamemode().equals(Gamemode.UNDEFINED)
                ? getGamemode().getName()
                : getPluginName();
    }

    public String getGameLabel() {
        return !getGamemode().equals(Gamemode.UNDEFINED)
                ? getGamemode().getLabel()
                : getPluginColor() + getPluginName();
    }

}
