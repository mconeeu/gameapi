package eu.mcone.gameapi.backpack;

import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class BackpackItemCategory {

    private Category category;
    private Set<BackpackItem> items;

    public BackpackItemCategory(Category category, Set<BackpackItem> items) {
        this.category = category;
        this.items = items;
    }

    @Override
    public boolean equals(Object obj) {
        BackpackItemCategory other = (BackpackItemCategory) obj;

        return other.category.equals(category)
                && other.items.equals(items);
    }

    @Override
    public String toString() {
        return "BackpackItemCategory{" +
                "category=" + category +
                ", items=" + items +
                '}';
    }
}
