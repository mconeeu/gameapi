package eu.mcone.gamesystem;

import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.gamestate.GameStateHandler;
import eu.mcone.gamesystem.listener.*;
import eu.mcone.gamesystem.player.DamageLogger;
import lombok.Getter;

public class GameSystem extends GameSystemAPI {

    private final static String MainPrefix = "§8[§8GameSystem§8] ";
    @Getter
    private GameStateHandler gameStateHandler;
    @Getter
    private DamageLogger damageLogger;

    @Override
    public void onEnable() {
        setInstance(this);

        damageLogger = new DamageLogger();
        gameStateHandler = new GameStateHandler();

        getServer().getConsoleSender().sendMessage(MainPrefix + "§aRegistering Events...");
        getServer().getPluginManager().registerEvents(new EntityDamageByEntity(), this);
        getServer().getPluginManager().registerEvents(new PlayerAchievementAwarded(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
        getServer().getPluginManager().registerEvents(new WeatherChange(), this);

        getServer().getConsoleSender().sendMessage(MainPrefix + "§aVersion §f" + this.getDescription().getVersion() + "§a enabled...");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(MainPrefix + "§cPlugin disabled!");
    }

}
