package eu.mcone.gameapi.api.scoreboard;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjective;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjectiveEntry;

public class LobbyObjective extends CoreSidebarObjective {

    public LobbyObjective() {
        super("Lobby");
    }

    @Override
    public void setScore(int score, String content) {
        super.setScore((score == 0 ? score + 3 : score + 2) , content);
    }

    @Override
    protected void onRegister(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
        super.setScore(2, "");
        super.setScore(1, "§f§lMCONE.EU");
    }

    @Override
    protected void onReload(CorePlayer corePlayer, CoreSidebarObjectiveEntry coreSidebarObjectiveEntry) {
    }
}
