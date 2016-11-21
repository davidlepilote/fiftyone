package fiftyone;

import fiftyone.game.Game;
import fiftyone.neuralnetwork.FiftyoneNN;
import fiftyone.player.GoodPlayer;
import fiftyone.player.NNPlayer;
import fiftyone.player.RandPlayer;
import neuralnetwork.Network;

/**
 * Created by David et Monireh on 16/10/2016.
 */
public class Main {

    public static final int NB_GAMES_TRAINED = 10000000;
    public static final int NB_GAMES_TEST = 10000;

    public static void main(String[] args) throws Exception {
        final long startingTime = System.currentTimeMillis();

        final FiftyoneNN neuralNetwork = Network.getNetwork("neuralnetworks/nn-1479194723269-11000000", FiftyoneNN.class);

        NNPlayer player1 = new NNPlayer(neuralNetwork, false, true);
        GoodPlayer goodPlayer = new GoodPlayer();
        NNPlayer player2 = new NNPlayer(neuralNetwork, true, true);
        RandPlayer randPlayer = new RandPlayer();

        final long timeStamp = System.currentTimeMillis();
        //for (int gameNumber = 0; gameNumber < NB_GAMES_TRAINED; gameNumber++) {
        int gameNumber = 10000000;
        //while(true){
        if (gameNumber % 1000 == 0 && gameNumber != 10000000) {
            System.out.println("---");
            System.out.println("Games played : " + gameNumber);
            final long elapsedTime = (System.currentTimeMillis() - startingTime) / 1000;
            //System.out.println("Time passed : " + elapsedTime + "/" + (NB_GAMES_TRAINED * elapsedTime / gameNumber) + "s");
        }
        if (gameNumber % 100000 == 0 && gameNumber != 10000000) {
            neuralNetwork.serialize("neuralnetworks/nn-" + timeStamp + "-" + gameNumber);
        }
        new Game(Game.GameVariant.V1, player1, player2).play();
        gameNumber++;
        //}
        int player1Wins = 0;
        int player2Wins = 0;
        int player3Wins = 0;
        int ties = 0;
        neuralNetwork.serialize("neuralnetworks/nn-" + timeStamp + "-" + NB_GAMES_TRAINED);
        NNPlayer trainedPlayer = new NNPlayer(neuralNetwork);
        //NNPlayer trainedPlayer1 = new NNPlayer(trainedNetwork);
        RandPlayer randPlayer2 = new RandPlayer(2);
        for (int i = 0; i < NB_GAMES_TEST; i++) {
            if (i % 1000 == 0) System.out.println("##" + i);
            final Game.Result result = new Game(Game.GameVariant.V1, trainedPlayer, goodPlayer).play();
            if ("nn1".equals(result.winner)) {
                player1Wins++;
            } else if ("good1".equals(result.winner)) {
                player2Wins++;
            } else if ("rand1".equals(result.winner)) {
                player3Wins++;
            } else {
                ties++;
            }
        }
        System.out.println("--- RESULTS ---");
        if (player1Wins != 0) {
            System.out.println("Neural Network..." + player1Wins);
        }
        if (player2Wins != 0) {
            System.out.println("Good............." + player2Wins);
        }
        if (player3Wins != 0) {
            System.out.println("Random..........." + player3Wins);
        }
        System.out.println("Ties............." + ties);
    }
}
