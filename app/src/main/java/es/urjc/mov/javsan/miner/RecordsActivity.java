package es.urjc.mov.javsan.miner;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        Intent i = getIntent();
        Bundle msg = i.getExtras();

        if (msg != null) {
            Integer score = msg.getInt("score");
            setFormRecord(score);
        }
    }

    private void setFormRecord(int score) {
        setPlayer();
        setScore(score);
        setClickListener();
    }

    private void setPlayer() {
        TextView t = (TextView) findViewById(R.id.player_name);

        t.setBackgroundColor(Color.BLACK);
    }

    private void setClickListener() {
        Button b = (Button) findViewById(R.id.confirm_record);

        b.setOnClickListener(new ConfirmRecord());
    }

    private void setScore(int score) {
        TextView t = (TextView) findViewById(R.id.score);

        t.setBackgroundColor(Color.BLACK);
        t.setText(String.valueOf(score));
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
            EditText e = (EditText)  findViewById(R.id.player_name);

            e.setText(R.string.player);
        }

        private String getNamePlayer() {
            EditText e = (EditText) findViewById(R.id.player_name);
            String name = e.getText().toString();

            name = name.replace("\n", "");
            name = name.replace(" ", "");
            return name;
        }

        private int getScorePlayer() {
            TextView t = (TextView) findViewById(R.id.score);

            return Integer.valueOf(t.getText().toString());
        }
    }

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

