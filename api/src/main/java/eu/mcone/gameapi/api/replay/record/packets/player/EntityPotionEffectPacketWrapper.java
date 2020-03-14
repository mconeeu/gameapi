package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import lombok.Getter;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

@Getter
public class EntityPotionEffectPacketWrapper extends PacketWrapper {

    private String typ;
    private int level;

    public EntityPotionEffectPacketWrapper(final Potion potion) {
        super(PacketType.ENTITY, EntityAction.POTION_EFFECT);
        this.typ = potion.getType().name();
        this.level = potion.getLevel();
    }

    public Potion getPotion() {
        return new Potion(PotionType.valueOf(typ), level);
    }
}
