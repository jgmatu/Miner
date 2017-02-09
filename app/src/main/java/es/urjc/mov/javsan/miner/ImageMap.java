package es.urjc.mov.javsan.miner;

import android.widget.ImageButton;

/**
 * Created by javsan on 8/02/17.
 */

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


    public void showMapLost(MinerMap mapper, Point p) {
        for (int i = 0; i < MinerActivity.ROWS; i++) {
            for (int j = 0; j < MinerActivity.FIELDS; j++) {
                Point np = new Point(i, j);
                if (p.equals(np)) {
                    // Image of square checked with failed mine...
                    images[i][j].setImageResource(R.mipmap.mine_fail);
                    continue;
                }
                if (mapper.isMine(np)) {
                    // Image of square with mine...
                    images[i][j].setImageResource(R.mipmap.mine);
                } else {
                    // Image without mine..
                    modImage(np , mapper.getMines(np));
                }
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

    public void fill (MinerMap mapper, boolean[][] paint) {
        for (int i = 0 ; i < MinerActivity.ROWS; i++) {
            for (int j = 0 ; j < MinerActivity.FIELDS ; j++) {
                Point p = new Point(i , j);
                if (paint[i][j]) {
                    modImage(p, mapper.getMines(p));
                }
            }
        }
    }

    public void restart() {
        for (int i = 0 ; i < MinerActivity.ROWS ; i++) {
            for (int j = 0; j < MinerActivity.FIELDS; j++) {
                images[i][j].setImageResource(R.mipmap.hidden);
            }
        }
    }
}
