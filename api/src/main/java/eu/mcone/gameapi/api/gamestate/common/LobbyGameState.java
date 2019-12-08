package eu.mcone.gameapi.api.gamestate.common;

import eu.mcone.gameapi.api.event.gamestate.GameStateEndEvent;
import eu.mcone.gameapi.api.gamestate.GameState;
import org.bukkit.Bukkit;

public class LobbyGameState extends GameState {

    private final int minPlayers;

    public LobbyGameState(int minPlayers) {
        super("Lobby");
        this.minPlayers = minPlayers;
    }

    @Override
    public void onEnd(GameStateEndEvent e) {
        if (Bukkit.getOnlinePlayers().size() < minPlayers) {
            e.setCancelled(true);
            e.setCancelReason("Um die Runde zu starten werden mindestens "+minPlayers+" Spieler benötigt!");
            e.setNewCountdown(20);
        }
    }

}
