package eu.mcone.gameapi.gamestate;

import eu.mcone.coresystem.api.bukkit.CorePlugin;
import eu.mcone.gameapi.GameAPIPlugin;
import eu.mcone.gameapi.api.event.gamestate.GameStateCountdownEvent;
import eu.mcone.gameapi.api.event.gamestate.GameStateEndEvent;
import eu.mcone.gameapi.api.event.gamestate.GameStateStartEvent;
import eu.mcone.gameapi.api.event.gamestate.GameStateTimeoutEvent;
import eu.mcone.gameapi.api.gamestate.GameState;
import eu.mcone.gameapi.listener.GameStateListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;

public class GameStateManager implements eu.mcone.gameapi.api.gamestate.GameStateManager {

    private final GameAPIPlugin system;
    private final CorePlugin gamePlugin;
    private final LinkedList<GameState> pipeline;
    @Getter
    private GameState running = null;

    private BukkitTask countdownTimer, timeoutTimer;
    private int countdown;

    public GameStateManager(GameAPIPlugin system, CorePlugin gamePlugin) {
        system.registerEvents(new GameStateListener());
        this.system = system;
        this.gamePlugin = gamePlugin;
        this.pipeline = new LinkedList<>();
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
            makeGameState(pipeline.getFirst());
        } else {
            throw new IllegalStateException("GameStateManager already started the game! Pipeline running currently with GameState "+running.getName());
        }
    }

    @Override
    public GameState getNextGameState() {
        if (running != null) {
            int i = 0;
            for (GameState gameState : pipeline) {
                if (gameState.equals(running)) {
                    if (++i <= pipeline.size()-1) {
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
                if (countdownTimer != null) {
                    countdownTimer.cancel();
                    countdownTimer = null;
                } else if (timeoutTimer != null) {
                    //If one GameState is running and a timout runs, cancel it
                    timeoutTimer.cancel();
                    timeoutTimer = null;
                }
            }

            //No previous running GameState: Start GameState without countdown
            //With previous running GameState: Start GameState countdown, End previous GameState on Countdown finish, start new GameState
            makeGameState(gameState);
            return true;
        } else {
            return false;
        }
    }



    private void makeGameState(GameState gameState) {
        GameStateCountdownEvent event = new GameStateCountdownEvent(this, running, gameState);
        Bukkit.getPluginManager().callEvent(event);

        //If there is already a GameState running
        if (running != null) {
            //Start countdown if countdown was set and was not marked as skipped
            if (event.getCountdown() > 0 && !event.isSkipped()) {
                //Countdown to new GameState, stop old Gamestate after Countdown finishes, start new GameState
                countdownGameState(gameState, gameState.getCountdown());
            } else {
                //If no countdown is present or is skipped, stop previous running GameState and start new one
                endGameState(running, gameState);
            }
        } else {
            //Start new GameState instantly without countdown, as its the first GameState of the Game
            startGameState(gameState);
        }
    }

    private void countdownGameState(GameState gameState, int countdown) {
        this.countdown = countdown;

        system.sendConsoleMessage("§fStarting Countdown for Gamestate "+gameState.getName()+" with "+countdown+" seconds!");
        countdownTimer = Bukkit.getScheduler().runTaskTimerAsynchronously(system, () -> {
            //Calling GameState.class overwriteable method
            gameState.onCountdownSecond(gamePlugin, this.countdown);

            //If countdown reaches 0, stop countdown, stop previous GameState, start new GameState
            if (this.countdown == 0) {
                endGameState(running, gameState);
                countdownTimer.cancel();
                countdownTimer = null;
            }
            this.countdown--;
        }, 0L, 20L);
    }

    private void startGameState(GameState gameState) {
        GameStateStartEvent event = new GameStateStartEvent(this, running, gameState);
        Bukkit.getPluginManager().callEvent(event);

        //Mark the started GameState as currently running
        running = gameState;

        if (event.getTimeout() > 0 && !event.isSkipped()) {
            system.sendConsoleMessage("§fStarted Gamestate "+gameState.getName());
            timoutGameState(gameState, gameState.getTimeout());
        } else {
            system.sendConsoleMessage("§fStarted Gamestate "+gameState.getName()+" without timeout!");
        }
    }

    private void timoutGameState(GameState gameState, long timeout) {
        system.sendConsoleMessage("§fStarted Timeout for Gamestate "+gameState.getName()+" with "+gameState.getTimeout()+" seconds!");
        timeoutTimer = Bukkit.getScheduler().runTaskLaterAsynchronously(system, () -> {
            continueGameState(gameState);

            timeoutTimer.cancel();
            timeoutTimer = null;
        }, timeout * 20);
    }

    private void continueGameState(GameState gameState) {
        GameState next = getNextGameState();
        GameStateTimeoutEvent event = new GameStateTimeoutEvent(this, running, next);

        if (event.isCancelled()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                gamePlugin.getMessager().send(p, "Ende verschoben"+ (event.getCancelReason() != null ? ": "+event.getCancelReason() : ""));
            }

            //If new timeout was set, start the timeout, come back here to the first if
            if (event.getNewTimeout() > 0) {
                system.sendConsoleMessage("§fRestarting Timeout for Gamestate "+gameState.getName()+" after End was cancelled with: "+ event.getCancelReason());
                timoutGameState(gameState, event.getNewTimeout());
            } else {
                //No new timeout set, pipeline will be stopped. must be manually started via setGameState()
                system.sendConsoleMessage("§fCancelling Start for Gamestate "+gameState.getName()+" without restarting timeout (newTimeout:"+event.getNewTimeout()+")");
            }
        } else {
            //If a next GameState exists
            if (next != null) {
                //Start the countdown of the next GameState, stop current GameState, start next Gamestate
                makeGameState(next);
            } else {
                //Stop current GameState without starting a new one
                endGameState(gameState, null);
            }
        }
    }

    private void endGameState(GameState gameState, GameState next) {
        GameStateEndEvent event = new GameStateEndEvent(this, running, next);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                gamePlugin.getMessager().send(p, "Countdown abgebrochen"+ (event.getCancelReason() != null ? ": "+event.getCancelReason() : ""));
            }

            //If new countdown was set, start new countdown, com back here to the first if
            if (event.getNewCountdown() > 0 && running != null) {
                system.sendConsoleMessage("§fRestarting Countdown for Gamestate "+gameState.getName()+" after Start was cancelled with: "+ event.getCancelReason());
                countdownGameState(gameState, event.getNewCountdown());
            } else {
                //No new countdown set, pipeline will be stopped. must be manually started via setGameState()
                system.sendConsoleMessage("§fCancelling End for Gamestate "+gameState.getName()+" without restarting countdown (newCountdown:"+event.getNewCountdown()+", previousGameState:"+running+")");
            }
        } else {
            if (next != null) {
                //Starting new GameState, after countdown of it has finished, and old GameState ended in this method
                system.sendConsoleMessage("§fEnded Gamestate " + gameState.getName() + "! Starting next: " + next.getName());
                startGameState(next);
            } else {
                //Do nothing here, as no new GameStates exists
                system.sendConsoleMessage("§fEnded Gamestate " + gameState.getName() + " as last in pipeline! Pipeline ended...");
            }
        }
    }

}
