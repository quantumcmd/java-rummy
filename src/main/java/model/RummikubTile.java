package model;

public class RummikubTile implements GamePiece{
    private final TileColour colour;
    private final int number;

    public RummikubTile(TileColour colour, int number) {
        this.colour = colour;
        this.number = number;
    }

    public TileColour getColour() {
        return colour;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public int getNumericalValue() {
        return getNumber();
    }

    @Override
    public String getCategory() {
        return getColour().name();
    }

    @Override
    public boolean isJoker() {
        return colour == TileColour.JOKER_COLOUR || number == 0;
    }

    @Override
    public String toString() {
        if(isJoker())
            return "Joker Tile";
        return number + " (" + colour + ")";
    }
}
