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

public class RummyArgentino extends BaseGameRules{

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
                    hasDrawn = handleDraw(state, ui, turnManager, hasDrawn, false);
                    break;
                case "2":
                    hasDrawn = handleDraw(state, ui, turnManager, hasDrawn, true);
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
                    turnEnded = handleDiscard(state, ui, turnManager, hasDrawn, currentPlayer);
                    break;
                case "S", "s":
                    handleSave(state, ui, turnManager);
                    break;
            }
        }

    }
}
