package es.urjc.mov.javsan.miner;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

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
        Button rad = (Button) a.findViewById(R.id.radar);

        RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lay.addRule(RelativeLayout.CENTER_HORIZONTAL);

        rad.setText("Radar");
        rad.setLayoutParams(lay);
        rad.setOnClickListener(new EventConsole(map, img, BUTTON.RADAR));
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
