package logic.rules;

import logic.PersistenceManager;
import logic.TurnManager;
import model.*;
import ui.InGameUI;
import ui.RummykubUI;

import java.util.ArrayList;
import java.util.List;

public class Rummikub extends BaseGameRules{
    @Override
    public void setupMatch(GameState state, TurnManager turnManager) {
        int numCards = 14;
        turnManager.dealStartingHands(state, numCards, false);
    }

    @Override
    public void playTurn(GameState state, InGameUI ui, TurnManager turnManager) {
        boolean turnEnded = false;
        boolean hasDrawn = false;
        boolean hasPlayed = false;

        Player currentPlayer = state.getCurrentPlayer();
        RummykubUI rui = (RummykubUI) ui;

        // Take a Snapshot of the original state
        Board originalBoard = state.getBoard().cloneBoard();
        List<GamePiece> originalHand = new ArrayList<>(currentPlayer.getHand());

        while (!turnEnded) {
            ui.displayBoard(state);
            String choice = rui.promptTurnAction();
            switch (choice) {
                case "1": // Draw
                    if (hasPlayed) {
                        ui.printError("You have manipulated the board. You cannot draw now!");
                    } else {
                        hasDrawn = handleDraw(state, ui, turnManager, hasDrawn, false);
                    }
                    break;
                case "2": // Play new meld
                    List<GamePiece> cardsToMeld = ui.promptMeldCards(currentPlayer);
                    if (cardsToMeld != null) {
                        Meld meld = new Meld(cardsToMeld);
                        if (!currentPlayer.hasOpened() && turnManager.calculateMeldPoints(meld) < 30) {
                            ui.printError("Your first meld must be worth at least 30 points!");
                        } else if (turnManager.playMeld(state, meld)) {
                            currentPlayer.setOpened(true);
                            hasPlayed = true;
                            ui.printMessage("Meld Successful");
                        } else {
                            ui.printError("Invalid meld. Try again!");
                        }
                    }
                    break;
                case "3": // Add to meld
                    if (!currentPlayer.hasOpened()) {
                        ui.printError("You must make your opening meld (30 pts) first!");
                    } else if (state.getBoard().getMeldCount() > 0) {
                        GamePiece tileToMeld = ui.promptCardSelection(currentPlayer);
                        int meldIndex = rui.promptMeldSelection(state);
                        if (turnManager.addTileToMeld(state, tileToMeld, meldIndex)) {
                            hasPlayed = true;
                        }
                    } else {
                        ui.printError("No melds on the board!");
                    }
                    break;
                case "5": // Pick up tile from board
                    if (!currentPlayer.hasOpened()) {
                        ui.printError("You must make your opening meld first!");
                    } else if (state.getBoard().getMeldCount() > 0) {
                        int meldIndex = rui.promptMeldSelection(state);
                        Meld chosenMeld = state.getBoard().getMeld(meldIndex);
                        int pieceIndex = rui.promptPieceSelectionFromMeld(chosenMeld);
                        GamePiece pickedPiece = chosenMeld.getPieces().get(pieceIndex);
                        chosenMeld.removePiece(pickedPiece);
                        currentPlayer.addCardToHand(pickedPiece);
                        state.getBoard().removeEmptyMelds();
                        hasPlayed = true;
                        ui.printMessage("Added " + pickedPiece + " to your hand!");
                    } else {
                        ui.printError("No melds on the board!");
                    }
                    break;
                case "4": // End turn
                    if (hasDrawn) {
                        turnEnded = true;
                    } else if (hasPlayed) {
                        // VALIDATE THE BOARD
                        boolean boardIsValid = true;
                        for (Meld m : state.getBoard().getAllMelds()) {
                            if (!logic.MeldValidator.isValidSet(m) && !logic.MeldValidator.isValidRun(m)) {
                                boardIsValid = false;
                                break;
                            }
                        }
                        if (boardIsValid) {
                            ui.printMessage("Board is valid! Turn successful.");
                            turnEnded = true;
                        } else {
                            ui.printError("The board contains invalid melds!");
                            ui.printError("Reverting board and hand to original state... You take a penalty draw.");
                            // Revert from snapshot
                            state.getBoard().restore(originalBoard);
                            currentPlayer.getHand().clear();
                            currentPlayer.getHand().addAll(originalHand);
                            // Draw penalty tile
                            turnManager.drawPiece(state, false);
                            turnEnded = true;
                        }
                    } else {
                        ui.printError("You must draw a tile or play tiles before ending your turn!");
                    }
                    break;
                case "S", "s": // Save
                    handleSave(state, ui, turnManager);
                    break;
            }
        }
    }
}
