package eu.mcone.gameapi.api.gamestate.common;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.inventory.PlayerInventorySlot;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.MainScoreboard;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.HotbarItem;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.event.gamestate.GameStateStartEvent;
import eu.mcone.gameapi.api.gamestate.GameState;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.player.GamePlayerState;
import eu.mcone.gameapi.api.scoreboard.LobbyObjective;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EndGameState extends GameState {

    @Setter
    @Getter
    private static Class<? extends LobbyObjective> objective;

    public EndGameState() {
        this(30);
    }

    public EndGameState(int countdown) {
        super("End", countdown);
    }

    @Override
    public void onCountdownSecond(CorePlugin plugin, int second) {
        switch (second) {
            case 30:
            case 15:
            case 10:
            case 5:
            case 3:
            case 2:
            case 1:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    plugin.getMessenger().send(p, CoreSystem.getInstance().getTranslationManager().get(
                            "game.countdown.restart",
                            CoreSystem.getInstance().getCorePlayer(p)
                    ).replace("%seconds%", Long.toString(second)));
                    p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
                }
                break;
            default:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.setLevel(second);
                }
        }
    }

    @Override
    public void onStart(GameStateStartEvent event) {
        GamePlugin.getGamePlugin().getGameStateManager().startCountdown();

        for (GamePlayer gp : GamePlugin.getGamePlugin().getOnlineGamePlayers()) {
            Player p = gp.bukkit();
            CorePlayer cp = gp.getCorePlayer();

            gp.setState(GamePlayerState.PLAYING);
            cp.setScoreboard(new MainScoreboard());

            p.getInventory().clear();
            p.getInventory().setArmorContents(new ItemStack[0]);
            p.getActivePotionEffects().clear();
            p.playSound(p.getLocation(), Sound.FIREWORK_BLAST, 1, 1);
            p.playSound(p.getLocation(), Sound.ENDERDRAGON_DEATH, 1, 1);
            p.setHealth(20);
            p.setFoodLevel(20);
            p.setLevel(0);
            p.setExp(0);
            p.getInventory().setArmorContents(null);
            p.setGameMode(GameMode.SURVIVAL);

            p.getInventory().setItem(PlayerInventorySlot.HOTBAR_SLOT_9, HotbarItem.QUIT);
            CoreSystem.getInstance().getWorldManager().getWorld(GamePlugin.getGamePlugin().getGameConfig().parseConfig().getLobby()).teleport(p, "spawn");
        }

        if (GamePlugin.getGamePlugin().hasModule(Module.GAME_HISTORY_MANAGER)) {
            if (GamePlugin.getGamePlugin().hasOption(Option.GAME_HISTORY_HISTORY_MODE)) {
                GamePlugin.getGamePlugin().getGameHistoryManager().saveHistory();
            }
        }

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
}
