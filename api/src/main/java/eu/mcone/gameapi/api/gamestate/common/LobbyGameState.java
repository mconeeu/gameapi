package eu.mcone.gameapi.api.gamestate.common;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.gamestate.GameState;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class LobbyGameState extends GameState {

    @Setter @Getter
    private static int lobbyCountdown = 60;
    @Setter @Getter
    private static int forceStartTime = 10;

    public LobbyGameState() {
        super("Lobby", lobbyCountdown);
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
                    plugin.getMessager().send(p, CoreSystem.getInstance().getTranslationManager().get(
                            "game.countdown.lobby.idling",
                            CoreSystem.getInstance().getCorePlayer(p)
                    ).replace("%seconds%", Long.toString(second)));
                    p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
                }
            default:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.setLevel(second);
                }
        }
    }

    public static boolean forceStart() {
        if (GamePlugin.getGamePlugin().getGameStateManager().getCountdownCounter() > forceStartTime) {
            return GamePlugin.getGamePlugin().getGameStateManager().updateCountdownCounter(forceStartTime);
        } else {
            return false;
        }
    }

}
