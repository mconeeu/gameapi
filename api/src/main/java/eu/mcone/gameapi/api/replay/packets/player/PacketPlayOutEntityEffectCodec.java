package eu.mcone.gameapi.api.replay.packets.player;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.util.ReflectionManager;
import eu.mcone.gameapi.api.replay.runner.PlayerRunner;
import net.minecraft.server.v1_8_R3.MobEffect;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEffect;
import org.bukkit.entity.Player;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PacketPlayOutEntityEffectCodec extends Codec<PacketPlayOutEntityEffect, PlayerRunner> {

    private byte typ;
    private byte amplifier;

    public PacketPlayOutEntityEffectCodec() {
        super("PotionSplash", PacketPlayOutEntityEffect.class, PlayerRunner.class);
    }

    @Override
    public Object[] decode(Player player, PacketPlayOutEntityEffect entityEffect) {
        typ = ReflectionManager.getValue(entityEffect, "b", byte.class);
        amplifier = ReflectionManager.getValue(entityEffect, "c", byte.class);

        return new Object[]{player};
    }

    @Override
    public void encode(PlayerRunner runner) {
        for (PotionEffect effect : getPotion().getEffects()) {
            runner.getPlayer().getNpc().addPotionEffect(new MobEffect(typ, effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles()), runner.getWatchers().toArray(new Player[0]));
        }
    }

    public Potion getPotion() {
        return new Potion(PotionType.getByEffect(PotionEffectType.getById(typ)), amplifier);
    }

    @Override
    protected void onWriteObject(ObjectOutputStream out) throws IOException {
        out.write(typ);
        out.write(amplifier);
    }

    @Override
    protected void onReadObject(ObjectInputStream in) throws IOException {
        typ = in.readByte();
        amplifier = in.readByte();
    }
}
