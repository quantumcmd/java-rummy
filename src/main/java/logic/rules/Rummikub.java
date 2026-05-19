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
            String choice = ui.promptTurnAction();
            Player currentPlayer = state.getCurrrentPlayer();

            switch (choice){
                case "1":
                    if(hasDrawn){
                        System.out.println("You have already drawn a card this turn!");
                    }
                    turnManager.drawPiece(state, false); // There's no discardPile
                    hasDrawn = true;
                    break;
                case "2":
                    List<GamePiece> cardsToMeld = ui.promptMeldCards(currentPlayer);
                    Meld meld = new Meld(cardsToMeld);
                    if(currentPlayer.hasOpened()){
                        boolean meldStatus = turnManager.playMeld(state, meld);
                        if(meldStatus){
                            hasPlayed = true;
                            System.out.println("Meld Successful");
                            ui.displayBoard(state);
                        } else{
                            System.out.println("Invalid meld. Try again!");
                        }
                    } else{
                        if(turnManager.calculateMeldPoints(meld) < 30){
                            System.out.println("Your first meld must be worth at least 30 points!");
                        } else{
                            boolean meldStatus = turnManager.playMeld(state, meld);
                            if(meldStatus){
                                currentPlayer.setOpened(true);
                                hasPlayed = true;
                                System.out.println("Opening meld successful");
                                ui.displayBoard(state);
                            }
                        }
                    }
                    break;
                case "3":
                    if(!currentPlayer.hasOpened()){
                        System.out.println("You must make your opening meld (30 pts) first!");
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
                        System.out.println("Draw a card first!");
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
