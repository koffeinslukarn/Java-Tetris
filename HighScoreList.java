package se.liu.davah125.tetris;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class HighScoreList {
    private List<HighScore> highScores;

    private static final String FILE_PATH = System.getProperty("user.home") + "/TetrisHighScores.json";
    private static final String FAKE_PATH = System.getProperty("user.home") + "/TetrisHighScores.txt";
    private boolean foundFilePath = true;


    public HighScoreList(final List<HighScore> highScores) {
	this.highScores = highScores;
	try {
	    loadHighScores();
	}catch (IOException e) {
	    // Catches Exception and makes viable a display of it.
	    JOptionPane.showMessageDialog(null, e);
	}
    }


    public void highScoreWithoutFile(){
	highScores = new ArrayList<>();
    }

    private void sortHighScores() {
	highScores.sort(new ScoreComparator());
    }

    public void addHighScore(HighScore highScore) {
	final int maxShowHighscore = 10;
	// Om Highscore har mindre än 10 läggs det endast till och sedan stannar.
	if (highScores.size() < maxShowHighscore) {
	    highScores.add(highScore);
	    sortHighScores();
	    try {
		saveHighScores();
	    } catch (IOException e) {
		JOptionPane.showMessageDialog(null, e);
	    }
	    return;
	}

	// Om listan redan har 10 highscores, kolla om den nya poängen är högre än den sämsta
	HighScore lowestHighScore = highScores.getLast();

	if (highScore.getScore() > lowestHighScore.getScore()) {
	    highScores.remove(lowestHighScore);
	    highScores.add(highScore);
	    sortHighScores();
	    try {
		saveHighScores();
	    }catch (IOException e) {
		JOptionPane.showMessageDialog(null, e);
	    }
	}
    }

    public List<HighScore> getHighScores() {
	return highScores;
    }

    public void saveHighScores() throws IOException {
	if (!foundFilePath) {
	    throw new IOException("Cant't save high scores to file");
	}

	File file = new File(FILE_PATH);

	if(!file.canWrite()) {
	    // if file exists but can't be edited.
	    // om chmod 000 aktiverat kan vi ej redigera filen.
	    throw new IOException("HighScores cant be saved to file");
	}

	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
	    writer.println(gson.toJson(this));
	} catch (IOException e) {
	    JOptionPane.showMessageDialog(null, e);
	}

    }

    public void loadHighScores() throws IOException {
	File file = new File(FILE_PATH);

	if (!file.exists() || !file.canRead()) {
	    createNewHighScoreFile(); // skapr ny fil och skickat meddelande att det måste skapas ny.
	    throw new IOException("TetrisHighScores.json not found skapar ny");
	} else foundFilePath = true;

	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
	    HighScoreList loadedScores = new Gson().fromJson(reader, HighScoreList.class);
	    if (loadedScores != null && loadedScores.highScores != null) {
		this.highScores = loadedScores.highScores;

	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

    public void createNewHighScoreFile() {
	File file = new File(FILE_PATH);
	// create new file if it doesn't already exist. Special case if chmod 000.
	try (FileWriter writer = new FileWriter(file, false)) {
	    this.highScores = new ArrayList<>();
	    saveHighScores();
	} catch (IOException e) {
	    JOptionPane.showMessageDialog(null, "Failed to create new highscore file: " + e.getMessage());
	}
    }


    @Override public String toString() {
	return "HighScoreList{" + "highScores=" + getHighScores() + '}';
    }
}
