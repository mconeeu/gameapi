package eu.mcone.gameapi.api.scoreboard;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjective;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjectiveEntry;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.gamestate.GameState;
import eu.mcone.gameapi.api.gamestate.common.InGameState;

public class InGameObjective extends CoreSidebarObjective {

    private boolean useTime = false;

    public InGameObjective() {
        super("Ingame");
    }

    @Override
    public void setScore(int score, String content) {
        setScore(score + (useTime ? (score == 0 ? 6 : 5) : (score == 0 ? 3 : 2)), content);
    }

    @Override
    public void onRegister(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
        onReload(player, entry);
        entry.setScore(2, "");
        entry.setScore(1, "§f§lMCONE.EU");
    }

    @Override
    public void onReload(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
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
