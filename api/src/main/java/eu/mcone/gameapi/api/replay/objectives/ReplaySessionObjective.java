package eu.mcone.gameapi.api.replay.objectives;

import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjective;
import eu.mcone.gameapi.api.replay.session.ReplaySession;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReplaySessionObjective extends CoreSidebarObjective {

    private final ReplaySession replaySession;

    public ReplaySessionObjective(final ReplaySession replaySession) {
        super("REPLAY_SESSION");
        this.replaySession = replaySession;
    }

    @Override
    protected void onRegister(CorePlayer corePlayer) {
        Gamemode gamemode = (replaySession.getInfo().getGamemode() != null && !replaySession.getInfo().getGamemode().isEmpty() ? Gamemode.valueOf(replaySession.getInfo().getGamemode()) : null);

        setDisplayName("§e§lReplay Server");
        setScore(15, "");
        setScore(14, "§8» §7ReplayID:");
        setScore(13, "§f   " + replaySession.getID());
        setScore(12, "§8» §7Spielmodus:");
        setScore(11, "§f   " + (gamemode != null ? gamemode.getColor() + gamemode.getName() : "§c✘"));
        setScore(10, "§8» §7Vom:");
        setScore(9, "§f   " + new SimpleDateFormat("dd.MM.yyyy").format(new Date(replaySession.getInfo().getStopped() * 1000)));
        setScore(8, "§8» §7Gestartet:");
        setScore(7, "§f   " + convertDate(replaySession.getInfo().getStarted()));
        setScore(6, "§8» §7Gestopt:");
        setScore(5, "§f   " + convertDate(replaySession.getInfo().getStopped()));
        setScore(4, "§8» §7Teams:");
        setScore(3, "§f   " + (replaySession.getInfo().getTeams() > 0 ? replaySession.getInfo().getTeams() : "§c✘"));
        setScore(2, "§8» §7Gewinner:");
        setScore(1, "§f   " + (!replaySession.getInfo().getWinnerTeam().isEmpty() ? replaySession.getInfo().getWinnerTeam() : "§c✘"));
    }

    @Override
    protected void onReload(CorePlayer corePlayer) {
    }

    private String convertDate(final long timestamp) {
        return new SimpleDateFormat("HH:mm").format(new Date(timestamp * 1000));
    }
}
