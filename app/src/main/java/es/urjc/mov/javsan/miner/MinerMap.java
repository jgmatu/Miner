package es.urjc.mov.javsan.miner;

import android.util.Log;

import java.util.Random;

/**
 * Created by javi on 5/02/17..
 * MinerMap class... images an map booleans of mines and Squares...
 */

public class MinerMap {

    private static final String TAG = "Miner";
    public final int ROWS;
    public final int FIELDS;
    public final int EASY;
    public static final int LOST = -1;
    public static final int RADIOUS = 1;

    private Square[][] map;
    private int sqMoves;
    private int seed;

    // Create new mine map and generate
    // the matrix images to set all them
    // in the UI method in the activity...
    MinerMap(int s, int easy, Point maxPoint) {
        EASY = easy;
        seed = s;
        ROWS = maxPoint.getRow();
        FIELDS = maxPoint.getField();
        map = createMinesMap(seed);
    }

    public boolean isLostMap() {
        return sqMoves == LOST;
    }

    public void setLostGame() {
        this.sqMoves = LOST;
    }

    public boolean isWinner() {
        return sqMoves == 0;
    }

    public boolean isMine(Point p) {
        if (isOutMap(p)) {
            return false;
        }
        return map[p.getRow()][p.getField()].isMine();
    }

    public boolean isInvSquare(Point pMap, Point pOff) {
        return isOutMap(pMap) || pOff.isCenter();
    }

    public int getMoves() {
        return sqMoves;
    }

    public boolean[][] fill(Point p) {
        boolean[][] paint = fillOut(p, initPaint());

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < FIELDS; j++) {
                Point np = new Point(i , j);
                if (paint[i][j]) {
                    modMapNoMine(np);
                }
            }
        }
        return paint;
    }

    public void modMapNoMine(Point p) {
        if (isOutMap(p)) {
            return;
        }
        if (map[p.getRow()][p.getField()].isHidden()) {
            // The square is now visible...
            if (sqMoves > 0) {
                sqMoves--;
            }
        }
        // The Square is change to visible...
        map[p.getRow()][p.getField()].visible();
    }


    public void restart() {
        Random random = new Random();
        seed++;
        random.setSeed(seed);

        sqMoves = 0;
        for (int i = 0 ; i < ROWS ; i++) {
            for (int j = 0 ; j < FIELDS ; j++) {
                map[i][j] = new Square(new Point(i , j), random.nextLong() % EASY == 0);
                if (!map[i][j].isMine()) {
                    sqMoves++;
                }
                map[i][j].hidden();
            }
        }
    }

    public int getMines(Point p) {
        int mines = 0;

        for (int i = RADIOUS; i >= -RADIOUS; i--) {
            for (int j = RADIOUS; j >= -RADIOUS; j--) {
                Point pMap = new Point(i + p.getRow(), j + p.getField());
                Point pOff = new Point(i, j);
                if (isInvSquare(pMap, pOff)) {
                    continue;
                }
                if (thIsMine(map[p.getRow() + i][p.getField() + j])) {
                    mines++;
                }
            }
        }
        return mines;
    }

    public boolean isEndGame() {
        return isWinner() || isLostMap();
    }

    public boolean isHidden(Point p) {
        return map[p.getRow()][p.getField()].isHidden();
    }

    private boolean thIsMine(Square s) {
        return s.isMine();
    }

    private boolean isMaxLim(Point p) {
        return p.getRow() >= ROWS || p.getField() >= FIELDS;
    }

    private boolean isMinLim(Point p) {
        return p.getField() < 0 || p.getRow() < 0;
    }

    private boolean isOutMap(Point p) {
        return isMaxLim(p) || isMinLim(p);
    }

    private Square[][] createMinesMap(int seed) {
        Square[][] m = new Square[ROWS][FIELDS];
        Random random = new Random();

        random.setSeed(seed);
        sqMoves = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < FIELDS; j++) {

                m[i][j] = new Square(new Point(i, j), random.nextLong() % EASY == 0);
                if (!m[i][j].isMine()) {
                    sqMoves++;
                }
                m[i][j].hidden();
            }
        }
        return m;
    }

    private boolean[][] initPaint() {
        boolean[][] paint = new boolean[ROWS][FIELDS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < FIELDS; j++) {
                paint[i][j] = false;
            }
        }
        return paint;
    }

    private boolean[][] fillOut(Point p, boolean[][] paint) {
        if (isOutMap(p)) {
            return paint;
        }
        if (paint[p.getRow()][p.getField()] || isMine(p) || !isHidden(p)) {
            return paint;
        }
        paint[p.getRow()][p.getField()] = true;

        if (getMines(p) > 0) {
            return paint;
        }
        for (int i = RADIOUS; i >= -RADIOUS; i--) {
            for (int j = RADIOUS; j >= -RADIOUS; j--) {
                Point pMap = new Point(i + p.getRow(), j + p.getField());
                Point pOff = new Point(i, j);
                if (isInvSquare(pMap, pOff)) {
                    continue;
                }
                int row = p.getRow() + i;
                int field = p.getField() + j;
                paint = fillOut(new Point(row, field), paint);
            }
        }
        return paint;
    }
}
