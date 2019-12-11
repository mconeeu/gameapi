package eu.mcone.gameapi.backpack;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.CoreSystem;
import eu.mcone.coresystem.api.bukkit.config.typeadapter.bson.ItemStackCodecProvider;
import eu.mcone.coresystem.api.bukkit.gamemode.Gamemode;
import eu.mcone.coresystem.api.bukkit.inventory.category.CategoryInventory;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.Option;
import eu.mcone.gameapi.api.backpack.BackpackInventoryListener;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.backpack.BackpackManager;
import eu.mcone.gameapi.api.backpack.Category;
import eu.mcone.gameapi.api.backpack.defaults.DefaultCategory;
import eu.mcone.gameapi.api.backpack.defaults.DefaultItem;
import eu.mcone.gameapi.api.backpack.handler.OutfitHandler;
import eu.mcone.gameapi.api.backpack.handler.PetHandler;
import eu.mcone.gameapi.api.backpack.handler.TrailHandler;
import eu.mcone.gameapi.api.player.GameAPIPlayer;
import eu.mcone.gameapi.backpack.defaults.*;
import eu.mcone.gameapi.command.ItemCMD;
import eu.mcone.gameapi.inventory.backpack.BackpackInventory;
import eu.mcone.gameapi.inventory.backpack.BackpackSellInventory;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class GameBackpackManager implements BackpackManager {

    private static final MongoCollection<BackpackItemCategory> BACKPACK_ITEM_GAMEMODE_COLLECTION = CoreSystem.getInstance().getMongoDB().withCodecRegistry(
            fromRegistries(getDefaultCodecRegistry(), fromProviders(new ItemStackCodecProvider(), PojoCodecProvider.builder().automatic(true).build()))
    ).getCollection("gamesystem_backpack_items", BackpackItemCategory.class);

    private final Option[] gameOptions;
    private final CorePlugin gamePlugin, system;
    private final List<BackpackItemCategory> backpackItems;
    private final Map<String, BackpackInventoryListener> clickListeners;

    private final List<String> additionalCategories;

    @Getter @Setter
    private boolean useRankBoots = false;

    public GameBackpackManager(GameAPIPlugin system, GamePlugin<?> gamePlugin, Option... gameOptions) {
        system.registerCommands(new ItemCMD(this));

        this.gameOptions = gameOptions;
        this.gamePlugin = gamePlugin;
        this.system = system;
        this.backpackItems = new ArrayList<>();
        this.clickListeners = new HashMap<>();
        this.additionalCategories = new ArrayList<>();

        system.sendConsoleMessage("§aLoading BackpackManager...");

        if (Arrays.asList(gameOptions).contains(Option.BACKPACK_MANAGER_USE_RANK_BOOTS)) {
            useRankBoots = true;
            system.registerEvents();

            for (Player p : Bukkit.getOnlinePlayers()) {
                setRankBoots(p);
            }
        }

        BackpackInventory.setPlugin(gamePlugin);
        BackpackSellInventory.setPlugin(gamePlugin);
        BackpackInventoryListener.setPlugin(gamePlugin);
        DefaultItem.setManager(this);

        reload();
    }

    @Override
    public void registerCategory(Category category, Set<BackpackItem> items, BackpackInventoryListener listener) throws IllegalArgumentException {
        registerCategory(category, items);

        if (listener != null) {
            clickListeners.put(category.getName(), listener);
        }
    }

    public void registerCategory(Category category, Set<BackpackItem> items) throws IllegalArgumentException {
        if (!isDefaultCategory(category.getName())) {
            if (!categoryExists(category.getName()) || getItemCategory(category.getName()).getCategory().getGamemode().equals(gamePlugin.getGamemode())) {
               if (idsUnique(items)) {
                   BackpackItemCategory previous = getItemCategory(category.getName());
                   BackpackItemCategory registered = new BackpackItemCategory(category, items);

                   if (previous == null || !previous.equals(registered)) {
                       BACKPACK_ITEM_GAMEMODE_COLLECTION.replaceOne(
                               eq("category.name", category.getName()),
                               registered,
                               ReplaceOptions.createReplaceOptions(new UpdateOptions().upsert(true))
                       );

                       if (previous != null) {
                           backpackItems.remove(previous);
                       }
                       backpackItems.add(registered);
                   }
               } else {
                   throw new IllegalArgumentException("Cannot register Category "+category.getName()+". At least one id was registered twice! That would cause an error!");
               }
            } else {
                throw new IllegalArgumentException("Cannot register Category "+category.getName()+". Category with that name already exists in Gamemode "+getItemCategory(category.getName()).getCategory().getGamemode().getName()+"!");
            }
        } else {
            throw new IllegalArgumentException("Cannot register Category "+category.getName()+". Category has the built in GameSystem default categories!");
        }
    }

    private void registerDefaultCategories(DefaultCategory... categories) {
        int sort = 10;
        for (DefaultCategory defaultCategory : categories) {
            Category category = new Category(
                    defaultCategory.name(), defaultCategory.getDescription(), true, true, sort++, Gamemode.UNDEFINED, defaultCategory.getItem()
            );
            Set<BackpackItem> items = new HashSet<>();

            for (DefaultItem item : DefaultItem.values()) {
                if (item.getCategory().equals(defaultCategory)) {
                    items.add(new BackpackItem(
                            item.getId(),
                            item.getName(),
                            item.getLevel(),
                            item.getItemStack(),
                            true,
                            true,
                            !item.equals(DefaultItem.HEAD_SECRET_STRIPCLUB),
                            item.getBuyemeralds(),
                            item.getSellemeralds()
                    ));
                }
            }

            if (idsUnique(items)) {
                backpackItems.add(new BackpackItemCategory(category, items));
                clickListeners.put(category.getName(), getDefaultInventoryListener(defaultCategory));
            } else {
                throw new IllegalArgumentException("Cannot register Category "+category.getName()+". At least one id was registered twice! That would cause an error!");
            }
        }
    }

    private void registerDefaultCategories(Option... gameOptions) {
        List<Option> optionList = Arrays.asList(gameOptions);

        if (optionList.contains(Option.BACKPACK_MANAGER_REGISTER_ALL_DEFAULT_CATEGORIES)) {
            system.sendConsoleMessage("§2Loading all DefaultCategories");
            registerDefaultCategories(DefaultCategory.values());
        } else {
            List<DefaultCategory> categories = new ArrayList<>();

            if (optionList.contains(Option.BACKPACK_MANAGER_REGISTER_PET_CATEGORY)) {
                system.sendConsoleMessage("§2Loading DefaultCategory ANIMAL");
                categories.add(DefaultCategory.PET);
            }
            if (optionList.contains(Option.BACKPACK_MANAGER_REGISTER_GADGET_CATEGORY)) {
                system.sendConsoleMessage("§2Loading DefaultCategory GADGET");
                categories.add(DefaultCategory.GADGET);
            }
            if (optionList.contains(Option.BACKPACK_MANAGER_REGISTER_HAT_CATEGORY)) {
                system.sendConsoleMessage("§2Loading DefaultCategory HAT");
                categories.add(DefaultCategory.HAT);
            }
            if (optionList.contains(Option.BACKPACK_MANAGER_REGISTER_OUTFIT_CATEGORY)) {
                system.sendConsoleMessage("§2Loading DefaultCategory OUTFIT");
                categories.add(DefaultCategory.OUTFIT);
            }
            if (optionList.contains(Option.BACKPACK_MANAGER_REGISTER_TRAIL_CATEGORY)) {
                system.sendConsoleMessage("§2Loading DefaultCategory TRAIL");
                categories.add(DefaultCategory.TRAIL);
            }
            if (optionList.contains(Option.BACKPACK_MANAGER_REGISTER_EXCLUSIVE_CATEGORY)) {
                system.sendConsoleMessage("§2Loading DefaultCategory ARMOR");
                categories.add(DefaultCategory.EXCLUSIVE);
            }

            registerDefaultCategories(categories.toArray(new DefaultCategory[0]));
        }
    }

    @Override
    public void loadAdditionalCategories(String... names) {
        for (String category : names) {
            if (!categoryExists(category)) {
                BackpackItemCategory entry = BACKPACK_ITEM_GAMEMODE_COLLECTION.find(eq("category.name", category)).first();

                if (entry != null) {
                    system.sendConsoleMessage("§2Loading additional Backpack Category " + category);

                    this.additionalCategories.add(category);
                    this.backpackItems.add(entry);
                } else {
                    throw new IllegalArgumentException("Could not load additional Category "+category+". Category does not exist in database!");
                }
            } else {
                throw new IllegalArgumentException("Could not load additional Category "+category+". Category is already registered!");
            }
        }
    }

    @Override
    public void reload() {
        backpackItems.clear();

        registerDefaultCategories(gameOptions);

        //Load all Categories that are marked for additional loading
        List<Bson> searchQuery = new ArrayList<>();
        for (String cat : additionalCategories) {
            searchQuery.add(eq("category.name", cat));
        }
        //Load all categories that belongs to this gamemode automatically
        searchQuery.add(eq("category.gamemode", gamePlugin.getGamemode().toString()));

        for (BackpackItemCategory item : BACKPACK_ITEM_GAMEMODE_COLLECTION.find(combine(searchQuery.toArray(new Bson[0])))) {
            system.sendConsoleMessage("§2Loading Backpack Category "+item.getCategory().getName());
            this.backpackItems.add(item);
        }
    }

    @Override
    public BackpackItem getBackpackItem(String category, int id) {
        for (BackpackItem item : getCategoryItems(category)) {
            if (item.getId() == id) {
                return item;
            }
        }

        if (categoryExists(category)) {
            throw new IllegalArgumentException("Could not get item with id "+id+" from category "+category+". Category exists but item not!");
        } else {
            return null;
        }
    }

    @Override
    public void openBackpackInventory(String name, Player p) throws IllegalArgumentException {
        Category category = getItemCategory(name).getCategory();

        if (category != null) {
            new BackpackInventory(p, category);
        } else {
            throw new IllegalArgumentException("Could not open BackpackInventory for Category "+name+". Category does not exist!");
        }
    }

    @Override
    public void openBackpackSellInventory(String name, Player p) throws IllegalArgumentException {
        Category category = getItemCategory(name).getCategory();

        if (category != null) {
            new BackpackSellInventory(p, category);
        } else {
            throw new IllegalArgumentException("Could not open BackpackSellInventory for Category "+name+". Category does not exist!");
        }
    }

    public void onCategoryInventoryCreate(Category category, GameAPIPlayer<?> player, CategoryInventory inventory, Player p) {
        if (clickListeners.containsKey(category.getName())) {
            clickListeners.get(category.getName()).setBackpackItems(inventory, category, getCategoryItems(category.getName()), player, p);
        }
    }

    @Override
    public Map<Category, Set<BackpackItem>> getBackpackItems(String... names) {
        List<String> categories = Arrays.asList(names);
        Map<Category, Set<BackpackItem>> result = new HashMap<>();

        for (BackpackItemCategory item : backpackItems) {
            if (categories.size() == 0 || categories.contains(item.getCategory().getName())) {
                result.put(item.getCategory(), item.getItems());
            }
        }

        return result;
    }

    public BackpackItemCategory getItemCategory(String category) {
        for (BackpackItemCategory itemCategory : backpackItems) {
            if (itemCategory.getCategory().getName().equals(category)) {
                return itemCategory;
            }
        }

        return null;
    }

    @Override
    public boolean categoryExists(String category) {
        return getItemCategory(category) != null;
    }

    @Override
    public boolean itemExists(BackpackItem item) {
        for (BackpackItemCategory backpackItem : backpackItems) {
            for (BackpackItem backpackItemItem : backpackItem.getItems()) {
                if (backpackItemItem.equals(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void stop() {

    }

    public Set<Category> getCategories() {
        Set<Category> categories = new HashSet<>();

        for (BackpackItemCategory backpackItemCategory : backpackItems) {
            categories.add(backpackItemCategory.getCategory());
        }

        return categories;
    }

    public Set<BackpackItem> getCategoryItems(String category) {
        BackpackItemCategory itemCategory = getItemCategory(category);
        return itemCategory != null ? itemCategory.getItems() : Collections.emptySet();
    }

    private boolean isDefaultCategory(String name) {
        for (DefaultCategory category : DefaultCategory.values()) {
            if (category.name().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public Category getCategoryByItemDisplayName(String displayname) {
        for (BackpackItemCategory backpackItem : backpackItems) {
            if (backpackItem.getCategory().getItemStack().getItemMeta().getDisplayName().equals(displayname)) {
                return backpackItem.getCategory();
            }
        }
        return null;
    }

    private boolean idsUnique(Set<BackpackItem> items) {
        Set<Integer> ids = new HashSet<>();

        for (BackpackItem item : items) {
            if (!ids.contains(item.getId())) {
                ids.add(item.getId());
            } else {
                return false;
            }
        }

        return true;
    }

    @Override
    public PetHandler getPetHandler() {
        return PetListener.getHandler();
    }

    @Override
    public TrailHandler getTrailHandler() {
        return TrailListener.getHandler();
    }

    @Override
    public OutfitHandler getOutfitHandler() {
        return OutfitListener.getHandler();
    }

    @Override
    public void setRankBoots(Player p) {
        CorePlayer cp = CoreSystem.getInstance().getCorePlayer(p);

        RankBoots boots = RankBoots.getBootsByGroup(cp.getMainGroup());
        if (boots != null) {
            p.getInventory().setBoots(boots.getItem());
        }
    }

    private BackpackInventoryListener getDefaultInventoryListener(DefaultCategory category) {
        switch (category) {
            case HAT: return new HatListener();
            case PET: return new PetListener();
            case TRAIL: return new TrailListener();
            case GADGET: return new GadgetListener();
            case OUTFIT: return new OutfitListener();
            case EXCLUSIVE: return new ExclusiveListener();
            default: return null;
        }
    }

}
