package eu.mcone.gameapi.api.replay.record.packets.world;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketType;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.PacketWrapper;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.WorldAction;
import lombok.Getter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@Getter
@BsonDiscriminator
public class WorldPlaySoundPacketWrapper extends PacketWrapper {

    private final String sound;
    private final Location location;

    public WorldPlaySoundPacketWrapper(final Sound sound, final Location location) {
        super(PacketType.ENTITY, WorldAction.PLAY_SOUND);
        this.sound = sound.name();
        this.location = location;
    }

    @BsonCreator
    public WorldPlaySoundPacketWrapper(@BsonProperty("sound") final String sound, @BsonProperty("location") final Location location) {
        super(PacketType.ENTITY, WorldAction.PLAY_SOUND);
        this.sound = sound;
        this.location = location;
    }

    @BsonIgnore
    public Sound getSound() {
        return Sound.valueOf(sound);
    }

    @BsonIgnore
    public void playSound() {
        location.getWorld().playSound(location, Sound.valueOf(sound), 1, 1);
    }

    @BsonIgnore
    public void playSound(final Player player) {
        player.playSound(location, Sound.valueOf(sound), 1, 1);
    }
}
