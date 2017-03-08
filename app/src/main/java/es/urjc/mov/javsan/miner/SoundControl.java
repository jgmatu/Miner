package es.urjc.mov.javsan.miner;

class SoundControl {
    private static final int TIMESOUND = 5000;

    public synchronized void waitSound() throws InterruptedException {
        wait(TIMESOUND);
    }

    public synchronized void endSound()  {
        notify();
    }
}

