package fiftyone.player;

import fiftyone.game.Card;
import fiftyone.game.PlayedCard;
import fiftyone.game.PlayingDeck;

import java.util.Random;

/**
 * Created by David et Monireh on 16/10/2016.
 */
public class RandPlayer extends Player {

    private Random rand = new Random();

    public RandPlayer(int number) {
        this.number = number;
    }

    public RandPlayer() {
    }

    @Override
    public PlayedCard play(PlayingDeck deck) {
        Card chosenCard = hand.remove(rand.nextInt(hand.size()));
        return new PlayedCard(chosenCard, chosenCard.getValues()[rand.nextInt(chosenCard.getValues().length)]);
    }

    @Override
    public String toString() {
        return "rand" + number;
    }
}
