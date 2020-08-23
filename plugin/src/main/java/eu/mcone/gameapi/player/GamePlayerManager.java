package eu.mcone.gameapi.player;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.PlayerInventorySlot;
import eu.mcone.coresystem.api.bukkit.vanish.VanishRule;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.player.GamePlayerState;
import eu.mcone.gameapi.api.player.PlayerManager;
import eu.mcone.gameapi.inventory.SpectatorInventory;
import eu.mcone.gameapi.listener.players.GamePlayerListener;
import eu.mcone.gameapi.listener.players.SpectatorListener;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutCamera;
import net.minecraft.server.v1_8_R3.PlayerAbilities;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class GamePlayerManager implements PlayerManager {

    private static final VanishRule SPECTATOR_VANISH_RULE = (p, canSeePlayer) -> {
        GamePlayer gp = GameAPI.getInstance().getGamePlayer(p);

        if (gp.getState().equals(GamePlayerState.SPECTATING)) {
            Set<Player> playing = GamePlugin.getGamePlugin().getPlayerManager().getPlayers(GamePlayerState.PLAYING);

            for (int i = 0; i < canSeePlayer.size(); i++) {
                Player player = canSeePlayer.get(i);

                if (playing.contains(player)) {
                    canSeePlayer.remove(player);
                }
            }
        }
    };

    @Getter
    private final int minPlayers, maxPlayers;
    private final Set<GamePlayer> cameraPlayers;

    public GamePlayerManager(GamePlugin plugin, GameAPIPlugin system) {
        this.minPlayers = plugin.getGameConfig().parseConfig().getMinPlayers();
        this.maxPlayers = plugin.getGameConfig().parseConfig().getMaxPlayers();
        this.cameraPlayers = new HashSet<>();

        plugin.registerEvents(
                new SpectatorListener(this),
                new GamePlayerListener(this)
        );
        CoreSystem.getInstance().getVanishManager().registerVanishRule(Integer.MAX_VALUE-10, SPECTATOR_VANISH_RULE);

        system.sendConsoleMessage("§aLoading PlayerManager...");
    }

    @Override
    public Set<Player> getPlayers(GamePlayerState state) {
        Set<Player> result = new HashSet<>();

        for (GamePlayer gamePlayer : GamePlugin.getGamePlugin().getOnlineGamePlayers()) {
            if (gamePlayer.getState().equals(state)) {
                result.add(gamePlayer.bukkit());
            }
        }

        return result;
    }

    @Override
    public Set<GamePlayer> getGamePlayers(GamePlayerState state) {
        Set<GamePlayer> result = new HashSet<>();

        for (GamePlayer gp : GameAPI.getInstance().getOnlineGamePlayers()) {
            if (gp.getState().equals(state)) {
                result.add(gp);
            }
        }

        return result;
    }

    @Override
    public boolean isInCameraMode(GamePlayer player) {
        return cameraPlayers.contains(player);
    }

    @Override
    public boolean maxPlayersReached() {
        return Bukkit.getOnlinePlayers().size() >= maxPlayers;
    }

    @Override
    public boolean minPlayersReached() {
        return Bukkit.getOnlinePlayers().size() >= minPlayers;
    }

    public void setState(GamePlayer player, GamePlayerState state) {
        if (!player.getState().equals(state)) {
            if (state.equals(GamePlayerState.SPECTATING)) {
                Player p = player.bukkit();
                setSpectatorAbilities(p, true);

                CoreSystem.getInstance().getVanishManager().recalculateVanishes();

                p.getInventory().clear();
                p.getInventory().setArmorContents(null);

                p.setMaxHealth(20);
                p.setHealth(20);
                p.setLevel(0);
                p.setExp(0);
                p.setFoodLevel(20);
                p.setWalkSpeed(0.2F);

                p.getInventory().setItem(PlayerInventorySlot.HOTBAR_SLOT_9, SpectatorInventory.NAVIGATOR);
                p.teleport(CoreSystem.getInstance().getWorldManager().getWorld(GamePlugin.getGamePlugin().getGameConfig().parseConfig().getGameWorld()).getLocation("game.spectator"));
            } else if (state.equals(GamePlayerState.PLAYING)) {
                Player p = player.bukkit();

                p.setAllowFlight(false);
                p.setFlying(false);
                setSpectatorAbilities(p, false);

                if (isInCameraMode(player)) {
                    removeFromCameraMode(player);
                }
                CoreSystem.getInstance().getVanishManager().recalculateVanishes();

                p.getInventory().remove(SpectatorInventory.NAVIGATOR);
                p.teleport(CoreSystem.getInstance().getWorldManager().getWorld(GamePlugin.getGamePlugin().getGameConfig().parseConfig().getLobby()).getLocation("game.spectator"));
            }
        }
    }

    public void setCameraMode(GamePlayer gp, Player t) {
        Player p = gp.bukkit();

        if (gp.getState().equals(GamePlayerState.SPECTATING)) {
            sendCameraPacket(p, t);
            cameraPlayers.add(gp);

            GamePlugin.getGamePlugin().getMessenger().send(p, "§7Du bist nun in der Ansicht des Spielers §f" + t.getName());
        } else
            throw new IllegalStateException("Could not set player " + p.getName() + " to camera mode. Player is not a spectator!");
    }

    public void removeFromCameraMode(GamePlayer gp) {
        Player p = gp.bukkit();

        if (isInCameraMode(gp)) {
            sendCameraPacket(p, p);
            cameraPlayers.remove(gp);

            GamePlugin.getGamePlugin().getMessenger().send(p, "§7Du hast die Kameraansicht verlassen!");
        } else
            throw new IllegalStateException("Could not remove player " + p.getName() + " from camera mode. Player is not in camera mode!");
    }

    public void openSpectatorInventory(Player player) {
        new SpectatorInventory(player, this);
    }

    public static void setSpectatorAbilities(Player p, boolean set) {
        p.setGameMode(GameMode.SURVIVAL);
        PlayerAbilities abilities = ((CraftPlayer) p).getHandle().abilities;

        abilities.canFly = set;
        abilities.canInstantlyBuild = !set;
        abilities.isInvulnerable = set;
        abilities.isFlying = set;
        abilities.mayBuild = !set;
    }

    private static void sendCameraPacket(Player p, Player t) {
        PacketPlayOutCamera camera = new PacketPlayOutCamera();
        camera.a = t.getEntityId();
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(camera);
    }
}
