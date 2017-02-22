package es.urjc.mov.javsan.miner;

import java.util.Random;

public class TestUI {
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

    public void debug() {
        Random m = new Random(MinerActivity.SEED);

        while (!mapper.isEndGame()) {
            Point p = new Point(m.nextInt(MinerActivity.ROWS), m.nextInt(MinerActivity.COLUMNS));
            if (winner) {
                goodMove(mapper, images, p);
            } else {
                badMove(mapper, images , p);
            }
        }
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
}

