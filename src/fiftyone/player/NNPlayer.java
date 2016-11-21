package fiftyone.player;

import fiftyone.game.Card;
import fiftyone.game.PlayedCard;
import fiftyone.game.PlayingDeck;
import fiftyone.neuralnetwork.FiftyoneNN;
import javafx.util.Pair;

/**
 * Created by David et Monireh on 07/11/2016.
 */
public class NNPlayer extends Player {

    private final FiftyoneNN neuralNetwork;

    private final boolean isOpponent;

    private final boolean updateNN;

    public NNPlayer() {
        this.neuralNetwork = new FiftyoneNN();
        this.isOpponent = false;
        this.updateNN = false;
    }

    public NNPlayer(FiftyoneNN neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
        this.isOpponent = false;
        this.updateNN = false;
    }

    public NNPlayer(FiftyoneNN neuralNetwork, boolean isOpponent, boolean updateNN) {
        this.neuralNetwork = neuralNetwork;
        this.isOpponent = isOpponent;
        this.updateNN = updateNN;
    }

    @Override
    public void reset() {
        super.reset();
        neuralNetwork.resetInputs();
    }

    @Override
    public PlayedCard play(PlayingDeck deck) {
        // Sets the current inputs
        neuralNetwork.update(deck, isOpponent);
        // Choose a card to play testing each possibility and keeping the best move
        final PlayedCard playedCard;
        if (deck.getScore() > 30) {
            playedCard = twoPlyPlay(deck);
        } else {
            playedCard = onePlyPlay();
        }
        if (playedCard == null) {
            throw new IllegalStateException("Probability less than -1., problem in the algo");
        }
        neuralNetwork.updateWeights(playedCard);
        hand.remove(playedCard.card);
        return playedCard;
    }

    private PlayedCard onePlyPlay() {
        Pair<PlayedCard, Double> bestMove = new Pair<>(null, isOpponent ? Double.MAX_VALUE : Double.MIN_VALUE);
        for (Card card : hand) {
            for (int value : card.getValues()) {
                bestMove = updatePlayedCard(bestMove, new PlayedCard(card, value));
            }
        }
        return bestMove.getKey();
    }

    private Pair<PlayedCard, Double> updatePlayedCard(Pair<PlayedCard, Double> bestMove, PlayedCard ... currentPlayedCard) {
        final double currentMoveProbability = neuralNetwork.getProbability(currentPlayedCard);
        double bestMoveProbability = bestMove.getValue();
        PlayedCard playedCard = bestMove.getKey();
        if(isOpponent){
            if (currentMoveProbability < bestMoveProbability) {
                bestMoveProbability = currentMoveProbability;
                playedCard = currentPlayedCard[0];
            }
        } else {
            if (currentMoveProbability > bestMoveProbability) {
                bestMoveProbability = currentMoveProbability;
                playedCard = currentPlayedCard[0];
            }
        }
        return new Pair<>(playedCard, bestMoveProbability);
    }

    private PlayedCard twoPlyPlay(PlayingDeck deck) {
        Pair<PlayedCard, Double> bestMove = new Pair<>(null, isOpponent ? Double.MAX_VALUE : Double.MIN_VALUE);
        for (Card firstCard : hand) {
            for (int firstCardValue : firstCard.getValues()) {
                PlayedCard firstPlayedCard = new PlayedCard(firstCard, firstCardValue);
                for (Card secondCard : Card.values()) {
                    for (int secondCardValue : secondCard.getValues()) {
                        PlayedCard secondPlayedCard = new PlayedCard(secondCard, secondCardValue);
                        // It is a card that the opponent may have (not in the deck and not in player's hand)
                        if (!deck.contains(secondPlayedCard) && !hand.contains(secondPlayedCard.card)) {
                            bestMove = updatePlayedCard(bestMove, firstPlayedCard, secondPlayedCard);
                        }
                    }
                }
            }
        }
        return bestMove.getKey();
    }

    private PlayedCard threePlyPlay(double bestMoveProbability, PlayingDeck deck) {
        Pair<PlayedCard, Double> bestMove = new Pair<>(null, isOpponent ? Double.MAX_VALUE : Double.MIN_VALUE);
        for (Card firstCard : hand) {
            for (int firstCardValue : firstCard.getValues()) {
                PlayedCard firstPlayedCard = new PlayedCard(firstCard, firstCardValue);
                for (Card secondCard : Card.values()) {
                    for (int secondCardValue : secondCard.getValues()) {
                        PlayedCard secondPlayedCard = new PlayedCard(secondCard, secondCardValue);
                        // It is a card that the opponent may have (not in the deck and not in player's hand)
                        if (!deck.contains(secondPlayedCard) && !hand.contains(secondPlayedCard.card)) {
                            for (Card thirdCard : hand) {
                                // other options in player's hand
                                if (thirdCard != firstPlayedCard.card) {
                                    for (int thirdCardValue : thirdCard.getValues()) {
                                        PlayedCard thirdPlayedCard = new PlayedCard(thirdCard, thirdCardValue);
                                        bestMove = updatePlayedCard(bestMove, firstPlayedCard, secondPlayedCard, thirdPlayedCard);
                                    }
                                }
                            }
                            for (Card thirdCard : Card.values()) {
                                for (int thirdCardValue : thirdCard.getValues()) {
                                    PlayedCard thirdPlayedCard = new PlayedCard(thirdCard, thirdCardValue);
                                    if (!deck.contains(thirdPlayedCard) && !hand.contains(thirdPlayedCard.card) && thirdCard != secondPlayedCard.card) {
                                        bestMove = updatePlayedCard(bestMove, firstPlayedCard, secondPlayedCard, thirdPlayedCard);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return bestMove.getKey();
    }

    @Override
    public void finish(PlayingDeck deck, double result) {
        neuralNetwork.update(deck, isOpponent);
        neuralNetwork.updateWeights(deck.getTopCard(), result);
    }

    @Override
    public String toString() {
        return "nn" + (isOpponent ? 2 : 1);
    }
}
