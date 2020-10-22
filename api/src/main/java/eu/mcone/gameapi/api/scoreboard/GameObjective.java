package eu.mcone.gameapi.api.scoreboard;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjective;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjectiveEntry;
import eu.mcone.gameapi.api.GamePlugin;

public class GameObjective extends CoreSidebarObjective {

    public GameObjective() {
        super("Game");
    }

    public GameObjective(String name) {
        super(name);
    }

    @Override
    protected void onRegister(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
        CoreSidebarObjectiveEntry lobbyEntry = new CoreSidebarObjectiveEntry();
        onGameObjectiveRegister(player, lobbyEntry);

        entry.setTitle(lobbyEntry.getTitle() != null ? lobbyEntry.getTitle() : "§7§l⚔ "+ GamePlugin.getGamePlugin().getPluginColor()+"§l§n"+GamePlugin.getGamePlugin().getGameName()+"§7§l ⚔");
        lobbyEntry.getScores().forEach((score, value) -> entry.setScore(score + 2, value));

        entry.setScore(1, "");
        entry.setScore(0, "§f§lMCONE.EU");
    }

    protected void onGameObjectiveRegister(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {}

    @Override
    protected void onReload(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
        CoreSidebarObjectiveEntry lobbyEntry = new CoreSidebarObjectiveEntry();
        onGameObjectiveReload(player, lobbyEntry);

        if (lobbyEntry.getTitle() != null) {
            entry.setTitle(lobbyEntry.getTitle());
        }

        lobbyEntry.getScores().forEach((score, value) -> entry.setScore(score + 2, value));
    }

    protected void onGameObjectiveReload(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {}
}
