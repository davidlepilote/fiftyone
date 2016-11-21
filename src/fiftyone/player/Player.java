package fiftyone.player;

import fiftyone.game.Card;
import fiftyone.game.DrawingDeck;
import fiftyone.game.PlayedCard;
import fiftyone.game.PlayingDeck;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David et Monireh on 16/10/2016.
 */
public abstract class Player {

    protected List<Card> hand = new ArrayList<>();

    protected int number = 1;

    public void getCards(DrawingDeck drawingDeck, int nbCardsInHand){
        while(hand.size() < nbCardsInHand){
            if(!drawingDeck.isEmpty()){
                hand.add(drawingDeck.popDeck());
            } else {
                return;
            }
        }
    }

    /**
     * Resets the player values if needed
     */
    public void reset(){
        hand.clear();
    }

    /**
     *
     * @param deck
     * @return the card chosen by the player as well as its chosen value
     */
    public abstract PlayedCard play(PlayingDeck deck);

    /**
     * Do nothing if you want. But if you want to update things for your player at the end of the game,
     * do it here
     * @param deck final deck
     * @param result 1 if player 1 won, 0 if player 2 won, 0.5 for a tie
     */
    public void finish(PlayingDeck deck, double result) {

    }

}
