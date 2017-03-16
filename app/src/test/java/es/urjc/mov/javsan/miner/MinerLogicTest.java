package es.urjc.mov.javsan.miner;

import org.junit.Test;

import java.util.ArrayList;
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
    private final int COLUMNS = 8;

    Point[] moves = {
            new Point(4 , 4),
            new Point(7 , 2),
            new Point(7 , 7),
            new Point(1 , 0),
            new Point(1 , 7),
            new Point(0 , 0)
    };

    @Test
    public void concrete_map_isCorrect() throws Exception {
        // Mapper of test... the constant are exactly for the test...
        MinerGame game = new MinerGame(new Point(ROWS, COLUMNS) , SEED , EASY);

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
        MinerGame map = new MinerGame(new Point(ROWS , COLUMNS) , SEED , EASY);
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

    @Test
    public void testMoves() {
        MinerGame game = new MinerGame(new Point(ROWS, COLUMNS), SEED, EASY);

        for (int i = 0 ; i < moves.length ; i++) {
            game.move(moves[i]);
        }
        ArrayList<Integer> numMoves= game.savedMoves();

        int pos = 0;
        for (Integer m : numMoves) {
            Point p = new Point(m / COLUMNS, m % COLUMNS);
            if (!p.equals(moves[pos])) {
                fail();
            }
            pos++;
        }
        restartMoves(game);
    }

    @Test
    public void testLostScore() {
        MinerGame game = new MinerGame(new Point(ROWS, COLUMNS), SEED, EASY);
        Random rand = new Random(SEED);

        while (!game.isEndGame()) {
            Point p = new Point(rand.nextInt(ROWS + 1), rand.nextInt(COLUMNS + 1));
            game.move(p);
        }

        if (game.isLostGame() && game.getSavedScore() != 0) {
            fail();
        }
    }

    @Test
    public void testWinScore() {
        MinerGame game = new MinerGame(new Point(ROWS, COLUMNS), SEED, EASY);

        win(game);

        game.restart();
        if (game.getSavedScore() == 0) {
            fail();
        }
    }

    @Test
    public void testScoreSaved() {
        MinerGame game = new MinerGame(new Point(ROWS, COLUMNS), SEED, EASY);

        win(game);
        int score = game.getSavedScore();
        game.restart();

        if (game.getSavedScore() != score) {
            fail();
        }
        for (int i = 0 ; i < 10 ; i++) {
            for (int j = 1 ; j < 1 ; j++) {
                Point p = new Point(i , j);
                if (!game.isFail(p)) {
                    game.move(p);
                }
            }
        }
        game.restart();

        if (game.getSavedScore() != score) {
            fail();
        }

        if (!game.isEndGame()) {
            // Comprobar que al reiniciar saved score es igual a score....
            // Se ha reiniciado se debe poner la ultima puntuacion salvada.
        }
    }

    private void win(MinerGame game) {
        Random rand = new Random(SEED);

        while (!game.isEndGame()) {
            Point p = new Point(rand.nextInt(ROWS + 1), rand.nextInt(COLUMNS + 1));
            if (!game.isFail(p)) {
                game.move(p);
            }
        }
    }


    private void restartMoves(MinerGame game) {
        game.restart();
        if (game.numMoves() == 0) {
            fail();
        }
        if (game.savedMoves().size() != 0) {
            fail();
        }
        if (game.isWinGame() && game.getSavedScore() == 0) {
            fail();
        }
    }

    private void fail_fill(MinerGame game) {
        // Fill method check.
        for (int i = 0; i < 4; i++) {
            game.move(new Point(i, 6));
        }
        game.fillSquares(new Point(4, 4));
    }

    private void move_win(MinerGame map) {
        for (int i = 0 ; i < moves.length ; i++) {
            move(map, moves[i]);
        }
    }

    private void move(MinerGame game , Point p) {
        if (game.isFail(p)) {
            game.setLostGame(p);
            return;
        }

        if (game.getMines(p) == 0) {
            game.fillSquares(p);
        } else {
            game.move(p);
        }
    }

    private boolean isGetMines (MinerGame map) {
        return map.getMines(new Point(0, 0)) == 2 && map.getMines(new Point(1, 2)) == 3 &&
                map.getMines(new Point(1, 7)) == 2;
    }
}