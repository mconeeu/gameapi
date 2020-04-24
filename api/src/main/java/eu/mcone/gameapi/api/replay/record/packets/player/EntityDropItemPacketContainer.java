package eu.mcone.gameapi.api.replay.record.packets.player;

import eu.mcone.coresystem.api.bukkit.npc.capture.packets.EntityAction;
import eu.mcone.coresystem.api.bukkit.npc.capture.packets.templates.EntityItemPacketTContainer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

@Getter
public class EntityDropItemPacketContainer extends EntityItemPacketTContainer {

    private final int entityID;

    private final double x;
    private final double y;
    private final double z;
    private final String worldName;

    public EntityDropItemPacketContainer(int entityID, ItemStack item, Location location) {
        super(EntityAction.DROP_ITEM, item);
        this.entityID = entityID;
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.worldName = location.getWorld().getName();
    }

    public Location calculateLocation() {
        return new Location(Bukkit.getWorld(this.worldName), this.x, this.y, this.z, 0, 0);
    }
}
