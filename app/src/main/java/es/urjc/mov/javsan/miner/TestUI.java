package es.urjc.mov.javsan.miner;

import android.os.AsyncTask;

import java.util.Random;

class TestUI extends AsyncTask<Void, Point, Void> {

    private MinerGame mapper;
    private ImagesMap images;
    private boolean winner;
    private TestUIControl control;

    TestUI (MinerGame map, ImagesMap img, boolean win, TestUIControl c) {
        mapper = map;
        images = img;
        winner = win;
        control = c;
    }


    private boolean isGoodMove(MinerGame game , Point p) {
        return !game.isFail(p) && game.isHidden(p);
    }

    private void goodMove(MinerGame map, Point p) {
        if (isGoodMove(map , p)) {
            publishProgress(p);
        }
    }

    private boolean isBadMove (MinerGame map , Point p) {
        return map.isFail(p) && map.isHidden(p);
    }

    private void badMove(MinerGame map , Point p) {
        if (isBadMove(map , p)) {
            publishProgress(p);
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            autoRandomPlay();
        } catch (InterruptedException e) {
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Point... values) {
        super.onProgressUpdate(values);
        images.push(values[0]);
    }

    private void autoRandomPlay() throws InterruptedException {
        Random m = new Random(MinerActivity.SEED);

        while (!mapper.isEndGame() && !control.isExit()) {
            Point p = new Point(m.nextInt(MinerActivity.ROWS), m.nextInt(MinerActivity.COLUMNS));
            if (winner) {
                goodMove(mapper , p);
            } else {
                badMove(mapper, p);
            }
        }
    }
}

