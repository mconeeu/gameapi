package eu.mcone.gameapi.api.backpack;

import eu.mcone.gameapi.api.GamePlugin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BackpackSimpleItem {

    private String category;
    private int id;

    public BackpackSimpleItem(String category, BackpackItem item) {
        this.category = category;
        this.id = item.getId();
    }

    public BackpackItem getBackpackItem() {
        return GamePlugin.getGamePlugin().getBackpackManager().getBackpackItem(category, id);
    }
}
