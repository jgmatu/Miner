package es.urjc.mov.javsan.miner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;


public class RecordListFrag extends Fragment {
    private static final int SIZETEXT = 25;

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.records_fragment, container, false);

        Records r = new Records(getActivity());
        showScores(r, view);
        return view;
    }

    private void showScores(Records records, View view) {
        TableLayout table = (TableLayout) view.findViewById(R.id.records);
        Object[] orderRecords = records.sortedByValues();

        for (Object r : orderRecords) {
            TableRow row = new TableRow(getContext());

            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT, 0,
                    1.0f / 10.0f ));

            TextView name = new TextView(getContext());
            TextView score = new TextView(getContext());

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
        setButtonGoPlay(view);
    }

    private void setButtonGoPlay(View view) {
        Button play = (Button) view.findViewById(R.id.go_play);

        play.setOnClickListener(new GoPlay());
    }

    private class GoPlay implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(getActivity(), MinerActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    }
}
