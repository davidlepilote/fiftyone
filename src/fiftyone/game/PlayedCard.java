package fiftyone.game;

/**
 * Created by David et Monireh on 16/10/2016.
 */
public class PlayedCard {

    public final Card card;

    public final int value;

    public PlayedCard(Card card, int value) {
        this.card = card;
        this.value = value;
    }

    @Override
    public String toString() {
        return card.toString() + "," + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayedCard that = (PlayedCard) o;

        if (value != that.value) return false;
        return card == that.card;

    }

    @Override
    public int hashCode() {
        int result = card != null ? card.hashCode() : 0;
        result = 31 * result + value;
        return result;
    }
}
