package eu.mcone.gameapi.player;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.InventorySlot;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.inventory.SpectatorInventory;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class GamePlayerManager implements eu.mcone.gameapi.api.player.PlayerManager {

    @Getter
    private List<Player> playing;
    private List<Player> spectating;
    @Getter
    private int minPlayers;
    @Getter
    private int maxPlayers;

    public GamePlayerManager(GamePlugin gamePlugin) {
        playing = new ArrayList<>();
        spectating = new ArrayList<>();
        minPlayers = gamePlugin.getGameConfig().parseConfig().getMinPlayers();
        maxPlayers = gamePlugin.getGameConfig().parseConfig().getMaxPlayers();
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
        if (var) {
            if (!spectating.contains(player)) {
                spectating.add(player);
                player.setGameMode(GameMode.SPECTATOR);
                player.setFlying(true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
                player.getInventory().setItem(InventorySlot.ROW_1_SLOT_5, SpectatorInventory.NAVIGATOR);

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

        player.teleport(CoreSystem.getInstance().getWorldManager().getWorld(GamePlugin.getPlugin().getGameConfig().parseConfig().getLobby()).getLocation("spawn"));
    }

    public void setPlaying(final Player player, final boolean var) {
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
