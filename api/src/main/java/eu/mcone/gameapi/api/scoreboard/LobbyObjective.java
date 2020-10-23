package eu.mcone.gameapi.api.scoreboard;

import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.coresystem.api.bukkit.scoreboard.CoreSidebarObjectiveEntry;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Module;
import eu.mcone.gameapi.api.player.GamePlayerState;
import eu.mcone.gameapi.api.player.PlayerManager;

public class LobbyObjective extends GameObjective {

    public LobbyObjective() {
        super("Lobby");
    }

    @Override
    protected void onGameObjectiveRegister(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
        CoreSidebarObjectiveEntry inGameEntry = new CoreSidebarObjectiveEntry();
        onLobbyRegister(player, inGameEntry);

        if (inGameEntry.getTitle() != null) {
            entry.setTitle(inGameEntry.getTitle());
        }

        boolean isPlayerManager = GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER);
        if (isPlayerManager) {
            PlayerManager playerManager = GamePlugin.getGamePlugin().getPlayerManager();

            entry.setScore(2, "");
            entry.setScore(1, "§8» §7Benötigte Spieler:");
            entry.setScore(0, " §f" + playerManager.getPlayers(GamePlayerState.PLAYING).size() + "§7 von §f" + playerManager.getMinPlayers());
        }

        inGameEntry.getScores().forEach((score, value) -> entry.setScore(score + (isPlayerManager ? 3 : 0), value));
    }

    protected void onLobbyRegister(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
    }

    @Override
    protected void onGameObjectiveReload(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
        CoreSidebarObjectiveEntry inGameEntry = new CoreSidebarObjectiveEntry();
        onLobbyReload(player, inGameEntry);

        if (inGameEntry.getTitle() != null) {
            entry.setTitle(inGameEntry.getTitle());
        }

        boolean isPlayerManager = GamePlugin.getGamePlugin().hasModule(Module.PLAYER_MANAGER);
        if (isPlayerManager) {
            PlayerManager playerManager = GamePlugin.getGamePlugin().getPlayerManager();
            entry.setScore(0, " §f" + playerManager.getPlayers(GamePlayerState.PLAYING).size() + "§7 von §f" + playerManager.getMinPlayers());
        }

        inGameEntry.getScores().forEach((score, value) -> entry.setScore(score + (isPlayerManager ? 3 : 0), value));
    }

    protected void onLobbyReload(CorePlayer corePlayer, CoreSidebarObjectiveEntry entry) {
    }

}
