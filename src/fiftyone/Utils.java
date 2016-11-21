package fiftyone;

import fiftyone.game.Card;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by David et Monireh on 16/10/2016.
 */
public class Utils {

    public static Card[] shuffle(Card[] cards){
        List<Card> shuffledCards = Arrays.asList(cards);
        Collections.shuffle(shuffledCards);
        return shuffledCards.toArray(new Card[shuffledCards.size()]);
    }
}
