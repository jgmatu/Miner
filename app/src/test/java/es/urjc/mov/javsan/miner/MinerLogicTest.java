package es.urjc.mov.javsan.miner;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.fail;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MinerLogicTest {

    private final int SEED = 10;
    private final int EASY = 12;

    private final int ROWS = 8;
    private final int FIELDS = 8;

    @Test
<<<<<<< HEAD:app/src/test/java/es/urjc/mov/javsan/miner/MinerTest.java
    public void concrete_map_isCorrect() {
=======
    public void concrete_map_isCorrect() throws Exception {
>>>>>>> d5bd2d936ef3d0d78fee13ffec1a86ab058dd7c8:app/src/test/java/es/urjc/mov/javsan/miner/MinerLogicTest.java
        // Mapper of test... the constant are exactly for the test...
        MinerMap map = new MinerMap(SEED, EASY, new Point(ROWS, FIELDS));
        if (!isGetMines(map)) {
            fail();
        }
        fail_fill(map);
        if (map.getMoves() != 5) {
            fail();
        }
        // Winner test...
        move_win(map);
        if (!map.isWinner()) {
            fail();
        }
    }

    @Test
    public void lost_game_isCorrect() throws Exception {
        MinerMap map = new MinerMap(SEED , EASY , new Point(ROWS , FIELDS));
        move(map , new Point(0 ,1)); // Mine!!!!
        if (!map.isLostMap()) {
            fail();
        }
    }

    @Test
    public void random_game_isCorrect() throws Exception {
        int seed = 3 , rows = 10, fields = 10 , easy = 2;
        MinerMap map = new MinerMap(seed , easy, new Point(rows , fields));
        Random val = new Random(seed + SEED);

        while (!map.isEndGame()) {
            // Test map with out of range limits...
            move(map, new Point(val.nextInt(rows + 1), val.nextInt(rows + 1)));
            move(map , new Point(val.nextInt(fields + 1), val.nextInt(fields + 1)));
        }
        // The map is with all mines... loser random moves.
        if (map.isWinner()) {
            fail();
        }
    }

    @Test
    public void winGame_isCorrect() throws Exception {
        int seed = 4 , rows = 10, fields = 10 , easy = 3;
        MinerMap map = new MinerMap(seed , easy , new Point(rows , fields));

        for (int i = 0 ; i < rows ; i++) {
            for (int j = 0 ; j < fields; j++) {
                Point p = new Point(i , j);
                if (!map.isMine(p)) {
                    move(map , p);
                }
            }
        }
        if (!map.isWinner()) {
            fail();
        }
    }

    private void fail_fill(MinerMap map) {
        // Fill method check.
        for (int i = 0; i < 4; i++) {
            map.modMapNoMine(new Point(i, 6));
        }
        map.fill(new Point(4, 4));
    }

    private void move_win(MinerMap map) {
        move(map , new Point(4 , 4));
        move(map , new Point(7 , 2));
        move(map , new Point(7 , 7));
        move(map , new Point(1 , 0));
        move(map , new Point(1 , 7));
        move(map , new Point(0 , 0));
    }

    private void move(MinerMap map , Point p) {
        if (map.isMine(p)) {
            map.setLostGame();
            return;
        }
        if (map.getMines(p) == 0) {
            map.fill(p);
        } else {
            map.modMapNoMine(p);
        }
    }

    private boolean isGetMines (MinerMap map) {
        return map.getMines(new Point(0, 0)) == 2 &&
                map.getMines(new Point(1, 2)) == 3 &&
                map.getMines(new Point(1, 7)) == 2;
    }
}