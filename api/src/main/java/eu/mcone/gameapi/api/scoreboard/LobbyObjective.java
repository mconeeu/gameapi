package eu.mcone.gameapi.api.scoreboard;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjective;

public class LobbyObjective extends CoreSidebarObjective {

    public LobbyObjective() {
        super("Lobby");
    }

    @Override
    protected void onRegister(CorePlayer player) {
        super.setScore(2, "");
        super.setScore(1, "§f§lMCONE.EU");
    }

    @Override
    protected void onReload(CorePlayer player) {
    }

    @Override
    public void setScore(int score, String content) {
        super.setScore((score == 0 ? score + 3 : score + 2) , content);
    }
}
