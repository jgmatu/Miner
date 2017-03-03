package es.urjc.mov.javsan.miner;

import android.widget.Toast;

/**
 * Created by javi on 6/02/17.
 */

public class Radar {

    private int numRadars;
    private boolean active;

    Radar (int nRad) {
        numRadars = 0;
        if (nRad > 0) {
            numRadars = nRad;
        }
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isEnable() {
        return numRadars > 0 && !isActive();
    }

    public void restart(int nRadar) {
        numRadars = nRadar;
        active = false;
    }

    public void setScan (MinerGame game, ImageMap images) {
        active = true;
        scan(game, images);
    }

    public void setClean(MinerGame game, ImageMap images) {
        active = false;
        scan(game, images);
    }

    private void scan(MinerGame game, ImageMap images) {
        for (int i = 0 ; i < MinerActivity.ROWS ; i++) {
            for (int j = 0; j < MinerActivity.COLUMNS; j++) {
                Point p = new Point(i , j);

                if (isScan(game, p)) {
                    images.getImage(p).setImageResource(R.mipmap.radar);
                }

                if (isClean(game, p)) {
                    images.getImage(p).setImageResource(R.mipmap.hidden);
                }
            }
        }
        discount();
    }

    public void msgDisable (MinerActivity a) {
        int time = Toast.LENGTH_SHORT;
        String txt = "The active is disable...";
        Toast msg = Toast.makeText(a , txt , time);

        msg.show();
    }

    private boolean isScan(MinerGame game, Point p) {
        return game.isFail(p) && active;
    }

    private boolean isClean(MinerGame game, Point p) {
        return game.isFail(p) && !active;
    }

    private void discount() {
        if (active) {
            numRadars--;
        }
    }
}
