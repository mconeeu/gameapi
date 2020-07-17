package eu.mcone.gameapi.api.scoreboard;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjective;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjectiveEntry;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.gamestate.GameState;
import eu.mcone.gameapi.api.gamestate.common.InGameState;

public abstract class InGameObjective extends LobbyObjective {

    private boolean useTime = false;

    public InGameObjective() {
        super("Ingame");
    }

    @Override
    protected void onLobbyRegister(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
        CoreSidebarObjectiveEntry inGameEntry = new CoreSidebarObjectiveEntry();
        onInGameRegister(player, inGameEntry);

        entry.setTitle(inGameEntry.getTitle());

        inGameEntry.getScores().forEach((score, value) -> entry.setScore(score + (useTime ? (score == 0 ? 4 : 3) : 0), value));

        if (GamePlugin.getGamePlugin().hasModule(Module.GAME_STATE_MANAGER) && GamePlugin.getGamePlugin().getGameStateManager().getRunning() instanceof InGameState) {
            GameState state = GamePlugin.getGamePlugin().getGameStateManager().getRunning();

            if (state.hasTimeout() && GamePlugin.getGamePlugin().getGameStateManager().isCountdownRunning()) {
                useTime = true;
                entry.setScore(5, "");
                entry.setScore(4, "§8» §7Zeit:");
                entry.setScore(3, "   §f§l" + format(GamePlugin.getGamePlugin().getGameStateManager().getTimeoutCounter()));
            }
        }
    }

    protected abstract void onInGameRegister(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry);

    @Override
    protected void onLobbyReload(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
        CoreSidebarObjectiveEntry inGameEntry = new CoreSidebarObjectiveEntry();
        onInGameReload(player, inGameEntry);

        entry.setTitle(inGameEntry.getTitle());

        inGameEntry.getScores().forEach((score, value) -> entry.setScore(score + (useTime ? (score == 0 ? 4 : 3) : 0), value));

        if (GamePlugin.getGamePlugin().hasModule(Module.GAME_STATE_MANAGER) && GamePlugin.getGamePlugin().getGameStateManager().getRunning() instanceof InGameState) {
            GameState state = GamePlugin.getGamePlugin().getGameStateManager().getRunning();

            if (state.hasTimeout() && GamePlugin.getGamePlugin().getGameStateManager().isCountdownRunning()) {
                useTime = true;
                entry.setScore(5, "");
                entry.setScore(4, "§8» §7Zeit:");
                entry.setScore(3, "   §f§l" + format(GamePlugin.getGamePlugin().getGameStateManager().getTimeoutCounter()));
            }
        }
    }

    protected abstract void onInGameReload(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry);

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
