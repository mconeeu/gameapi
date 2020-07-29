package eu.mcone.gameapi.replay.player;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.CoreInventory;
import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.world.CoreLocation;
import eu.mcone.coresystem.api.core.player.Group;
import eu.mcone.gameapi.api.replay.objectives.ReplayPlayerSidebarObjective;
import eu.mcone.gameapi.replay.inventory.ReplayPlayerInventory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@Getter
public class ReplayPlayer implements eu.mcone.gameapi.api.replay.player.ReplayPlayer, Listener {

    private UUID uuid;
    private String name;
    private String displayName;
    @Setter
    private boolean reported;
    @Setter
    private long joined;
    @Setter
    private long leaved;
    @Setter
    private CoreLocation spawnLocation;

    @Setter
    private transient Map<Integer, ItemStack> inventoryItems;
    private transient eu.mcone.gameapi.api.replay.player.ReplayPlayer.Stats stats;
    @Setter
    private transient int health;
    @Setter
    private transient int food;
    private transient Map<Player, CoreInventory> inventoryViewers;
    @Setter
    private transient ReplayPlayerSidebarObjective scoreboard;
    @Setter
    private transient PlayerNpc npc;

    public ReplayPlayer(final Player player) {
        CorePlayer corePlayer = CoreSystem.getInstance().getCorePlayer(player);
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.displayName = (corePlayer.isNicked() ? Group.SPIELER.getPrefix() : corePlayer.getMainGroup().getPrefix()) + player.getName();
        this.inventoryItems = new HashMap<>();
        this.stats = new Stats(0, 0, 0);
        this.health = 10;
        this.food = 20;
        this.inventoryViewers = new HashMap<>();
    }
    public void setInventoryItem(int slot, ItemStack itemStack) {
        inventoryItems.put(slot, itemStack);
    }

    public void openInventory(Player player) {
        inventoryViewers.put(player, new ReplayPlayerInventory(player, this, inventoryItems));
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Stats implements eu.mcone.gameapi.api.replay.player.ReplayPlayer.Stats {
        private int kills = 0;
        private int deaths = 0;
        private int goals = 0;
    }
}
