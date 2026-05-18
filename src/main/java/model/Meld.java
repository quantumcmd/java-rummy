package model;

import java.util.List;

public class Meld {
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
}
