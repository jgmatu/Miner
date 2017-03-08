package es.urjc.mov.javsan.miner;

class Square {

    private Point point;
    private boolean mine;
    private boolean hidden;

    Square(Point nowPoint, boolean state) {
        point = nowPoint;
        mine = state;
        hidden = true;
    }

    boolean isHidden() {
        return hidden;
    }

    void visible() {
        this.hidden = false;
    }

    boolean isMine() {
        return mine;
    }

    @Override
    public String toString() {
        return String.format("%s : (%d , %d)", (mine) ? "Mine" : "Not Mine", point.toString());
    }
}
