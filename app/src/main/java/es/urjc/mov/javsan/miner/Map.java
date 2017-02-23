package es.urjc.mov.javsan.miner;

import java.util.Random;

/**
 * Created by javsan on 23/02/17.
 */

public class Map {
    public final int ROWS;
    public final int FIELDS;
    public final int EASY;
    public static final int RADIUS = 1;

    private Square[][] map;
    private Random rand;
    private int seed;

    Map(Point limits, int s, int e) {
        EASY = e;
        ROWS = limits.getRow();
        FIELDS = limits.getField();

        seed = s;

        rand = new Random(seed);
        map = new Square[ROWS][FIELDS];
    }

    public int restart() {
        int moves = 0;

        rand.setSeed(seed);
        seed++;

        for (int i = 0 ; i < ROWS ; i++) {
            for (int j = 0 ; j < FIELDS ; j++) {
                map[i][j] = new Square(new Point(i , j), rand.nextLong() % EASY == 0);
                if (!map[i][j].isMine()) {
                    moves++;
                }
                map[i][j].hidden();
            }
        }
        return moves;
    }

    public boolean isMine(Point p) {
        if (isOutMap(p)) {
            return false;
        }
        return map[p.getRow()][p.getField()].isMine();
    }

    public boolean[][] fill(Point p) {
        boolean[][] paint = fillOut(p, initPaint());

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < FIELDS; j++) {
                Point np = new Point(i , j);
                if (paint[i][j]) {
                    changeVisible(np);
                }
            }
        }
        return paint;
    }

    public int getMines(Point p) {
        int mines = 0;

        for (int i = RADIUS; i >= -RADIUS; i--) {
            for (int j = RADIUS; j >= -RADIUS; j--) {
                Point pMap = new Point(i + p.getRow(), j + p.getField());
                Point pOff = new Point(i, j);
                if (isInvPoint(pMap, pOff)) {
                    continue;
                }
                if (isMine(map[p.getRow() + i][p.getField() + j])) {
                    mines++;
                }
            }
        }
        return mines;
    }

    public void changeVisible (Point p) {
        if (isOutMap(p)) {
            return;
        }
        if (map[p.getRow()][p.getField()].isHidden()) {
            map[p.getRow()][p.getField()].visible();
        }
    }

    public boolean isHidden(Point p) {
        return map[p.getRow()][p.getField()].isHidden();
    }

    private boolean isMine(Square s) {
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

    private boolean[][] initPaint() {
        boolean[][] paint = new boolean[ROWS][FIELDS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < FIELDS; j++) {
                paint[i][j] = false;
            }
        }
        return paint;
    }

    private boolean isEndFillOut(boolean[][] paint , Point p) {
        return isOutMap(p) || paint[p.getRow()][p.getField()] || isMine(p) || !isHidden(p);
    }

    private boolean[][] fillOut(Point p, boolean[][] paint) {
        if (isEndFillOut(paint, p)) {
            return paint;
        }
        paint[p.getRow()][p.getField()] = true;

        if (getMines(p) > 0) {
            return paint;
        }

        for (int i = RADIUS; i >= -RADIUS; i--) {
            for (int j = RADIUS; j >= -RADIUS; j--) {
                Point pMap = new Point(i + p.getRow(), j + p.getField());
                Point pOff = new Point(i, j);
                if (isInvPoint(pMap, pOff)) {
                    continue;
                }
                int row = p.getRow() + i;
                int field = p.getField() + j;
                paint = fillOut(new Point(row, field), paint);
            }
        }
        return paint;
    }

    private boolean isInvPoint(Point pMap, Point pOff) {
        return isOutMap(pMap) || pOff.isCenter();
    }
}
