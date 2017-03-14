package es.urjc.mov.javsan.miner;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MinerActivity extends AppCompatActivity {

    public static final String TAG = "Mines Debug : ";

    public static final int BUTTRADAR = 2;
    public static final int ROWS = 8;
    public static final int COLUMNS = 8;
    public static final int SEED = 4;
    public static final int EASY = 10;
    public static final int RADARS = 5;
    private static final int SIZESCORE = 25;

    public static final int SIZEROWSTABLE = BUTTRADAR + ROWS;

    static class VIEWS {
        private static final int IDSCORE = 0;
        private static final int NUMVIEWS = 1;
    }

    private View[] views;

    private MinerGame game;
    private ImagesMap imagesMap;
    private RadarUI radarUI;

    private ImagesGame imagesGame;

    private boolean debug = true;
    private boolean isShowLostMines = false;
    private int seed = 0;


    private SoundControl soundControl;
    private TestUIControl testControl;
    private SoundControl soundMoves;


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
                exitPrevTestUI();
                newGame();
                return true;
            case R.id.help:
                toastMsg("Help!");
                return true;
            case R.id.debug_win:
                debPlayingUI(true);  // Thread win...
                return true;
            case R.id.debug_lost:
                debPlayingUI(false); // Thread lost...
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!debug) {
            menu.findItem(R.id.debug_win).setVisible(false);
            menu.findItem(R.id.debug_lost).setVisible(false);
        }
        return true;
    }

    public void exitPrevTestUI() {
        if (testControl != null) {
            testControl.exit();
        }
    }

    public void refreshScore() {
        TextView s = (TextView) views[VIEWS.IDSCORE];

        s.setText(String.valueOf(game.getScore()));
    }

    public MinerGame getGame() {
        return game;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mines);

        seed = SEED;
        views = new View[VIEWS.NUMVIEWS];
        debug = true;
        isShowLostMines = false;

        if (savedInstanceState != null) {
            seed = savedInstanceState.getInt("seed");
        }
        createGame(seed);
        if (savedInstanceState != null) {
            setStateActivity(savedInstanceState);
        }
        soundControl = null;
        testControl = null;
        soundMoves = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        state.putInt("score", game.getScore());
        state.putInt("seed", seed);
        state.putInt("radars", radarUI.getNumRadars());
        state.putIntegerArrayList("moves", game.savedMoves());
    }

    private void setStateActivity(Bundle state) {
        restoreMap(state);
        restoreRadar(state);

        if (game.isWinGame()) {
            imagesGame.showWin();
        }
        refreshScore();
        radarUI.refreshRadar();
    }

    private void restoreRadar(Bundle state) {
        int numRadars = state.getInt("radars");

        for (int i = numRadars ; i < RADARS; i++) {
            radarUI.setScan(game);
            radarUI.setClean(game);
        }
    }

    private void restoreMap(Bundle state) {
        ArrayList<Integer> moves = state.getIntegerArrayList("moves");
        if (moves == null) {
            return;
        }

        for (Integer m : moves) {
            Point p = new Point(m / COLUMNS, m % COLUMNS);

            if (game.isFail(p)) {
                game.setLostGame(p);
                imagesMap.showMapLost(game, p);
                break;
            }
            game.move(p);
            imagesMap.setImageVisible(p , game.getMines(p));
        }
    }

    private void createGame(int s) {
        imagesMap = new ImagesMap(new Point(ROWS, COLUMNS));
        game = new MinerGame(new Point(ROWS, COLUMNS) , s , EASY);
        addScore();
        createMinerMapUI();
        imagesGame = new ImagesGame(this);
        createRadar();
    }

    private void createRadar() {
        radarUI = new RadarUI(this, game, imagesMap, new Radar(RADARS));
    }

    private void debPlayingUI(boolean win) {
        exitPrevTestUI();
        cleanScan();
        createTestUI(win);
    }

    private void createTestUI(boolean win) {
        if (!game.isEndGame()) {
            testControl = new TestUIControl();
            new TestUI(game, imagesMap, win, testControl).execute();
        }
    }

    private void newGame() {
        seed = game.getSeed();
        game.restart();
        imagesMap.restart();
        radarUI.restart(RADARS);
        imagesGame.showMap();
        isShowLostMines = false;
        stopSound();
        refreshScore();
        cleanScan();
        radarUI.refreshRadar();
    }

    private void stopSound() {
        if (soundControl != null) {
            soundControl.endSound();
        }
    }

    private void createMinerMapUI() {
        TableLayout table = (TableLayout) findViewById(R.id.map);

        TableLayout.LayoutParams rows = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, 0,
                1.0f / (float) SIZEROWSTABLE);

        TableRow.LayoutParams imgDesign = new TableRow.LayoutParams(
                0, TableRow.LayoutParams.MATCH_PARENT,
                1.0f / (float) COLUMNS);

        for (int i = 0; i < ROWS; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(rows);

            for (int j = 0; j < COLUMNS; j++) {
                Point point = new Point(i, j);

                ImageButton imgBut = initialButton(imgDesign, point);
                imagesMap.setImagesMap(imgBut, point);

                row.addView(imgBut);
                row.setGravity(Gravity.CENTER);
            }
            table.addView(row);
        }

        table.setStretchAllColumns(true);
        table.setGravity(Gravity.CENTER);
    }

    private void addScore() {
        TextView score = (TextView) findViewById(R.id.game_score);

        score.setText("0");

        score.setTextSize(SIZESCORE);

        views[VIEWS.IDSCORE] = score;
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

    private void cleanScan() {
        boolean[][] isClean = radarUI.setClean(game);

        for (int i = 0 ; i < game.getRows(); i++) {
            for (int j = 0 ; j < game.getCols(); j++) {
                if (isClean[i][j]) {
                    Point p = new Point(i, j);
                    imagesMap.getImage(p).setImageResource(R.mipmap.hidden);
                }
            }
        }
    }

    private class EventMap implements View.OnClickListener {
        private Point point;

        EventMap(Point newPoint) {
            point = newPoint;
        }

        @Override
        public void onClick(View v) {

            if (game.isEndGame()) {
                showImage();
                return;
            }
            cleanScan();

            int score = checkGameMove(point);
            refreshScore();
            showWinGame();

            if (game.isLostGame()) {
                stopSound();
                saveRecordScore(score);
            }
        }

        private void showWinGame() {
            if (game.isWinGame()) {
                if (!debug) {
                    showWin();
                }
                toastMsg(R.string.win_info);
            }
        }

        private int checkGameMove(Point p) {
            int score;

            if (game.isFail(p)) {
                score = setBadMove(p);
            } else {
                score = setGoodMove(p);
            }
            return score;
        }

        private void showImage() {
            showLost();
            showWin();
        }

        private void showWin() {
            if (game.isWinGame() && !debug) {
                imagesGame.showWin();
            }
        }

        private void showLost() {
            if (game.isLostGame() && !debug && isShowLostMines) {
                imagesGame.showLost();
                toastMsg(R.string.lost_info);
            }
        }

        private void saveRecordScore(int score) {
            Records records = new Records(MinerActivity.this);

            if (records.isRecord(score)) {
                Intent i = new Intent(MinerActivity.this, RecordsFragAc.class);
                i.putExtra("score", score);
                startActivity(i);
            }
        }

        private int setGoodMove(Point p) {
            int mines = game.getMines(p);

            stopSound();
            startSound(p);
            if (mines == 0) {
                floodEmptySquares();
            }
            showImage(mines);
            game.move(p);

            return game.getScore();
        }

        private int setBadMove(Point p) {
            int score = game.getScore();

            game.setLostGame(p);
            imagesMap.showMapLost(game, p);
            isShowLostMines = true;
            stopSound();
            boomSound();
            return score;
        }

        private void boomSound() {
            int time = 5000;
            soundControl = new SoundControl(time);
            new SoundAudio(MinerActivity.this, soundControl , R.raw.explosion).execute();
        }

        private void floodEmptySquares() {
            imagesMap.fill(game, game.fillSquares(point));
        }

        private void showImage(int mines) {
            if (game.isHidden(point)) {
                imagesMap.setImageVisible(point, mines);
            }
        }

        private void stopSound() {
            if (soundMoves != null) {
                soundMoves.endSound();
            }
        }

        private void startSound(Point p) {
            if (game.isHidden(p)) {
                int time = 1500;
                soundMoves = new SoundControl(time);
                new SoundTone(game.getMines(p) * 100 + 500, soundMoves).execute();
            }
        }
    }

    private void toastMsg(String txt) {
        int time = Toast.LENGTH_SHORT;
        Toast msg = Toast.makeText(this, txt, time);

        msg.show();
    }

    private void toastMsg(int txt) {
        int time = Toast.LENGTH_SHORT;
        Toast msg = Toast.makeText(this, txt, time);

        msg.show();
    }
}
