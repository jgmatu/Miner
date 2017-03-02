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
    public void concrete_map_isCorrect() throws Exception {
        // Mapper of test... the constant are exactly for the test...
        MinerGame game = new MinerGame(new Point(ROWS, FIELDS) , SEED , EASY);

        if (!isGetMines(game)) {
            fail();
        }
        fail_fill(game);
        if (game.numMoves() != 5) {
            fail();
        }
        // Winner test...
        move_win(game);
        if (!game.isWinGame()) {
            fail();
        }
    }

    @Test
    public void lost_game_isCorrect() throws Exception {
        MinerGame map = new MinerGame(new Point(ROWS , FIELDS) , SEED , EASY);
        move(map , new Point(0 ,1)); // Mine!!!!
        if (!map.isLostGame()) {
            fail();
        }
    }

    @Test
    public void random_game_isCorrect() throws Exception {
        int seed = 3 , rows = 10, fields = 10 , easy = 2;
        MinerGame game = new MinerGame(new Point(rows , fields), seed , easy);
        Random val = new Random(seed + SEED);

        while (!game.isEndGame()) {
            // Test game with out of range limits...
            move(game, new Point(val.nextInt(rows + 1), val.nextInt(rows + 1)));
            move(game , new Point(val.nextInt(fields + 1), val.nextInt(fields + 1)));
        }
        // The game is with all mines... loser random moves.
        if (game.isWinGame()) {
            fail();
        }
    }

    @Test
    public void winGame_isCorrect() throws Exception {
        int seed = 4 , rows = 10, fields = 10 , easy = 3;
        MinerGame game = new MinerGame(new Point(rows , fields), seed , easy);

        for (int i = 0 ; i < rows ; i++) {
            for (int j = 0 ; j < fields; j++) {
                Point p = new Point(i , j);
                if (!game.isFail(p)) {
                    move(game , p);
                }
            }
        }
        if (!game.isWinGame()) {
            fail();
        }
    }

    private void fail_fill(MinerGame game) {
        // Fill method check.
        for (int i = 0; i < 4; i++) {
            game.move(new Point(i, 6));
        }
        game.empty(new Point(4, 4));
    }

    Point[] moves = {
            new Point(4 , 4),
            new Point(7 , 2),
            new Point(7 , 7),
            new Point(1 , 0),
            new Point(1 , 7),
            new Point(0 , 0)
    };

    private void move_win(MinerGame map) {
        for (int i = 0 ; i < moves.length ; i++) {
            move(map, moves[i]);
        }
    }

    private void move(MinerGame game , Point p) {
        if (game.isFail(p)) {
            game.setLostGame();
            return;
        }

        if (game.getMines(p) == 0) {
            game.empty(p);
        } else {
            game.move(p);
        }
    }

    private boolean isGetMines (MinerGame map) {
        return map.getMines(new Point(0, 0)) == 2 && map.getMines(new Point(1, 2)) == 3 &&
                map.getMines(new Point(1, 7)) == 2;
    }
}