package eu.mcone.gameapi.api.replay.container;

import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.coresystem.api.bukkit.item.Skull;
import eu.mcone.gameapi.api.replay.player.ReplayPlayer;
import eu.mcone.gameapi.api.replay.runner.ReplaySpeed;
import eu.mcone.gameapi.api.replay.session.Replay;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public interface ReplayContainer {

    //Slot 1
    ItemStack REPLAY_TELEPORT = new ItemBuilder(Material.COMPASS, 1).displayName("§7Teleporter").create();
    //Slot 2
    ItemStack REPLAY_INFORMATION = new ItemBuilder(Material.REDSTONE_COMPARATOR, 1).displayName("§eReplay").create();

    //Slot 4
    ItemStack REPLAY_SKIP_FORWARD = new Skull("natatos").setDisplayName("§7Überspringen").getItemStack();
    //Slot 5
    ItemStack REPLAY_PAUSE = new ItemBuilder(Material.WOOL, 1, 14).displayName("§7Pausieren").create();
    //Slot 5
    ItemStack REPLAY_START = new ItemBuilder(Material.WOOL, 1, 13).displayName("§7Vortfahren").create();
    //Slot 6
    ItemStack REPLAY_SKIP_BACKWARD = new Skull("saidus2").setDisplayName("§7Zurückspringen").getItemStack();

    //Slot 7
    ItemStack REPLAY_FORWARD = new Skull("MHF_ArrowRight").setDisplayName("§7Vorwährts§8/§7§lRückwährts").getItemStack();
    //Slot 7
    ItemStack REPLAY_BACKWARD = new Skull("MHF_ArrowLeft").setDisplayName("§7§lVorwährts§8/§7Rückwährts").getItemStack();
    //Slot 8
    ItemStack REPLAY_SPEED_INCREASE = new ItemBuilder(Material.EMERALD, 1).displayName("§7Vorspulen §8(§ex1.0§8)").create();

    Replay getReplay();

    UUID getContainerUUID();

    boolean isPlaying();

    AtomicInteger getCurrentTick();

    boolean isForward();

    boolean isBackward();

    ReplaySpeed getReplaySpeed();

    HashSet<Player> getWatchers();

    HashMap<Integer, Integer> getEntities();

    void addWatcher(final Player... players);

    void removeWatcher(final Player... players);

    void restart();

    void play();

    void stop();

    void startPlaying();

    void stopPlaying();

    void forward();

    void backward();

    void setSpeed(ReplaySpeed speed);

    void skip(int ticks);

    void invite(final Player sender, final Player target);

    eu.mcone.gameapi.api.replay.runner.PlayerRunner createSingleRunner(final ReplayPlayer replayPlayer);

    void openSpectatorInventory(Player player);
}
