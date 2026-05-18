package ui;

public class ErrorMessages {

    public static void invalidAdditionRummyKub(){
        System.out.println("ERROR! The card you tried to add to the existing meld makes the meld invalid");
    }

    public static void noPiecesLeft(){
        System.out.println("ERROR: No pieces left to draw!");
    }

    public static void emptyMeldCards() {
        System.out.println("ERROR! Enter the indices.");
    }
}
