package eu.mcone.gameapi.api.stats;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StatsHistory {

    private int kills;
    private int deaths;
    private int goals;

    public void addKills(int kills) {
        this.kills += kills;
    }

    public void addDeaths(int deaths) {
        this.deaths += deaths;
    }

    public void addGoals(int goals) {
        this.goals += goals;
    }

    public void removeKills(int kills) {
        if ((this.kills - kills) >= 0) {
            this.kills -= kills;
        } else {
            this.kills = 0;
        }
    }

    public void removeDeaths(int deaths) {
        if ((this.deaths - deaths) >= 0) {
            this.deaths -= deaths;
        } else {
            this.deaths = 0;
        }
    }

    public void removeGoals(int goals) {
        if ((this.goals - goals) >= 0) {
            this.goals -= goals;
        } else {
            this.goals = 0;
        }
    }

    public double getKD() {
        double kill = kills;
        double death = deaths;

        if (kill == 0 && death == 0) {
            return 0.00D;
        } else if (kill == 0) {
            return 0.00D;
        } else if (death == 0) {
            return kill + .00D;
        } else {
            return kill / death;
        }
    }
}
