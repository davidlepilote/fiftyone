package fiftyone.game;

/**
 * Created by David et Monireh on 16/10/2016.
 */
public enum Card {

    S7(Color.SPADES, Value.SEVEN),
    S8(Color.SPADES, Value.EIGHT),
    S9(Color.SPADES, Value.NINE),
    S10(Color.SPADES, Value.TEN),
    SJ(Color.SPADES, Value.JACK),
    SQ(Color.SPADES, Value.QUEEN),
    SK(Color.SPADES, Value.KING),
    SA(Color.SPADES, Value.ACE),
    D7(Color.DIAMONDS, Value.SEVEN),
    D8(Color.DIAMONDS, Value.EIGHT),
    D9(Color.DIAMONDS, Value.NINE),
    D10(Color.DIAMONDS, Value.TEN),
    DJ(Color.DIAMONDS, Value.JACK),
    DQ(Color.DIAMONDS, Value.QUEEN),
    DK(Color.DIAMONDS, Value.KING),
    DA(Color.DIAMONDS, Value.ACE),    
    H7(Color.HEARTS, Value.SEVEN),
    H8(Color.HEARTS, Value.EIGHT),
    H9(Color.HEARTS, Value.NINE),
    H10(Color.HEARTS, Value.TEN),
    HJ(Color.HEARTS, Value.JACK),
    HQ(Color.HEARTS, Value.QUEEN),
    HK(Color.HEARTS, Value.KING),
    HA(Color.HEARTS, Value.ACE),    
    C7(Color.CLUBS, Value.SEVEN),
    C8(Color.CLUBS, Value.EIGHT),
    C9(Color.CLUBS, Value.NINE),
    C10(Color.CLUBS, Value.TEN),
    CJ(Color.CLUBS, Value.JACK),
    CQ(Color.CLUBS, Value.QUEEN),
    CK(Color.CLUBS, Value.KING),
    CA(Color.CLUBS, Value.ACE);

    private enum Color{
        SPADES,
        DIAMONDS,
        HEARTS,
        CLUBS
    }

    private enum Value{
        SEVEN(7),
        EIGHT(8),
        NINE(0),
        TEN(10, -10),
        JACK(2),
        QUEEN(3),
        KING(4),
        ACE(1, 11);

        private int[] values;

        Value(int ... values) {
            this.values = values;
        }
    }

    private Color color;

    private Value value;

    Card(Color color, Value value) {
        this.color = color;
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public int[] getValues() {
        return value.values;
    }
}
