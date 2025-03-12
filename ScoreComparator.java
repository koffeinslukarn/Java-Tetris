package se.liu.davah125.tetris;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScoreComparator implements Comparator<HighScore> {

    public int compare(HighScore o1, HighScore o2) {
	return Integer.compare(o2.getScore(), o1.getScore());
    }
}
