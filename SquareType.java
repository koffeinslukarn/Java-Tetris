package se.liu.davah125.tetris;

import java.util.Random;

public enum SquareType {
    EMPTY, I, O, T, S, Z, J, L, OUTSIDE;

    public static void main(String[] args) {
        Random rnd = new Random();
        SquareType[] squares = SquareType.values();
        for (int i = 0; i < 25; i++) {
            SquareType randomType = squares[rnd.nextInt(squares.length)];
            System.out.println(randomType);
        }
    }



}



