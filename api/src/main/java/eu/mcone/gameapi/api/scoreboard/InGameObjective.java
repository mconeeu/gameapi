package eu.mcone.gameapi.api.scoreboard;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjective;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.gamestate.common.InGameState;

public class InGameObjective extends CoreSidebarObjective {

    private boolean useTime = false;

    public InGameObjective(String name) {
        super(name);
    }

    @Override
    protected void onRegister(CorePlayer player) {
        onReload(player);
        super.setScore(2, "");
        super.setScore(1, "§f§lMCONE.EU");
    }

    @Override
    protected void onReload(CorePlayer player) {
        if (GamePlugin.getGamePlugin().hasModule(Module.GAME_STATE_MANAGER)) {
            if (GamePlugin.getGamePlugin().getGameStateManager().getOptions().contains(Option.USE_SEASON_TIMEOUT)) {
                useTime = true;
                super.setScore(5, "");
                super.setScore(4, "§8» §7Zeit:");
                super.setScore(3, "   §f§l" + format(((InGameState) GamePlugin.getGamePlugin().getGameStateManager().getRunning()).getTimeoutSeconds()));
            }
        }
    }

    @Override
    public void setScore(int score, String content) {
        super.setScore(score + (useTime ? (score == 0 ? 6 : 5) : (score == 0 ? 3 : 2)), content);
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
