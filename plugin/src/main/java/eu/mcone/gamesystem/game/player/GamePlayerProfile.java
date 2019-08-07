package eu.mcone.gamesystem.game.player;

import eu.mcone.coresystem.api.bukkit.player.profile.GameProfile;
import eu.mcone.gamesystem.api.GameTemplate;
import eu.mcone.gamesystem.api.enums.Item;
import eu.mcone.gamesystem.api.game.achivements.SolvedAchievement;
import eu.mcone.gamesystem.api.game.manager.kit.sorting.CustomKit;
import eu.mcone.gamesystem.api.lobby.cards.ItemCard;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.*;

@NoArgsConstructor
@Getter
@Setter
public class GamePlayerProfile extends GameProfile {

    private List<Integer> items = new ArrayList<>();
    private Map<String, Boolean> itemCards = new HashMap<>();
    private Map<String, CustomKit> customKits = new HashMap<>();
    private HashSet<SolvedAchievement> solvedAchievements = new HashSet<>();

    private transient List<Item> itemList = new ArrayList<>();
    private transient Map<ItemCard, Boolean> itemCardMap = new HashMap<>();

    GamePlayerProfile(final Player p, final List<Item> lobbyItems, final Map<ItemCard, Boolean> itemCards, final Map<String, CustomKit> customKits, final HashSet<SolvedAchievement> solvedAchievements) {
        super(p);

        for (Item item : lobbyItems) {
            this.items.add(item.getId());
        }

        for (Map.Entry<ItemCard, Boolean> entry : itemCards.entrySet()) {
            this.itemCards.put(entry.getKey().getName(), entry.getValue());
        }

        this.customKits = customKits;
        this.solvedAchievements = solvedAchievements;
    }

    @Override
    public void doSetData(Player p) {
        for (int id : items) {
            itemList.add(Item.getItemByID(id));
        }

        for (Map.Entry<String, Boolean> entry : itemCards.entrySet()) {
            ItemCard itemCard = GameTemplate.getInstance().getItemCardManager().getItemCard(entry.getKey());
            if (itemCard != null) {
                itemCardMap.put(itemCard, entry.getValue());
            } else {
                GameTemplate.getInstance().getMessager().send(p, "§cDas item mit dem Namen §f" + entry.getKey() + " §ckonnte nicht gefunden werden!");
            }
        }
    }
}
