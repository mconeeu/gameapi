package eu.mcone.gameapi.replay;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.replay.Replay;
import eu.mcone.gameapi.api.replay.container.ReplayContainer;
import eu.mcone.gameapi.api.replay.event.container.ReplayContainerRemovedEvent;
import eu.mcone.gameapi.api.replay.event.runner.ReplayStopEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class ReplayViewManager implements eu.mcone.gameapi.api.replay.ReplayViewManager, Listener {

    @Setter
    @Getter
    private Location spawnLocation;

    private final HashMap<UUID, String> viewer;
    private final HashMap<String, Replay> cache;

    public ReplayViewManager() {
        viewer = new HashMap<>();
        cache = new HashMap<>();

        GamePlugin.getGamePlugin().registerEvents(this);

        CoreWorld coreWorld = CoreSystem.getInstance().getWorldManager().getWorld(GamePlugin.getGamePlugin().getGameConfig().parseConfig().getLobby());
        if (coreWorld != null) {
            spawnLocation = coreWorld.getLocation("spawn");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(ReplayContainerRemovedEvent e) {
        System.out.println("REMOVE CONTAINER");
        Replay replay = cache.get(e.getReplayID());

        if (replay != null) {
            if (replay.getContainers().size() == 0) {
                System.out.println("REMOVED");
                cache.remove(replay.getID());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(ReplayStopEvent e) {
        System.out.println("STOP REPLAY");
        if (spawnLocation != null) {
            Replay replay = cache.get(e.getReplayID());

            if (replay != null) {
                ReplayContainer replayContainer = replay.getContainer(e.getContainerID());

                if (replayContainer != null) {
                    for (Player player : replayContainer.getViewers()) {
                        player.teleport(spawnLocation);
                        player.getInventory().clear();
                        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
                    }
                }
            }
        }
    }

    public void joinReplay(Player player, Replay replay) {
        System.out.println("JOIN REPLAY");
        viewer.put(player.getUniqueId(), replay.getID());
        cache.put(replay.getID(), replay);
    }

    public void leaveReplay(Player player) {
        System.out.println("LEAVE REPLAY");
        viewer.remove(player.getUniqueId());
    }

    public Replay getReplay(String id) {
        return cache.getOrDefault(id, null);
    }

    public Replay getReplay(Player player) {
        if (viewer.containsKey(player.getUniqueId())) {
            return cache.get(viewer.get(player.getUniqueId()));
        }

        return null;
    }

    public ReplayContainer getContainer(UUID containerUUID) {
        ReplayContainer found;
        for (Replay replay : cache.values()) {
            found = replay.getContainer(containerUUID);
            if (found != null) {
                return found;
            }
        }

        return null;
    }

    public ReplayContainer getContainer(Player player) {
        if (viewer.containsKey(player.getUniqueId())) {
            Replay replay = cache.get(viewer.get(player.getUniqueId()));

            if (replay != null) {
                return replay.getContainer(player);
            }
        }

        return null;
    }

    public Collection<Replay> getCachedReplays() {
        return cache.values();
    }
}
