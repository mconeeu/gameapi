package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketTyp;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketContainer;
import lombok.Getter;

@Getter
public class EntityStatsChangePacketContainer extends PacketContainer {

    private final int kills;
    private final int deaths;
    private final int goals;

    public EntityStatsChangePacketContainer(final int kills, final int deaths, final int goals) {
        super(PacketTyp.ENTITY, EntityAction.STATS);

        this.kills = kills;
        this.deaths = deaths;
        this.goals = goals;
    }
}
