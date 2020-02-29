package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

@BsonDiscriminator
@Getter
public class EntityPotionEffectPacketWrapper extends PacketWrapper {

    private String typ;
    private int level;

    public EntityPotionEffectPacketWrapper(final Potion potion) {
        super(PacketType.ENTITY, EntityAction.POTION_EFFECT);
        this.typ = potion.getType().name();
        this.level = potion.getLevel();
    }

    @BsonCreator
    public EntityPotionEffectPacketWrapper(@BsonProperty("packetType") PacketType packetType, @BsonProperty("entityAction") EntityAction entityAction, @BsonProperty("typ") final String typ, @BsonProperty("level") final int level) {
        super(packetType, entityAction);
        this.typ = typ;
        this.level = level;
    }

    @BsonIgnore
    public Potion getPotion() {
        return new Potion(PotionType.valueOf(typ), level);
    }
}
