package es.urjc.mov.javsan.miner;

import android.util.Log;

import java.util.ArrayList;

class MinerGame {

    private static final int LOST = -1;
    private static final int FACTOR = 50;

    private final int ROWS;
    private final int COLS;

    private Map map;
    private int numMoves;
    private ArrayList<Integer> movesGame;
    private int score;
    private int savedScore;
    private int seed;
//     private int savedScore;

    MinerGame(Point limits , int s, int e) {
        map = new Map(limits, e);
        seed = s;
        numMoves = map.restart(seed);
        ROWS = limits.getRow();
        COLS = limits.getCol();
        movesGame = new ArrayList<>();
    }

    public void restart() {
        if (!isWinGame()) {
            score = savedScore - score;
        }
        seed++;
        numMoves = map.restart(seed);
        movesGame.clear();
    }

    public boolean isLostGame() {
        return numMoves == LOST;
    }

    public boolean isWinGame() {
        return numMoves == 0;
    }

    public boolean isEndGame() {
        return isWinGame() || isLostGame();
    }

    public void setLostGame(Point p) {
        score = 0;
        movesGame.add(p.getRow() * COLS + p.getCol());
        this.numMoves = LOST;
    }

    public int numMoves() {
        return numMoves;
    }

    public void move(Point p) {
        if (map.isHidden(p)) {
            score += FACTOR * getMines(p);
            numMoves--;
        }
        map.changeVisible(p);
        movesGame.add(p.getRow() * COLS + p.getCol());
        if (isWinGame()) {
            savedScore = score;
        }
    }

    public ArrayList<Integer> savedMoves() {
        return movesGame;
    }

    public boolean[][] fillSquares(Point p) {
        return movesFilled(map.getFillSquares(p));
    }

    public boolean isFail(Point p) {
        return map.isMine(p);
    }

    public int getMines (Point p) {
        return map.getMines(p);
    }

    public boolean isHidden(Point p) {
        return map.isHidden(p);
    }

    public int getSeed() {
        return seed;
    }

    @Override
    public String toString() {
        String result = "";

        result += String.format("Num Moves : %d\n", numMoves);
        result += map.toString();

        return result;
    }

    public int getRows() {
        return ROWS;
    }

    public int getCols() {
        return COLS;
    }

    public int getSavedScore() {
        return savedScore;
    }

    public int getScore () {
        return score;
    }

    private boolean[][] movesFilled(boolean[][] fill) {
        for (int i = 0; i < ROWS ; i++) {
            for (int j = 0 ; j < COLS; j++){
                if (fill[i][j]) {
                    this.move(new Point(i, j));
                }
            }
        }
        return fill;
    }
}
