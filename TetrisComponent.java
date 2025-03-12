package se.liu.davah125.tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.EnumMap;
import java.util.Map;


public class TetrisComponent extends JComponent implements BoardListener {
    private Board board;
    private final static int SQUARE_SIZE = 30;
    private final static EnumMap<SquareType, Color> SQUARE_COLORS = createColorMap();


    public TetrisComponent(final Board board) {
	this.board = board;
	board.addBoardListener(this);
	setPreferredSize(new Dimension(board.getWidth() * SQUARE_SIZE, board.getHeight() * SQUARE_SIZE));
	setUpKeyPressed();
    }


    @Override public Dimension getPreferredSize() {
	//return super.getPreferredSize();

	int limit4 = Board.DOUBLE_MARGIN;
	int width = (board.getWidth() - limit4) * SQUARE_SIZE;
	int height = (board.getHeight() - limit4) * SQUARE_SIZE;

	return new Dimension(width, height);
    }


    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	Graphics2D g2d = (Graphics2D) g;

	int limit2 = Board.MARGIN;
	final int sizeAndY = 20;
	final int xForScreen = 10;

	for (int y = 2; y < board.getHeight() - limit2; y++) {
	    for (int x = 2; x < board.getWidth() - limit2; x++) {

		SquareType square = board.getVisibleSquareAt(x, y);
		if (square == SquareType.OUTSIDE) continue;

		g2d.setColor(SQUARE_COLORS.get(square));
		g2d.fillRect((x - limit2) * SQUARE_SIZE, (y - limit2) * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
		g2d.setColor(Color.BLACK);
		g2d.drawRect((x - limit2) * SQUARE_SIZE, (y - limit2) * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
	    }
	}
	// Visar hur mycket poäng du har under spelomgången.
	g2d.setColor(Color.WHITE);
	g2d.setFont(new Font("Arial", Font.BOLD, sizeAndY));
	g2d.drawString("Poäng: " + board.getScore(), xForScreen, sizeAndY);

    }


    private static EnumMap<SquareType, Color> createColorMap() {
	EnumMap<SquareType, Color> squareColors = new EnumMap<>(SquareType.class);
	squareColors.put(SquareType.EMPTY, Color.BLACK);
	squareColors.put(SquareType.I, Color.CYAN);
	squareColors.put(SquareType.J, Color.BLUE);
	squareColors.put(SquareType.L, Color.ORANGE);
	squareColors.put(SquareType.O, Color.YELLOW);
	squareColors.put(SquareType.S, Color.GREEN);
	squareColors.put(SquareType.T, Color.MAGENTA);
	squareColors.put(SquareType.Z, Color.RED);
	squareColors.put(SquareType.OUTSIDE, Color.WHITE);
	return squareColors;
    }
    @Override public void boardChanged(final Board board) {
	repaint();
    }


    private void setUpKeyPressed() {
	InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	ActionMap actionMap = getActionMap();

	inputMap.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
	inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
	inputMap.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
	inputMap.put(KeyStroke.getKeyStroke("A"), "rotLeft");
	inputMap.put(KeyStroke.getKeyStroke("D"), "rotRight");

	actionMap.put("moveLeft", new AbstractAction() {
	    public void actionPerformed(ActionEvent e) {
		board.move(Direction.LEFT);
	    }
	});
	actionMap.put("moveRight", new AbstractAction() {
	    public void actionPerformed(ActionEvent e) {
		board.move(Direction.RIGHT);
	    }
	});
	actionMap.put("moveDown", new AbstractAction() {
	    public void actionPerformed(ActionEvent e) {
		board.move(Direction.DOWN);
	    }
	});
	actionMap.put("rotLeft", new AbstractAction() {
	    public void actionPerformed(ActionEvent e) {
		board.rotate(Direction.ROTATE_LEFT);
	    }
	});
	actionMap.put("rotRight", new AbstractAction() {
	    public void actionPerformed(ActionEvent e) {
		board.rotate(Direction.ROTATE_RIGHT);
	    }
	});

    }


}

