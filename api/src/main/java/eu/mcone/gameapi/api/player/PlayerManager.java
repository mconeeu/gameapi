package eu.mcone.gameapi.api.player;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface PlayerManager {

    ItemStack SPECTATOR = new ItemBuilder(Material.BED, 1, 0).displayName("§c§lSpectator §8» §7§oteleportiere dich zu einem Spieler.").create();

    List<Player> getPlaying();

    int getMinPlayers();

    int getMaxPlayers();

    void addSpectator(final Player player);

    void removeSpectator(final Player player);

    boolean isSpectator(final Player player);

    void setSpectating(final Player player, final boolean var);

    void setPlaying(final Player player, final boolean var);

    void openSpectatorInventory(final Player player);
}
