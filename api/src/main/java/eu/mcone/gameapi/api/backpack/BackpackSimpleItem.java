package eu.mcone.gameapi.api.backpack;

import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BackpackSimpleItem {

    private DefaultCategory category;
    private int id;

    public BackpackSimpleItem(DefaultCategory category, BackpackItem item) {
        this.category = category;
        this.id = item.getId();
    }

    public BackpackItem getBackpackItem() {
        return GamePlugin.getGamePlugin().getBackpackManager().getBackpackItem(category.name(), id);
    }
}
