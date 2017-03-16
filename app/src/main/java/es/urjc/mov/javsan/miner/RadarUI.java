package es.urjc.mov.javsan.miner;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

class RadarUI {

    private static final int SIZESRADAR = 25;

    private MinerActivity activity;
    private Radar radar;
    private RadarSound radarSound;
    private View numRadarsView;

    RadarUI (MinerActivity a, MinerGame game, ImagesMap img, Radar r) {
        activity = a;
        radar = r;
        radarSound = new RadarSound(activity);
        createRadar(game, img);
    }

    public void restart(int nRadar) {
        radar.restart(nRadar);
        refreshRadar();
    }

    public void refreshRadar() {
        TextView t = (TextView) numRadarsView;

        t.setText(String.valueOf(radar.getNumRadars()));
    }

    public boolean[][] setScan(MinerGame game) {
        radarSound.play();
        return radar.setScan(game);
    }

    public boolean[][] setClean(MinerGame game) {
        radarSound.stop();
        return radar.setClean(game);
    }

    public ArrayList<Integer> getRadarsUsed() {
        return radar.getRadarsUsed();
    }

    public void showScan(MinerGame game, ImagesMap imagesMap) {
        boolean[][] isScan = setScan(game);

        for (int i = 0 ; i < game.getRows() ; i++) {
            for (int j = 0 ; j < game.getCols(); j++) {
                if (isScan[i][j]) {
                    Point p = new Point(i , j);
                    imagesMap.getImage(p).setImageResource(R.mipmap.radar);
                }
            }
        }
    }

    private void createRadar(MinerGame game, ImagesMap img) {
        setButton(game, img);
        setNumRadars();
    }

    private void setButton(MinerGame game, ImagesMap img) {
        Button rad = (Button) activity.findViewById(R.id.buton_radar);

        rad.setTextSize(SIZESRADAR);

        rad.setOnClickListener(new RadarEvent(game, img));
    }

    private void setNumRadars() {
        TextView t = (TextView) activity.findViewById(R.id.num_radar);

        t.setText(String.valueOf(radar.getNumRadars()));

        t.setGravity(Gravity.CENTER);
        t.setTextSize(SIZESRADAR);

        numRadarsView = t;
    }

    private class RadarEvent implements View.OnClickListener {
        private MinerGame game;
        private ImagesMap imagesMap;

        RadarEvent(MinerGame g, ImagesMap img) {
            imagesMap = img;
            game = g;
        }

        @Override
        public void onClick(View v) {
            activity.exitPrevTestUI();

            if (radar.getNumRadars() == 0) {
                msgDisable();
            }

            if (radar.isEnable() && !game.isEndGame()) {
                showScan(game, imagesMap);
            }
            refreshRadar();
        }


        private void msgDisable () {
            int time = Toast.LENGTH_SHORT;
            Toast msg = Toast.makeText(activity , R.string.radar_disable, time);

            msg.show();
        }
    }
}
