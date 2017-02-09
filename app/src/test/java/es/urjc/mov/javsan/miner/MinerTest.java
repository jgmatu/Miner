package es.urjc.mov.javsan.miner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MinerTest {

    private static final String TAG = "TestMines :";
    private final int SEED = 10;
    private final int EASY = 12;

    private final int ROWS = 8;
    private final int FIELDS = 8;

    @Before
    public void create_map() {
        // Mapper of test... the constant are exactly for the test...
        MinerMap map = new MinerMap(SEED, EASY, new Point(ROWS, FIELDS));
        mines_isCorrect(map);
        fill_isCorrect(map);
        win_game(map);
    }

    @Test
    public void lost_game() throws Exception {
        MinerMap map = new MinerMap(SEED , EASY , new Point(ROWS , FIELDS));
        move(map , new Point(0 ,1)); // Mine!!!!
        if (!map.isLostMap()) {
            fail();
        }
    }

    @Test
    public void random_game() throws Exception {
        MinerMap map = new MinerMap(3 , EASY, new Point(ROWS , FIELDS));
        ;
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

    private void mines_isCorrect(MinerMap map) {
        Point point = new Point(0, 0);
        int mines = map.getMines(point);

        if (mines != 2) {
            fail();
        }
        mines = map.getMines(new Point(1, 2));
        if (mines != 3) {
            fail();
        }

        mines = map.getMines(new Point(1, 7));
        if (mines != 2) {
            fail();
        }
    }

    private void fill_isCorrect(MinerMap map) {
        for (int i = 0; i < 4; i++) {
            map.modMapNoMine(new Point(i, 6));
        }
        map.fill(new Point(4, 4));
        if (map.getMoves() != 5) {
            fail();
        }
    }

    private void win_game(MinerMap map) {
        move(map , new Point(4 , 4));
        move(map , new Point(7 , 2));
        move(map , new Point(7 , 7));
        move(map , new Point(1 , 0));
        move(map , new Point(1 , 7));
        move(map , new Point(0 , 0));
        if (!map.isWinner()) {
            fail();
        }
    }
}