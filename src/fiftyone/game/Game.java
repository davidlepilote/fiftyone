package fiftyone.game;

import fiftyone.player.Player;

/**
 * Created by David et Monireh on 16/10/2016.
 */
public class Game {

    public static enum GameVariant {
        V1(3),
        V2(4),
        V3(5);

        private final int nbCardsInHand;

        GameVariant(int nbCardsInHand) {
            this.nbCardsInHand = nbCardsInHand;
        }
    }

    public static class Result{
        public final String winner;

        public final String game;

        public final String playedCards;


        public Result(String winner, String game, String playedCards) {
            this.winner = winner;
            this.game = game;
            this.playedCards = playedCards;
        }

        @Override
        public String toString() {
            return " --------- " +
                    "Number of cards played : " + playedCards +
                    "Game played : " + game +
                    "Winner : " + (winner == null ? "Tie" : winner);
        }
    }

    private final Player[] players = new Player[2];

    private int turn = 0;

    private final DrawingDeck drawingDeck = new DrawingDeck();

    private final PlayingDeck playingDeck = new PlayingDeck();

    public final int nbCardsInHand;

    public Game(Player player1, Player player2) {
        this(GameVariant.V1, player1, player2);
    }

    public Game(GameVariant variant, Player player1, Player player2) {
        nbCardsInHand = variant.nbCardsInHand;
        players[0] = player1;
        players[1] = player2;
    }

    public Result play() {
        for (Player player : players) {
            player.reset();
        }
        for (Player player : players) {
            player.getCards(drawingDeck, nbCardsInHand);
        }
        while (!isOver()) {
            Player currentPlayer = players[turn % 2];
            playingDeck.addCard(currentPlayer.play(playingDeck));
            currentPlayer.getCards(drawingDeck, nbCardsInHand);
            turn++;
        }
        return getWinner();
    }

    private Result getWinner() {
        String winner = null;
        double result = 2.; // A tie
        if (!drawingDeck.isEmpty()){
            final int winningPlayer;
            final int score = playingDeck.getScore();
            if (score < 51) { // Should not happen in the game process
                System.out.println("The game is not finished yet");
                return null;
            }
            if (score == 51) { // The player who just played won, because he scored 51
                winningPlayer = (turn + 1) % 2;
            } else { // The player who just played lost, because he scored more than 51
                winningPlayer = turn % 2;
            }
            winner = players[winningPlayer].toString();
            result = winningPlayer == 0 ? 1. : 0.;
        }
        for (int playerIndex = 0; playerIndex < players.length; playerIndex++) {
            final Player player = players[playerIndex];
            if(result == 2.){
                player.finish(playingDeck, (double) playerIndex);
            } else {
                player.finish(playingDeck, result);
            }
        }
        return new Result(winner, playingDeck.toString(), "" + playingDeck.getSize());
    }

    public boolean isOver() {
        return playingDeck.getScore() >= 51 || drawingDeck.isEmpty();
    }
}
