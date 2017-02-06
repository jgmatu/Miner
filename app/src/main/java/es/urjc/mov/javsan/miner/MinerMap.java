package es.urjc.mov.javsan.miner;

import android.widget.ImageButton;

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

    private Square[][] map;
    private ImageButton[][] images;
    private int sqHiddens;
    private boolean test;

    // Create new mine map and generate
    // the matrix images to set all them
    // in the UI method in the activity...
    MinerMap(int seed) {
        EASY = 14;
        ROWS = 9;
        FIELDS = 10;

        map = createMinesMap(seed);
        images = new ImageButton[ROWS][FIELDS];
        test = false;
    }

    MinerMap(int seed, int easy, Point maxPoint, boolean isTest) {
        EASY = easy;
        ROWS = maxPoint.getRow();
        FIELDS = maxPoint.getField();

        map = createMinesMap(seed);
        images = new ImageButton[ROWS][FIELDS];
        test = isTest;
    }

    public Square[][] getMap() {
        return map;
    }

    public boolean isLostMap() {
        return sqHiddens == MinerActivity.LOST;
    }

    public boolean isWinner() {
        return sqHiddens == 0;
    }

    public boolean isMine(Point point) {
        return map[point.getRow()][point.getField()].isMine();
    }

    public boolean isInvSquare(Point pMap, Point pOff) {
        return isOutMap(pMap) || pOff.isCenter();
    }

    public void setImage(ImageButton image, Point point) {
        // Put the image to the matrix of map images...
        // This method is called in the creation of UI to
        // set the images of the miner map class...
        this.images[point.getRow()][point.getField()] = image;
    }

    public int getSqHiddens() {
        return sqHiddens;
    }

    public void setSqHiddens(int sqH) {
        if (sqHiddens >= 0) {
            // Decrease to square without mine or
            // set the square to new match..
            this.sqHiddens = sqH;

        } else if (sqHiddens == MinerActivity.LOST) {
            // We have got a fail in the game...
            // the player lost the game...
            this.sqHiddens = MinerActivity.LOST;

        }
    }

    public void chImgNoMine(Point p) {
        modMap(p);
        if (!test) {
            modImages(p);
        }
    }

    public void showMapLost(Point p) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < FIELDS; j++) {
                Point np = new Point(i, j);
                if (p.equals(np)) {
                    // Image of square checked with failed mine...
                    images[i][j].setImageResource(R.mipmap.mine_fail);
                    continue;
                }
                if (map[i][j].isMine()) {
                    // Image of square with mine...
                    images[i][j].setImageResource(R.mipmap.mine);
                } else {
                    // Image of square without mine..
                    chImgNoMine(np);
                }
            }
        }
    }

    public void fill(Point p) {
        boolean[][] paint = fillOut(p, initPaint());

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < FIELDS; j++) {
                if (paint[i][j]) {
                    chImgNoMine(new Point(i, j));
                }
            }
        }
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

    private boolean isHidden(Point p) {
        return map[p.getRow()][p.getField()].isHidden();
    }

    private void modImages(Point p) {
        Mines mines = new Mines(this, p);
        if (mines.clear()) {
            // The image is now visible this image has not mines near...
            images[p.getRow()][p.getField()].setImageResource(R.mipmap.square_yellow);
        } else {
            // The image is now visible with mines nears...
            images[p.getRow()][p.getField()].setImageResource(R.mipmap.mines1 + mines.getMines() - 1);
        }
    }

    private void modMap(Point p) {
        if (map[p.getRow()][p.getField()].isHidden()) {
            // The square is now visible we change always the
            // image hidden by other...
            this.setSqHiddens(sqHiddens - 1);
        }
        // The image is change to visible...
        map[p.getRow()][p.getField()].setHidden(false);
    }

    private Square[][] createMinesMap(int seed) {
        Square[][] m = new Square[ROWS][FIELDS];
        Random random = new Random();

        random.setSeed(seed);
        sqHiddens = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < FIELDS; j++) {

                m[i][j] = new Square(new Point(i, j), random.nextLong() % EASY == 0);
                if (!m[i][j].isMine()) {
                    sqHiddens++;
                }
                m[i][j].setHidden(true);
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
        if (paint[p.getRow()][p.getField()] || isMine(p) || !isHidden(p)) {
            return paint;
        }
        paint[p.getRow()][p.getField()] = true;
        Mines mines = new Mines(this, p);
        if (mines.warning()) {
            return paint;
        }
        for (int i = 1; i >= -1; i--) {
            for (int j = 1; j >= -1; j--) {
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
