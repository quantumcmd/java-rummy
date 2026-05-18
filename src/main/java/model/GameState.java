package model;

import java.util.List;
import java.util.Stack;

public class GameState {
    private List<Player> players;
    private Integer currentPlayerIndex;
    private Board board;
    private Deck drawPile;
    private Stack<GamePiece> discardPile;
    private String gameType;
    private boolean roundOver; // Used by variants (like Gin Rummy) to signal end of round;

    public GameState(List<Player> players, Deck drawPile, String gameType){
        this.players = players;
        this.drawPile = drawPile;
        this.gameType = gameType;
        currentPlayerIndex = 0;
        board = new Board();
        discardPile = new Stack<>();
        roundOver = false;
    }

    public boolean isRoundOver(){
        return roundOver;
    }

    public void setRoundOver(boolean roundOver){
        this.roundOver = roundOver;
    }

    public String getGameType(){
        return gameType;
    }

    public List<Player> getPlayers(){
        return players;
    }

    public void setPlayers(List<Player> players){
        this.players = players;
    }

    public Player getCurrrentPlayer(){
        return players.get(currentPlayerIndex);
    }

    public Integer getCurrentPlayerIndex(){
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(Integer currentPlayerIndex){
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public Board getBoard(){
        return board;
    }

    public void setBoard(Board board){
        this.board = board;
    }

    public Deck getDrawPile(){
        return drawPile;
    }

    public void setDrawPile(Deck drawPile){
        this.drawPile = drawPile;
    }

    public Stack<GamePiece> getDiscardPile(){
        return discardPile;
    }

    public void setDiscardPile(Stack<GamePiece> discardPile){
        this.discardPile = discardPile;
    }

    public int getNumberOfPlayers(){
        return players.size();
    }
}
