package es.urjc.mov.javsan.miner;

/**
 * Created by javi on 6/02/17.
 */

public class Radar {

    private int numRadar;
    private boolean radar;
    private MinerMap mapper;

    Radar (MinerMap map , int nRad) {
        if (nRad > 0) {
            numRadar = nRad;
        } else {
            numRadar = 1;
        }
        radar = false;
        mapper = map;
    }

    public boolean isActive() {
        return numRadar > 0;
    }

    public void decreaseRadar() {
        if (radar && numRadar > 0) {
            numRadar--;
            radar = !radar;
        }
    }

    public void setRadar(boolean radar) {
        this.radar = radar;
    }

    public void active() {
        for (int i = 0 ; i < MinerActivity.ROWS ; i++) {
            for (int j = 0 ; j < MinerActivity.FIELDS; j++) {
                Point p = new Point(i , j);
                if (mapper.isMine(p) && radar) {
                    mapper.getImage(p).setImageResource(R.mipmap.radar);
                } else if (mapper.isMine(p)) {
                    mapper.getImage(p).setImageResource(R.mipmap.hidden);
                }
            }
        }
    }
}
