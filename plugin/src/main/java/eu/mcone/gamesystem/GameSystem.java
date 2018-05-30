package eu.mcone.gamesystem;

import eu.mcone.gamesystem.gamestate.GameStateHandler;
import eu.mcone.gamesystem.listener.*;
import eu.mcone.gamesystem.player.DamageLogger;
import lombok.Getter;

public class GameSystem extends eu.mcone.gamesystem.api.GameSystemAPI {

    private static String MainPrefix = "§8[§8GameAPI§8] ";
    private static GameStateHandler gameStateHandler;

    @Getter
    private static DamageLogger damageLogger;

    @Override
    public void onEnable() {
        setInstance(this);
        setGameStateHandler(new eu.mcone.gamesystem.gamestate.GameStateHandler());
        createGameStateHandler();

        damageLogger = new DamageLogger();
        setDamageLogger(damageLogger);

        gameStateHandler = (GameStateHandler) getGameStateHandler(1);

        getServer().getConsoleSender().sendMessage(MainPrefix + "§aBefehle und Events werden registriert...");
        getServer().getPluginManager().registerEvents(new EntityDamageByEntity(), this);
        getServer().getPluginManager().registerEvents(new PlayerAchievementAwarded(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
        getServer().getPluginManager().registerEvents(new WeatherChange(), this);

        getServer().getConsoleSender().sendMessage(MainPrefix + "§aVersion §f" + this.getDescription().getVersion() + "§a wurde aktiviert...");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(MainPrefix + "§cPlugin wurde deaktiviert!");
    }

}
