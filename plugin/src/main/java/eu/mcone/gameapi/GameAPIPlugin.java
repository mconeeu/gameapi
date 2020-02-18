package eu.mcone.gameapi;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.achievement.GameAchievementManager;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.achievement.AchievementManager;
import eu.mcone.gameapi.api.backpack.BackpackManager;
import eu.mcone.gameapi.api.damage.DamageLogger;
import eu.mcone.gameapi.api.kit.KitManager;
import eu.mcone.gameapi.api.map.MapManager;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.backpack.GameBackpackManager;
import eu.mcone.gameapi.damage.GameDamageLogger;
import eu.mcone.gameapi.gamestate.GameStateManager;
import eu.mcone.gameapi.kit.GameKitManager;
import eu.mcone.gameapi.listener.GamePlayerListener;
import eu.mcone.gameapi.map.GameMapManager;
import eu.mcone.gameapi.player.GameAPIPlayer;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class GameAPIPlugin extends GameAPI {

    @Getter
    private static GameAPIPlugin system;

    @Getter
    private List<GameAPIPlayer> players;

    @Override
    public void onEnable() {
        system = this;
        setInstance(this);

        this.players = new ArrayList<>();

        CoreSystem.getInstance().getTranslationManager().loadCategories(this);

        registerEvents(
                new GamePlayerListener()
        );

        sendConsoleMessage("§aVersion §f" + this.getDescription().getVersion() + "§a enabled...");
    }

    @Override
    public void onDisable() {
        sendConsoleMessage("§cPlugin disabled!");
    }

    @Override
    public eu.mcone.gameapi.api.gamestate.GameStateManager constructGameStateManager(GamePlugin gamePlugin) {
        return new GameStateManager(this, gamePlugin);
    }

    @Override
    public MapManager constructMapManager() {
        return new GameMapManager(this);
    }

    @Override
    public BackpackManager constructBackpackManager(GamePlugin gamePlugin, Option... options) {
        return new GameBackpackManager(this, gamePlugin, options);
    }

    @Override
    public KitManager constructKitManager(GamePlugin gamePlugin, Option... options) {
        return new GameKitManager(this, gamePlugin, options);
    }

    @Override
    public AchievementManager constructAchievementManager(GamePlugin gamePlugin, Option... options) {
        return new GameAchievementManager(gamePlugin, options);
    }

    @Override
    public DamageLogger constructDamageLogger() {
        return new GameDamageLogger(this);
    }

    @Override
    public GameAPIPlayer getGamePlayer(CorePlayer cp) {
        return getGamePlayer(cp.getUuid());
    }

    @Override
    public GameAPIPlayer getGamePlayer(Player p) {
        return getGamePlayer(p.getUniqueId());
    }

    @Override
    public GameAPIPlayer getGamePlayer(UUID uuid) {
        for (GameAPIPlayer gp : players) {
            if (gp.getCorePlayer().getUuid().equals(uuid)) {
                return gp;
            }
        }
        return null;
    }

    @Override
    public GameAPIPlayer getGamePlayer(String name) {
        for (GameAPIPlayer gp : players) {
            if (gp.getCorePlayer().getName().equals(name)) {
                return gp;
            }
        }
        return null;
    }

    @Override
    public Collection<GamePlayer> getOnlineGamePlayers() {
        return new ArrayList<>(players);
    }

    public void registerGamePlayer(GameAPIPlayer gp) {
        players.add(gp);
    }

    public void unregisterGamePlayer(GameAPIPlayer gp) {
        players.remove(gp);
    }

}
