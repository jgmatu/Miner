package es.urjc.mov.javsan.miner;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

public class MinerActivity extends Activity {

    public static final String TAG = "Mines Debug : ";
    public static final int LOST = -1;
    public static final int ROWS = 8;
    public static final int FIELDS = 6;
    public static final int SEED = 10;
    public static final int EASY = 12;

    private MinerMap mapper;
    private Console console;

    // Methods Activity....
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_options, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mines);
        mapper = createMinerMap();
    }

    private ImageButton initialButton(TableRow.LayoutParams lr, Point point) {
        ImageButton imgBut = new ImageButton(this);

        // Desing properties...
        imgBut.setPadding(0, 0, 0, 0);
        imgBut.setLayoutParams(lr);
        imgBut.setScaleType(ImageView.ScaleType.MATRIX);

        // Core properties...
        imgBut.setImageResource(R.mipmap.hidden);
        imgBut.setOnClickListener(new EventButt(point));

        return imgBut;
    }

    private MinerMap createMinerMap() {
        TableLayout table = (TableLayout) findViewById(R.id.map);
        boolean isTest = false;
        MinerMap map = new MinerMap(SEED, EASY, new Point(ROWS, FIELDS), isTest);

        for (int i = 0; i < ROWS; i++) {
            TableRow row = new TableRow(this);

            // Generate table rows to the UI...
            TableRow.LayoutParams lr = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lr);

            for (int j = 0; j < FIELDS; j++) {
                Point point = new Point(i, j);

                // Here we fill out the images of mapper to the botons...
                ImageButton imgBut = initialButton(lr, point);
                map.setImage(imgBut, point);

                row.addView(imgBut);
                row.setGravity(Gravity.CENTER);
            }
            table.addView(row);
        }
        // At last step change some properties of UI in the TableLayout..
        table.setStretchAllColumns(true);
        table.setGravity(Gravity.CENTER);
        return map;
    }

    private class EventButt implements View.OnClickListener {
        private Point point;

        EventButt(Point newPoint) {
            point = newPoint;
        }

        @Override
        public void onClick(View v) {
            if (mapper.isLostMap()) {
                // We have lost the match.. BOOM!!
                showLost();
                return;
            }
            if (mapper.isMine(point)) {
                // BOOM!!! Square with mine, dead!
                badMove();
            } else {
                // There is not mine in the square...
                goodMove();
            }
            if (mapper.isWinner()) {
                // Objetivo cumplido, campo de minas despejado!
                showWin();
            }
        }

        public void showWin() {
            // Get image to show for win...
            ImageView img = (ImageView) findViewById(R.id.winner_image);

            img.setImageResource(R.mipmap.win_image);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);

            img.setLayoutParams(new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            // Get table...
            TableLayout table = (TableLayout) findViewById(R.id.map);
            table.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
        }

        public void showLost() {
            // Get image to show for Lost...
            ImageView img = (ImageView) findViewById(R.id.boom_image);

            img.setImageResource(R.mipmap.image_lost);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);

            img.setLayoutParams(new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            // Get table...
            TableLayout table = (TableLayout) findViewById(R.id.map);
            table.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
        }

        private void goodMove() {
            Mines mines = new Mines(mapper, point);
            if (mines.clear()) {
                mapper.fill(point);
            } else {
                mapper.chImgNoMine(point);
            }
        }

        private void badMove() {
            mapper.setSqHiddens(LOST);
            mapper.showMapLost(point);
        }
        // End button class...
    }
}
