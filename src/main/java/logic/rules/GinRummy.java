package logic.rules;

import logic.PersistenceManager;
import logic.TurnManager;
import model.GameState;
import model.Player;
import ui.InGameUI;

public class GinRummy implements GameRules{
    @Override
    public void setupMatch(GameState state, TurnManager turnManager) {
        int numCards = 10;
        turnManager.dealStartingHands(state, numCards, true);
    }

    @Override
    public void playTurn(GameState state, InGameUI ui, TurnManager turnManager) {
        boolean turnEnded = false;
        boolean hasDrawn = false;

        while(!turnEnded){
            ui.displayBoard(state);
            String choice = ui.promptTurnAction();
            Player currentPlayer = state.getCurrentPlayer();

            switch (choice){
                case "1": // Draw from drawPile
                    if(hasDrawn){
                        ui.printError("You have already drawn a card this turn!");
                    } else{
                        turnManager.drawPiece(state, false);
                        hasDrawn = true;
                    }
                    break;
                case "2": // Draw from discardPile
                    if(hasDrawn){
                        ui.printError("You have already drawn a card this turn!");
                    } else{
                        turnManager.drawPiece(state, true);
                        hasDrawn = true;
                    }
                    break;
                case "3": // Discard
                    if(!hasDrawn){
                        ui.printError("You must draw a card first!");
                    } else{
                        turnManager.discardPiece(state, ui.promptCardSelection(currentPlayer));
                        turnEnded = true;
                    }
                    break;
                case "4": // Knock
                    if(!hasDrawn){
                        ui.printError("You must draw a card first!");
                    } else{
                        if(calculateDeadwood(currentPlayer) <= 10){
                            ui.printMessage(currentPlayer.getName() + " KNOCKED!");
                            state.setRoundOver(true);
                            turnEnded = true;
                        }else {
                            ui.printError("You can't knock; deadwood is too high (must be ≤ 10)!");
                        }
                    }
                    break;
                case "5": // Go Gin
                    if(!hasDrawn){
                        ui.printError("You must draw a card first!");
                    } else{
                        if(calculateDeadwood(currentPlayer) == 0){
                            ui.printMessage(currentPlayer.getName() + " GOES GIN!");
                            state.setRoundOver(true);
                            turnEnded = true;
                        }else {
                            ui.printError("You can't go Gin; you still have deadwood!");
                        }
                    }
                    break;
                case "S", "s": // Save
                    System.out.print("Enter a name to save your file (e.g., save1.dat): ");
                    String saveName = ui.getScanner().nextLine();
                    PersistenceManager.saveGame(state, saveName);
                    turnManager.getLogger().exportLogsToFile("match_log.txt");
                    break;
            }
        }
    }

    private int calculateDeadwood(Player currentPlayer) {
        return 0;
    }

    @Override
    public int calculatePoints(GameState state, Player winner) {
        // NOTE: Calculating the true "deadwood" value requires finding the optimal
        // combination of melds from the player's hand, which is a hard
        // combinatorial problem. Due to its complexity, this method is not
        // fully implemented. It currently returns 0, meaning Knock/Gin validation
        // is not enforced.
        return 0;
    }
}
