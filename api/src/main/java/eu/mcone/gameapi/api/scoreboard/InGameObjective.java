/*
 * Copyright (c) 2017 - 2021 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api.scoreboard;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjectiveEntry;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.gamestate.GameState;
import eu.mcone.gameapi.api.gamestate.common.InGameState;

public class InGameObjective extends GameObjective {

    public InGameObjective() {
        super("Ingame");
    }

    @Override
    protected void onGameObjectiveRegister(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
        CoreSidebarObjectiveEntry inGameEntry = new CoreSidebarObjectiveEntry();
        onInGameRegister(player, inGameEntry);

        if (inGameEntry.getTitle() != null) {
            entry.setTitle(inGameEntry.getTitle());
        }

        boolean useTime = setTimeScores(entry);
        if (useTime) {
            entry.setScore(2, "");
            entry.setScore(1, "§8» §7Zeit:");
        }

        inGameEntry.getScores().forEach((score, value) -> entry.setScore(score + (useTime ? 3 : 0), value));
    }

    protected void onInGameRegister(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {}

    @Override
    protected void onGameObjectiveReload(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
        CoreSidebarObjectiveEntry inGameEntry = new CoreSidebarObjectiveEntry();
        onInGameReload(player, inGameEntry);

        if (inGameEntry.getTitle() != null) {
            entry.setTitle(inGameEntry.getTitle());
        }

        boolean useTime = setTimeScores(entry);
        inGameEntry.getScores().forEach((score, value) -> entry.setScore(score + (useTime ? 3 : 0), value));
    }

    protected void onInGameReload(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {}

    private boolean setTimeScores(CoreSidebarObjectiveEntry entry) {
        GameState state;
        boolean useTime = GamePlugin.getGamePlugin().hasModule(Module.GAME_STATE_MANAGER)
                && (state = GamePlugin.getGamePlugin().getGameStateManager().getRunning()) instanceof InGameState
                && state.hasTimeout()
                && GamePlugin.getGamePlugin().getGameStateManager().isCountdownRunning();

        if (useTime) {
            entry.setScore(0, "   §f§l" + format(GamePlugin.getGamePlugin().getGameStateManager().getTimeoutCounter()));
        }

        return useTime;
    }

    public static String format(double time) {
        String formatted;

        double min = time / 60;
        if (min < 1) {
            formatted = (int) time + " §fSekunden";
        } else {
            formatted = (int) min + (min < 2 ? " §fMinute" : " §fMinuten");
        }

        return formatted;
    }

}
