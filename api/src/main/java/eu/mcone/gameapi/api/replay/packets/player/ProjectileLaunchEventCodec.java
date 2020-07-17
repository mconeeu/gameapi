package eu.mcone.gameapi.api.replay.packets.player;

import eu.mcone.coresystem.api.bukkit.codec.Codec;
import eu.mcone.coresystem.api.bukkit.npc.entity.EntityProjectile;
import eu.mcone.coresystem.api.bukkit.npc.entity.PlayerNpc;
import lombok.Getter;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@Getter
public class ProjectileLaunchEventCodec extends Codec<ProjectileLaunchEvent, PlayerNpc> {

    private int id;

    public ProjectileLaunchEventCodec() {
        super("Projectile", ProjectileLaunchEvent.class, PlayerNpc.class);
    }

    @Override
    public Object[] decode(Player player, ProjectileLaunchEvent launchEvent) {
        Projectile projectile = launchEvent.getEntity();
        if (projectile instanceof Snowball) {
            Snowball snowball = (Snowball) projectile;

            if (snowball.getShooter() instanceof Player) {
                Player shooter = (Player) snowball.getShooter();
                id = EntityProjectile.SNOWBALL.getId();

                return new Object[]{shooter};
            }
        } else if (projectile instanceof Egg) {
            Egg egg = (Egg) projectile;
            if (egg.getShooter() instanceof Player) {
                Player shooter = (Player) egg.getShooter();
                id = EntityProjectile.EGG.getId();
                return new Object[]{shooter};
            }
        }

        return null;
    }

    @Override
    public void encode(PlayerNpc playerNpc) {
        playerNpc.throwProjectile(getProjectile(), playerNpc.getLocation().getDirection());
    }

    private EntityProjectile getProjectile() {
        for (EntityProjectile projectile : EntityProjectile.values()) {
            if (projectile.getId() == id) {
                return projectile;
            }
        }

        return null;
    }

    @Override
    protected void onWriteObject(ObjectOutputStream out) throws IOException {
        out.writeInt(id);
    }

    @Override
    protected void onReadObject(ObjectInputStream in) throws IOException {
        id = in.readInt();
    }
}
