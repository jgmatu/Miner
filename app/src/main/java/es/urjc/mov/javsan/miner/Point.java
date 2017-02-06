package es.urjc.mov.javsan.miner;

/**
 * Created by javi on 5/02/17.
 */

public class Point {
    private int row;
    private int field;

    Point(int pRow, int pField) {
        row = pRow;
        field = pField;
    }

    public int getField() {
        return field;
    }

    public int getRow() {
        return row;
    }

    public boolean isCenter() {
        return row == 0 && field == 0;
    }

    public boolean equals(Point p) {
        return row == p.getRow() && field == p.getField();
    }

    @Override
    public String toString() {
        return String.format("(%d , %d)", row, field);
    }
}
