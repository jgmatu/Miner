package es.urjc.mov.javsan.miner;

/**
 * Created by javi on 6/02/17.
 */

public class Mines {
    private int mines;

    Mines(MinerMap mapper, Point p) {
        Square[][] map = mapper.getMap();

        for (int i = 1; i >= -1; i--) {
            for (int j = 1; j >= -1; j--) {
                Point pMap = new Point(i + p.getRow(), j + p.getField());
                Point pOff = new Point(i, j);
                if (mapper.isInvSquare(pMap, pOff)) {
                    continue;
                }
                if (map[p.getRow() + i][p.getField() + j].isMine()) {
                    mines++;
                }
            }
        }
    }

    public int getMines() {
        return mines;
    }

    public boolean clear() {
        return mines == 0;
    }

    public boolean warning() {
        return mines > 0;
    }

}
