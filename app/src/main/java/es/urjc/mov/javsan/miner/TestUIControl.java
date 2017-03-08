package es.urjc.mov.javsan.miner;

class TestUIControl {
    private boolean exit;
    private static final int INTERVALMOVE = 50;

    TestUIControl() {
        exit = false;
    }

    public synchronized boolean isExit() throws InterruptedException {
        wait(INTERVALMOVE);
        return exit;
    }

    public synchronized void exit() {
        exit = true;
        notify();
    }

}
