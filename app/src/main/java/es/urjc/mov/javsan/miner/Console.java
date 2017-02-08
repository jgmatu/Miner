package es.urjc.mov.javsan.miner;

import android.app.ActionBar;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

/**
 * Created by javi on 6/02/17.
 */

public class Console {

    private MinerMap mapper;
    private MinerActivity activity;
    private enum BUTON {RADAR , RESTART , QUIT};
    private Radar radar;

    Console(MinerMap map, MinerActivity a) {
        newRadar(a);
        newRestart(a);
        newQuit(a);
        activity = a;
        mapper = map;
        radar = new Radar(map , 2);
    }

    public void updRadar() {
        radar.decreaseRadar();
        radar.setRadar(false);
        radar.active();
    }

    private void newRestart(MinerActivity m) {
        Button res = (Button) m.findViewById(R.id.restart);

        RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        res.setText("Restart");
        res.setLayoutParams(lay);
        res.setOnClickListener(new EventConsole(BUTON.RESTART));
    }

    private void newRadar(MinerActivity m) {
        Button rad = (Button) m.findViewById(R.id.radar);

        RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lay.addRule(RelativeLayout.CENTER_HORIZONTAL);

        rad.setText("Radar");
        rad.setLayoutParams(lay);
        rad.setOnClickListener(new EventConsole(BUTON.RADAR));
    }

    private void newQuit (MinerActivity m) {
        Button q = (Button) m.findViewById(R.id.quit);

        RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lay.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        q.setText("Quit");
        q.setLayoutParams(lay);
        q.setOnClickListener(new EventConsole(BUTON.QUIT));
    }

    private void showMap () {
        hiddenWin();
        hiddenLost();
        showMiner();
    }

    private void hiddenWin() {
        ImageView img = (ImageView) activity.findViewById(R.id.winner_image);
        img.setLayoutParams(new RelativeLayout.LayoutParams(0 , 0));
    }

    private void hiddenLost() {
        ImageView img = (ImageView) activity.findViewById(R.id.boom_image);
        img.setLayoutParams(new RelativeLayout.LayoutParams(0 , 0));
    }

    private void showMiner() {
        // Get image to show for win...
        TableLayout tab = (TableLayout) activity.findViewById(R.id.map);
        tab.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private class EventConsole implements View.OnClickListener {
        private BUTON buton;

        EventConsole(BUTON b) {
            buton = b;
        }

        @Override
        public void onClick(View v) {
            switch (buton) {
                case RADAR:
                    if (radar.isActive()) {
                        radar.setRadar(true);
                        radar.active();
                    } else {
                        disable();
                    }
                    break;
                case RESTART:
                    mapper.restart();
                    radar = new Radar(mapper , 2);
                    showMap();
                    break;
                case QUIT:
                    Log.v(MinerActivity.TAG, "QUIT!");
                    break;
                default:
                    Log.v(MinerActivity.TAG, "Not selected button...");
            }
        }
    }

    private void disable () {
        int time = Toast.LENGTH_SHORT;
        String txt = "The radar is disable...";
        Toast msg = Toast.makeText(activity , txt , time);
        msg.show();
    }
}
