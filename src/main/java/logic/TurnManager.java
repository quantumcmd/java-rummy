package logic;

import model.GamePiece;
import model.GameState;
import model.Meld;
import model.Player;

public class TurnManager {

    public void drawPiece(GameState state, boolean isFromDiscardPile){
        Player currentPlayer = state.getCurrrentPlayer();
        GamePiece pieceToDraw = null;

        if(isFromDiscardPile){
            if(!state.getDiscardPile().isEmpty()){
                pieceToDraw = state.getDiscardPile().pop();
            }
        } else{
            pieceToDraw = state.getDrawPile().drawPiece(state.getDiscardPile());
        }

        if(pieceToDraw != null){
            currentPlayer.addCardToHand(pieceToDraw);
        } else{
            System.out.println("ERROR: No pieces left to draw!");
        }
    }

    public boolean playMeld(GameState state, Meld meld){
        Player currentPlayer = state.getCurrrentPlayer();

        if(!currentPlayer.getHand().containsAll(meld.getPieces()))
            return false;

        if(!MeldValidator.isValidSet(meld) && !MeldValidator.isValidRun(meld))
            return false;

        for(GamePiece piece : meld.getPieces()){
            currentPlayer.removeCardFromHand(piece);
        }

        state.getBoard().addMeld(meld);
        return true;
    }

    public void discardPiece(GameState state, GamePiece pieceToDiscard){
        Player currentPlayer = state.getCurrrentPlayer();

        if(!currentPlayer.getHand().contains(pieceToDiscard))
            return;

        currentPlayer.removeCardFromHand(pieceToDiscard);
        state.getDiscardPile().push(pieceToDiscard);

        if(state.getCurrentPlayerIndex() == state.getNumberOfPlayers() - 1){
            state.setCurrentPlayerIndex(0);
        } else{
            state.setCurrentPlayerIndex(state.getCurrentPlayerIndex() + 1);
        }
    }

    public boolean checkWin(Player player){
        return player.getHand().isEmpty();
    }

    public boolean addTileToMeld(GameState state, GamePiece tile, int boardMeldIndex){
        Player currentPlayer = state.getCurrrentPlayer();
        Meld meld = state.getBoard().getMeld(boardMeldIndex);
        meld.addPiece(tile);

        if(MeldValidator.isValidRun(meld) || MeldValidator.isValidSet(meld)){
            state.getCurrrentPlayer().removeCardFromHand(tile);
            return true;
        }

        meld.removePiece(tile);
        System.out.println("ERROR! The card you tried to add to the existing meld makes the meld invalid");
        return false;
    }

    public int calculateMeldPoints(Meld meld){
        int meldPoints = 0;
        for(GamePiece piece : meld.getPieces()){
            meldPoints += piece.getNumericalValue();
        }
        return meldPoints;
    }

    public void dealStartingHands(GameState state, int numCards, boolean createDiscardPile){
        for(int i = 0; i < numCards; i++){
            for(Player player : state.getPlayers()){
                GamePiece piece = state.getDrawPile().drawPiece(state.getDiscardPile());
                player.addCardToHand(piece);
            }
        }

        if(!createDiscardPile)
            return;

        GamePiece starterPiece = state.getDrawPile().drawPiece(state.getDiscardPile());
        state.getDiscardPile().push(starterPiece);
    }
}
