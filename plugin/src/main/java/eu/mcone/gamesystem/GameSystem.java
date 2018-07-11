package eu.mcone.gamesystem;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.gamestate.GameStateHandler;
import eu.mcone.gamesystem.listener.*;
import eu.mcone.gamesystem.player.DamageLogger;
import lombok.Getter;

public class GameSystem extends GameSystemAPI {

    @Getter
    private GameStateHandler gameStateHandler;
    @Getter
    private DamageLogger damageLogger;

    @Override
    public void onEnable() {
        setInstance(this);

        damageLogger = new DamageLogger();
        gameStateHandler = new GameStateHandler();
        CoreSystem.getInstance().getTranslationManager().loadCategories(this);

        sendConsoleMessage("§aRegistering Events...");
        getServer().getPluginManager().registerEvents(new EntityDamageByEntity(), this);
        getServer().getPluginManager().registerEvents(new PlayerAchievementAwarded(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
        getServer().getPluginManager().registerEvents(new WeatherChange(), this);

        sendConsoleMessage("§aVersion §f" + this.getDescription().getVersion() + "§a enabled...");
    }

    @Override
    public void onDisable() {
        sendConsoleMessage("§cPlugin disabled!");
    }

}
