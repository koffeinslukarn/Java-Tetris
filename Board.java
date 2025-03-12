package se.liu.davah125.tetris;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Board {
    private SquareType[][] squares;
    private int width;
    private int height;
    private Poly falling = null;
    private Point fallingPos = null;
    private List<BoardListener> boardListener = new ArrayList<>();
    public final static int MARGIN = 2;
    public final static int DOUBLE_MARGIN = 2 * MARGIN;

    private int score = 0;
    private boolean firstClear = true;
    /*
    FirtClear is crucial to make game work, First placeFalling peace erases 4 rows, this boolean makes us ignore that and continue game.
     */
    public boolean playAgain = false; // Makes us only ans once if want to plat again.
    public boolean gameOver = false;

    private final static Random RND = new Random();

    private static final Map<Integer, Integer> POINT_MAP = Map.of(
	    // Map of possible Scores depending on rows erased.
	    1, 100,
	    2,300,
	    3,500,
	    4, 800,
	    5, 1100
    );



    public Board(int width, int height) {
	//Creates a Board of wanted sixe with standars Squaretype
	this.width = width;
	this.height = height;
	this.squares = new SquareType[height][width];
	for (int i = 0; i < height; i++) {
	    for (int j = 0; j < width; j++) {
		if (i <= MARGIN + 1 || i > height - 1 || j <= MARGIN + 1 || j > width - 1) {
		    squares[i][j] = SquareType.OUTSIDE; // Endast yttersta raderna och kolumnerna blir OUTSIDE
		} else {
		    squares[i][j] = SquareType.EMPTY;
		}
	    }
	}

	final TetrominoMaker maker = new TetrominoMaker();

	setFalling(maker.getRandomPoly(), new Point(width / 2, MARGIN));
	notifyListeners();
    }

    public void addBoardListener(BoardListener boardListener) {
	this.boardListener.add(boardListener);
    }

    private void notifyListeners() {
	for (BoardListener boardListener : boardListener) {
	    boardListener.boardChanged(this);
	}
    }


    public void setFalling(Poly polly, Point pos) {
	//Sets a falling SquareType to spesific position
	this.falling = polly;
	this.fallingPos = pos;
	notifyListeners();
    }


    public void randBoard(){
	//Converts all Squaretypes in board to random Squaretyypes.
	SquareType[] values = SquareType.values();
	for (int i = 0; i < height; i++) {
	    for (int j = 0; j < width; j++) {
		SquareType randomType = values[RND.nextInt(values.length)];
		setSquare(i,j, randomType);
		notifyListeners();
	    }
	}
    }


    public void setSquare(int row, int col, SquareType value) {
	//Sets specific SQUARETYPE on specifik place
	if (row < 0 || row >= height || col < 0 || col >= width) {
	    throw new IllegalArgumentException("Position (" + row + ", " + col + ") är utanför brädet!");
	}
	squares[row][col] = value;
	notifyListeners();
    }


    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

    public Poly getFalling() {
	return falling;
    }


    public Point getFallingPos() {
	return fallingPos;
    }


    public SquareType getVisibleSquareAt(int x, int y){
	// Gives SqureType of a falling Squaretype
	if (falling != null){
	    int x1 = fallingPos.x;
	    int y1 = fallingPos.y;
	    int x2 = x1 + falling.getWidth() - 1;
	    int y2 = y1 + falling.getHeight() - 1;

	    if (x >= x1 && x<= x2 && y >= y1 && y<= y2){
		SquareType fallingSquare = falling.getSquareType(x - x1, y - y1);
		if(fallingSquare != SquareType.EMPTY){
		    return  fallingSquare;
		}
	    }
	}
	return getSquare(y,x);
    }


    public SquareType getSquare(int row, int col) {
	// Gives a Square of specific position
	int foundRow = row + MARGIN;
	int foundCol = col + MARGIN;

	if ( foundRow < 0 || foundRow >= height || foundCol < 0 || foundCol >= width) {
	    return SquareType.OUTSIDE;
	}
	return squares[foundRow][foundCol];
    }


    @Override public String toString() {
	StringBuilder tetrisPlayfield = new StringBuilder();
	for (int i = 0; i < height; i++) {
	    for (int j = 0; j < width; j++) {
		tetrisPlayfield.append(getVisibleSquareAt(j, i)).append(" ");
	    }
	    tetrisPlayfield.append("\n");
	}
	return tetrisPlayfield.toString();
    }

    public void tick(){
	// Makes the game go forward
	if (falling == null || gameOver) return; // 🔹 Stoppar spelet om gameOver är sant

	fallingPos = new Point(fallingPos.x, fallingPos.y + 1);

	if (hasCollision()) {
	    placeFallingPiece();
	    spawnNewPiece();
	}
	notifyListeners();
    }


    public void move(Direction direction) {
	// Change position of Poly by given direction
	if (falling == null || gameOver) return;

	Point oldPos = fallingPos;

	if(direction == Direction.RIGHT){
	    if (hasCollision()) {
		fallingPos.x -= 1;
	    }
	    int newX = fallingPos.x + 1;
	    int newY = fallingPos.y;
	    fallingPos = new Point(newX, newY);

	}
	else if(direction == Direction.LEFT){
	    if (hasCollision()) {
		fallingPos.x += 1;
		return;
	    }
	    int newX = fallingPos.x - 1;
	    int newY = fallingPos.y;
	    fallingPos = new Point(newX, newY);
	}
	else if (direction == Direction.DOWN) {
	    if(hasCollision()) {
		fallingPos.y -= 1;
		placeFallingPiece();
		spawnNewPiece();
		return;
	    }
	    int newX = fallingPos.x;
	    int newY = fallingPos.y + 1;
	    fallingPos = new Point(newX, newY);
	}

	if (hasCollision()) {
	    fallingPos = oldPos; // Återställ om det blev kollision
	}
	notifyListeners();
    }


    public boolean hasCollision() {
	// Gives true if poly is about to collide with something
	if (falling == null) return false;

	for (int y = 0; y < falling.getHeight(); y++) {
	    for (int x = 0; x < falling.getWidth(); x++) {
		SquareType fallingSquare = falling.getSquareType(x, y);
		if (fallingSquare != SquareType.EMPTY) { // Endast icke-tomma rutor räknas
		    int boardX = fallingPos.x + x;
		    int boardY = fallingPos.y + y;

		    // Kontrollera om biten går utanför brädet
		    if (boardX < 0 || boardX >= width || boardY < 0 || boardY >= height) {
			return true;
		    }

		    // Kolla om rutan på brädet redan är upptagen
		    if (getSquare(boardY, boardX) != SquareType.EMPTY) {
			return true;
		    }
		}
	    }
	}
	return false;
    }


    private void placeFallingPiece() {
	// Places falling Poly at colliding point and erases falling.
	for (int y = 0; y < falling.getHeight(); y++) {
	    for (int x = 0; x < falling.getWidth(); x++) {
		SquareType type = falling.getSquareType(x, y);
		if (type != SquareType.EMPTY) {
		    int boardX = fallingPos.x + x + MARGIN;
		    int boardY = fallingPos.y + y + 1;
		    setSquare(boardY, boardX, type);
		}
	    }
	}
	clearRows();

	falling = null;
    }



    private void spawnNewPiece() {
	/* Spawn new Piece but this function has a collisoinshandler
	that can end the game.
	 */
	final TetrominoMaker maker = new TetrominoMaker();

	Poly newPiece = maker.getRandomPoly();

	Point startPos = new Point(width / 2 - 1, MARGIN);
	setFalling(newPiece, startPos);

	if (hasCollision()) {
	    gameOver = true;
	}
	notifyListeners();
    }


    public void rotate(Direction dir) {
	if (falling == null) return;

	Poly rotatedPoly;
	switch (dir) {
	    case ROTATE_RIGHT -> rotatedPoly = falling.rotateRight();
	    case ROTATE_LEFT -> rotatedPoly = falling.rotateLeft();
	    default -> {
		return;
	    }
	}

	Poly oldPoly = falling;
	falling = rotatedPoly;

	if (hasCollision()) {
	    falling = oldPoly;
	}
	notifyListeners();
    }

    public int getScore() {
	return score;
    }

    public void clearRows(){
	int numOfClear = 0;

	for (int y = height - 1; y >= 0; y--) {
	    if(isFullRow(y)){
		removeRow(y);
		numOfClear++; // stores scorevalue
		y++;
	    }
	}
	if(numOfClear > 0) {
	    if (firstClear) {
		// Gör att spelet fortsätter som vanligt.
		firstClear = false;
	    }
	    else {
	    score += POINT_MAP.get(numOfClear);
	    }
	    notifyListeners();
	}

    }


    private void removeRow(int row) {
	for (int y = row; y > 0; y--) {
	    for (int x = MARGIN; x < width; x++) {
		squares[y][x] = squares[y - 1][x];
	    }
	}

	for (int x = DOUBLE_MARGIN; x < width; x++) {
	    squares[0][x] = SquareType.EMPTY;
	}
    }

    private boolean isFullRow(int row) {
	for (int i = 0; i < width; i++) {
	    if (squares[row][i] == SquareType.EMPTY) {
		return false;
	    }
	}
	return true;
    }



    public static void main(String[] args) {
	Board board = new Board(10, 20);

	BoardToTextConverter converter = new BoardToTextConverter();
	String boardText = converter.convertToText(board);
	System.out.println(boardText);

    }
}

