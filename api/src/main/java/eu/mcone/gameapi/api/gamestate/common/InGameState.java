package eu.mcone.gameapi.api.gamestate.common;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Modules;
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
        if (GamePlugin.getPlugin().getModules().contains(Modules.REPLAY)) {
            GamePlugin.getPlugin().getReplaySession().recordSession();
            System.out.println("replay on");
        }
    }

    @Override
    public void onStop(GameStateStopEvent event) {
        if (GamePlugin.getPlugin().getModules().contains(Modules.REPLAY)) {
            GamePlugin.getPlugin().getReplaySession().saveSession();
            System.out.println("replay off");
        }
    }
}
