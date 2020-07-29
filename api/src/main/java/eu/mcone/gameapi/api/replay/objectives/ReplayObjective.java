package eu.mcone.gameapi.api.replay.objectives;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjective;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjectiveEntry;
import eu.mcone.gameapi.api.GamePlugin;

public class ReplayObjective extends CoreSidebarObjective {

    public ReplayObjective() {
        super("REPLAY_SERVER");
    }

    @Override
    protected void onRegister(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
        entry.setTitle("§e§lReplay Server");
        entry.setScore(5, "");
        entry.setScore(4, "§8» §7Replays:");
        entry.setScore(3, "§f§l" + GamePlugin.getGamePlugin().getReplayManager().getReplaySize());
        entry.setScore(2, "");
        entry.setScore(1, "§f§lMCONE.EU ");
    }

    @Override
    protected void onReload(CorePlayer corePlayer, CoreSidebarObjectiveEntry coreSidebarObjectiveEntry) {

    }
}
