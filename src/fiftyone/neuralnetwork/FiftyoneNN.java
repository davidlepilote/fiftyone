package fiftyone.neuralnetwork;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fiftyone.game.Card;
import fiftyone.game.PlayedCard;
import fiftyone.game.PlayingDeck;
import neuralnetwork.InputNeuron;
import neuralnetwork.Network;
import neuralnetwork.Neuron;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by David et Monireh on 07/11/2016.
 * A neural network especially designed to model the fifty one game
 */
public class FiftyoneNN extends Network {

    private final double alpha = 0.05;

    private final double lambda = 0.7;

    // Mapping of cards regarding their position in the input layer of the neural network
    private static Map<PlayedCard, Integer> lookup = new HashMap<>();

    static {
        int index = 0;
        for (Card card : Card.values()) {
            for (int value : card.getValues()) {
                lookup.put(new PlayedCard(card, value), index++);
            }
        }
    }

    @JsonCreator
    public FiftyoneNN(@JsonProperty("inputToHiddenWeights") List<List<Double>> inputToHiddenWeights, @JsonProperty("hiddenToOutputWeights") List<Double> hiddenToOutputWeights){
        super(inputToHiddenWeights, hiddenToOutputWeights);
    }

    public FiftyoneNN() {
        super(42, 8);
    }

    public void update(PlayingDeck deck, boolean isOpponent) {
        // TODO accelerate
        for (PlayedCard playedCard : deck) {
            ((InputNeuron) layers.get(0).get(lookup.get(playedCard))).setActivated(true);
        }
        ((InputNeuron) layers.get(0).get(40)).setActivated(!isOpponent);
        ((InputNeuron) layers.get(0).get(41)).setActivated(isOpponent);
    }

    // Gets the activation adding the played card
    // The played card is removed from the inputs
    public double getProbability(PlayedCard... playedCards) {
        for (PlayedCard playedCard : playedCards) {
            ((InputNeuron) layers.get(0).get(lookup.get(playedCard))).setActivated(true);
        }
        final double probability = getActivation();
        for (PlayedCard playedCard : playedCards) {
            ((InputNeuron) layers.get(0).get(lookup.get(playedCard))).setActivated(false);
        }
        return probability;
    }

    public void updateWeights(PlayedCard playedCard) {
        updateWeights(playedCard, -1.);
    }

    public void updateWeights(PlayedCard playedCard, double result) {
        // Update weights
        // Update weights between hidden and output
        InputNeuron playedInputNeuron = (InputNeuron) layers.get(0).get(lookup.get(playedCard));
        final Neuron outputNeuron = layers.get(layers.size() - 1).get(0);
        final double outputNeuronActivation = outputNeuron.activation();
        playedInputNeuron.setActivated(true);
        final double error = (result > -1. ? result : outputNeuron.activation()) - outputNeuronActivation;
        playedInputNeuron.setActivated(false);
        for (Neuron.Arc arc : outputNeuron) {
            arc.setWeight(arc.getWeight() + alpha * error * arc.eligibilityTrace);
        }
        // Update arcs between input and hidden
        for (Neuron hiddenNeuron : layers.get(layers.size() - 2)) {
            for (Neuron.Arc arc : hiddenNeuron) {
                arc.setWeight(arc.getWeight() + alpha * error * arc.eligibilityTrace);
            }
        }
        if (result < 0.) {
            // Update eligibility traces
            // Update arcs between hidden and output
            final double outputNeuronGradient = outputNeuronActivation * (1 - outputNeuronActivation);
            for (Neuron.Arc arc : outputNeuron) {
                arc.eligibilityTrace = lambda * arc.eligibilityTrace + (outputNeuronGradient * arc.neuron.activation());
            }
            // Update arcs between input and hidden
            for (Neuron hiddenNeuron : layers.get(layers.size() - 2)) {
                final double hiddenNeuronActivation = hiddenNeuron.activation();
                final double hiddenNeuronGradient = hiddenNeuronActivation * (1 - hiddenNeuronActivation);
                for (Neuron.Arc arc : hiddenNeuron) {
                    arc.eligibilityTrace = lambda * arc.eligibilityTrace + (outputNeuronGradient * outputNeuron.getArc(hiddenNeuron).getWeight() * hiddenNeuronGradient * arc.neuron.activation());
                }
            }
        }
    }
}
