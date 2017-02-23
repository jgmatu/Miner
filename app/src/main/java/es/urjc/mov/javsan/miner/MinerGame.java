package es.urjc.mov.javsan.miner;

import java.util.Random;

import javax.sql.RowSet;

import static es.urjc.mov.javsan.miner.MinerActivity.ROWS;

/**
 * Created by javi on 5/02/17..
 * MinerGame class... images an map booleans of mines and Squares...
 */

public class MinerGame {

    private static final String TAG = "Miner";

    private static final int LOST = -1;
    private final int ROWS;
    private final int COLS;

    private Map map;
    private int numMoves;

    // Create new mine map and generate
    // the matrix images to set all them
    // in the UI method in the activity...
    MinerGame(Point limit , int s, int e) {
        map = new Map(limit, s, e);
        numMoves = map.restart();
        ROWS = limit.getRow();
        COLS = limit.getField();
    }

    public boolean isLostMap() {
        return numMoves == LOST;
    }

    public boolean isWinner() {
        return numMoves == 0;
    }

    public boolean isEndGame() {
        return isWinner() || isLostMap();
    }

    public void setLostGame() {
        this.numMoves = LOST;
    }

    public int numMoves() {
        return numMoves;
    }

    public void changeVisible(Point p) {
        map.changeVisible(p);
        discountMove();
    }

    public void restart() {
        numMoves = map.restart();
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

    private int fillMoves(boolean[][] fill) {
        int moves = 0;

        for (int i = 0; i < ROWS ; i++) {
            for (int j = 0 ; j < COLS; j++){
                if (fill[i][j]) {
                    moves++;
                }
            }
        }
        return moves;
    }

    private void discountMove() {
        if (numMoves > 0) {
            numMoves--;
        }
    }
}
