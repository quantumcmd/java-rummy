package model;

import java.io.Serializable;
import java.util.List;

public class Meld implements Serializable {
    private List<GamePiece> pieces;

    public Meld(List<GamePiece> initialPieces){
        pieces = initialPieces;
    }

    public void addPiece(GamePiece piece){
        this.pieces.add(piece);
    }

    public List<GamePiece> getPieces(){
        return pieces;
    }

    public int meldSize(){
        return pieces.size();
    }

    public void removePiece(GamePiece piece){
        pieces.remove(piece);
    }

    @Override
    public String toString() {
        return pieces != null ? pieces.toString() : "[]";
    }
}
