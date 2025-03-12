package se.liu.davah125.tetris;


public class HighScore {
    private int score;
    private String playerName;

    public HighScore(final int score, final String playerName) {
	this.score = score;
	this.playerName = playerName;
    }


    public int getScore() {
	return score;
    }

    public String getPlayerName() {
	return playerName;
    }

    @Override public String toString() {
	return "HighScore{score=" + ", playerName=" + playerName + "}";
    }
}
