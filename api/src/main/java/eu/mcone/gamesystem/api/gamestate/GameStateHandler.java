/*
 * Copyright (c) 2017 - 2018 Rufus Maiwald and the MC ONE Minecraftnetwork. All rights reserved
 * You are not allowed to decompile the code
 */

package eu.mcone.gamesystem.api.gamestate;

public interface GameStateHandler {

    /**
     * German description:
     *
     * 1. Registriere eine neu GameState Klasse auf dir der GameStateHandler zugreift, wenn der für die Klasse zugewiesener GameState aktiv ist.
     *    dazu gibst du einfach die GameState Klasse an und den GameStateType inform der GameStateID.
     *      GameStateID(ERROR (0), LOBBY (1), INGAME (2), END (3), RESTART (4), MAINTENANCE (5))
     *      GameState new DeineKlasse
     *
     *      Beispiel:
     *          GameSystem.getInstance.getGameStateHandler.registerGameStateClass(new DeineKlasse, GameStateID.LOBBY);
     *
     * 2. Nachdem du alle GameState Klassen im GameStateHandler registriert hast kannst du verschiedene Dinge machen.
     *    2.1 SetGameState(GameStateID DeinGameState) hiermit kannst du den Aktuellen GameState mit hilfe der GameStateID setzen.
     *        Aber Vorsicht!!! Wernn du zb denn Aktuellen GameState auf LOBBY setzt willst aber keine Klasse dafür definiert hast auf der der GameStateHandler zugreifen kann kommt es zu einem Fehler.
     *
     *    2.2 getCurrentState() Diese Methode gibt dir den Aktuellen GameState zurück. Wenn aber momentan kein GameState aktiv ist wird eine NullPointerException geworfen,
     *        dies kannst du ganzeinfach beheben in dem du die in Punkt 1 und 2.1 gezeigten schritte befolgst.
     *
     *    2.3 getCurrentStateID() diese Methode gibt denn Aktuellen GameState inform eines Enum's der GameStateID zurück. Wenn aber momentan kein GameState aktiv ist wird eine NullPointerException geworfen,
     *        dies kannst du ganzeinfach beheben in dem du die in Punkt 1 und 2.1 gezeigten schritte befolgst.
     *
     *    2.4 hasGameState(GameStateID.DeinGameState) diese Methode gibt true oder false zurück wernn der Angegebenen GameState aktiv(true) oder nicht aktiv(false) ist.
     *
     * English description:
     *
     * 1. Register a new GameState class on you the GameStateHandler accesses, if the assigned to the class GameState is active.
     *    just add the GameState class and the GameStateType inform of the GameStateID.
     *    GameStateID (ERROR (0), LOBBY (1), INGAME (2), END (3), RESTART (4), MAINTENANCE (5))
     *    GameState new your class
     *
     *    Example:
     *       GameSystem.getInstance.getGameStateHandler.registerGameStateClass (new yourclass, GameStateID.LOBBY);
     *
     * 2. After registering all GameState classes in GameStateHandler you can do different things.
     *    2.1 SetGameState (GameStateID DeinGameState) allows you to set the current GameState using the GameStateID.
     *        But beware!!! If, for example, you want to set the current GameState to LOBBY but you have not defined a class for the GameStateHandler to access, you will get an error.
     *
     *    2.2 getCurrentState () This method returns the current GameState. But if no GameState is currently active, a NullPointerException is thrown,
     *        You can easily fix this by following the steps in steps 1 and 2.1.
     *
     *    2.3 getCurrentStateID () This method returns the current GameState inform of an Enum's GameStateID. But if no GameState is currently active, a NullPointerException is thrown,
     *        You can easily fix this by following the steps in steps 1 and 2.1.
     *
     *    2.4 hasGameState (GameStateID.YourGameState) This method returns true or false if the specified GameState is active (true) or not active (false).
     *
     */


    /**
     * Join a new GameState class in the GameStateHandler.
     * @param gameState Passes the relevant class
     * @param type      Passes the GameState type
     */
    void registerGameStateClass(GameState gameState, GameStateID type);

    /**
     * Set a new GameState
     * @param id Specifies the GameState type with the GameStateID.
     */
    void setGameState(GameStateID id);

    /**
     * Check if the GameState agrees with the current and returns true or false.
     *
     * @param type
     * @return true or false
     */
    boolean hasGameState(GameStateID type);

    /**
     * Returns the current GameState
     * @return current GameState
     */
    GameState getCurrentState();

    /**
     * Returns the current GameState in the form of a GamesStateID
     * @return current GameStateID
     */
    GameStateID getCurrentStateID();

}
