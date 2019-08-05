package eu.mcone.gamesystem.api.lobby.cards;

import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;

public interface ItemCardManager {

    void loadItemCards();

    void saveItemCards();

    void registerItemCards(ItemCard... itemCards);

    void registerItemCard(ItemCard itemCard);

    boolean existsItemCard(String name);

    boolean existsItemCard(Gamemode gamemode, String name);

    ItemCard getItemCard(final String itemCardName);

    ItemCard getItemCard(final Gamemode gamemode, final String itemCardName);

    ItemCardCollection getItemCardCollection();

    ItemCardCollection getItemCardCollection(Gamemode gamemode);
}
