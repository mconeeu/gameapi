package eu.mcone.gameapi.api.replay.record.packets.world;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.*;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@BsonDiscriminator
public class WorldPlayEffectPacketWrapper extends PacketWrapper {

    private final String effectName;
    private final int i;
    private final Location location;

    public WorldPlayEffectPacketWrapper(final Effect effect, final int i, final Location location) {
        super(PacketType.WORLD, WorldAction.PLAY_EFFECT);
        this.effectName = effect.getName();
        this.i = i;
        this.location = location;
    }

    @BsonCreator
    public WorldPlayEffectPacketWrapper(@BsonProperty("effectName") final String effectName, @BsonProperty("i") final int i, @BsonProperty("location") final Location location) {
        super(PacketType.WORLD, WorldAction.PLAY_EFFECT);
        this.effectName = effectName;
        this.i = i;
        this.location = location;
    }

    @BsonIgnore
    public Effect getEffect() {
        return Effect.valueOf(effectName);
    }

    @BsonIgnore
    public void spawnEffect() {
        location.getWorld().playEffect(location, Effect.getByName(effectName), i);
    }

    @BsonIgnore
    public void spawnEffect(final Player player) {
        player.playEffect(location, Effect.getByName(effectName), i);
    }
}
