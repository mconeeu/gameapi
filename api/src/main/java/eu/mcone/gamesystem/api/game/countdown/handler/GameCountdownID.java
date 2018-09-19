package eu.mcone.gamesystem.api.game.countdown.handler;

public enum GameCountdownID {
    LOBBY_COUNTDOWN(1),
    RESTART_COUNTDOWN(3),
    SPAWN_COUNTDOWN(2);

    private int ID;

    GameCountdownID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}
