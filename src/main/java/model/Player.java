package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private String name;
    private List<GamePiece> hand;
    private int score;
    private boolean hasOpened;

    public Player(String name){
        this.name = name;
        this.hand = new ArrayList<>();
        hasOpened = false;
        this.score = 0;
    }

    public void addCardToHand(GamePiece piece){
        this.hand.add(piece);
    }

    public void removeCardFromHand(GamePiece piece){
        this.hand.remove(piece);
    }

    public String getName(){
        return this.name;
    }

    public List<GamePiece> getHand(){
        return this.hand;
    }

    public boolean hasOpened(){
        return hasOpened;
    }

    public void setOpened(boolean hasOpened){
        this.hasOpened = hasOpened;
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;

        if(!(obj instanceof Player))
            return false;

        Player comparedPlayer = (Player) obj;
        return this.name.equals(comparedPlayer.name);
    }
}
