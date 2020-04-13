package eu.mcone.gameapi.api.scoreboard;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjective;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.gamestate.common.InGameState;

public class EndGameObjective extends CoreSidebarObjective {

    private boolean useTime = false;

    public EndGameObjective(String name) {
        super(name);
    }

    @Override
    protected void onRegister(CorePlayer corePlayer) {
        if (GamePlugin.getGamePlugin().hasModule(Module.GAME_STATE_MANAGER)) {
            if (GamePlugin.getGamePlugin().getGameStateManager().getOptions().contains(Option.USE_SEASON_TIMEOUT)) {
                useTime = true;
                super.setScore(5, "");
                super.setScore(4, "§8» §7Zeit:");
                super.setScore(3, "   §f§l" + InGameObjective.format(getTimeoutSeconds()));
            }
        }

        super.setScore(2, "");
        super.setScore(1, "§f§lMCONE.EU");
    }

    private double getTimeoutSeconds() {
        if (GamePlugin.getGamePlugin().getGameStateManager().getRunning() instanceof InGameState) {
            return ((InGameState) GamePlugin.getGamePlugin().getGameStateManager().getRunning()).getTimeoutSeconds();
        } else if (GamePlugin.getGamePlugin().getGameStateManager().getPrevious() instanceof InGameState) {
            return ((InGameState) GamePlugin.getGamePlugin().getGameStateManager().getPrevious()).getTimeoutSeconds();
        }

        return 0;
    }

    @Override
    protected void onReload(CorePlayer corePlayer) {
    }

    @Override
    public void setScore(int score, String content) {
        super.setScore(score + (useTime ? (score == 0 ? 6 : 5) : (score == 0 ? 3 : 2)), content);
    }
}
