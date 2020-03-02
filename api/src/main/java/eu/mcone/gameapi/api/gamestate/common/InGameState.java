package eu.mcone.gameapi.api.gamestate.common;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.event.gamestate.GameStateStartEvent;
import eu.mcone.gameapi.api.event.gamestate.GameStateStopEvent;
import eu.mcone.gameapi.api.gamestate.GameState;

public class InGameState extends GameState {

    public InGameState(String name) {
        super(name);
    }

    @Override
    public void onCountdownSecond(CorePlugin plugin, int second) {}

    @Override
    public void onStart(GameStateStartEvent event) {
        if (GamePlugin.getGamePlugin().hasModule(Module.REPLAY)) {
            GamePlugin.getGamePlugin().getReplaySession().recordSession();
            System.out.println("replay on");
        }
    }

    @Override
    public void onStop(GameStateStopEvent event) {
        if (GamePlugin.getGamePlugin().hasModule(Module.REPLAY)) {
            GamePlugin.getGamePlugin().getReplaySession().saveSession();
            System.out.println("replay off");
        }
    }
}
