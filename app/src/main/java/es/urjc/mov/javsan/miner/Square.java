package es.urjc.mov.javsan.miner;

/**
 * Created by javi on 2/02/17.
 */

public class Square {

    private Point point;
    private boolean mine;
    private boolean hidden;

    public Square(Point nowPoint, boolean state) {
        point = nowPoint;
        mine = state;
        hidden = true;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void visible() {
        this.hidden = false;
    }

    public boolean isMine() {
        return mine;
    }

    @Override
    public String toString() {
        return String.format("%s : (%d , %d)", (mine) ? "Mine" : "Not Mine", point.toString());
    }
}
