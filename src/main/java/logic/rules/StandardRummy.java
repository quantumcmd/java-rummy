package logic.rules;

import logic.PersistenceManager;
import logic.TurnManager;
import model.GamePiece;
import model.GameState;
import model.Meld;
import model.Player;
import ui.InGameUI;

import java.util.List;

public class StandardRummy implements GameRules{
    @Override
    public void setupMatch(GameState state, TurnManager turnManager) {
        int numPlayers = state.getNumberOfPlayers();
        int numCards = 0;

        if(numPlayers == 2)
            numCards = 10;
        else if(numPlayers == 3 || numPlayers == 4)
            numCards = 7;
        turnManager.dealStartingHands(state, numCards, true);
    }

    @Override
    public void playTurn(GameState state, InGameUI ui, TurnManager turnManager) {
        ui.displayBoard(state);
        boolean hasDrawn = false;
        boolean turnEnded = false;

        while(!turnEnded){
            String choice = ui.promptTurnAction();
            Player currentPlayer = state.getCurrrentPlayer();

            switch(choice){
                case "1":
                    if(!hasDrawn){
                        turnManager.drawPiece(state, false);
                        hasDrawn = true;
                        ui.displayBoard(state);
                    } else{
                        System.out.println("You have already drawn a card this turn!");
                    }
                    break;
                case "2":
                    if(!hasDrawn){
                        turnManager.drawPiece(state, true);
                        hasDrawn = true;
                        ui.displayBoard(state);
                    } else{
                        System.out.println("You have already drawn a card this turn!");
                    }
                    break;
                case "3":
                    if(!hasDrawn){
                        System.out.println("You must draw a card first!");
                    } else{
                        List<GamePiece> cardsToMeld = ui.promptMeldCards(currentPlayer);
                        Meld meld = new Meld(cardsToMeld);
                        boolean meldStatus = turnManager.playMeld(state, meld);

                        if(meldStatus){
                            System.out.println("Meld successful");
                            ui.displayBoard(state);
                        } else{
                            System.out.println("Invalid Meld. Try again!");
                        }
                    }
                    break;
                case "4":
                    if(!hasDrawn){
                        System.out.println("You must draw a card first!");
                    } else{
                        turnManager.discardPiece(state, ui.promptCardSelection(currentPlayer));
                        turnEnded = true;
                    }
                    break;
                case "S", "s":
                    System.out.print("Enter a name to save your file (e.g., save1.dat): ");
                    String saveName = ui.getScanner().nextLine();
                    PersistenceManager.saveGame(state, saveName);
                    turnManager.getLogger().exportLogsToFile("match_log.txt");
                    break;
            }
        }
    }

    @Override
    public int calculatePoints(GameState state, Player winner) {
        int pointsWon = 0;
        for(Player player : state.getPlayers()){
            if(!player.equals(winner)){
                for(GamePiece piece : player.getHand()){
                    pointsWon += piece.getNumericalValue();
                }
            }
        }
        return pointsWon;
    }
}
