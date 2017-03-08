package es.urjc.mov.javsan.miner;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

class RadarUI {

    private static final int SIZESCORE = 20;

    private MinerActivity activity;
    private Radar radar;
    private View scoreView;

    RadarUI (MinerActivity a, MinerGame game, ImageMap img, Radar r) {
        activity = a;
        radar = r;
        createRadar(game, img);
    }

    public Radar getRadar() {
        return radar;
    }

    public void restart(int nRadar) {
        radar.restart(nRadar);
        refreshRadar();
    }

    private void createRadar(MinerGame game, ImageMap img) {
        float weigth = 1.0f / (float) MinerActivity.ROWS;
        TableLayout tab = (TableLayout) activity.findViewById(R.id.map);
        TableLayout.LayoutParams rows = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, 0, weigth);
        TableRow row = new TableRow(activity);

        row.setLayoutParams(rows);
        row.setGravity(Gravity.CENTER);
        row.addView(getButton(game, img));
        row.addView(getNumRadars());
        tab.addView(row);
    }

    private Button getButton(MinerGame game, ImageMap img) {
        Button rad = new Button(activity);

        rad.setText(R.string.radar);
        rad.setLayoutParams(new TableRow.LayoutParams(0,
                TableRow.LayoutParams.MATCH_PARENT, 0.6f));
        rad.setOnClickListener(new RadarEvent(game, img));

        return rad;
    }

    private TextView getNumRadars() {
        TextView t = new TextView(activity);

        t.setText(String.valueOf(radar.getNumRadars()));
        t.setLayoutParams(new TableRow.LayoutParams(0,
                TableRow.LayoutParams.MATCH_PARENT, 0.4f));
        t.setGravity(Gravity.CENTER);
        t.setTextSize(SIZESCORE);

        scoreView = t;

        return t;
    }

    private void refreshRadar() {
        TextView t = (TextView) scoreView;
        t.setText(String.valueOf(radar.getNumRadars()));
    }

    private class RadarEvent implements View.OnClickListener {
        private MinerGame game;
        private ImageMap imagesMap;

        RadarEvent(MinerGame g, ImageMap img) {
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
                showScan();
            }
            refreshRadar();
        }

        private void showScan() {
            boolean[][] isScan = radar.setScan(game);

            for (int i = 0 ; i < game.getRows() ; i++) {
                for (int j = 0 ; j < game.getCols(); j++) {
                    if (isScan[i][j]) {
                        Point p = new Point(i , j);
                        imagesMap.getImage(p).setImageResource(R.mipmap.radar);
                    }
                }
            }
        }

        private void msgDisable () {
            int time = Toast.LENGTH_SHORT;
            String txt = "The radar is disable...";
            Toast msg = Toast.makeText(activity , txt , time);

            msg.show();
        }
    }
}
