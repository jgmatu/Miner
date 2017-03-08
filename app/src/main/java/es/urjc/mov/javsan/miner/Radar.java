package es.urjc.mov.javsan.miner;

class Radar {

    private int numRadars;
    private boolean active;
    private RadarSound radarSound;

    Radar(int nRadar, RadarSound rS) {
        restart(nRadar);
        radarSound = rS;
    }

    Radar (int nRadar) {
        restart(nRadar);
        radarSound = null;
    }

    void restart(int nRadar) {
        numRadars = 0;
        if (nRadar > 0) {
            numRadars = nRadar;
        }
        active = false;
    }

    boolean[][] setScan (MinerGame game) {
        numRadars--;
        playSound();
        return getScan(game);
    }

    boolean[][] setClean (MinerGame game) {
        stopSound();
        return getScan(game);
    }

    int getNumRadars () {
        return numRadars;
    }

    boolean isEnable() {
        return numRadars > 0 && !active;
    }

    private void playSound() {
        if (radarSound != null){
            radarSound.play();
        }
    }

    private void stopSound() {
        if (radarSound != null) {
            radarSound.stop();
        }
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
