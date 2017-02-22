package es.urjc.mov.javsan.miner;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Created by javi on 6/02/17.
 */

public class Console {
    private enum BUTTON {RADAR};

    private MinerActivity activity;
    private Radar radar;

    Console(MinerActivity a , int nRadar) {
        activity = a;
        radar = new Radar(nRadar);
    }

    public void newRadar (MinerActivity a, MinerMap map , ImageMap img) {
        float weigth = 1.0f / (float) MinerActivity.ROWS;
        TableLayout tab = (TableLayout) a.findViewById(R.id.map);

        TableLayout.LayoutParams rows = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, 0, weigth);

        TableRow row = new TableRow(a);

        row.setLayoutParams(rows);
        row.setGravity(Gravity.CENTER);
        row.addView(getButton(a, map, img));

        tab.addView(row);
    }

    public void disableRadar(MinerMap map , ImageMap img) {
        if (radar.isActive()) {
            radar.disable();
            radar.scan(map, img);
        }
    }

    public void restartRadar(int nRadar) {
        radar.restart(nRadar);
    }

    private Button getButton(MinerActivity a , MinerMap map , ImageMap img) {
        Button rad = new Button(a);

        rad.setText("Radar");
        rad.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        rad.setOnClickListener(new EventConsole(map, img, BUTTON.RADAR));

        return rad;
    }

    private class EventConsole implements View.OnClickListener {
        private MinerMap mapper;
        private ImageMap images;
        private BUTTON button;

        EventConsole(MinerMap map, ImageMap img, BUTTON b) {
            mapper = map;
            images = img;
            button = b;
        }

        @Override
        public void onClick(View v) {
            if (isRadar()) {
                setRadar();
            }
        }

        private boolean isRadar() {
            return button == BUTTON.RADAR && !mapper.isEndGame();
        }

        private void setRadar() {
            if (radar.aviable()) {
                radar.active();
                radar.scan(mapper, images);
            } else if (radar.isDisable()) {
                radar.msgDisable(activity);
            }
        }
    }
}
