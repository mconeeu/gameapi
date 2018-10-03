package eu.mcone.gamesystem.game.manager.team;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.npc.NpcData;
import eu.mcone.coresystem.api.bukkit.npc.NpcManager;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.coresystem.api.core.exception.CoreException;
import eu.mcone.coresystem.api.core.player.SkinInfo;
import eu.mcone.gamesystem.api.GameSystemAPI;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.player.GamePlayer;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TeamStage implements eu.mcone.gamesystem.api.game.manager.team.TeamStage {

    private Logger log;
    private Map<Location, GamePlayer> teamStage;
    private Map<Location, GamePlayer> teamStageCached;
    private NpcManager npcManager;
    private CoreWorld coreWorld;

    TeamStage() {
        log = GameSystemAPI.getInstance().getLogger();
        teamStage = new HashMap<>();
        teamStageCached = new HashMap<>();
        npcManager = CoreSystem.getInstance().getNpcManager();

        coreWorld = CoreSystem.getInstance().getWorldManager().getWorld(GameTemplate.getInstance().getGameSettings().getConfig().getString("lobbyWorld"));
    }

    public void setPlayer(GamePlayer gamePlayer) {
        for (int i = 1; i <= GameTemplate.getInstance().getPlayerPreTeam(); i++) {
            Location loc = coreWorld.getLocation(gamePlayer.getTeam().getString() + ".stage." + i).bukkit(coreWorld);
            if (!(teamStageCached.containsKey(loc))) {
                teamStage.put(loc, gamePlayer);
                teamStageCached.put(loc, gamePlayer);

                npcManager.addLocalNPC
                        (
                                gamePlayer.getTeam() + "-" + gamePlayer.getName(),
                                gamePlayer.getTeam().getColor() + gamePlayer.getName(),
                                gamePlayer.getName(),
                                NpcData.SkinKind.PLAYER,
                                loc
                        );

                log.info("Add player `" + gamePlayer.getName() + "` to TeamStage `" + gamePlayer.getTeam() + "`");
                break;
            }
        }
    }

    public void removePlayer(GamePlayer gamePlayer) {
        if (teamStageCached.containsValue(gamePlayer)) {
            for (Map.Entry<Location, GamePlayer> entry : teamStageCached.entrySet()) {
                if (entry.getValue().equals(gamePlayer)) {
                    teamStage.remove(entry.getKey());
                    npcManager.removeNPC
                            (
                                    npcManager.getNPC(coreWorld, gamePlayer.getTeam() + "-" + gamePlayer.getName())
                            );
                    log.info("Remove `" + gamePlayer.getName() + "` from TeamStage `" + gamePlayer.getTeam() + "`");
                }
            }
        }
    }
}
