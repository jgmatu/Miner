package es.urjc.mov.javsan.miner;

import android.media.AudioTrack;
import android.media.MediaPlayer;

class SoundControl {
    private final int TIMESOUND;

    SoundControl (int time) {
        TIMESOUND = time;
    }

    public synchronized void waitSound(MediaPlayer sound) throws InterruptedException {
        sound.start();
        wait(TIMESOUND);
        sound.stop();
        sound.release();
    }

    public synchronized void waitSound(AudioTrack sound) throws InterruptedException {
        sound.play();
        wait(TIMESOUND);
        sound.stop();
        sound.release();
    }

    public synchronized void endSound()  {
        notify();
    }
}

