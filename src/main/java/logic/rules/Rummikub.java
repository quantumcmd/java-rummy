package logic.rules;

import logic.PersistenceManager;
import logic.TurnManager;
import model.GamePiece;
import model.GameState;
import model.Meld;
import model.Player;
import ui.InGameUI;
import ui.RummykubUI;

import java.util.List;

public class Rummikub implements GameRules{
    @Override
    public void setupMatch(GameState state, TurnManager turnManager) {
        int numCards = 14;
        turnManager.dealStartingHands(state, numCards, false);
    }

    @Override
    public void playTurn(GameState state, InGameUI ui, TurnManager turnManager) {
        // NOTE: Full board manipulation (splitting and rearranging existing melds)
        // is not implemented. A complete implementation would require a temporary
        // "staging" copy of the board for the turn, allowing tiles to be moved
        // freely between melds, and rolling back to the original state if the
        // final board contains any invalid melds. Due to this complexity, the
        // current implementation only supports adding a single tile to an existing
        // meld (case 3), which is a subset of the full Rummikub board manipulation rules.
        boolean turnEnded = false;
        boolean hasDrawn = false;
        boolean hasPlayed = false;

        while(!turnEnded){
            ui.displayBoard(state);
            String choice = ui.promptTurnAction();
            Player currentPlayer = state.getCurrentPlayer();

            switch (choice){
                case "1":
                    if(hasDrawn){
                        ui.printError("You have already drawn a card this turn!");
                    } else{
                        turnManager.drawPiece(state, false); // There's no discardPile
                        hasDrawn = true;
                    }
                    break;
                case "2":
                    List<GamePiece> cardsToMeld = ui.promptMeldCards(currentPlayer);
                    Meld meld = new Meld(cardsToMeld);
                    if(currentPlayer.hasOpened()){
                        boolean meldStatus = turnManager.playMeld(state, meld);
                        if(meldStatus){
                            hasPlayed = true;
                            ui.printMessage("Meld Successful");
                        } else{
                            ui.printError("Invalid meld. Try again!");
                        }
                    } else{
                        if(turnManager.calculateMeldPoints(meld) < 30){
                            ui.printError("Your first meld must be worth at least 30 points!");
                        } else{
                            boolean meldStatus = turnManager.playMeld(state, meld);
                            if(meldStatus){
                                currentPlayer.setOpened(true);
                                hasPlayed = true;
                                ui.printMessage("Opening meld successful");
                            }
                        }
                    }
                    break;
                case "3":
                    if(!currentPlayer.hasOpened()){
                        ui.printError("You must make your opening meld (30 pts) first!");
                    } else{
                        GamePiece tileToMeld = ui.promptCardSelection(currentPlayer);
                        int meldIndex = ((RummykubUI) ui).promptMeldSelection(state);
                        boolean added = turnManager.addTileToMeld(state, tileToMeld, meldIndex);
                        if(added)
                            hasPlayed = true;
                    }
                    break;
                case "4":
                    if(hasDrawn || hasPlayed){
                        turnEnded = true;
                    } else{
                        ui.printError("Draw a card first!");
                    }
                    break;
                case "S", "s":
                    String saveName = ui.promptSaveFileName();
                    boolean saved = PersistenceManager.saveGame(state, saveName);
                    if(saved)
                        ui.printMessage("Game saved successfully to " + saveName);
                    else
                        ui.printError("Error saving game!");
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
