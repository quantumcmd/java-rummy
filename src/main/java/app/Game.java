package app;

import logic.PersistenceManager;
import logic.TurnManager;
import logic.rules.*;
import model.Deck;
import model.GameState;
import model.Player;
import ui.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private final MainMenu mainMenu;
    private boolean isRunning;

    public Game(MainMenu mainMenu){
        this.mainMenu = mainMenu;
        this.isRunning = true;
    }

    public void start(){
        while(isRunning){
            String input = mainMenu.displayMenu();

            switch (input){
                case "0":
                    startSavedGame();
                    break;
                case "1":
                    startStandardRummy();
                    break;
                case "2":
                    startGinRummy();
                    break;
                case "3":
                    startRummikub();
                    break;
                case "4":
                    startRummyArgentino();
                    break;
                case "5":
                    System.out.println("Thanks for playing! Goodbye.");
                    isRunning = false;
                    break;
            }

        }
    }

    private void startSavedGame() {
        System.out.println("Load Game...");

        // List all .dat files in the current directory
        File currentDir = new File(".");
        File[] saveFiles = currentDir.listFiles(f -> f.getName().endsWith(".dat"));

        if(saveFiles == null || saveFiles.length == 0){
            ErrorMessages.noSavedGamesFound();
            return;
        }

        System.out.println("Available Saved Games:");
        for(int i = 0; i < saveFiles.length; i++){
            System.out.println("  " + i + " -> " + saveFiles[i].getName());
        }

        System.out.print("Enter the filename to load (e.g., game1.dat): ");
        String fileName = mainMenu.getScanner().nextLine();
        GameState savedState = PersistenceManager.loadGame(fileName);

        if(savedState == null){
            ErrorMessages.noSavedGamesFound();
            return;
        }

        String gameType = savedState.getGameType();
        TurnManager turnManager = new TurnManager();

        switch(gameType){
            case "STANDARD":
                runGameLoop(new StandardRummy(), savedState, new InGameUI(mainMenu.getScanner()), turnManager);
                break;
            case "GIN":
                runGameLoop(new GinRummy(), savedState, new GinRummyUI(mainMenu.getScanner()), turnManager);
                break;
            case "RUMMIKUB":
                runGameLoop(new Rummikub(), savedState, new RummykubUI(mainMenu.getScanner()), turnManager);
                break;
            case "ARGENTINO":
                runGameLoop(new RummyArgentino(), savedState, new InGameUI(mainMenu.getScanner()), turnManager);
                break;
        }
    }

    private void startStandardRummy() {
        System.out.println("Starting Standard Rummy...");
        String gameType = "STANDARD";
        int minPlayers = 2;
        int maxPlayers = 4;
        GameSetupUI setupUI = new GameSetupUI(mainMenu.getScanner(), minPlayers, maxPlayers);
        List<Player> players = setupUI.setupPlayers();

        Deck deck = new Deck(gameType);
        GameState state = new GameState(players, deck, gameType);
        TurnManager turnManager = new TurnManager();
        StandardRummy standardRummy = new StandardRummy();
        standardRummy.setupMatch(state, turnManager);
        InGameUI inGameUI = new InGameUI(mainMenu.getScanner());

        runGameLoop(standardRummy, state, inGameUI, turnManager);
    }

    private void startGinRummy(){
        System.out.println("Starting Gin Rummy...");
        String gameType = "GIN";
        int minPlayers = 2;
        int maxPlayers = 2;
        GameSetupUI setupUI = new GameSetupUI(mainMenu.getScanner(), minPlayers, maxPlayers);
        List<Player> players = setupUI.setupPlayers();

        Deck deck = new Deck(gameType); // drawPile
        GameState state = new GameState(players, deck, gameType);
        TurnManager turnManager = new TurnManager();
        GinRummy ginRummy = new GinRummy();
        ginRummy.setupMatch(state, turnManager);
        GinRummyUI ginRummyUI = new GinRummyUI(mainMenu.getScanner());

        runGameLoop(ginRummy, state, ginRummyUI, turnManager);
    }

    private void startRummikub(){
        System.out.println("Starting Rummikub...");
        String gameType = "RUMMIKUB";
        int minPlayers = 2;
        int maxPlayers = 2;
        GameSetupUI setupUI = new GameSetupUI(mainMenu.getScanner(), minPlayers, maxPlayers);
        List<Player> players = setupUI.setupPlayers();

        Deck deck = new Deck(gameType);
        GameState state = new GameState(players, deck, gameType);
        TurnManager turnManager = new TurnManager();
        Rummikub rummikub = new Rummikub();
        rummikub.setupMatch(state, turnManager);
        RummykubUI rummykubUI = new RummykubUI(mainMenu.getScanner());

        runGameLoop(rummikub, state, rummykubUI, turnManager);
    }

    private void startRummyArgentino(){
        System.out.println("Starting Rummy Argentino...");
        String gameType = "ARGENTINO";
        int minPlayers = 2;
        int maxPlayers = 4;
        GameSetupUI setupUI = new GameSetupUI(mainMenu.getScanner(), minPlayers, maxPlayers);
        List<Player> players = setupUI.setupPlayers();

        Deck deck = new Deck(gameType); // 2 decks + 2 jokers
        GameState state = new GameState(players, deck, gameType);
        TurnManager turnManager = new TurnManager();
        RummyArgentino rummyArgentino = new RummyArgentino();
        rummyArgentino.setupMatch(state, turnManager);
        InGameUI inGameUI = new InGameUI(mainMenu.getScanner());

        runGameLoop(rummyArgentino, state, inGameUI, turnManager);

    }

    private void runGameLoop(GameRules variation, GameState state, InGameUI ui, TurnManager turnManager){
        boolean tournamentOver = false;

        while(!tournamentOver){
            boolean gameOver = false;
            state.setRoundOver(false); // Reset at the start of each round

            while(!gameOver){
                variation.playTurn(state, ui, turnManager);

                Player currentPlayer = state.getCurrrentPlayer();
                
                if(turnManager.checkWin(currentPlayer) || state.isRoundOver()){
                    int pointsWon = variation.calculatePoints(state, currentPlayer);
                    currentPlayer.addTournamentPoints(pointsWon);
                    ui.printWinMessage(currentPlayer);
                    gameOver = true;
                }

            }

            if(ui.promptPlayAgain()){
                for(Player player : state.getPlayers()){
                    player.getHand().clear();
                    player.setOpened(false); // Reset Argentino state
                }

                // Clear the table
                state.getBoard().clear();
                state.getDiscardPile().clear();

                // New shuffled deck and share cards
                state.setDrawPile(new Deck(state.getGameType()));
                variation.setupMatch(state, turnManager);
            } else{
                tournamentOver = true;
                System.out.println("TOURNAMENT ENDED");

                int highestScore = -1;
                Player playerWithHighest = null;

                for(Player player : state.getPlayers()){
                    if(player.getTournamentScore() > highestScore){
                        highestScore = player.getTournamentScore();
                        playerWithHighest = player;
                    }
                }

                if(playerWithHighest != null){
                    System.out.println("\n==================================================");
                    System.out.println("THE OVERALL CHAMPION IS: " + playerWithHighest.getName());
                    System.out.println("WITH A TOTAL SCORE OF: " + highestScore);
                    System.out.println("===================================================\n");
                }
            }
        }
        turnManager.getLogger().exportLogsToFile("match_log.txt");
    }
}
