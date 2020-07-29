package eu.mcone.gameapi.api.scoreboard;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjective;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjectiveEntry;
import lombok.NoArgsConstructor;

public abstract class LobbyObjective extends CoreSidebarObjective {

    public LobbyObjective() {
        super("Lobby");
    }

    LobbyObjective(String name) {
        super(name);
    }

    @Override
    protected void onRegister(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
        CoreSidebarObjectiveEntry lobbyEntry = new CoreSidebarObjectiveEntry();
        onLobbyRegister(player, lobbyEntry);

        entry.setTitle(lobbyEntry.getTitle());

        lobbyEntry.getScores().forEach((score, value) -> entry.setScore((score == 0 ? score + 3 : score + 2), value));

        entry.setScore(2, "");
        entry.setScore(1, "§f§lMCONE.EU");
    }

    protected abstract void onLobbyRegister(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry);

    @Override
    protected void onReload(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
        CoreSidebarObjectiveEntry lobbyEntry = new CoreSidebarObjectiveEntry();
        onLobbyReload(player, lobbyEntry);

        entry.setTitle(lobbyEntry.getTitle());

        lobbyEntry.getScores().forEach((score, value) -> {
            entry.setScore((score == 0 ? score + 3 : score + 2), value);
        });
    }

    protected abstract void onLobbyReload(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry);
}
