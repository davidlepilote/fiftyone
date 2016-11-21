package fiftyone.game;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by David et Monireh on 16/10/2016.
 */
public class PlayingDeck implements Iterable<PlayedCard> {

    private Deque<PlayedCard> deck = new ArrayDeque<>();

    // Used to know if a card is contained in the deck quickly
    private Set<PlayedCard> cardSet = new HashSet<>();

    private int score = 0;

    public void addCard(PlayedCard card){
        score += card.value;
        deck.addFirst(card);
        cardSet.add(card);
    }

    public PlayedCard getTopCard(){
        return deck.peek();
    }

    public int getScore(){
        return score;
    }

    public int getSize(){
        return deck.size();
    }

    @Override
    public String toString() {
        StringJoiner cards = new StringJoiner(":");
        for (PlayedCard playedCard : deck) {
            cards.add(playedCard.toString());
        }
        return cards.toString();
    }

    public boolean contains(PlayedCard playedCard){
        return cardSet.contains(playedCard);
    }

    @Override
    public Iterator<PlayedCard> iterator() {
        return deck.iterator();
    }

    @Override
    public void forEach(Consumer<? super PlayedCard> action) {
        deck.forEach(action);
    }

    @Override
    public Spliterator<PlayedCard> spliterator() {
        return deck.spliterator();
    }
}
