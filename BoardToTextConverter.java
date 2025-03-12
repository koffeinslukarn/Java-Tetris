package se.liu.davah125.tetris;

public class BoardToTextConverter {

    public String convertToText(Board board){
	StringBuilder tetrisPlayfield = new StringBuilder();

	for (int i = 0; i < board.getHeight(); i++) {
	    for (int j = 0; j < board.getWidth(); j++) {
		SquareType square = board.getVisibleSquareAt(j,i);
		switch (square){
		    case I -> tetrisPlayfield.append("# ");
		    case J -> tetrisPlayfield.append("J ");
		    case EMPTY -> tetrisPlayfield.append("* ");
		    case O -> tetrisPlayfield.append("O ");
		    case T -> tetrisPlayfield.append("T ");
		    case S -> tetrisPlayfield.append("S ");
		    case Z -> tetrisPlayfield.append("Z ");
		    case L -> tetrisPlayfield.append("L ");
		    case OUTSIDE -> tetrisPlayfield.append("B ");
		    default -> tetrisPlayfield.append("Nothing ");
		}
	    }
	    tetrisPlayfield.append("\n");
	}
	return tetrisPlayfield.toString();
    }
}






