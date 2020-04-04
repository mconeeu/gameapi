package eu.mcone.gameapi.gamestate;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.GamePlugin;
import eu.mcone.gameapi.api.event.gamestate.*;
import eu.mcone.gameapi.api.gamestate.GameState;
import eu.mcone.gameapi.listener.gamestate.GameStateListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;

public class GameStateManager implements eu.mcone.gameapi.api.gamestate.GameStateManager {

    private final GameAPIPlugin system;
    private final CorePlugin gamePlugin;
    private final LinkedList<GameState> pipeline;
    @Getter
    private GameState running = null;

    private BukkitTask countdownTask, timeoutTask;
    @Getter
    private long timeoutCounter;
    @Getter
    private int countdownCounter;

    public GameStateManager(GameAPIPlugin system, CorePlugin plugin) {
        this.system = system;
        this.gamePlugin = plugin;
        this.pipeline = new LinkedList<>();

        system.sendConsoleMessage("§aLoading GameStateManager...");
        system.registerEvents(new GameStateListener());
    }

    @Override
    public GameStateManager addGameState(GameState gameState) {
        pipeline.add(gameState);
        return this;
    }

    @Override
    public GameStateManager addGameStateFirst(GameState gameState) {
        pipeline.addFirst(gameState);
        return this;
    }

    @Override
    public void startGame() {
        if (running == null) {
            startGameState(pipeline.getFirst());
        } else {
            throw new IllegalStateException("GameStateManager already started the game! Pipeline running currently with GameState " + running.getName());
        }
    }

    @Override
    public GameState getNextGameState() {
        if (running != null) {
            int i = 0;
            for (GameState gameState : pipeline) {
                if (gameState.equals(running)) {
                    if (++i <= pipeline.size() - 1) {
                        return pipeline.get(i);
                    } else {
                        return null;
                    }
                }
                i++;
            }

            return null;
        } else {
            return pipeline.getFirst();
        }
    }

    @Override
    public boolean setGameState(GameState gameState, boolean force) {
        //Checks if no GameState is running or force is set to set GameState even if one other is currently running
        if (running == null || force) {
            if (running != null) {
                //If one GameState is running and a countdown runs, cancel it
                if (countdownTask != null) {
                    countdownTask.cancel();
                    countdownTask = null;
                } else if (timeoutTask != null) {
                    //If one GameState is running and a timout runs, cancel it
                    timeoutTask.cancel();
                    timeoutTask = null;
                }

                stopAndStartNextGameState(running, "Manual set new GameState: " + gameState.getName());
            } else {
                //Start GameState (start Timeout if present)
                startGameState(gameState);
            }

            return true;
        } else {
            return false;
        }
    }


    @Override
    public boolean isTimeoutRunning() {
        return timeoutTask != null;
    }

