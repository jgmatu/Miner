package es.urjc.mov.javsan.miner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;

public class RecordsActivity extends AppCompatActivity {
    private final static int SIZETEXT = 25;
    private final static int LENNAMEPLYER = 10;

    static class REGISTERPLAYER {
        static final int IDSCORE = 0;
        static final int IDNAME = 1;
        static final int NUMVIEWS = 2;
    }

    private View[] views;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        views = new View[REGISTERPLAYER.NUMVIEWS];

        Intent i = getIntent();
        Bundle msg = i.getExtras();

        if (msg != null) {
            Integer score = msg.getInt("score");
            newRecordUI(score);
        }
    }

    private void newRecordUI(int score) {
        TableLayout table = (TableLayout) findViewById(R.id.records);

        table.addView(playerRegister());
        table.addView(playerScore(score));
        table.addView(playerConfirm());
    }

    private TableRow playerRegister() {
        TableRow player = new TableRow(this);
        EditText namePlayer = new EditText(this);

        namePlayer.setTextSize(SIZETEXT);
        namePlayer.setGravity(Gravity.CENTER);
        namePlayer.setBackgroundColor(Color.BLACK);
        namePlayer.setAlpha(0.6f);

        views[REGISTERPLAYER.IDNAME] = namePlayer;

        player.setGravity(Gravity.CENTER);
        player.addView(namePlayer);
        return player;
    }

    private TableRow playerScore(int score) {
        TableRow playerScore = new TableRow(this);
        TextView textScore = new TextView(this);

        textScore.setText(String.valueOf(score));
        textScore.setTextSize(SIZETEXT);
        textScore.setGravity(Gravity.CENTER);
        textScore.setBackgroundColor(Color.BLACK);
        textScore.setAlpha(0.6f);

        views[REGISTERPLAYER.IDSCORE] = textScore;

        playerScore.setGravity(Gravity.CENTER);
        playerScore.addView(textScore);
        return playerScore;
    }

    private TableRow playerConfirm() {
        TableRow playerConf = new TableRow(this);
        Button confirm = new Button(this);

        confirm.setOnClickListener(new ConfirmRecord());
        confirm.setGravity(Gravity.CENTER);
        confirm.setText(R.string.confirm_record);
        confirm.setBackgroundColor(Color.BLACK);
        confirm.setAlpha(0.6f);

        playerConf.setGravity(Gravity.CENTER);
        playerConf.addView(confirm);
        return playerConf;
    }

    private class ConfirmRecord implements View.OnClickListener {
        private Records records;

        @Override
        public void onClick(View v) {
            records = new Records(RecordsActivity.this);
            String name = getNamePlayer();
            int score = getScorePlayer();

            setInitName();
            if (name.length() > 0 && name.length() < LENNAMEPLYER) {
                records.newRecord(name, score);
                showScores(records);
            }
        }

        private void setInitName() {
            EditText e = (EditText) views[REGISTERPLAYER.IDNAME];

            e.setText(R.string.player);
        }

        private String getNamePlayer() {
            EditText e = (EditText) views[REGISTERPLAYER.IDNAME];
            String name = e.getText().toString();

            name = name.replace("\n", "");
            name = name.replace(" ", "");
            return name;
        }

        private int getScorePlayer() {
            TextView t = (TextView) views[REGISTERPLAYER.IDSCORE];

            return Integer.valueOf(t.getText().toString());
        }
    }

    @SuppressLint("SetTextI18n")
    private void showScores(Records records) {
        TableLayout table = (TableLayout) findViewById(R.id.records);
        Object[] orderRecords = records.sortedByValues();

        table.removeAllViews();
        for (Object r : orderRecords) {
            TableRow row = new TableRow(this);

            TextView name = new TextView(this);
            TextView score = new TextView(this);

            name.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    0.6f));

            name.setText(((HashMap.Entry<String, Integer>) r).getKey());
            name.setTextSize(SIZETEXT);

            score.setLayoutParams(new TableRow.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    0.4f));

            score.setText(((HashMap.Entry<String, Integer>) r).getValue().toString());
            score.setTextSize(SIZETEXT);

            row.setBackgroundColor(Color.BLACK);
            row.setAlpha(0.6f);
            row.addView(name);
            row.addView(score);

            table.addView(row);

        }
        table.addView(setButtonGoPlay());
    }

    private TableRow setButtonGoPlay() {
        TableRow row = new TableRow(this);

        Button play = new Button(this);
        play.setText(R.string.play);
        play.setOnClickListener(new GoPlay());

        row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, 0 , 0.1f));
        row.addView(play);
        row.setGravity(Gravity.CENTER);

        return row;
    }

    private class GoPlay implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(RecordsActivity.this, MinerActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }
}

