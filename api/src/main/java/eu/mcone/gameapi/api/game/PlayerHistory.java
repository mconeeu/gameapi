package eu.mcone.gameapi.api.game;

import eu.mcone.gameapi.api.stats.StatsHistory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PlayerHistory {

    private UUID uuid;
    @Setter
    private long joined;
    @Setter
    private long leaved;
    @Setter
    private String team;
    @Setter
    private boolean won;
    @Setter
    private StatsHistory statsHistory;

    public PlayerHistory(Player player) {
        this.uuid = player.getUniqueId();
        this.statsHistory = new StatsHistory();
        this.joined = System.currentTimeMillis() / 1000;
    }

    @BsonIgnore
    public long getPlayTime() {
        return leaved - joined;
    }
}
