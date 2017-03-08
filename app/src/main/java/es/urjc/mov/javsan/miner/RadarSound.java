package es.urjc.mov.javsan.miner;


class RadarSound {
    private MinerActivity activity;
    private SoundControl control;

    RadarSound (MinerActivity a) {
        activity = a;
        control = new SoundControl();
    }

    void play() {
        new SoundAudio(activity, control, R.raw.radar).execute();
    }

    void stop() {
        control.endSound();
    }
}
