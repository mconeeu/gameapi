package eu.mcone.gameapi.player;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.inventory.SpectatorInventory;
import eu.mcone.gameapi.listener.PlayerManagerListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GamePlayerManager implements eu.mcone.gameapi.api.player.PlayerManager {

    @Getter
    private final List<Player> playing;
    @Getter
    private final List<Player> spectating;
    @Getter
    private final List<Player> inCamera;
    @Getter
    private final int minPlayers;
    @Getter
    private final int maxPlayers;

    public GamePlayerManager(GamePlugin plugin, GameAPIPlugin system) {
        playing = new ArrayList<>();
        spectating = new ArrayList<>();
        inCamera = new ArrayList<>();
        minPlayers = plugin.getGameConfig().parseConfig().getMinPlayers();
        maxPlayers = plugin.getGameConfig().parseConfig().getMaxPlayers();

        GamePlugin.getGamePlugin().registerEvents(new PlayerManagerListener(this));
        system.sendConsoleMessage("§aLoading PlayerManager...");
    }

    public void addSpectator(final Player player) {
        if (!spectating.contains(player)) {
            spectating.add(player);
        }
    }

    public void removeSpectator(final Player player) {
        spectating.remove(player);
    }

    public boolean isSpectator(final Player player) {
        return spectating.contains(player);
    }

    public void setSpectating(final Player player, final boolean add) {
        playing.remove(player);

        if (add) {
            if (!spectating.contains(player)) {
                spectating.add(player);

                ((CraftPlayer) player).getHandle().abilities.canFly = true;
                ((CraftPlayer) player).getHandle().abilities.canInstantlyBuild = false;
                ((CraftPlayer) player).getHandle().abilities.isInvulnerable = true;
                ((CraftPlayer) player).getHandle().abilities.isFlying = true;
                ((CraftPlayer) player).getHandle().abilities.mayBuild = false;

                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.hidePlayer(player);
                }

                for (Player spectator : spectating) {
                    player.showPlayer(spectator);
                }
            }
        } else {
            spectating.remove(player);
            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().remove(SpectatorInventory.NAVIGATOR);

            for (Player all : Bukkit.getOnlinePlayers()) {
                all.showPlayer(player);
            }
        }

        player.teleport(CoreSystem.getInstance().getWorldManager().getWorld(GamePlugin.getGamePlugin().getGameConfig().parseConfig().getLobby()).getLocation("game.spectator"));
    }

    public void setPlaying(final Player player, final boolean var) {
        spectating.remove(player);

        if (var) {
            if (!playing.contains(player)) {
                playing.add(player);
            }
        } else {
            playing.remove(player);
        }
    }

    public void openSpectatorInventory(final Player player) {
        new SpectatorInventory(player, this);
    }
}
