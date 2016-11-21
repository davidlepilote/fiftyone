package fiftyone.game;

import fiftyone.Utils;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.NoSuchElementException;

/**
 * Created by David et Monireh on 16/10/2016.
 */
public class DrawingDeck {

    private final Deque<Card> deck = new ArrayDeque<>(Arrays.asList(Utils.shuffle(Card.values())));

    public boolean isEmpty(){
        return deck.isEmpty();
    }

    public Card popDeck(){
        try{
            return deck.pop();
        } catch (NoSuchElementException exception){
            return null;
        }
    }
}
