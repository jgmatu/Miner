package es.urjc.mov.javsan.miner;

import android.os.SystemClock;

import java.util.Random;

/**
 * Created by javi on 11/02/17.
 */

public class TestUI implements Runnable {
    MinerMap mapper;
    ImageMap images;
    ImagesGame imgGames;
    private boolean winner;

    TestUI (MinerMap map, ImageMap img, MinerActivity a, boolean win) {
        mapper = map;
        images = img;
        imgGames = new ImagesGame(a);
        winner = win;
    }

    @Override
    public void run() {
        debug();
    }

    private boolean isGoodMove(MinerMap map , Point p) {
        return !map.isMine(p) && map.isHidden(p);
    }

    private void goodMove(MinerMap map, ImageMap img , Point p) {
        if (isGoodMove(map , p)) {
            img.push(p);
        }
    }

    private boolean isBadMove (MinerMap map , Point p) {
        return map.isMine(p) && map.isHidden(p);
    }

    private void badMove(MinerMap map , ImageMap img, Point p) {
        if (isBadMove(map , p)) {
            img.push(p);
        }
    }

    private synchronized void debug() {
        Random m = new Random(MinerActivity.SEED);

        while (!mapper.isWinner() && !mapper.isLostMap()) {
            Point p = new Point(m.nextInt(MinerActivity.ROWS), m.nextInt(MinerActivity.FIELDS));
            if (winner) {
                goodMove(mapper, images, p);
            } else {
                badMove(mapper, images , p);
            }
        }
    }
}

