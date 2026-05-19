package ui;

import java.util.Scanner;

public class GinRummyUI extends InGameUI{
    public GinRummyUI(Scanner scanner){
        super(scanner);
    }

    @Override
    protected void printOptions() {
        System.out.println("\n=== Gin Rummy Options ===");
        System.out.println("1 -> Draw from Deck");
        System.out.println("2 -> Draw from Discard Pile");
        System.out.println("3 -> Discard a piece (End Turn)");
        System.out.println("4 -> Knock (End Round)");
        System.out.println("5 -> Go Gin (End Round)");
        System.out.println("S -> Save and quit");
        System.out.println("============================\n");
    }

    @Override
    public String promptTurnAction() {
        while(true){
            printOptions();

            System.out.print("Choose an action (1-5 || S): ");
            String input = scanner.nextLine();

            switch(input){
                case "1", "2", "3", "4", "5", "S", "s":
                    return input;
                default:
                    System.out.println("Invalid choice. Enter a number between 1 and 5 || S");
                    break;
            }
        }
    }
}
