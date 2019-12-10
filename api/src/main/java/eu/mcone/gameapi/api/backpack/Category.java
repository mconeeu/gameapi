package eu.mcone.gameapi.api.backpack;

import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    private String name, description;
    private boolean sellable, showInBackpack;
    private int sort;
    private Gamemode gamemode;
    private ItemStack itemStack;

    @Override
    public boolean equals(Object obj) {
        Category other = (Category) obj;

        return other.name.equals(name)
                && other.description.equals(description)
                && other.sellable == sellable
                && other.showInBackpack == showInBackpack
                && other.gamemode.equals(gamemode)
                && other.itemStack.equals(itemStack);
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", sellable=" + sellable +
                ", showInBackpack=" + showInBackpack +
                ", gamemode=" + gamemode +
                ", itemStack=" + itemStack +
                '}';
    }
}
