package se.liu.davah125.tetris;


public class Poly {
    public SquareType[][] shape;
    private int width;
    private int height;



    public Poly(SquareType[][] shape) {
	this.shape = shape;
	this.height = shape.length;
	this.width = shape[0].length;

    }

    public SquareType[][] getShape() {
	return shape;
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

    public SquareType getSquareType(int x, int y){
	return shape[y][x];
    }



    public Poly rotateRight() {

	Poly newPoly = new Poly(new SquareType[height][height]);

	for (int r = 0; r < height; r++) {
	    for (int c = 0; c < height; c++){
		newPoly.shape[c][height-1-r] = this.shape[r][c];
	    }
	}
	return newPoly;
    }

    public Poly rotateLeft() {
	Poly leftNewPoly = new Poly(new SquareType[height][height]);

	for (int r = 0; r < height; r++) {
	    for (int c = 0; c < height; c++) {
		leftNewPoly.shape[height - 1 - c][r] = this.shape[r][c];  // Vänsterrotation
	    }
	}

	return leftNewPoly;
    }


}
