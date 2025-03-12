package se.liu.davah125.tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;


public class StartScreen extends JComponent {
    private final ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("images/startTetris.png"));
    private final HighScoreList highScoreList;

    public StartScreen(final Board board, final HighScoreList highScoreList) {
	this.highScoreList = highScoreList;
    }


    protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2d = (Graphics2D) g;
	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	// Hämta bildens originalstorlek
	int imgWidth = icon.getIconWidth();
	int imgHeight = icon.getIconHeight();

	// Hämta panelens nuvarande storlek
	int panelWidth = getWidth();
	int panelHeight = getHeight();

	final int fifty = 50;
	final int twenty = 20;
	final int ten = 10;


	// Rita bilden så att den fyller hela panelen
	g2d.drawImage(icon.getImage(), 0, 0, panelWidth, panelHeight, this);

	g.setFont(new Font("Arial", Font.BOLD, twenty));
	g.setColor(Color.WHITE);
	g.drawString("Highscores", fifty, fifty);

	List<HighScore> highScores = highScoreList.getHighScores();

	int maxScores = Math.min(highScores.size(), ten);
	int y = fifty + twenty + ten;
	for (int i = 0; i < maxScores; i++) {
	    HighScore highScore = highScores.get(i);
	    g.drawString(highScore.getPlayerName() + " Score: " + highScore.getScore(), fifty, y);
	    y += twenty + ten;
	    // Visar endast de 10 första i listan.
	}
    }
}
