package ui;

import java.util.Scanner;

public class MainMenu {
    private final Scanner scanner;

    public MainMenu(Scanner scanner){
        this.scanner = scanner;
    }

    public String displayMenu(){
        while(true){
            printCommands();
            System.out.print("Please choose an option (0-5): ");
            String userOption = scanner.nextLine();

            switch(userOption){
                case "0", "1", "2", "3", "4", "5":
                    return userOption;
                default:
                    System.out.println("Invalid choice. Please enter a number between 0 and 5.");
                    break;
            }
        }
    }

    private void printCommands() {
        System.out.println("\n=== WELCOME TO RUMMY ===");
        System.out.println("0 -> Load saved game?");
        System.out.println("1 -> Play Standard Rummy");
        System.out.println("2 -> Play Gin Rummy");
        System.out.println("3 -> Play Rummikub");
        System.out.println("4 -> Play Rummy Argentino");
        System.out.println("5 -> Exit\n");
    }

    public Scanner getScanner() {
        return scanner;
    }
}
