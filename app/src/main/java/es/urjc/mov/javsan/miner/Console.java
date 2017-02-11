package es.urjc.mov.javsan.miner;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.Toast;

/**
 * Created by javi on 6/02/17.
 */

public class Console {
    private enum BUTON {RADAR , RESTART , QUIT};

    private MinerActivity activity;
    private Radar radar;



    Console(MinerActivity a , int nRadar) {
        activity = a;
        radar = new Radar(nRadar);
    }

    public void newRestart (MinerActivity a, MinerMap map , ImageMap img) {
        Button res = (Button) a.findViewById(R.id.restart);

        RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lay.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        res.setText("Restart");
        res.setLayoutParams(lay);
        res.setOnClickListener(new EventConsole(map , img, BUTON.RESTART));
    }

    public void newRadar (MinerActivity a, MinerMap map, ImageMap img) {
        Button rad = (Button) a.findViewById(R.id.radar);

        RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lay.addRule(RelativeLayout.CENTER_HORIZONTAL);

        rad.setText("Radar");
        rad.setLayoutParams(lay);
        rad.setOnClickListener(new EventConsole(map, img, BUTON.RADAR));
    }

    public void newQuit (MinerActivity a, MinerMap map , ImageMap img) {
        Button q = (Button) a.findViewById(R.id.quit);

        RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lay.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        q.setText("Quit");
        q.setLayoutParams(lay);
        q.setOnClickListener(new EventConsole(map , img, BUTON.QUIT));
    }

    public void disRadar(MinerMap map, ImageMap img) {
        radar.scan(map , img);
    }

    private void showMap () {
        hiddenWin(activity);
        hiddenLost(activity);
        showMiner(activity);
    }

    private void hiddenWin(MinerActivity a) {
        ImageView img = (ImageView) a.findViewById(R.id.winner_image);
        img.setLayoutParams(new RelativeLayout.LayoutParams(0 , 0));
    }

    private void hiddenLost(MinerActivity a) {
        ImageView img = (ImageView) a.findViewById(R.id.boom_image);
        img.setLayoutParams(new RelativeLayout.LayoutParams(0 , 0));
    }

    private void showMiner(MinerActivity a) {
        // Get image to show for win...
        TableLayout tab = (TableLayout) a.findViewById(R.id.map);
        tab.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private class EventConsole implements View.OnClickListener {
        private MinerMap mapper;
        private ImageMap images;
        private BUTON buton;
        
        EventConsole(MinerMap map, ImageMap img, BUTON b) {
            mapper = map;
            images = img;
            buton = b;
        }

        @Override
        public void onClick(View v) {
            switch (buton) {
                case RADAR:
                    setRadar();
                    break;
                case RESTART:
                    restartGame();
                    break;
                case QUIT:
                    Log.v(MinerActivity.TAG, "QUIT!");
                    break;
                default:
                    Log.v(MinerActivity.TAG, "Not selected button...");
            }
        }

        private void setRadar() {
            if (radar.isActive()) {
                radar.active();
                radar.scan(mapper , images);
            } else {
                radar.disable(activity);
            }
        }

        private void restartGame() {
            mapper.restart();
            images.restart();
            radar.restart(MinerActivity.RADARS);
            showMap();
        }
    }
}
