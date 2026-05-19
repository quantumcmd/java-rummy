package model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Stack;

public class Deck implements Serializable {
    private String gameType;
    private Stack<GamePiece> drawPile;
    private final int SET = 2;
    private final int MAX_RUMMIKUB = 13;

    public Deck(String gameType){
        this.gameType = gameType;
        this.drawPile = new Stack<>();
        initPieces();
    }

    private void initPieces(){
        if(gameType.equals("RUMMIKUB")){
            initRummikubTiles();
        }else{
            initStandardCards();
        }
        shuffleDrawPile();
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

    private void initRummikubTiles(){
        // Rummikub has TWO sets of tiles from 1 to 13 in 4 colors
        for(int i = 0; i < SET; i++){
            for(TileColour colour : TileColour.values()){
                if(colour == TileColour.JOKER_COLOUR) continue;
                for(int number = 1; number <= MAX_RUMMIKUB; number++){
                    RummikubTile tile = new RummikubTile(colour, number);
                    drawPile.push(tile);
                }
            }
        }
        // Add 2 Rummikub Jokers manually
        drawPile.push(new RummikubTile(TileColour.JOKER_COLOUR, 0));
        drawPile.push(new RummikubTile(TileColour.JOKER_COLOUR, 0));
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
