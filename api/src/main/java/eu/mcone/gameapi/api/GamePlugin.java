package eu.mcone.gameapi.api;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.gameapi.api.achievement.AchievementManager;
import eu.mcone.gameapi.api.backpack.BackpackManager;
import eu.mcone.gameapi.api.kit.KitManager;
import eu.mcone.gameapi.api.map.MapManager;
import lombok.Getter;
import org.bukkit.ChatColor;

public abstract class GamePlugin extends CorePlugin {

    @Getter
    private static GamePlugin gamePlugin;

    private final Option[] options;
    private MapManager mapManager;
    private BackpackManager backpackManager;
    private AchievementManager achievementManager;
    private KitManager kitManager;

    protected GamePlugin(String pluginName, ChatColor pluginColor, String prefixTranslation, Option... options) {
        super(pluginName, pluginColor, prefixTranslation);
        gamePlugin = this;

        this.options = options;
    }

    protected GamePlugin(Gamemode pluginGamemode, String prefixTranslation, Option... options) {
        super(pluginGamemode, prefixTranslation);
        gamePlugin = this;

        this.options = options;
    }

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

}
