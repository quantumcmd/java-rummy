package ui;

import model.GamePiece;
import model.GameState;
import model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InGameUI {
    protected Scanner scanner;

    public InGameUI(Scanner scanner){
        this.scanner = scanner;
    }

    public void displayBoard(GameState state){
        Player currentPlayer = state.getCurrrentPlayer();
        System.out.println("=== " + currentPlayer.getName() + " ===");

        if(!state.getDiscardPile().isEmpty()){
            System.out.println("Discard Pile: " + state.getDiscardPile().peek());
        } else{
            System.out.println("Discard Pile: [EMPTY]");
        }

        System.out.println("Board: " + state.getBoard());
        System.out.println("Your Hand: " + currentPlayer.getHand());
    }

    public String promptTurnAction(){
        while(true){
            printOptions();

            System.out.print("Choose an action (1-4 || S): ");
            String input = scanner.nextLine();

            switch(input){
                case "1", "2", "3", "4", "S", "s":
                    return input;
                default:
                    System.out.println("Invalid choice. Enter a number between 1 and 4 Or S to save the game");
                    break;
            }
        }
    }

    protected void printOptions() {
        System.out.println("\n=== In Game Options ===");
        System.out.println("1 -> Draw from Deck");
        System.out.println("2 -> Draw from Discard Pile");
        System.out.println("3 -> Play a Meld");
        System.out.println("4 -> Discard a piece (End Turn)");
        System.out.println("S -> Save and quit\n");
    }

    public GamePiece promptCardSelection(Player player){
        while(true){
            int maxIndex = player.getHand().size() - 1;
            System.out.print("Enter the index of the card (0 to " + maxIndex + "): ");
            String input = scanner.nextLine();

            try{
                int index = Integer.parseInt(input);

                if(index >= 0 && index <= maxIndex) return player.getHand().get(index);
                System.out.println("Invalid index. Try again.");
            }catch(NumberFormatException e){
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public List<GamePiece> promptMeldCards(Player player){
        System.out.print("Enter the indices of the cards to meld separated by spaces (e.g. 0 2 3): ");
        String userInput = scanner.nextLine();

        if(userInput.isEmpty()){
            ErrorMessages.emptyMeldCards();
            return null;
        }

        ArrayList<GamePiece> cardsToMeld = new ArrayList<>();
        String[] parts = userInput.split(" ");

        for(String p : parts){
            try{
                int index = Integer.parseInt(p);
                cardsToMeld.add(player.getHand().get(index));
            } catch (NumberFormatException | IndexOutOfBoundsException e){
                System.out.println("ERROR: " + e.getMessage());
            }
        }

        return cardsToMeld;
    }

    public Scanner getScanner(){
        return this.scanner;
    }

    public void printWinMessage(Player player){
        System.out.println(player.getName() + " HAS WON THE GAME WITH " + player.getTournamentScore() + " points!");
    }

    public boolean promptPlayAgain() {
        System.out.println("Do you want to play another round in this tournament? (Y/N)");

        String userInput = scanner.nextLine();
        return userInput.equalsIgnoreCase("Y");
    }
}
