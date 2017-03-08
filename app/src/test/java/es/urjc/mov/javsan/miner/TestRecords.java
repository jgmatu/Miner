package es.urjc.mov.javsan.miner;

import org.junit.Test;

import java.util.Random;

/**
 * Created by javi on 5/03/17.
 */

public class TestRecords {
    private static final int ROWS = 10;
    private static final int COLS = 10;
    private static final int SEED = 10;
    private static final int EASY = 10;

    @Test
    public void testWinRecord() {
        MinerGame game = new MinerGame(new Point(ROWS, COLS), SEED, EASY);
        Random r = new Random(SEED);

        while (!game.isEndGame()) {
            Point p = new Point(r.nextInt(ROWS), r.nextInt(COLS));
            if (!game.isFail(p)) {
                game.move(p);
            }
        }
        //Records records = new Records();
    }

}
