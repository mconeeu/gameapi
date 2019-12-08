package eu.mcone.gameapi;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gameapi.achievement.GameAchievementManager;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.achievement.AchievementManager;
import eu.mcone.gameapi.api.backpack.BackpackManager;
import eu.mcone.gameapi.api.map.MapManager;
import eu.mcone.gameapi.backpack.GameBackpackManager;
import eu.mcone.gameapi.map.GameMapManager;
import lombok.Getter;

public class GameAPIPlugin extends GameAPI {

    @Getter
    private static GameAPIPlugin system;

    @Override
    public void onEnable() {
        system = this;
        setInstance(this);

        CoreSystem.getInstance().getTranslationManager().loadCategories(this);

        sendConsoleMessage("§aVersion §f" + this.getDescription().getVersion() + "§a enabled...");
    }

    @Override
    public void onDisable() {
        sendConsoleMessage("§cPlugin disabled!");
    }

    @Override
    public MapManager constructMapManager() {
        return new GameMapManager(this);
    }

    @Override
    public BackpackManager constructBackpackManager(GamePlugin<?> gamePlugin, Option... options) {
        return new GameBackpackManager(this, gamePlugin, options);
    }

    @Override
    public AchievementManager constructAchievementManager(GamePlugin<?> gamePlugin, Option... options) {
        return new GameAchievementManager(gamePlugin, options);
    }

}
