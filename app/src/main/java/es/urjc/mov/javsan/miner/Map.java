package es.urjc.mov.javsan.miner;

import java.util.Random;


public class Map {

    public final int ROWS;
    public final int COLS;
    public final int EASY;
    public static final int RADIUS = 1;

    private Square[][] map;
    private Random rand;
    private int seed;

    Map(Point limits, int s, int e) {
        EASY = e;
        ROWS = limits.getRow();
        COLS = limits.getField();
        seed = s;

        rand = new Random(seed);
        map = new Square[ROWS][COLS];
    }

    public int restart() {
        int moves = 0;

        for (int i = 0 ; i < ROWS ; i++) {
            for (int j = 0; j < COLS; j++) {

                boolean mine = rand.nextLong() % EASY == 0;
                Point p  =  new Point(i , j);

                map[i][j] = new Square(p , mine);

                if (!mine) {
                    moves++;
                }
            }
        }
        newSeed();

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
            for (int j = 0; j < COLS; j++) {
                Point np = new Point(i , j);
                if (paint[i][j] && !isInvPoint(p , np)) {
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

    @Override
    public String toString() {
        String result = "";

        for (int i = 0 ; i < ROWS ; i++) {
            for (int j = 0; j < COLS; j++) {
                Point p = new Point(i , j);
                if (map[i][j].isMine()) {
                    result += String.format(" %5d ", -1);
                } else {
                    result += String.format(" %5d ", getMines(p));
                }
            }
            result += String.format("%c", '\n');
        }

        return result;
    }

    public int getSeed() {
        return seed;
    }

    private void newSeed() {
        seed++;
        rand.setSeed(seed);
    }

    private boolean[][] initPaint() {
        boolean[][] paint = new boolean[ROWS][COLS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                paint[i][j] = false;
            }
        }
        return paint;
    }

    private boolean isEndFillOut(boolean[][] paint , Point p) {
        return isOutMap(p) || isMine(p) || !isHidden(p) || paint[p.getRow()][p.getField()];
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

    private boolean isMine(Square s) {
        return s.isMine();
    }

    private boolean isMaxLim(Point p) {
        return p.getRow() >= ROWS || p.getField() >= COLS;
    }

    private boolean isMinLim(Point p) {
        return p.getField() < 0 || p.getRow() < 0;
    }

    private boolean isOutMap(Point p) {
        return isMaxLim(p) || isMinLim(p);
    }

}
