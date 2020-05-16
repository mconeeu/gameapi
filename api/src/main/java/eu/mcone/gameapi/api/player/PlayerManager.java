package eu.mcone.gameapi.api.player;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public interface PlayerManager {

    ItemStack SPECTATOR = new ItemBuilder(Material.BED, 1, 0).displayName("§c§lSpectator §8» §7§oteleportiere dich zu einem Spieler.").create();

    int getMinPlayers();

    int getMaxPlayers();

    Set<Player> getPlayers(GamePlayerState state);

    Set<GamePlayer> getGamePlayers(GamePlayerState state);

    boolean isInCameraMode(GamePlayer player);

    boolean maxPlayersReached();

    boolean minPlayersReached();

    void openSpectatorInventory(Player player);
}