    @Override
    public boolean cancelTimeout() {
        if (countdownTask != null) {
            countdownTask.cancel();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean startTimeout() {
        return startTimeout(false);
    }

    @Override
    public boolean startTimeout(boolean force) {
        if (running.hasTimeout()) {
            return startTimeout(force, running.getTimeout());
        } else {
            throw new IllegalArgumentException("Cannot start Timeout. No timeout set! Please use GameStateManager#startTimeout(boolean, int)");
        }
    }

    @Override
    public boolean startTimeout(boolean force, long countdown) {
        if (countdownTask == null) {
            if (timeoutTask == null || force) {
                system.sendConsoleMessage("§fManually starting Timeout in Gamestate " + running.getName() + " with " + countdown + " seconds!");
                timeoutGameState(running, countdown);
                return true;
            } else {
                return false;
            }
        } else {
            throw new IllegalStateException("Cannot start TimeoutTask of GameState " + running + "! Timeout already running.");
        }
    }


    @Override
    public boolean isCountdownRunning() {
        return countdownTask != null;
    }

    @Override
    public boolean cancelCountdown() {
        if (countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean startCountdown() {
        return startCountdown(false);
    }

    @Override
    public boolean startCountdown(boolean force) {
        if (running.hasCountdown()) {
            return startCountdown(force, running.getCountdown());
        } else {
            throw new IllegalArgumentException("Cannot start Countdown. No countdown set! Please use GameStateManager#startCountdown(boolean, int)");
        }
    }

    @Override
    public boolean startCountdown(boolean force, int countdown) {
        if (countdownTask == null) {
            if (timeoutTask == null || force) {
                system.sendConsoleMessage("§fManually starting Countdown in Gamestate " + running.getName() + " with " + countdown + " seconds!");
                initializeCountdown(running, countdown);
                return true;
            } else {
                return false;
            }
        } else {
            throw new IllegalStateException("Cannot start CountdownTask of GameState " + running + "! Countdown already running.");
        }
    }

    private void startGameState(GameState gameState) {
        GameStateStartEvent startEvent = new GameStateStartEvent(this, running, gameState);
        gameState.onStart(startEvent);
        gamePlugin.getServer().getPluginManager().callEvent(startEvent);

        //Mark the started GameState as currently running
        running = gameState;

        //Start GameState if not cancelled
        if (!startEvent.isCancelled()) {
            //Start Timeout if present, otherwise do idle
            if (gameState.hasTimeout()) {
                initializeTimeout(gameState);
            } else {
                system.sendConsoleMessage("§fStarted Gamestate " + gameState.getName() + " without timeout! Idling...");
            }
        } else {
            //If GameState Start cancelled and gamestate was marked as skipped, start next Gamestate, otherwise do idle
            if (!startEvent.isSkipGameState()) {
                GameState next = getNextGameState();

                if (next != null) {
                    system.sendConsoleMessage("§fSkipped Gamestate " + gameState.getName() + "! Starting new GameState " + next.getName());
                    startGameState(next);
                } else {
                    system.sendConsoleMessage("§fSkipped Gamestate " + gameState.getName() + "! No new GameState in pipeline: The Game has ended...");
                }
            } else {
                system.sendConsoleMessage("§fCancelled Start of Gamestate " + gameState.getName() + "! Idling...");
            }
        }
    }

    private void initializeTimeout(GameState gameState) {
        GameStateTimeoutStartEvent timeoutStartEvent = new GameStateTimeoutStartEvent(this, gameState);
        gameState.onTimeoutStart(timeoutStartEvent);
        gamePlugin.getServer().getPluginManager().callEvent(timeoutStartEvent);

        //If Timeout was not cancelled start timeout, otherwise to idle
        if (!timeoutStartEvent.isCancelled()) {
            long timeout = timeoutStartEvent.getTimeout() > 0 ? timeoutStartEvent.getTimeout() : gameState.getTimeout();

            system.sendConsoleMessage("§fStarting Gamestate " + gameState.getName() + " with timout of " + timeout + " seconds!");
            timeoutGameState(gameState, timeout);
        } else {
            system.sendConsoleMessage("§fStarted Gamestate " + gameState.getName() + " without timeout! Idling...");
        }
    }

    private void timeoutGameState(GameState gameState, long timeout) {
        this.timeoutCounter = timeout;

        this.timeoutTask = Bukkit.getScheduler().runTaskTimer(system, () -> {
            //If timeoutTask reached the end call timeout event
            if (this.timeoutCounter < 1) {
                this.timeoutTask.cancel();
                this.timeoutTask = null;
                this.timeoutCounter = 0;

                GameStateTimeoutEndEvent timeoutEndEvent = new GameStateTimeoutEndEvent(this, gameState);
                gameState.onTimeoutEnd(timeoutEndEvent);
                gamePlugin.getServer().getPluginManager().callEvent(timeoutEndEvent);

                //If Timeout end was not cancelled go on, otherwise restart timeout
                if (!timeoutEndEvent.isCancelled()) {
                    //If gamestate has countdown, start countdown, otherwise stop current and start new gamestate
                    if (gameState.hasCountdown()) {
                        initializeCountdown(gameState);
                    } else {
                        stopAndStartNextGameState(gameState, "Reached Timeout End, No Countdown set");
                    }
                } else {
                    long newTimeout = timeoutEndEvent.getNewTimeout() > 0 ? timeoutEndEvent.getNewTimeout() : gameState.getTimeout();

                    system.sendConsoleMessage("§fRestarting Timeout for Gamestate " + gameState.getName() + " with new timeout of " + newTimeout + " seconds");
                    timeoutGameState(gameState, newTimeout);
                }
            }

            this.timeoutCounter--;
        }, 0, 20);
    }

    private void initializeCountdown(GameState gameState) {
        initializeCountdown(gameState, -1);
    }

    private void initializeCountdown(GameState gameState, int countdown) {
        GameStateCountdownStartEvent countdownStartEvent = new GameStateCountdownStartEvent(this, gameState, countdown);
        gameState.onCountdownStart(countdownStartEvent);
        gamePlugin.getServer().getPluginManager().callEvent(countdownStartEvent);

        //If countdown was not cancelled, start countdown, otherwise stop current and start new gamestate
        if (!countdownStartEvent.isCancelled()) {
            countdown = countdownStartEvent.getCountdown() > 0 ? countdownStartEvent.getCountdown() : gameState.getCountdown();

            system.sendConsoleMessage("§fStarting Countdown in Gamestate " + gameState.getName() + " with " + countdown + " seconds!");
            countdownGameState(gameState, countdown);
        } else {
            stopAndStartNextGameState(gameState, "Reached Timeout End, Countdown was cancelled");
        }
    }

    private void countdownGameState(GameState gameState, int countdown) {
        this.countdownCounter = countdown;

        this.countdownTask = gamePlugin.getServer().getScheduler().runTaskTimer(system, () -> {
            //If countdownTask reached the end call timeout event
            if (this.countdownCounter < 1) {
                this.countdownTask.cancel();
                this.countdownTask = null;
                this.countdownCounter = 0;

                GameStateCountdownEndEvent countdownEndEvent = new GameStateCountdownEndEvent(this, gameState);
                gameState.onCountdownEnd(countdownEndEvent);
                gamePlugin.getServer().getPluginManager().callEvent(countdownEndEvent);

                //If Countdown end was not cancelled stop current and start new gamestate, otherwise restart countdown
                if (!countdownEndEvent.isCancelled()) {
                    stopAndStartNextGameState(gameState, "Reached Countdown End");
                } else {
                    int newCountdown = countdownEndEvent.getNewCountdown() > 0 ? countdownEndEvent.getNewCountdown() : gameState.getCountdown();

                    system.sendConsoleMessage("§fRestarting Countdown for Gamestate " + gameState.getName() + " with new countdown of " + newCountdown + " seconds");
                    countdownGameState(gameState, newCountdown);
                }
            }

            gameState.onCountdownSecond(GamePlugin.getGamePlugin(), countdownCounter);
            this.countdownCounter--;
        }, 0, 20);
    }

    private void stopAndStartNextGameState(GameState gameState, String stopReason) {
        GameState next = getNextGameState();

        GameStateStopEvent stopEvent = new GameStateStopEvent(this, gameState, next);
        gameState.onStop(stopEvent);
        gamePlugin.getServer().getPluginManager().callEvent(stopEvent);

        //If next GameState exists, start it, otherwise do idle
        if (next != null) {
            system.sendConsoleMessage("§fStopping Gamestate " + gameState.getName() + " (" + stopReason + "). Starting new Gamemode " + next.getName());
            startGameState(next);
        } else {
            system.sendConsoleMessage("§fStopping Gamestate " + gameState.getName() + " (" + stopReason + "). No new GameState in pipeline: The Game has ended...");
        }
    }

}
