package eu.mcone.gameapi.api.replay.objectives;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjective;
import eu.mcone.gameapi.api.GamePlugin;

public class ReplayObjective extends CoreSidebarObjective {

    public ReplayObjective() {
        super("REPLAY_SERVER");
    }

    @Override
    protected void onRegister(CorePlayer corePlayer) {
        setDisplayName("§e§lReplay Server");
        setScore(5, "");
        setScore(4, "§8» §7Replays:");
        setScore(3, "§f§l" + GamePlugin.getPlugin().getSessionManager().getReplaySessions().size());
        setScore(2, "");
        setScore(1, "§f§lMCONE.EU ");
    }

    @Override
    protected void onReload(CorePlayer corePlayer) {}
}
