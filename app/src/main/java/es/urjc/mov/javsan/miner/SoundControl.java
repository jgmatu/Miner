package es.urjc.mov.javsan.miner;

class SoundControl {
    private final int TIMESOUND;

    SoundControl (int time) {
        TIMESOUND = time;
    }

    public synchronized void waitSound() throws InterruptedException {
        wait(TIMESOUND);
    }

    public synchronized void endSound()  {
        notify();
    }
}

