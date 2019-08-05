package eu.mcone.gamesystem.api.lobby.cards;

import lombok.Getter;

@Getter
public enum ItemCardType {

    KIT("KIT", "§c§lKit"),
    ITEM("ITEM", "§7§lItem");

    private String name;
    private String displayName;

    ItemCardType(final String name, final String displayName) {
        this.name = name;
        this.displayName = displayName;
    }
}
