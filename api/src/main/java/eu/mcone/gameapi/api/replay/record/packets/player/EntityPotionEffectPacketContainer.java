package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketTyp;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketContainer;
import lombok.Getter;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

@Getter
public class EntityPotionEffectPacketContainer extends PacketContainer {

    private String typ;
    private int level;

    public EntityPotionEffectPacketContainer(final Potion potion) {
        super(PacketTyp.ENTITY, EntityAction.POTION_EFFECT);
        this.typ = potion.getType().name();
        this.level = potion.getLevel();
    }

    public Potion getPotion() {
        return new Potion(PotionType.valueOf(typ), level);
    }
}
