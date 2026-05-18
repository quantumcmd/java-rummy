package model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Stack;

public class Deck implements Serializable {
    private String gameType;
    private Stack<GamePiece> drawPile;

    public Deck(String gameType){
        this.gameType = gameType;
        this.drawPile = new Stack<>();
        initPieces();
    }

    private void initPieces(){
        initStandardCards();
    }

    private void initStandardCards(){
        for(Suit suit : Suit.values()){
            if(suit == Suit.JOKER_SUIT) continue;
            for(Rank rank : Rank.values()){
                if(rank == Rank.JOKER_RANK) continue;
                StandardCard card = new StandardCard(suit, rank);
                drawPile.push(card);
            }
        }
    }

    private void shuffleDrawPile(){
        Collections.shuffle(drawPile);
    }

    public GamePiece drawPiece(Stack<GamePiece> tableDiscardPile){
        // If the deck is empty, it takes all the cards from the table's discard pile,
        // shuffles them, and the returns the top card
        if(drawPile.isEmpty()){
            while(!tableDiscardPile.empty()){
                drawPile.push(tableDiscardPile.pop());
            }
            shuffleDrawPile();
        }
        return drawPile.pop();
    }
}
