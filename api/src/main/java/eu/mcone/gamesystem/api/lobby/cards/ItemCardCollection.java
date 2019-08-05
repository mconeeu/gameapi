package eu.mcone.gamesystem.api.lobby.cards;

import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

@Getter
@Setter
@AllArgsConstructor
public class ItemCardCollection {

    private String gamemode;
    private HashSet<ItemCard> itemCards;

    public ItemCardCollection(){}

    public ItemCardCollection(Gamemode gamemode, HashSet<ItemCard> itemCards) {
        this.gamemode = gamemode.toString();
        this.itemCards = itemCards;
    }
}
