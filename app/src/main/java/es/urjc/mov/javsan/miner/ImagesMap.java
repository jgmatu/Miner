package es.urjc.mov.javsan.miner;

import android.widget.ImageButton;

class ImagesMap {

    private ImageButton[][] images;
    private int id;

    ImagesMap(Point maxPoint) {
        images = new ImageButton[maxPoint.getRow()][maxPoint.getCol()];
        id = 0;
    }

    public void setImagesMap(ImageButton image, Point point) {
        // Put the image to the matrix of map images...
        // This method is called in the creation of UI to
        // set the images of the miner map class...
        image.setId(id);
        this.images[point.getRow()][point.getCol()] = image;
        id = id + 1;
    }

    public ImageButton getImage(Point p) {
        return images[p.getRow()][p.getCol()];
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

    public void setImageVisible(Point p , int mines) {
        // The image is now visible this image has not mines near...
        images[p.getRow()][p.getCol()].setImageResource(R.mipmap.square_yellow);

        if (mines > 0) {
            // The image is now visible with mines nears...
            images[p.getRow()][p.getCol()].setImageResource(R.mipmap.mines1 + mines - 1);
        }
    }

    public void fill (MinerGame game, boolean[][] paint) {
        for (int i = 0 ; i < MinerActivity.ROWS; i++) {
            for (int j = 0; j < MinerActivity.COLUMNS; j++) {
                Point p = new Point(i , j);
                if (paint[i][j]) {
                    setImageVisible(p, game.getMines(p));
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
        images[p.getRow()][p.getCol()].performClick();
    }

    private void showImage(MinerGame game, Point p) {
        if (game.isFail(p)) {
            // Image of square with mine...
            images[p.getRow()][p.getCol()].setImageResource(R.mipmap.mine);
        } else {
            // Image without mine..
            setImageVisible(p , game.getMines(p));
        }
    }
}
