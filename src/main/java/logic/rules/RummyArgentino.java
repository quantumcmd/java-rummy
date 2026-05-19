package logic.rules;

import logic.PersistenceManager;
import logic.TurnManager;
import model.GamePiece;
import model.GameState;
import model.Meld;
import model.Player;
import ui.ErrorMessages;
import ui.InGameUI;

import java.util.List;

public class RummyArgentino implements GameRules{

    // NOTE: Joker/wildcard logic is not fully implemented for this variant.
    // Rummy Argentino uses two types of wildcards: the Joker (worth 50 pts)
    // and the "Mono" (the 2 card, with variable point value). Rules include:
    // - Max one wildcard per meld.
    // - Players may swap a joker on the board for the natural card it represents.
    // - The "2" card is worth 5 pts if substituting cards 3-7, or 20 pts otherwise.
    // Due to this complexity, wildcards are currently treated as normal cards.

    @Override
    public void setupMatch(GameState state, TurnManager turnManager) {
        int numPlayers = state.getNumberOfPlayers();
        int numCards = 0;

        if(numPlayers == 2){
            numCards = 10;
        } else if(numPlayers == 3 || numPlayers == 4){
            numCards = 7;
        }

        turnManager.dealStartingHands(state, numCards, true);
    }

    @Override
    public void playTurn(GameState state, InGameUI ui, TurnManager turnManager) {
        boolean hasDrawn = false;
        boolean turnEnded = false;

        while(!turnEnded){
            ui.displayBoard(state);
            String choice = ui.promptTurnAction();
            Player currentPlayer = state.getCurrentPlayer();

            switch (choice){
                case "1":
                    if(hasDrawn){
                        ui.printError("You have already drawn a card this turn!");
                    } else{
                        turnManager.drawPiece(state, false);
                        hasDrawn = true;
                    }
                    break;
                case "2":
                    if(hasDrawn){
                        ui.printError("You have already drawn a card this turn!");
                    } else{
                        turnManager.drawPiece(state, true);
                        hasDrawn = true;
                    }
                    break;
                case "3":
                    if(!hasDrawn){
                        ui.printError("You must draw a card first!");
                    } else{
                        if(currentPlayer.hasOpened()){
                            List<GamePiece> cardsToMeld = ui.promptMeldCards(currentPlayer);
                            Meld proposedMeld = new Meld(cardsToMeld);
                            boolean meldStatus = turnManager.playMeld(state, proposedMeld);

                            if(meldStatus){
                                ui.printMessage("Meld Successful");
                            } else{
                                ui.printError("Invalid Meld. Try again!");
                            }
                        } else{
                            List<GamePiece> cards = ui.promptMeldCards(currentPlayer);
                            Meld proposedMeld = new Meld(cards);
                            if(turnManager.calculateMeldPoints(proposedMeld) >= 40){
                                turnManager.playMeld(state, proposedMeld);
                                currentPlayer.setOpened(true);
                            } else{
                                ErrorMessages.meldPointsNotEnough();
                            }
                        }
                    }
                    break;
                case "4":
                    if(!hasDrawn){
                        ui.printError("You must draw a card first!");
                    } else{
                        turnManager.discardPiece(state, ui.promptCardSelection(currentPlayer));
                        turnEnded = true;
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
