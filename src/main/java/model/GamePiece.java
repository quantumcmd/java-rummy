package model;

import java.io.Serializable;

public interface GamePiece extends Serializable {
    int getNumericalValue();
    String getCategory();
    boolean isJoker();
}
