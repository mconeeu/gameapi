package eu.mcone.gameapi.player;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.inventory.SpectatorInventory;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutCamera;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class GamePlayerManager implements eu.mcone.gameapi.api.player.PlayerManager, Listener {

    @Getter
    private List<Player> playing;
    @Getter
    private List<Player> spectating;
    private List<Player> inCamera;
    @Getter
    private int minPlayers;
    @Getter
    private int maxPlayers;

    public GamePlayerManager(GamePlugin plugin, GameAPIPlugin system) {
        playing = new ArrayList<>();
        spectating = new ArrayList<>();
        minPlayers = plugin.getGameConfig().parseConfig().getMinPlayers();
        maxPlayers = plugin.getGameConfig().parseConfig().getMaxPlayers();

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

    public void setSpectating(final Player player, final boolean var) {
        playing.remove(player);

        if (var) {
            if (!spectating.contains(player)) {
                spectating.add(player);

                ((CraftPlayer) player).getHandle().abilities.canFly = true;
                ((CraftPlayer) player).getHandle().abilities.canInstantlyBuild = false;
                ((CraftPlayer) player).getHandle().abilities.isInvulnerable = true;
                ((CraftPlayer) player).getHandle().abilities.isFlying = true;
                ((CraftPlayer) player).getHandle().abilities.mayBuild = true;

                for (Player all : Bukkit.getOnlinePlayers()) {
                    all.hidePlayer(player);
                }
            }
        } else {
            spectating.remove(player);
            player.setGameMode(GameMode.SURVIVAL);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.getInventory().remove(SpectatorInventory.NAVIGATOR);

            for (Player all : Bukkit.getOnlinePlayers()) {
                all.showPlayer(player);
            }
        }

        player.teleport(CoreSystem.getInstance().getWorldManager().getWorld(GamePlugin.getGamePlugin().getGameConfig().parseConfig().getLobby()).getLocation("spawn"));
    }

    @EventHandler
    public void on(PlayerInteractAtEntityEvent e) {
        Player player = e.getPlayer();
        if (spectating.contains(player) && !inCamera.contains(player)) {
            if (e.getRightClicked() instanceof Player) {
                Player target = (Player) e.getRightClicked();

                PacketPlayOutCamera camera = new PacketPlayOutCamera();
                camera.a = target.getEntityId();
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(camera);
                inCamera.add(player);
                GamePlugin.getGamePlugin().getMessager().send(player, "§7Du bist nun in der Ansicht des Spielers §f" + player);
            }
        }
    }

    @EventHandler
    public void on(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        if (spectating.contains(player) && inCamera.contains(player)) {
            if (e.isSneaking()) {
                PacketPlayOutCamera camera = new PacketPlayOutCamera();
                camera.a = player.getEntityId();
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(camera);
                inCamera.remove(player);
                GamePlugin.getGamePlugin().getMessager().send(player, "§7Du bist nun nicht mehr in der Ansicht des Spielers §f" + player);
            }
        }
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
