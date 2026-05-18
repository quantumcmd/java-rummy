package logic;

import model.GamePiece;
import model.Meld;

import java.util.ArrayList;
import java.util.List;

public class MeldValidator {
    public static boolean isValidSet(List<GamePiece> pieces){
        // SIZE RULE
        if(pieces.size() != 3 && pieces.size() != 4) return false;

        // NUMBER RULE
        int firstPieceValue = pieces.get(0).getNumericalValue();

        for(int i = 1; i < pieces.size(); i++){
            if(pieces.get(i).getNumericalValue() != firstPieceValue)
                return false;
        }

        // CATEGORY RULE
        ArrayList<String> seenCategories = new ArrayList<>();
        for(GamePiece piece : pieces){
            String category = piece.getCategory();
            if(seenCategories.contains(category))
                return false;
            seenCategories.add(category);
        }
        return true;
    }

    public static boolean isValidRun(List<GamePiece> pieces){
        // SIZE RULE
        if(pieces.size() < 3)
            return false;

        sortByNumber(pieces);

        // CATEGORY RULE
        String firstCategory = pieces.get(0).getCategory();
        for(int i = 1; i < pieces.size(); i++){
            if(!firstCategory.equals(pieces.get(i).getCategory()))
                return false;
        }

        // CONSECUTIVE RULE
        for(int i = 1; i < pieces.size(); i++){
            if(pieces.get(i).getNumericalValue() - pieces.get(i-1).getNumericalValue() != 1)
                return false;
        }

        return true;
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
