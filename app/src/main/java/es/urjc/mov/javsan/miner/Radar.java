package es.urjc.mov.javsan.miner;

import java.util.ArrayList;

class Radar {

    private final int MAXRADARS;

    static class STATES {
        static final int UNUSED = 1;
        static final int USED = 2;
        static final int ACTIVED = 3;
    };

    private int numRadars;
    private boolean active;
    private ArrayList<Integer> actives;
    private int nActive;

    Radar(int nRadar) {
        restart(nRadar);
        MAXRADARS = nRadar;
    }

    public void restart(int nRadar) {
        numRadars = 0;
        nActive = 0;
        if (nRadar > 0) {
            numRadars = nRadar;
        }
        actives = initActives();
        active = false;
    }

    public boolean[][] setScan (MinerGame game) {
        numRadars--;
        active = true;
        actives.set(nActive, STATES.ACTIVED);
        return getScan(game);
    }

    public boolean[][] setClean (MinerGame game) {
        if (active && nActive < MAXRADARS) {
            actives.set(nActive, STATES.USED);
            nActive++;
        }
        active = false;
        return getScan(game);
    }

    public int getNumRadars () {
        return numRadars;
    }

    public boolean isEnable() {
        return numRadars > 0 && !active;
    }

    public ArrayList<Integer> getRadarsUsed() {
        return actives;
    }

    public String getState() {
        String result = "";

        for (int i = 0 ; i < MAXRADARS; i++) {
            switch (actives.get(i)) {
                case STATES.ACTIVED :
                    result += String.format("%s\n", "ACTIVED");
                    break;
                case STATES.UNUSED :
                    result += String.format("%s\n", "UNUSED");
                    break;
                case STATES.USED:
                    result += String.format("%s\n", "USED");
                    break;
                default:
            }
        }
        return result;
    }

    private ArrayList<Integer> initActives() {
        ArrayList<Integer> actives = new ArrayList<>(numRadars);

        for (int i = 0 ; i < numRadars ; i++) {
            actives.add(STATES.UNUSED);
        }
        return actives;
    }

    private boolean[][] initScan(MinerGame game) {
        boolean[][] scan = new boolean[game.getRows()][game.getCols()];
        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0 ; j < game.getCols(); j++){
                scan[i][j] = false;
            }
        }
        return scan;
    }

    private boolean[][] getScan(MinerGame game) {
        boolean[][] isScan = initScan(game);

        for (int i = 0 ; i < MinerActivity.ROWS ; i++) {
            for (int j = 0; j < MinerActivity.COLUMNS; j++) {
                Point p = new Point(i , j);
                if (game.isFail(p)) {
                    isScan[i][j] = true;
                }
            }
        }
        return isScan;
    }
}
