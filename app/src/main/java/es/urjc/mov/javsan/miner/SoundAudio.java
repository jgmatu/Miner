package es.urjc.mov.javsan.miner;

import android.media.MediaPlayer;
import android.os.AsyncTask;

class SoundAudio extends AsyncTask<String , String , String> {
    private MediaPlayer sound;
    private SoundControl control;

    SoundAudio(MinerActivity a, SoundControl cS, int r) {
        sound = MediaPlayer.create(a, r);
        control = cS;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            control.waitSound(sound);
        } catch (InterruptedException e) {
        }
        return "";
    }
}
