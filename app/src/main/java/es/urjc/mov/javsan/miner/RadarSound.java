package es.urjc.mov.javsan.miner;


import android.os.AsyncTask;

class RadarSound {
    private MinerActivity activity;
    private SoundControl control;
    private static final int TIME = 1500;

    RadarSound (MinerActivity a) {
        activity = a;
        control = new SoundControl(TIME);
    }

    void play() {
        new SoundAudio(activity, control, R.raw.radar).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    void stop() {
        control.endSound();
    }
}
