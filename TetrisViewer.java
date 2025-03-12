package se.liu.davah125.tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class TetrisViewer {
    private final Board board;
    private Timer timer = null;
    private final HighScoreList highScoreList;
    private JFrame frame = null;


    public TetrisViewer(final Board board, final HighScoreList highScoreList ) {
	this.board = board;
	this.highScoreList = highScoreList;
    }


    public JMenuBar setMenuBar() {
	final JMenuBar menuBar = new JMenuBar();
	final JMenu menu = new JMenu("Meny");
	final JMenuItem end = new JMenuItem("Avsluta");
	end.addActionListener(e ->  {
	    int response = JOptionPane.showConfirmDialog(null,
							 "Är du säker på att du vill avsluta?",
							 "Avsluta spelet",
							 JOptionPane.YES_NO_OPTION);
	    if (response == JOptionPane.YES_OPTION) {
		System.exit(0);
	    }

	});
	menu.add(end);
	menuBar.add(menu);

	return menuBar;
    }


    public void askPlayAgain() {
	int val = JOptionPane.showConfirmDialog(null, "vill du spela igen?", "avsluta spelet", JOptionPane.YES_NO_OPTION);
	if (val == JOptionPane.YES_OPTION) {
	    stopTimer();
	    if(frame != null) {
		frame.dispose();
	    }
	    Board newBoard = new Board(board.getWidth(), board.getHeight());
	    newBoard.playAgain = false;

	    TetrisViewer newViewer = new TetrisViewer(newBoard, highScoreList);

	    newViewer.show();
	    newViewer.startTimer(); // Sets up a new game if YES was chosen.

	    newBoard.addBoardListener(new BoardListener() {
		@Override
		public void boardChanged(Board board) {
		    if (board.gameOver && !board.playAgain) {

			String name = JOptionPane.showInputDialog(null, "Vad heter du?");
			HighScore highScore = new HighScore(newBoard.getScore(), name);
			highScoreList.addHighScore(highScore);

			board.playAgain = true; // Säkerställer att dialogen bara visas en gång
			newViewer.stopTimer();
			System.out.println(highScoreList);
			newViewer.askPlayAgain();
		    }
		}
	    });

	} else if (val == JOptionPane.NO_OPTION) {
	    System.exit(0);

	}
    }


    private void timer(){
	final Action doOneStep = new AbstractAction() {
	    public void actionPerformed(ActionEvent e) {
		board.tick();
	    }
	};

	timer = new Timer(500, doOneStep);
	timer.setCoalesce(true);

    }

    public void startTimer() {
	if (timer == null) {
	    timer();
	}
	timer.start();
    }

    public void stopTimer() {
	if (timer != null) {
	    timer.stop();
	    System.out.println("stopp");
	}
    }


    public void show(){
	frame = new JFrame("Tetris Viewer");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setLayout(new BorderLayout());

	final JMenuBar menuBar = setMenuBar();
	frame.setJMenuBar(menuBar);

	StartScreen startScreen = new StartScreen(board, highScoreList);
	frame.add(startScreen, BorderLayout.CENTER);
	frame.setSize(600, 600); // Fixed StartScreen size
	frame.setVisible(true);

	Timer startTimer = new Timer(3000,null); // timer
	startTimer.addActionListener(new ActionListener() {
	    // Sätter igång spelet när timer är klar.

	    public void actionPerformed(ActionEvent e) {
		frame.remove(startScreen);
		TetrisComponent tetrisComponent = new TetrisComponent(board);
		frame.add(tetrisComponent, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		startTimer.stop();
		timer();
	    }

	});
	startTimer.setRepeats(false);
	startTimer.start();

    }

}
