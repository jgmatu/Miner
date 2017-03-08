package es.urjc.mov.javsan.miner;


class Point {
    private int row;
    private int field;

    Point(int pRow, int pField) {
        row = pRow;
        field = pField;
    }

    int getCol() {
        return field;
    }

    int getRow() {
        return row;
    }

    boolean isCenter() {
        return row == 0 && field == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point)) {
            return false;
        }
        Point p = (Point) o;
        return row == p.getRow() && field == p.getCol();
    }

    @Override
    public String toString() {
        return String.format("(%d , %d)", row, field);
    }
}
