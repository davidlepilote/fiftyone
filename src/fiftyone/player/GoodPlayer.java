package fiftyone.player;

import fiftyone.game.Card;
import fiftyone.game.PlayedCard;
import fiftyone.game.PlayingDeck;

/**
 * Created by David et Monireh on 11/11/2016.
 */
public class GoodPlayer extends Player {

    @Override
    public PlayedCard play(PlayingDeck deck) {
        PlayedCard playedCard = canWin(deck);
        if(playedCard == null){
            playedCard = canAvoidDanger(deck);
        }
        if(playedCard == null){
            playedCard = canPlayRegularCard();
        }
        if(playedCard == null){
            playedCard = new PlayedCard(hand.get(0), hand.get(0).getValues()[0]);
        }
        hand.remove(playedCard.card);
        return playedCard;
    }

    private PlayedCard canPlayRegularCard() {
        for (Card card : hand) {
            for (int cardValue : card.getValues()) {
                if((cardValue >= 2 && cardValue <= 4) || cardValue == 7 || cardValue == 8){
                    return new PlayedCard(card, cardValue);
                }
            }
        }
        return null;
    }

    private PlayedCard canAvoidDanger(PlayingDeck deck) {
        if(deck.getScore() >= 29){
            for (Card card : hand) {
                for (int cardValue : card.getValues()) {
                    final int score = deck.getScore() + cardValue;
                    if(score < 39 || score == 42 || score == 45 || score == 46){
                        return new PlayedCard(card, cardValue);
                    }
                }
            }
        }
        return null;
    }

    private PlayedCard canWin(PlayingDeck deck){
        for (Card card : hand) {
            for (int cardValue : card.getValues()) {
                if(deck.getScore() + cardValue == 51){
                    return new PlayedCard(card, cardValue);
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "good" + number;
    }
}
