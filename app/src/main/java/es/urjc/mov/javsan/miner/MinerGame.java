package es.urjc.mov.javsan.miner;

import java.util.ArrayList;

/**
 * Created by javi on 5/02/17..
 * MinerGame class... images an map booleans of mines and Squares...
 */

public class MinerGame {

    private static final int LOST = -1;
    private final int ROWS;
    private final int COLS;

    private Map map;
    private int numMoves;
    private ArrayList<Integer> movesGame;

    // Create new mine map and generate
    // the matrix images to set all them
    // in the UI method in the activity...
    MinerGame(Point limit , int s, int e) {
        map = new Map(limit, s, e);
        numMoves = map.restart();
        ROWS = limit.getRow();
        COLS = limit.getField();
        movesGame = new ArrayList<>();
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
        movesGame.add(p.getRow() * COLS + p.getField());
        this.numMoves = LOST;
    }

    public int numMoves() {
        return numMoves;
    } // Method only in test...

    public void move(Point p) {
        if (map.isHidden(p)) {
            numMoves--;
        }
        map.changeVisible(p);
        movesGame.add(p.getRow() * COLS + p.getField());
    }

    public ArrayList<Integer> saveMoves() {
        return movesGame;
    }

    public void restart() {
        numMoves = map.restart();
        movesGame.clear();
    }

    public boolean[][] empty(Point p) {
        boolean[][] fill = map.fill(p);

        numMoves -= fillMoves(fill);
        return fill;
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
        return map.getSeed();
    }

    @Override
    public String toString() {
        String result = "";

        result += String.format("Num Moves : %d\n", numMoves);
        result += map.toString();

        return result;
    }

    private int fillMoves(boolean[][] fill) {
        int moves = 0;

        for (int i = 0; i < ROWS ; i++) {
            for (int j = 0 ; j < COLS; j++){
                Point p = new Point(i , j);
                if (fill[i][j]) {
                    move(p);
                    moves++;
                }
            }
        }
        return moves;
    }
}
