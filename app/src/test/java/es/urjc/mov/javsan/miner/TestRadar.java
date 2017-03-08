package es.urjc.mov.javsan.miner;

import org.junit.Test;

import static junit.framework.Assert.fail;

public class TestRadar {
    private static final int RADARS = 2;
    private static final int ROWS = 4;
    private static final int COLS = 4;
    private static final int SEED = 3;
    private static final int EASY = 2;

    @Test
    public void testScanRadar() {
        MinerGame game = new MinerGame(new Point(ROWS,COLS), SEED , EASY);
        Radar radar = new Radar(RADARS);

        for (int i = 0 ; i < RADARS; i++) {
            radar.setScan(game);
        }
        if (radar.isEnable()) {
            fail();
        }
    }


    @Test
    public void testCleanRadar() {
        MinerGame game = new MinerGame(new Point(ROWS, COLS), SEED, EASY);
        Radar radar = new Radar(RADARS);

        for (int i = 0 ; i < RADARS; i++) {
            radar.setClean(game);
        }
        if (!radar.isEnable()) {
            fail();
        }
    }

    @Test
    public void testRestartRadar() {
        MinerGame game = new MinerGame(new Point(ROWS, COLS), SEED, EASY);
        Radar radar = new Radar(RADARS);

        for (int i = 0 ; i < RADARS; i++) {
            radar.setScan(game);
        }
        radar.restart(RADARS);
        if (radar.getNumRadars() != RADARS) {
            fail();
        }
    }
}
