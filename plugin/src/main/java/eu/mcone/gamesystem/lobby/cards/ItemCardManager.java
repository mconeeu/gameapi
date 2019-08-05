package eu.mcone.gamesystem.lobby.cards;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.gamesystem.GameSystem;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.lobby.cards.ItemCard;
import eu.mcone.gamesystem.api.lobby.cards.ItemCardCollection;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;

import static com.mongodb.client.model.Filters.eq;

public class ItemCardManager implements eu.mcone.gamesystem.api.lobby.cards.ItemCardManager {

    private Gamemode localGamemode;

    @Getter
    private MongoCollection<ItemCardCollection> itemCardCollectionDB;

    @Getter
    private HashMap<Gamemode, ItemCardCollection> itemCards;

    private HashSet<Gamemode> changeLog;

    public ItemCardManager() {
        localGamemode = GameTemplate.getInstance().getGamemode();

        itemCardCollectionDB = CoreSystem.getInstance().getMongoDB().getCollection(GameSystem.getSystem().getPluginName() + "_itemcards", ItemCardCollection.class);

        itemCards = new HashMap<>();
        changeLog = new HashSet<>();

        loadItemCards();
    }

    public void loadItemCards() {
        for (ItemCardCollection collection : itemCardCollectionDB.find()) {
            itemCards.put(Gamemode.valueOf(collection.getGamemode()), collection);
        }
    }

    public void saveItemCards() {
        for (Gamemode gamemode : changeLog) {
            ItemCardCollection itemCardCollection = itemCards.get(gamemode);
            if (itemCardCollection != null) {
                itemCardCollectionDB.replaceOne(eq("gamemode", gamemode.toString()), itemCardCollection, new ReplaceOptions().upsert(true));
            }
        }
    }

    public void registerItemCards(ItemCard... itemCards) {
        for (ItemCard itemCard : itemCards) {
            registerItemCard(itemCard);
        }
    }

    public void registerItemCard(ItemCard itemCard) {
        if (itemCards.containsKey(localGamemode)) {
            itemCards.get(localGamemode).getItemCards().add(itemCard);
        } else {
            itemCards.put(localGamemode, new ItemCardCollection(localGamemode, new HashSet<ItemCard>() {{
                add(itemCard);
            }}));
        }

        addChange(localGamemode);
    }

    public boolean existsItemCard(String name) {
        return existsItemCard(localGamemode, name);
    }

    public boolean existsItemCard(Gamemode gamemode, String name) {
        if (itemCards.containsKey(gamemode)) {
            for (ItemCard itemCard : itemCards.get(gamemode).getItemCards()) {
                if (itemCard.getName().equalsIgnoreCase(name)) {
                    return false;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public ItemCard getItemCard(final String itemCardName) {
        return getItemCard(localGamemode, itemCardName);
    }

    public ItemCard getItemCard(final Gamemode gamemode, final String itemCardName) {
        if (itemCards.containsKey(gamemode)) {
            for (ItemCard itemCard : itemCards.get(gamemode).getItemCards()) {
                if (itemCard.getName().equalsIgnoreCase(itemCardName)) {
                    return itemCard;
                }
            }
        } else {
            return null;
        }

        return null;
    }

    public ItemCardCollection getItemCardCollection() {
        return getItemCardCollection(localGamemode);
    }

    public ItemCardCollection getItemCardCollection(Gamemode gamemode) {
        return itemCards.get(gamemode);
    }

    private void addChange(Gamemode gamemode) {
        changeLog.add(gamemode);
    }
}
