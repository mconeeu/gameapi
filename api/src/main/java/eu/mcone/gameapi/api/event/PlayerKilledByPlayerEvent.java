package eu.mcone.gameapi.api.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public final class PlayerKilledByPlayerEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    private final Player player, killer;
    private final List<ItemStack> drops;

    @Setter
    private String deathMessage;
    @Setter
    private int newExp, newLevel, newTotalExp, dropExp;
    @Setter
    private boolean keepLevel, keepInventory;

    public PlayerKilledByPlayerEvent(Player player, Player killer, List<ItemStack> drops, int droppedExp, int newExp, int newTotalExp, int newLevel, boolean keepLevel, boolean keepInventory, String deathMessage) {
        this.player = player;
        this.killer = killer;
        this.drops = drops;

        this.deathMessage = deathMessage;
        this.newExp = newExp;
        this.newLevel = newLevel;
        this.newTotalExp = newTotalExp;

        this.dropExp = droppedExp;
        this.keepLevel = keepLevel;
        this.keepInventory = keepInventory;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

}
