package es.urjc.mov.javsan.miner;

import android.widget.ImageButton;

public class ImageMap {

    private ImageButton[][] images;

    ImageMap (Point maxPoint) {
        images = new ImageButton[maxPoint.getRow()][maxPoint.getField()];
    }

    public void setImage(ImageButton image, Point point) {
        // Put the image to the matrix of map images...
        // This method is called in the creation of UI to
        // set the images of the miner map class...
        this.images[point.getRow()][point.getField()] = image;
    }

    public ImageButton getImage(Point p) {
        return images[p.getRow()][p.getField()];
    }

    public void showMapLost(MinerGame game, Point p) {
        for (int i = 0; i < MinerActivity.ROWS; i++) {
            for (int j = 0; j < MinerActivity.COLUMNS; j++) {
                Point np = new Point(i, j);
                if (p.equals(np)) {
                    // Image of square checked with failed mine...
                    images[i][j].setImageResource(R.mipmap.mine_fail);
                    continue;
                }
                showImage(game , np);
            }
        }
    }

    public void modImage(Point p , int mines) {
        if (mines == 0) {
            // The image is now visible this image has not mines near...
            images[p.getRow()][p.getField()].setImageResource(R.mipmap.square_yellow);
        } else {
            // The image is now visible with mines nears...
            images[p.getRow()][p.getField()].setImageResource(R.mipmap.mines1 + mines - 1);
        }
    }

    public void fill (MinerGame game, boolean[][] paint) {
        for (int i = 0 ; i < MinerActivity.ROWS; i++) {
            for (int j = 0; j < MinerActivity.COLUMNS; j++) {
                Point p = new Point(i , j);
                if (paint[i][j]) {
                    modImage(p, game.getMines(p));
                }
            }
        }
    }

    public void restart() {
        for (int i = 0 ; i < MinerActivity.ROWS ; i++) {
            for (int j = 0; j < MinerActivity.COLUMNS; j++) {
                images[i][j].setImageResource(R.mipmap.hidden);
            }
        }
    }

    public void push(Point p) {
        images[p.getRow()][p.getField()].performClick();
    }

    private void showImage(MinerGame game, Point p) {
        if (game.isFail(p)) {
            // Image of square with mine...
            images[p.getRow()][p.getField()].setImageResource(R.mipmap.mine);
        } else {
            // Image without mine..
            modImage(p , game.getMines(p));
        }
    }
}
