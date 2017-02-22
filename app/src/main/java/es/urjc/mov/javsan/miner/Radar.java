package es.urjc.mov.javsan.miner;

import android.util.Log;
import android.widget.Toast;

/**
 * Created by javi on 6/02/17.
 */

public class Radar {

    private int numRadar;
    private boolean radar;

    Radar (int nRad) {
        numRadar = 0;
        if (nRad > 0) {
            numRadar = nRad;
        }
        radar = false;
    }

    public void disable() { radar = false; };
    public void active() {radar = true; }

    public boolean isDisable() {
        return radar == false;
    }
    public boolean isActive() {
        return radar == true;
    }

    public boolean aviable() {
        return numRadar > 0 && isDisable();
    }

    public void restart(int nRadar) {
        numRadar = nRadar;
        radar = false;
    }

    public void scan(MinerMap mapper, ImageMap images) {
        for (int i = 0 ; i < MinerActivity.ROWS ; i++) {
            for (int j = 0; j < MinerActivity.COLUMNS; j++) {
                Point p = new Point(i , j);
                if (mapper.isMine(p) && radar) {
                    images.getImage(p).setImageResource(R.mipmap.radar);
                } else if (mapper.isMine(p)) {
                    images.getImage(p).setImageResource(R.mipmap.hidden);
                }
            }
        }
        if (radar) {
            numRadar--;
        }
    }

    public void msgDisable (MinerActivity a) {
        int time = Toast.LENGTH_SHORT;
        String txt = "The radar is disable...";
        Toast msg = Toast.makeText(a , txt , time);

        msg.show();
    }
}
