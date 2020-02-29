package eu.mcone.gameapi.api.replay.channel;

import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;

public interface ChannelHandler {

    void createRegisterRequest(String replayID, Gamemode gamemmode);

    void createUnregisterRequest();

}
