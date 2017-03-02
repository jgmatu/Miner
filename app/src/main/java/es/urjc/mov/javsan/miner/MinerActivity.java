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

import java.util.ArrayList;

public class MinerActivity extends AppCompatActivity {

    public static final String TAG = "Mines Debug : ";

    public static final int BUTTRADAR = 1;
    public static final int ROWS = 10;
    public static final int COLUMNS = 10;
    public static final int SEED = 4;
    public static final int EASY = 3;
    public static final int RADARS = 2;

    private MinerGame game;
    private ImageMap images;
    private Console console;
    private ImagesGame imagesGame;
    private TestUI test;
    private boolean debug = true;
    private boolean isShowLostMines = false;
    private int seed;

    // Methods Activity....
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_options, menu);
        test = null;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean win = true;

        switch (item.getItemId()) {
            case R.id.new_game:
                newGame();
                return true;
            case R.id.help:
                toastMsg("Help!");
                return true;
            case R.id.debug_win:
                debPlayingUI(win);  // Thread win...
                return true;
            case R.id.debug_lost:
                debPlayingUI(!win); // Thread lost...
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Change option on condition.
        if (!debug) {
            menu.findItem(R.id.debug_win).setVisible(false);
            menu.findItem(R.id.debug_lost).setVisible(false);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (test != null) {
            test.cancel(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mines);
        seed = SEED;

        if (savedInstanceState != null) {
            seed = savedInstanceState.getInt("seed");
        }

        createGame(seed);
        if (savedInstanceState != null) {
            setStateActivity(savedInstanceState);
            Log.v(MinerActivity.TAG, String.format("Winner -> %b , Looser -> %b" , game.isWinGame() , game.isLostGame()));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt("seed", seed);
        state.putIntegerArrayList("moves", game.saveMoves());
        Log.v(MinerActivity.TAG, String.format("Winner -> %b , Looser -> %b" , game.isWinGame() , game.isLostGame()));
    }

    private void setStateActivity(Bundle state) {
        ArrayList<Integer> moves = state.getIntegerArrayList("moves");

        for (Integer m : moves) {
            Point p = new Point(m / COLUMNS, m % COLUMNS);
            if (game.isFail(p)) {
                Log.v(MinerActivity.TAG, "FAIL!!!!");
                game.setLostGame(p);
                images.showMapLost(game, p);
                break;
            }
            game.move(p);
            images.setImageVisible(p , game.getMines(p));
        }

        if (game.isWinGame()) {
            imagesGame.showWin();
        }
    }

    private void createGame(int seed) {
        images = new ImageMap(new Point(ROWS, COLUMNS));
        game = createMinerMapUI(seed);
        console = getConsole();
        imagesGame = new ImagesGame(this);
    }

    private void toastMsg(String txt) {
        int time = Toast.LENGTH_SHORT;
        Toast msg = Toast.makeText(this, txt, time);
        msg.show();
    }

    private void debPlayingUI(boolean win) {
        cancelPrevTestUI();
        createTestUI(win);
    }

    private void cancelPrevTestUI() {
        if (test != null) {
            test.cancel(true);
        }
    }

    private void createTestUI(boolean win) {
        if (!game.isEndGame()) {
            test = new TestUI(game, images, this, win);
            test.execute();
        }
    }

    private void newGame() {
        cancelPrevTestUI();
        seed = game.getSeed();
        game.restart();
        images.restart();
        console.restartRadar(RADARS);
        imagesGame.showMap();
        isShowLostMines = false;
    }

    private Console getConsole() {
        Console c = new Console(this, RADARS);
        c.newRadar(this, game, images);
        return c;
    }

    private ImageButton initialButton(TableRow.LayoutParams design, Point point) {
        ImageButton imgBut = new ImageButton(this);

        // Desing properties...
        imgBut.setPadding(0, 0, 0, 0);
        imgBut.setLayoutParams(design);
        imgBut.setScaleType(ImageView.ScaleType.FIT_XY);

        // Core properties...
        imgBut.setImageResource(R.mipmap.hidden);
        imgBut.setOnClickListener(new EventMap(point));

        return imgBut;
    }

    private MinerGame createMinerMapUI(int seed) {
        TableLayout table = (TableLayout) findViewById(R.id.map);
        MinerGame map = new MinerGame(new Point(ROWS, COLUMNS) , seed , EASY);

        TableLayout.LayoutParams rows = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 0, 1.0f / (float) (ROWS + BUTTRADAR));
        TableRow.LayoutParams imgDesign = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f / (float) COLUMNS);


        for (int i = 0; i < ROWS; i++) {
            TableRow row = new TableRow(this);

            // Generate table rows to the UI...
            row.setLayoutParams(rows);

            for (int j = 0; j < COLUMNS; j++) {
                Point point = new Point(i, j);

                // Here we fill out the images UI...
                ImageButton imgBut = initialButton(imgDesign, point);
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

            if (game.isEndGame()) {
                //soundLost();
                showResult();
                return;
            }

            console.disableRadar(game, images);
            checkGameMove();

            if (game.isWinGame() && !debug) {
                //soundWin();
                showWin();
            }
        }

        private void soundLost() {
            int freqTone = 400;
            for (int i = 0 ; i < 10 ; i++) {
                new Sound().play(freqTone);
                freqTone -= 10;
            }
        }

        private void soundWin () {
            int freqTone = 100;
            for (int i = 0 ; i < 10 ; i++) {
                new Sound().play(freqTone);
                freqTone += 10;
            }
        }

        private void checkGameMove() {
            if (game.isFail(point)) {
                //int freqTone = 440;
                //new Sound().play(freqTone);
                // BOOM!!! Square have a mine, dead!
                setBadMove();
            } else {
                //int freqTone = 220;
                //new Sound().play(freqTone);
                // There is not mine in the square...
                setGoodMove();
            }
        }

        private void showResult() {
            showLost();
            showWin();
        }

        private void showWin() {
            if (game.isWinGame() && !debug) {
                // Objetivo cumplido, campo de minas despejado!
                imagesGame.showWin();
            }
        }

        private void showLost() {
            if (game.isLostGame() && !debug && isShowLostMines) {
                // We have lost the match.. BOOM!!
                imagesGame.showLost();
            }
        }

        private void setGoodMove() {
            int mines = game.getMines(point);

            if (mines == 0) {
                fill();
            }
            show(mines); // Collateral efects we must show mines only later of fill...
        }

        private void setBadMove() {
            game.setLostGame(point);
            images.showMapLost(game, point); // ImageMap.
            isShowLostMines = true;
        }

        private void fill() {
            images.fill(game, game.empty(point));
        }

        private void show(int mines) {
            game.move(point);
            images.setImageVisible(point, mines);
        }
    }

}
