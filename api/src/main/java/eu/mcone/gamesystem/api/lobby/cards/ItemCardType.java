package eu.mcone.gamesystem.api.lobby.cards;

import lombok.Getter;

@Getter
public enum ItemCardType {

    //TODO: Add Coins SUPPORT / D.L 29.9.2019
    KIT("KIT", "§c§lKit"),
    ITEM("ITEM", "§7§lItem"),
    COINS("COINS", "§a§lCoins");

    private String name;
    private String displayName;

    ItemCardType(final String name, final String displayName) {
        this.name = name;
        this.displayName = displayName;
    }
}
