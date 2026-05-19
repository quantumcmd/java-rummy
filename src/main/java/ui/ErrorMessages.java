package ui;

public class ErrorMessages {

    public static void invalidAdditionRummyKub(){
        System.out.println("ERROR! The card you tried to add to the existing meld makes the meld invalid");
    }

    public static void noPiecesLeftToDraw(){
        System.out.println("ERROR: No pieces left to draw!");
    }

    public static void emptyMeldCards() {
        System.out.println("ERROR! Enter the indices.");
    }

    public static void noSavedGamesFound() {
        System.out.println("No saved games found (.dat files).");
    }

    public static void meldPointsNotEnough() {
        System.out.println("Not enough points to open! You need 40");
    }
}
