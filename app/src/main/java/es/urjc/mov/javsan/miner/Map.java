package es.urjc.mov.javsan.miner;

import java.util.Random;


class Map {

    public final int ROWS;
    private final int COLS;
    private final int EASY;
    private static final int RADIUS = 1;

    private Square[][] map;
    private Random rand;

    Map(Point limits,  int e) {
        EASY = e;
        ROWS = limits.getRow();
        COLS = limits.getCol();

        rand = new Random(0);
        map = new Square[ROWS][COLS];
    }

    int restart(int s) {
        int moves = 0;

        rand.setSeed(s);
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
        return moves;
    }

    boolean isMine(Point p) {
        return !isOutMap(p) && map[p.getRow()][p.getCol()].isMine();
    }

    boolean[][] getFillSquares(Point p) {
        return fillOut(p, initPaint());
    }

    int getMines(Point p) {
        int mines = 0;

        for (int i = RADIUS; i >= -RADIUS; i--) {
            for (int j = RADIUS; j >= -RADIUS; j--) {
                Point pMap = new Point(i + p.getRow(), j + p.getCol());
                Point pOff = new Point(i, j);
                if (isInvPoint(pMap, pOff)) {
                    continue;
                }
                if (isMine(map[p.getRow() + i][p.getCol() + j])) {
                    mines++;
                }
            }
        }
        return mines;
    }

    void changeVisible (Point p) {
        if (isOutMap(p)) {
            return;
        }
        if (map[p.getRow()][p.getCol()].isHidden()) {
            map[p.getRow()][p.getCol()].visible();
        }
    }

    boolean isHidden(Point p) {
        return !isOutMap(p) && map[p.getRow()][p.getCol()].isHidden();
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
        return isOutMap(p) || isMine(p) || !isHidden(p) || paint[p.getRow()][p.getCol()];
    }

    private boolean[][] fillOut(Point p, boolean[][] paint) {
        if (isEndFillOut(paint, p)) {
            return paint;
        }
        paint[p.getRow()][p.getCol()] = true;

        if (getMines(p) > 0) {
            return paint;
        }

        for (int i = RADIUS; i >= -RADIUS; i--) {
            for (int j = RADIUS; j >= -RADIUS; j--) {
                Point pMap = new Point(i + p.getRow(), j + p.getCol());
                Point pOff = new Point(i, j);
                if (isInvPoint(pMap, pOff)) {
                    continue;
                }
                int row = p.getRow() + i;
                int field = p.getCol() + j;
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
        return p.getRow() >= ROWS || p.getCol() >= COLS;
    }

    private boolean isMinLim(Point p) {
        return p.getCol() < 0 || p.getRow() < 0;
    }

    private boolean isOutMap(Point p) {
        return isMaxLim(p) || isMinLim(p);
    }

}
