package logic.rules;

import logic.MeldValidator;
import logic.PersistenceManager;
import logic.TurnManager;
import model.GamePiece;
import model.GameState;
import model.Meld;
import model.Player;
import ui.InGameUI;

import java.util.ArrayList;
import java.util.List;

public class GinRummy extends BaseGameRules{
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
                    hasDrawn = handleDraw(state, ui, turnManager, hasDrawn, false);
                    break;
                case "2": // Draw from discardPile
                    hasDrawn = handleDraw(state, ui, turnManager, hasDrawn, true);
                    break;
                case "3": // Discard
                    turnEnded = handleDiscard(state, ui, turnManager, hasDrawn, currentPlayer);
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
                    handleSave(state, ui, turnManager);
                    break;
            }
        }
    }

    @Override
    public int calculatePoints(GameState state, Player winner) {
        int points = 0;

        // 1. Add up the deadwood of all other players
        for (Player p : state.getPlayers()) {
            if (!p.equals(winner)) {
                points += calculateDeadwood(p);
            }
        }

        // 2. Subtract the winner's deadwood
        int winnerDeadwood = calculateDeadwood(winner);
        points -= winnerDeadwood;

        // 3. Add 25 bonus points if they went Gin (0 deadwood)
        if (winnerDeadwood == 0) {
            points += 25;
        }

        // Return points (Math.max prevents negative points if the opponent actually had less deadwood)
        return Math.max(0, points);
    }

    private int calculateDeadwood(Player currentPlayer) {
        List<GamePiece> hand = currentPlayer.getHand();
        int minDeadwood = 0;
        for (GamePiece piece : hand) {
            minDeadwood += piece.getNumericalValue();
        }

        List<Meld> possibleMelds = getAllValidMelds(hand);
        for (Meld meld : possibleMelds) {
            List<GamePiece> remainingHand = new ArrayList<>(hand);
            remainingHand.removeAll(meld.getPieces());
            Player tempPlayer = new Player("Temp");
            for (GamePiece p : remainingHand) {
                tempPlayer.addCardToHand(p);
            }
            int score = calculateDeadwood(tempPlayer);
            if (score < minDeadwood) {
                minDeadwood = score;
            }
        }
        return minDeadwood;
    }

    private List<Meld> getAllValidMelds(List<GamePiece> hand) {
        List<Meld> validMelds = new ArrayList<>();
        // Start the recursion at card index 0, with an empty bucket
        findMeldsRecursively(hand, 0, new ArrayList<>(), validMelds);
        return validMelds;
    }

    private void findMeldsRecursively(List<GamePiece> hand, int index, List<GamePiece> currentSubset, List<Meld> validMelds) {
        // Base Case: We have made a choice for every single card in the hand
        if (index == hand.size()) {
            // Check if our current bucket of cards forms a valid meld
            if (currentSubset.size() >= 3) {
                if (MeldValidator.isValidSet(currentSubset) || MeldValidator.isValidRun(currentSubset)) {
                    // It is valid! Save a copy of it.
                    validMelds.add(new Meld(new ArrayList<>(currentSubset)));
                }
            }
            return;
        }

        // Choice 1: INCLUDE the current card in our bucket
        currentSubset.add(hand.get(index));
        // Move to the next card
        findMeldsRecursively(hand, index + 1, currentSubset, validMelds);

        // Choice 2: DO NOT include the current card (backtrack by removing it)
        currentSubset.remove(currentSubset.size() - 1);
        // Move to the next card
        findMeldsRecursively(hand, index + 1, currentSubset, validMelds);
    }


}
