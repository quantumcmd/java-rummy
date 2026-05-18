package model;

public class StandardCard implements GamePiece{
    private final Suit suit;
    private final Rank rank;

    public StandardCard(Suit suit, Rank rank){
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    @Override
    public int getNumericalValue() {
        return rank.getValue();
    }

    @Override
    public String getCategory() {
        return suit.name();
    }

    @Override
    public boolean isJoker() {
        return rank  == Rank.JOKER_RANK || suit == Suit.JOKER_SUIT;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}
