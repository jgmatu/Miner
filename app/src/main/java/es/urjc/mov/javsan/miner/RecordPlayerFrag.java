package es.urjc.mov.javsan.miner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class RecordPlayerFrag extends Fragment {

    private static final int LENNAMEPLYER = 10;

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.player_fragment, container, false);

        Intent i = getActivity().getIntent();
        Bundle b = i.getExtras();
        int score = b.getInt("score");

        setFormRecord(score, view);
        return view;
    }

    private void setFormRecord(int score, View view) {
        setPlayer(view);
        setScore(score , view);
        setClickListener(view);
    }

    private void setPlayer(View view) {
        TextView t = (TextView) view.findViewById(R.id.player_name);

        t.setBackgroundColor(Color.BLACK);
    }

    private void setClickListener(View view) {
        Button b = (Button) view.findViewById(R.id.confirm_record);

        b.setOnClickListener(new ConfirmRecord(view));
    }

    private void setScore(int score, View view) {
        TextView t = (TextView) view.findViewById(R.id.score);

        t.setBackgroundColor(Color.BLACK);
        t.setText(String.valueOf(score));
    }

    private class ConfirmRecord implements View.OnClickListener {
        private Records records;
        private View view;

        ConfirmRecord (View v) {
            view = v;
        }

        @Override
        public void onClick(View v) {
            records = new Records(getActivity());
            String name = getNamePlayer();
            int score = getScorePlayer();

            setInitName();
            if (name.length() > 0 && name.length() < LENNAMEPLYER) {
                records.newRecord(name, score);
                goToList();
            }
        }

        private void setInitName() {
            EditText e = (EditText) view.findViewById(R.id.player_name);

            e.setTextColor(Color.BLACK);
            e.setText(R.string.player);
        }

        private String getNamePlayer() {
            EditText e = (EditText) view.findViewById(R.id.player_name);
            String name = e.getText().toString();

            name = name.replace("\n", "");
            name = name.replace(" ", "");

            return name;
        }

        private int getScorePlayer() {
            TextView t = (TextView) view.findViewById(R.id.score);

            return Integer.valueOf(t.getText().toString());
        }

        public void goToList() {
            RecordListFrag listRecords = new RecordListFrag();

            listRecords.setArguments(getActivity().getIntent().getExtras());

            FragmentManager mgr = getActivity().getSupportFragmentManager();
            FragmentTransaction t = mgr.beginTransaction();

            t.replace(R.id.main_frame, listRecords);
            t.addToBackStack(null);
            t.commit();
        }
    }
}
