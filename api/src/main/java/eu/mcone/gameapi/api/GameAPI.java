/*
 * Copyright (c) 2017 - 2019 Dominik Lippl, Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gameapi.api;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.coresystem.api.bukkit.player.CorePlayer;
import eu.mcone.gameapi.api.achievement.AchievementManager;
import eu.mcone.gameapi.api.backpack.BackpackManager;
import eu.mcone.gameapi.api.damage.DamageLogger;
import eu.mcone.gameapi.api.gamestate.GameStateManager;
import eu.mcone.gameapi.api.kit.KitManager;
import eu.mcone.gameapi.api.map.MapManager;
import eu.mcone.gameapi.api.onepass.OnePassManager;
import eu.mcone.gameapi.api.player.GamePlayer;
import eu.mcone.gameapi.api.player.PlayerManager;
import eu.mcone.gameapi.api.replay.session.ReplayManager;
import eu.mcone.gameapi.api.team.TeamManager;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public abstract class GameAPI extends CorePlugin {

    public GameAPI() {
        super(
                "gameapi",
                ChatColor.DARK_GRAY,
                "system.prefix.server",
                "https://2529672f0fcf4f21b41524b81f147ae6@o267551.ingest.sentry.io/5198718?stacktrace.app.packages=eu.mcone.gameapi"
        );
    }

    @Getter
    private static GameAPI instance;

    protected void setInstance(final GameAPI instance) {
        if (instance == null) {
            System.err.println("GameAPI instance cannot be set twice!");
        } else {
            GameAPI.instance = instance;
        }
    }

    public abstract GameStateManager constructGameStateManager(GamePlugin gamePlugin);

    public abstract MapManager constructMapManager();

    public abstract ReplayManager constructReplayManager(Option... options);

    public abstract TeamManager constructTeamManager(GamePlugin gamePlugin, Option[] options);

    public abstract PlayerManager constructPlayerManager(GamePlugin gamePlugin);

    public abstract BackpackManager constructBackpackManager(GamePlugin gamePlugin, Option... options);

    public abstract KitManager constructKitManager(GamePlugin plugin, Option... option);

    public abstract AchievementManager constructAchievementManager(GamePlugin gamePlugin, Option... options);

    public abstract DamageLogger constructDamageLogger();

    public abstract OnePassManager constructOnePassManager();

    public abstract GamePlayer getGamePlayer(CorePlayer cp);

    public abstract GamePlayer getGamePlayer(Player p);

    public abstract GamePlayer getGamePlayer(UUID uuid);

    public abstract GamePlayer getGamePlayer(String name);

    public abstract List<GamePlayer> getOnlineGamePlayers();

}
