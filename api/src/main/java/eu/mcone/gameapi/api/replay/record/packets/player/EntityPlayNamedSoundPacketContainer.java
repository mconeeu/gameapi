package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.templates.EntityLocationTContainer;
import lombok.Getter;

@Getter
public class EntityPlayNamedSoundPacketContainer extends EntityLocationTContainer {

    private String id;
    private float volume;

    public EntityPlayNamedSoundPacketContainer(String id, float volume, int x, int y, int z) {
        super(eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketTyp.ENTITY, EntityAction.PLAY_SOUND, x, y, z, 0, 0, "");
        this.id = id;
        this.volume = volume;
    }
}
