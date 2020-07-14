package eu.mcone.gameapi.api.replay.packets.player;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class EntityDamageByEntityEventCodec extends Codec<EntityDamageByEntityEvent, PlayerNpc> {

    private double x;
    private double y;
    private double z;

    public EntityDamageByEntityEventCodec() {
        super("SHOOT");
    }

    @Override
    public Object[] decode(Player player, EntityDamageByEntityEvent damageByEntityEvent) {
        org.bukkit.entity.Entity entity = damageByEntityEvent.getEntity();
        org.bukkit.entity.Entity projectile = damageByEntityEvent.getDamager();

        if (projectile != null && entity != null) {
            if (entity instanceof Player) {
                Player damaged = (Player) entity;
                if ((projectile instanceof Arrow)) {
                    Arrow arrow = (Arrow) projectile;

                    if (arrow.getShooter() instanceof Player) {
                        Player shooter = (Player) arrow.getShooter();
                        x = damaged.getLocation().getX();
                        y = damaged.getLocation().getX();
                        z = damaged.getLocation().getX();
                        return new Object[]{shooter};
                    }
                }
            }
        }

        return null;
    }

    @Override
    public void encode(PlayerNpc playerNpc) {
        playerNpc.getLocation().setY(1.5);
        playerNpc.getLocation().getWorld().spawnArrow(playerNpc.getLocation(), getVector().subtract(playerNpc.getVector()), (float) 3, (float) 0);
        playerNpc.getLocation().getWorld().playSound(playerNpc.getLocation(), Sound.ARROW_HIT, 1, 1);
    }

    public Vector getVector() {
        return new Vector(x, y, z);
    }

    @Override
    protected void onWriteObject(ObjectOutputStream out) throws IOException {
        out.writeDouble(x);
        out.writeDouble(y);
        out.writeDouble(z);
    }

    @Override
    protected void onReadObject(ObjectInputStream in) throws IOException {
        x = in.readDouble();
        y = in.readDouble();
        z = in.readDouble();
    }
}
