package ui;

import model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameSetupUI {
    private Scanner scanner;
    private int minPlayers;
    private int maxPlayers;

    public GameSetupUI(Scanner scanner, int minPlayers, int maxPlayers){
        this.scanner = scanner;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    public List<Player> setupPlayers(){
        List<Player> players = new ArrayList<>();
        int numPlayers = -1;

        do{
            System.out.print("How many people are playing? ");
            try{
                numPlayers = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        } while (!numPlayersIsValid(numPlayers));

        for(int i = 1; i <= numPlayers; i++){
            System.out.print("What is Player " + i + "'s name? ");
            String name = scanner.nextLine();
            Player player = new Player(name);
            players.add(player);
        }
        return players;
    }

    private boolean numPlayersIsValid(int numPlayers) {
        return numPlayers >= minPlayers && numPlayers <= maxPlayers;
    }
}
