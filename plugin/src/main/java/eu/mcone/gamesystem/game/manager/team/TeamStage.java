package eu.mcone.gamesystem.game.manager.team;

import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.world.CoreWorld;
import eu.mcone.coresystem.api.core.exception.CoreException;
import eu.mcone.coresystem.api.core.player.SkinInfo;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.game.player.GamePlayer;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class TeamStage {

    private Map<Location, GamePlayer> teamStages;
    private Map<Location, GamePlayer> teamStageCached;
    private CoreWorld coreWorld;

    TeamStage() {
        teamStages = new HashMap<>();
        teamStageCached = new HashMap<>();

        coreWorld = CoreSystem.getInstance().getWorldManager().getWorld(GameTemplate.getInstance().getGameSettings().getConfig().getString("lobbyWorld"));
    }

    public void setPlayer(GamePlayer gamePlayer) {
        try {
            for (int i = 1; i > GameTemplate.getInstance().getPlayerPreTeam(); i++) {
                Location location = coreWorld.getLocation(gamePlayer.getTeam().getString() + ".stagePlace.1").bukkit(coreWorld);
                if (!(teamStageCached.containsKey(location))) {
                    teamStages.put(location, gamePlayer);
                    teamStageCached.put(location, gamePlayer);

                    SkinInfo skinInfo = CoreSystem.getInstance().getPlayerUtils().getSkinInfo(gamePlayer.getBukkitPlayer().getName(), "player");
                    skinInfo.uploadSkindata();
                    CoreSystem.getInstance().getNpcManager().addLocalNPC(gamePlayer.getTeam().getString() + "-" + gamePlayer.getBukkitPlayer().getName(), gamePlayer.getTeam().getColor() + gamePlayer.getBukkitPlayer().getName(), skinInfo.getName(), location);
                    break;

                } else {
                    location.setY(location.getY() + i - 1);
                }

                /*
                if ((coreWorld.getLocation(gamePlayer.getTeam().getString() + ".stagePlace").getY() + i) != null) {
                    Location location = coreWorld.getLocation(gamePlayer.getTeam().getString() + ".stagePlace." + i).bukkit(coreWorld);
                    if (!teamStages.containsKey(location)) {
                        teamStages.put(location, gamePlayer);
                        oldCached.put(location, gamePlayer);

                        SkinInfo skinInfo = CoreSystem.getInstance().getPlayerUtils().getSkinInfo(gamePlayer.getPlayer().getName());
                        skinInfo.uploadSkindata();
                        CoreSystem.getInstance().getNpcManager().addLocalNPC(gamePlayer.getTeam().getString() + "-" + gamePlayer.getPlayer().getName(), gamePlayer.getTeam().getColor() + gamePlayer.getPlayer().getName(), skinInfo.getName(), location);
                        break;
                    }
                } else {
                    throw new GameSystemException("Error the Location " + gamePlayer.getTeam().getString() + ".stagePlace." + i + " does not exist.");
                }
                */
            }
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    public void removePlayer(GamePlayer gamePlayer) {
        if (teamStageCached.containsValue(gamePlayer)) {
            for (Map.Entry<Location, GamePlayer> entry : teamStageCached.entrySet()) {
                if (entry.getValue().equals(gamePlayer)) {
                    teamStages.remove(entry.getKey());
                    CoreSystem.getInstance().getNpcManager().removeNPC(CoreSystem.getInstance().getNpcManager().getNPC(coreWorld, gamePlayer.getTeam().getString() + "-" + gamePlayer.getBukkitPlayer().getName()));
                }
            }
        }
    }
}
