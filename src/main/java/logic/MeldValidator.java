package logic;

import model.GamePiece;
import model.Meld;

import java.util.ArrayList;
import java.util.List;

public class MeldValidator {
    public static boolean isValidSet(List<GamePiece> pieces){
        // SIZE RULE
        if (pieces.size() != 3 && pieces.size() != 4)
            return false;

        // Separate jokers and normal pieces
        List<GamePiece> normalPieces = new ArrayList<>();
        List<GamePiece> jokerPieces = new ArrayList<>();

        for (GamePiece piece : pieces) {
            if (piece.isJoker())
                jokerPieces.add(piece);
            else
                normalPieces.add(piece);
        }

        if (jokerPieces.size() > 1) {
            return false;
        }

        // NUMBER RULE
        int firstPieceValue = normalPieces.get(0).getNumericalValue();

        for (int i = 1; i < normalPieces.size(); i++) {
            if (normalPieces.get(i).getNumericalValue() != firstPieceValue)
                return false;
        }

        // CATEGORY RULE
        ArrayList<String> seenCategories = new ArrayList<>();
        for (GamePiece piece : normalPieces) {
            String category = piece.getCategory();
            if (seenCategories.contains(category))
                return false;
            seenCategories.add(category);
        }
        return true;
    }

    public static boolean isValidRun(List<GamePiece> pieces){
        // SIZE RULE
        if (pieces.size() < 3)
            return false;

        // Separate jokers and normal pieces
        List<GamePiece> normalPieces = new ArrayList<>();
        List<GamePiece> jokerPieces = new ArrayList<>();

        for (GamePiece piece : pieces) {
            if (piece.isJoker())
                jokerPieces.add(piece);
            else
                normalPieces.add(piece);
        }

        if (jokerPieces.size() > 1) {
            return false;
        }

        sortByNumber(normalPieces);

        // CATEGORY RULE
        String firstCategory = normalPieces.get(0).getCategory();
        for (int i = 1; i < normalPieces.size(); i++) {
            if (!firstCategory.equals(normalPieces.get(i).getCategory()))
                return false;
        }

        // CONSECUTIVE RULE
        int wildCardsNeeded = 0;
        for(int i = 1; i < normalPieces.size(); i++){
            int diff = normalPieces.get(i).getNumericalValue() - normalPieces.get(i-1).getNumericalValue();
            if(diff == 0) return false;
            if(diff > 1){
                wildCardsNeeded += diff - 1;
            }
        }

        return wildCardsNeeded <= jokerPieces.size();
    }

    private static void sortByNumber(List<GamePiece> pieces) {
        GamePiece temp;
        for(int i = 0; i < pieces.size(); i++){
            for(int j = 0; j < pieces.size() - 1 - i; j++){
                if(pieces.get(j).getNumericalValue() > pieces.get(j+1).getNumericalValue()){
                    temp = pieces.get(j);
                    pieces.set(j, pieces.get(j+1));
                    pieces.set(j+1, temp);
                }
            }
        }
    }

    public static boolean isValidSet(Meld meld){
        return isValidSet(meld.getPieces());
    }

    public static boolean isValidRun(Meld meld){
        return isValidRun(meld.getPieces());
    }


}
