package es.urjc.mov.javsan.miner;

import android.os.AsyncTask;
import android.os.SystemClock;

import java.util.Random;

public class TestUI extends AsyncTask<Void, Point, Void> {

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

    private boolean isGoodMove(MinerMap map , Point p) {
        return !map.isMine(p) && map.isHidden(p);
    }

    private void goodMove(MinerMap map, Point p) {
        if (isGoodMove(map , p)) {
            publishProgress(p);
        }
    }

    private boolean isBadMove (MinerMap map , Point p) {
        return map.isMine(p) && map.isHidden(p);
    }

    private void badMove(MinerMap map , Point p) {
        if (isBadMove(map , p)) {
            publishProgress(p);
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        Random m = new Random(MinerActivity.SEED);

        while (!mapper.isEndGame() && !isCancelled()) {
            Point p = new Point(m.nextInt(MinerActivity.ROWS), m.nextInt(MinerActivity.COLUMNS));
            if (winner) {
                goodMove(mapper , p);
            } else {
                badMove(mapper, p);
            }
            SystemClock.sleep(100);
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Point... values) {
        super.onProgressUpdate(values);
        images.push(values[0]);
    }
}

