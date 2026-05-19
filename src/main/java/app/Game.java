package app;

import logic.PersistenceManager;
import logic.TurnManager;
import logic.rules.GameRules;
import model.Deck;
import model.GameState;
import model.Player;
import ui.ErrorMessages;
import ui.GameSetupUI;
import ui.InGameUI;
import ui.MainMenu;

import java.io.File;
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
                    // Do something
                    break;
                case "3":
                    // Do something
                    break;
                case "4":
                    // Do something
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
                runGameLoop();
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

        // DO SOMETHING
    }

    private void runGameLoop(GameRules variation, GameState state, InGameUI ui, TurnManager turnManager){
        boolean gameOver = false;

        while(!gameOver){
            variation.playTurn(state, ui, turnManager);

            Player currentPlayer = state.getCurrrentPlayer();

            if(turnManager.checkWin(currentPlayer)){
                ui.printWinMessage(currentPlayer);
                gameOver = true;
            }
        }

        turnManager.getLogger().exportLogsToFile("match_log.txt");
    }
}
