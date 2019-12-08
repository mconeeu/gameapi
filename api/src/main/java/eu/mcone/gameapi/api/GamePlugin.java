package eu.mcone.gameapi.api;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.api.achievement.AchievementManager;
import eu.mcone.gameapi.api.backpack.BackpackManager;
import eu.mcone.gameapi.api.map.MapManager;
import eu.mcone.gameapi.api.player.GameAPIPlayer;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public abstract class GamePlugin<P extends GameAPIPlayer<?>> extends CorePlugin {

    @Getter
    private static GamePlugin<?> plugin;

    @Getter
    private List<P> players;
    private final Option[] options;

    private MapManager mapManager;
    private BackpackManager backpackManager;
    private AchievementManager achievementManager;

    protected GamePlugin(String pluginName, ChatColor pluginColor, String prefixTranslation, Option... options) {
        super(pluginName, pluginColor, prefixTranslation);
        plugin = this;

        this.players = new ArrayList<>();
        this.options = options;
    }

    protected GamePlugin(Gamemode pluginGamemode, String prefixTranslation, Option... options) {
        super(pluginGamemode, prefixTranslation);
        plugin = this;

        this.players = new ArrayList<>();
        this.options = options;
    }

    public MapManager getMapManager() {
        return mapManager != null ? mapManager : (mapManager = GameAPI.getInstance().constructMapManager());
    }

    public BackpackManager getBackpackManager() {
        return backpackManager != null ? backpackManager : (backpackManager = GameAPI.getInstance().constructBackpackManager(this, options));
    }

    public AchievementManager getAchievementManager() {
        return achievementManager != null ? achievementManager : (achievementManager = GameAPI.getInstance().constructAchievementManager(this, options));
    }

    public P getGamePlayer(CorePlayer cp) {
        return getGamePlayer(cp.getUuid());
    }

    public P getGamePlayer(Player p) {
        return getGamePlayer(p.getUniqueId());
    }

    public P getGamePlayer(UUID uuid) {
        for (P gp : players) {
            if (gp.getCorePlayer().getUuid().equals(uuid)) {
                return gp;
            }
        }
        return null;
    }

    public P getGamePlayer(String name) {
        for (P gp : players) {
            if (gp.getCorePlayer().getName().equals(name)) {
                return gp;
            }
        }
        return null;
    }

    public Collection<P> getOnlineGamePlayers() {
        return players;
    }

    public void registerGamePlayer(P gp) {
        players.add(gp);
    }

    public void unregisterGamePlayer(P gp) {
        players.remove(gp);
    }

}
