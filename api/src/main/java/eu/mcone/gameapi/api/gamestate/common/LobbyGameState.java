package eu.mcone.gameapi.api.gamestate.common;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.item.ItemBuilder;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.event.gamestate.GameStateCountdownEndEvent;
import eu.mcone.gameapi.api.event.gamestate.GameStateStartEvent;
import eu.mcone.gameapi.api.gamestate.GameState;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.player.GamePlayerState;
import eu.mcone.gameapi.api.scoreboard.LobbyObjective;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LobbyGameState extends GameState {

    public static final ItemStack QUIT_ITEM = new ItemBuilder(Material.IRON_DOOR, 1, 0).displayName("§4§lVerlassem §8» §7§overlasse die Spielrunde.").create();
    private static final int FORCE_START_TIME = 10;

    @Setter @Getter
    private static Class<? extends LobbyObjective> objective;

    public LobbyGameState() {
        this(30);
    }

    public LobbyGameState(int countdown) {
        super("Lobby", countdown);
    }

    @Override
    public void onCountdownSecond(CorePlugin plugin, int second) {
        switch (second) {
            case 60:
            case 30:
            case 15:
            case 10:
            case 5:
            case 3:
            case 2:
            case 1:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    plugin.getMessenger().sendTransl(p, "game.gamestate.lobby.countdown.idling", second);
                    p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
                }
            default:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.setLevel(second);
                }
        }
    }

    @Override
    public void onStart(GameStateStartEvent event) {
        if (GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER)) {
            try {
                if (objective == null) {
                    objective = LobbyObjective.class;
                }

                for (GamePlayer gp : GamePlugin.getGamePlugin().getPlayerManager().getGamePlayers(GamePlayerState.PLAYING)) {
                    gp.getCorePlayer().getScoreboard().setNewObjective(objective.newInstance());
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCountdownEnd(GameStateCountdownEndEvent event) {
        super.onCountdownEnd(event);

        if (GamePlugin.getGamePlugin().hasModule(Module.BACKPACK_MANAGER)) {
            if (GamePlugin.getGamePlugin().getBackpackManager().getGameOptions().contains(Option.BACKPACK_MANAGER_REGISTER_TRAIL_CATEGORY)) {
                GamePlugin.getGamePlugin().getBackpackManager().getTrailHandler().stop();
            }

            if (GamePlugin.getGamePlugin().getBackpackManager().getGameOptions().contains(Option.BACKPACK_MANAGER_REGISTER_PET_CATEGORY)) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    GamePlugin.getGamePlugin().getBackpackManager().getPetHandler().despawnPet(player);
                }
            }
        }
    }

    public static boolean forceStart() {
        if (GamePlugin.getGamePlugin().getGameStateManager().getCountdownCounter() > FORCE_START_TIME) {
            return GamePlugin.getGamePlugin().getGameStateManager().updateCountdownCounter(FORCE_START_TIME);
        } else {
            return false;
        }
    }

}
