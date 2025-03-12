package se.liu.davah125.tetris;

import java.util.Random;

public class TetrominoMaker {

    private static final int NUMBER_OF_TYPES = 7;
    private final static Random RANDOM = new Random();


    public int getNumberOfTypes(){
        return NUMBER_OF_TYPES;
    }


    public Poly getPoly(int n){
        // Creates different Polys depending on number given.
        switch (n){
            case 0 -> {return createI();}
            case 1 -> {return createO();}
            case 2 -> {return createT();}
            case 3 -> {return createS();}
            case 4 -> {return createZ();}
            case 5 -> {return createJ();}
            case 6 -> {return createL();}
            default -> {throw new IllegalArgumentException("Index out of range " + n);}
        }
    }

    private Poly createI(){
        SquareType[][] shape = {
                {SquareType.EMPTY, SquareType.I, SquareType.EMPTY, SquareType.EMPTY},
                {SquareType.EMPTY, SquareType.I, SquareType.EMPTY, SquareType.EMPTY},
                {SquareType.EMPTY, SquareType.I, SquareType.EMPTY, SquareType.EMPTY},
                {SquareType.EMPTY, SquareType.I, SquareType.EMPTY, SquareType.EMPTY},
        };
        return new Poly(shape);
    }

    private Poly createO(){
        SquareType[][] shape = {
                {SquareType.O, SquareType.O},
                {SquareType.O, SquareType.O}

        };
        return  new Poly(shape);
    }

    public Poly createT(){
        SquareType[][] shape = {
                {SquareType.EMPTY, SquareType.T, SquareType.EMPTY},
                {SquareType.T, SquareType.T, SquareType.T},
                {SquareType.EMPTY, SquareType.EMPTY, SquareType.EMPTY}
        };
        return  new Poly(shape);
    }

    private Poly createS() {
        SquareType[][] shape = {
                { SquareType.EMPTY, SquareType.S, SquareType.S },
                { SquareType.S, SquareType.S, SquareType.EMPTY },
                { SquareType.EMPTY, SquareType.EMPTY, SquareType.EMPTY}
        };
        return new Poly(shape);
    }

    private Poly createZ() {
        SquareType[][] shape = {
                { SquareType.Z, SquareType.Z, SquareType.EMPTY },
                { SquareType.EMPTY, SquareType.Z, SquareType.Z },
                { SquareType.EMPTY, SquareType.EMPTY, SquareType.EMPTY}
        };
        return new Poly(shape);
    }

    private Poly createJ() {
        SquareType[][] shape = {
                { SquareType.J, SquareType.EMPTY, SquareType.EMPTY },
                { SquareType.J, SquareType.J, SquareType.J },
                { SquareType.EMPTY, SquareType.EMPTY, SquareType.EMPTY}
        };
        return new Poly(shape);
    }

    private Poly createL() {
        SquareType[][] shape = {
                { SquareType.EMPTY, SquareType.EMPTY, SquareType.L},
                { SquareType.L, SquareType.L, SquareType.L},
                { SquareType.EMPTY, SquareType.EMPTY, SquareType.EMPTY}
        };
        return new Poly(shape);

    }

    public Poly getRandomPoly() {
        int randomIndex = RANDOM.nextInt(NUMBER_OF_TYPES);
        return getPoly(randomIndex);
    }
}
