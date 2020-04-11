package eu.mcone.gameapi.api.scoreboard;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjective;

public class InGameObjective extends CoreSidebarObjective {

    public InGameObjective(String name) {
        super(name);
    }

    @Override
    protected void onRegister(CorePlayer player) {

    }

    @Override
    protected void onReload(CorePlayer player) {

    }

    @Override
    public void setScore(int score, String content) {
        super.setScore(score + 2, content);
    }
}
