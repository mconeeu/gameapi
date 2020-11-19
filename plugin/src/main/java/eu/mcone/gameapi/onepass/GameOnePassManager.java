package eu.mcone.gameapi.onepass;

import eu.mcone.coresystem.api.bukkit.facades.Sound;
import eu.mcone.gameapi.api.GameAPI;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.backpack.BackpackItem;
import eu.mcone.gameapi.api.onepass.OnePassLevelAward;
import eu.mcone.gameapi.api.onepass.OnePassManager;
import eu.mcone.gameapi.api.onepass.Quests;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.inventory.onepass.OnePassInventory;
import org.bukkit.entity.Player;

public class GameOnePassManager implements OnePassManager {

    @Override
    public void levelChanged(GamePlayer gp, int oldLevel, int newLevel, boolean notify) {
        for (OnePassLevelAward award : OnePassLevelAward.values()) {
            if (award.getLevel() == newLevel && (award.isFreeAward() || gp.isOnePass())) {
                BackpackItem item = GamePlugin.getGamePlugin().getBackpackManager().getBackpackItem(
                        award.getBackpackItemCategory(),
                        award.getBackPackItemId()
                );

                if (newLevel > oldLevel) {
                    if (!gp.hasBackpackItem(award.getBackpackItemCategory(), award.getBackpackItem())) {
                        GameAPI.getInstance().getMessenger().send(gp.bukkit(), "§2Dir wurde das Item §a" + award.getBackpackItem().getName() + "§2 wegen deiner neuen §aOnePass Stufe " + newLevel + " §2hinzugefügt");
                        Sound.done(gp.bukkit());
                        gp.addBackpackItem(
                                award.getBackpackItemCategory(),
                                item
                        );
                    } else {
                        GameAPI.getInstance().getMessenger().send(gp.bukkit(), "§2Du hast die OnePass §aStufe " + newLevel + " §2erreicht");
                    }
                } else if (newLevel < oldLevel) {
                    gp.removeBackpackItem(
                            award.getBackpackItemCategory(),
                            item
                    );
                }
            }
        }
    }


    public void openOnePassInventory(Player player) {
        GamePlayer gamePlayer = GameAPI.getInstance().getGamePlayer(player);
        if (gamePlayer.getOneLevel() >= OnePassManager.MAX_ONE_PASS_LEVEL) {
            new OnePassInventory(player, (OnePassManager.MAX_ONE_PASS_LEVEL - 2) / 8);
        } else {
            new OnePassInventory(player, (GameAPI.getInstance().getGamePlayer(player).getOneLevel() - 1) / 8);
        }
    }

    public int getSecretAward() {
        return Quests.SECRETS.getAwardXp();
    }

}
