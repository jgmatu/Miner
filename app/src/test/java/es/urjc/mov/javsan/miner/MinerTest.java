package es.urjc.mov.javsan.miner;

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

    private MinerMap mapper;

    @Before
    public void create_map() {
        // Mapper of test... the constant are exactly for the test...
        boolean isTest = true;
        mapper = new MinerMap(SEED, EASY, new Point(ROWS, FIELDS), isTest);
    }

    @Test
    public void mines_isCorrect() throws Exception {
        Point point = new Point(0, 0);
        Mines mines = new Mines(mapper, point);

        if (mines.getMines() != 2) {
            fail();
        }
        mines = new Mines(mapper, new Point(1, 2));
        if (mines.getMines() != 3) {
            fail();
        }
        mines = new Mines(mapper, new Point(1, 7));
        if (mines.getMines() != 2) {
            fail();
        }
    }

    @Test
    public void fill_isCorrect() throws Exception {
        for (int i = 0; i < 4; i++) {
            mapper.chImgNoMine(new Point(i, 6));
        }
        mapper.fill(new Point(4, 4));
        if (mapper.getSqHiddens() != 5) {
            fail();
        }
    }
}