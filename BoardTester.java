package se.liu.davah125.tetris;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


public class BoardTester {

    public static  void main(String[] args) {
	final List<HighScore> newHighScores = new ArrayList<>();

	final HighScoreList highScoreList = new HighScoreList(newHighScores);
	int width = 10;
	int height = 20;
	Board board1 = new Board(width, height);

	TetrisViewer viewer = new TetrisViewer(board1, highScoreList);
	viewer.show();
	viewer.startTimer();

	board1.addBoardListener(new BoardListener() {
	    // Listen too board if game-over and stops timer.
	    @Override
	    public void boardChanged(Board board) {
		if (board.gameOver && !board.playAgain) {

		    board.playAgain = true;
		    viewer.stopTimer();
		    Gson gson = new GsonBuilder().setPrettyPrinting().create();

		    String name = JOptionPane.showInputDialog(null, "Enter your name");

		    HighScore highScore = new HighScore(board.getScore(), name);
		    highScoreList.addHighScore(highScore);

		    String json = gson.toJson(highScoreList); // Visar highScoreList efter att första rundan är klar.
		    JOptionPane.showMessageDialog(null, json);

		    viewer.askPlayAgain();
		}
	    }
	});


    }
}
