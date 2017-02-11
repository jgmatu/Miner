package es.urjc.mov.javsan.miner;

import android.widget.Toast;

/**
 * Created by javi on 6/02/17.
 */

public class Radar {

    private int numRadar;
    private boolean radar;

    Radar (int nRad) {
        if (nRad > 0) {
            numRadar = nRad;
        } else {
            numRadar = 1;
        }
        radar = false;
    }

    public boolean isActive() {
        return numRadar > 0;
    }

    public void active() {
        radar = true;
    }

    public void scan(MinerMap mapper, ImageMap images) {
        for (int i = 0 ; i < MinerActivity.ROWS ; i++) {
            for (int j = 0 ; j < MinerActivity.FIELDS; j++) {
                Point p = new Point(i , j);
                if (mapper.isMine(p) && radar) {
                    images.getImage(p).setImageResource(R.mipmap.radar);
                } else if (mapper.isMine(p)) {
                    images.getImage(p).setImageResource(R.mipmap.hidden);
                }
            }
        }
        decreaseRadar();
    }
    public void restart(int nRadar) {
        numRadar = nRadar;
        radar = false;
    }

    public void disable (MinerActivity a) {
        int time = Toast.LENGTH_SHORT;
        String txt = "The radar is disable...";
        Toast msg = Toast.makeText(a , txt , time);
        msg.show();
    }

    private void decreaseRadar() {
        if (radar && numRadar > 0) {
            numRadar--;
            radar = !radar;
        }
    }
}
