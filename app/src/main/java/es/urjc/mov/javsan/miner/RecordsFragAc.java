package es.urjc.mov.javsan.miner;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class RecordsFragAc extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragments_records);

        if (savedInstanceState != null) {
            return;
        }

        RecordPlayerFrag player = new RecordPlayerFrag();
        player.setArguments(getIntent().getExtras());

        FragmentManager mgr = getSupportFragmentManager();
        FragmentTransaction t = mgr.beginTransaction();

        t.add(R.id.main_frame, player);
        t.commit();

    }
}
