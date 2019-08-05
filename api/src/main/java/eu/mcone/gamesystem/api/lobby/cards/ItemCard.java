package eu.mcone.gamesystem.api.lobby.cards;

import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.lobby.api.enums.Category;
import eu.mcone.lobby.api.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ItemCard {

    private transient Gamemode gamemode;
    private transient ItemCardType itemCardType;
    private transient Category category;
    private transient Level level;

    private String name;
    private String gamemodeName;
    private String itemCardTypeName;
    private String categoryName;
    private String levelName;
    private ItemCardBuilder itemCardBuilder;

    public ItemCard() {}

    public ItemCard(final String name, final ItemCardType itemCardType, final Category category, final Level level, final ItemCardBuilder itemCardBuilder) {
        this.name = name;

        this.gamemode = GameTemplate.getInstance().getGamemode();
        this.gamemodeName = GameTemplate.getInstance().getGamemode().toString();

        this.itemCardType = itemCardType;
        this.itemCardTypeName = itemCardType.toString();

        this.category = category;
        this.categoryName = category.toString();

        this.level = level;
        this.levelName = level.toString();

        this.itemCardBuilder = itemCardBuilder;
    }

    public boolean hasCategory() {
        return category != null;
    }

    public boolean hasLevel() {
        return level != null;
    }
}
