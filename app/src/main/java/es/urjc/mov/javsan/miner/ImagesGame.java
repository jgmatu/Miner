package es.urjc.mov.javsan.miner;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;

class ImagesGame {

    private TableLayout table;
    private ImageView win;
    private ImageView lost;

    ImagesGame (MinerActivity a) {
        table = (TableLayout) a.findViewById(R.id.map);
        win = (ImageView) a.findViewById(R.id.winner_image);
        lost = (ImageView) a.findViewById(R.id.boom_image);
    }

    void showWin() {

        // Set image to show for win...
        win.setImageResource(R.mipmap.win_image);
        win.setScaleType(ImageView.ScaleType.CENTER_CROP);

        win.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // Set table...
        table.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
    }

    void showLost() {

        // Set image to show for Lost...
        lost.setImageResource(R.mipmap.image_lost);
        lost.setScaleType(ImageView.ScaleType.CENTER_CROP);

        lost.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // Set table...
        table.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
    }

    void showMap () {

        // Hidden win or lost image...
        win.setLayoutParams(new LinearLayout.LayoutParams(0 , 0));
        lost.setLayoutParams(new LinearLayout.LayoutParams(0 , 0));

        // Show new table...
        table.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0 , 0.8f));
    }
}
