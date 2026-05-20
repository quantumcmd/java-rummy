package ui;

import model.GameState;
import model.Meld;

import java.util.Scanner;

public class RummykubUI extends InGameUI{

    public RummykubUI(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected void printOptions(){
        System.out.println("\n=== Rummykub Options ===");
        System.out.println("1 -> Draw from Deck");
        System.out.println("2 -> Play a new meld (from hand)");
        System.out.println("3 -> Add tile to existing meld");
        System.out.println("4 -> Discard a piece (End turn)");
        System.out.println("5 -> Pick up a tile from the board");
        System.out.println("S -> Save and quit");
        System.out.println("==========================\n");
    }

    @Override
    public String promptTurnAction(){
        while (true){
            printOptions();

            System.out.print("Choose an action (1-5) || S: ");
            String input = scanner.nextLine();

            switch (input){
                case "1", "2", "3", "4", "5", "S", "s":
                    return input;
                default:
                    System.out.println("Invalid choice. Enter a number between 1 and 5 || S");
                    break;
            }
        }
    }

    public int promptMeldSelection(GameState state){
        while(true){
            for(int i = 0; i < state.getBoard().getMeldCount(); i++){
                System.out.println(i + ": " + state.getBoard().getMeld(i).getPieces());
            }

            System.out.print("Enter a meld index: ");
            String input = scanner.nextLine();

            try{
                int meldIndex = Integer.parseInt(input);
                if(indexIsWithinRange(meldIndex, 0, state.getBoard().getMeldCount() - 1))
                    return meldIndex;
            } catch (NumberFormatException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private boolean indexIsWithinRange(int index, int min, int max){
        return index >= min && index <= max;
    }

    public int promptPieceSelectionFromMeld(Meld meld){
        while(true){
            System.out.println("Meld: " + meld.getPieces());
            System.out.print("Enter the index of the piece to pick up (0 to " + (meld.meldSize() - 1) + "): ");

            String input = scanner.nextLine();
            try {
                int index = Integer.parseInt(input);
                if (index >= 0 && index < meld.meldSize()) {
                    return index;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
        }
    }
}
