package eu.mcone.gameapi;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.achievement.GameAchievementManager;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.achievement.AchievementManager;
import eu.mcone.gameapi.api.backpack.BackpackManager;
import eu.mcone.gameapi.api.damage.DamageLogger;
import eu.mcone.gameapi.api.kit.KitManager;
import eu.mcone.gameapi.api.map.MapManager;
import eu.mcone.gameapi.api.onepass.OnePassManager;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.backpack.GameBackpackManager;
import eu.mcone.gameapi.damage.GameDamageLogger;
import eu.mcone.gameapi.game.GameHistoryManager;
import eu.mcone.gameapi.gamestate.GameStateManager;
import eu.mcone.gameapi.kit.GameKitManager;
import eu.mcone.gameapi.listener.GamePlayerListener;
import eu.mcone.gameapi.listener.hotbar.LobbyTriggerListener;
import eu.mcone.gameapi.map.GameMapManager;
import eu.mcone.gameapi.onepass.GameOnePassManager;
import eu.mcone.gameapi.player.GameAPIPlayer;
import eu.mcone.gameapi.player.GamePlayerManager;
import eu.mcone.gameapi.replay.ReplayManager;
import eu.mcone.gameapi.team.GameTeamManager;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameAPIPlugin extends GameAPI {

    @Getter
    private static GameAPIPlugin system;

    @Getter
    private List<GameAPIPlayer> players;

    @Override
    public void onEnable() {
        withErrorLogging(() -> {
            super.onEnable();

            system = this;
            setInstance(this);

            this.players = new ArrayList<>();

            registerEvents(
                    new GamePlayerListener(),
                    new LobbyTriggerListener()
            );

            sendConsoleMessage("§aVersion §f" + this.getDescription().getVersion() + "§a enabled...");
        });
    }

    @Override
    public void onDisable() {
        withErrorLogging(() -> {
            for (GamePlayer gp : getOnlineGamePlayers()) {
                ((GameAPIPlayer) gp).saveData();
            }

            sendConsoleMessage("§cPlugin disabled!");
        });
    }

    @Override
    public eu.mcone.gameapi.api.gamestate.GameStateManager constructGameStateManager(GamePlugin gamePlugin) {
        return new GameStateManager(this, gamePlugin);
    }

    @Override
    public MapManager constructMapManager() {
        return new GameMapManager(GamePlugin.getGamePlugin());
    }

    @Override
    public BackpackManager constructBackpackManager(GamePlugin gamePlugin) {
        return new GameBackpackManager(this, gamePlugin);
    }

    @Override
    public KitManager constructKitManager(GamePlugin gamePlugin) {
        return new GameKitManager(this, gamePlugin);
    }

    @Override
    public AchievementManager constructAchievementManager(GamePlugin gamePlugin) {
        return new GameAchievementManager(gamePlugin, this);
    }

    @Override
    public ReplayManager constructReplayManager() {
        return new ReplayManager();
    }

    @Override
    public GameTeamManager constructTeamManager(GamePlugin gamePlugin) {
        return new GameTeamManager(gamePlugin, this);
    }

    @Override
    public GamePlayerManager constructPlayerManager(GamePlugin gamePlugin) {
        return new GamePlayerManager(gamePlugin, this);
    }

    @Override
    public OnePassManager constructOnePassManager() {
        return new GameOnePassManager();
    }

    @Override
    public GameHistoryManager constructGameHistoryManager() throws NoSuchFieldException, IllegalAccessException {
        return new GameHistoryManager();
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
    public List<GamePlayer> getOnlineGamePlayers() {
        return new ArrayList<>(players);
    }

    public void registerGamePlayer(GameAPIPlayer gp) {
        players.add(gp);
    }

    public void unregisterGamePlayer(GameAPIPlayer gp) {
        players.remove(gp);
    }

}
