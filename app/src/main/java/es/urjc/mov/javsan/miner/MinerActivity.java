package es.urjc.mov.javsan.miner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class MinerActivity extends AppCompatActivity {

    public static final String TAG = "Mines Debug : ";
    public static final int ROWS = 8;
    public static final int FIELDS = 8;
    public static final int SEED = 10;
    public static final int EASY = 12;
    public static final int RADARS = 2;

    private MinerMap mapper;
    private ImageMap images;
    private Console console;
    private ImagesGame imagesGame;
    private boolean debug;

    // Methods Activity....
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                newGame();
                return true;
            case R.id.help:
                toastMsg("Help!");
                return true;
            case R.id.debug_win:
                debug = true;
                winUI(); // Thread win...
                return true;
            case R.id.debug_lost:
                debug = true;
                lostUI(); // Thread lost...
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mines);

        images = new ImageMap(new Point(ROWS , FIELDS));
        mapper = createMinerMap();
        console = getConsole();
        imagesGame = new ImagesGame(this);
        debug = false;
    }

    private void toastMsg(String txt) {
        int time = Toast.LENGTH_SHORT;
        Toast msg = Toast.makeText(this , txt , time);
        msg.show();
    }

    private void lostUI() {
        if (!mapper.isEndGame()) {
            boolean winner = false;
            new TestUI(mapper, images, this , winner).run();
        }
    }

    private void winUI() {
        if (!mapper.isEndGame()) {
            boolean winner = true;
            new TestUI(mapper, images, this , winner).run();
        }
    }

    private void newGame() {
        mapper.restart();
        images.restart();
        imagesGame.showMap();
        console.restartRadar(RADARS);
    }

    private Console getConsole() {
        Console c = new Console(this , RADARS);
        c.newRadar(this , mapper, images);
        return  c;
    }

    private ImageButton initialButton(TableRow.LayoutParams lr, Point point) {
        ImageButton imgBut = new ImageButton(this);

        // Desing properties...
        imgBut.setPadding(0, 0, 0, 0);
        imgBut.setLayoutParams(lr);
        imgBut.setScaleType(ImageView.ScaleType.FIT_XY);

        // Core properties...
        imgBut.setImageResource(R.mipmap.hidden);
        imgBut.setOnClickListener(new EventMap(point));

        return imgBut;
    }

    private MinerMap createMinerMap() {
        TableLayout table = (TableLayout) findViewById(R.id.map);
        MinerMap map = new MinerMap(SEED, EASY, new Point(ROWS, FIELDS));

        for (int i = 0; i < ROWS; i++) {
            TableRow row = new TableRow(this);

            // Generate table rows to the UI...
            TableRow.LayoutParams lr = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lr);

            for (int j = 0; j < FIELDS; j++) {
                Point point = new Point(i, j);

                // Here we fill out the images UI...
                ImageButton imgBut = initialButton(lr, point);
                images.setImage(imgBut, point); // Set images in ImageMap.

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

    private class EventMap implements View.OnClickListener {
        private Point point;

        EventMap(Point newPoint) {
            point = newPoint;
        }

        @Override
        public void onClick(View v) {
            if (mapper.isEndGame()) {
                showResult();
            } else {
                move();
                console.disableRadar(mapper, images);
            }
        }

        private void showResult() {
            showLost();
            showWin();
        }

        private void move() {
            if (mapper.isMine(point)) {
                // BOOM!!! Square with mine, dead!
                badMove();
            } else {
                // There is not mine in the square...
                goodMove();
            }
        }

        private void showWin() {
            if (mapper.isWinner() && !debug) {
                // Objetivo cumplido, campo de minas despejado!
                imagesGame.showWin();
            }
        }

        private void showLost() {
            if (mapper.isLostMap() && !debug) {
                // We have lost the match.. BOOM!!
                imagesGame.showLost();
            }
        }

        private void goodMove() {
            int mines= mapper.getMines(point);

            if (mines == 0) {
                fillEmptySquares();
            } else {
                showSquare(mines);
            }
        }

        private void fillEmptySquares() {
            boolean[][] paint = mapper.fill(point);
            images.fill(mapper , paint);
        }

        private void showSquare(int mines) {
            images.modImage(point , mines);
            mapper.modMapNoMine(point);
        }

        private void badMove() {
            mapper.setLostGame();
            images.showMapLost(mapper, point); // ImageMap.
        }
    }
}
