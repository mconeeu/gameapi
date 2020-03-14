package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import lombok.Getter;

@Getter
public class EntityStatsChangePacketWrapper extends PacketWrapper {

    private final int kills;
    private final int deaths;
    private final int goals;

    public EntityStatsChangePacketWrapper(final int kills, final int deaths, final int goals) {
        super(PacketType.ENTITY, EntityAction.STATS);

        this.kills = kills;
        this.deaths = deaths;
        this.goals = goals;
    }
}
