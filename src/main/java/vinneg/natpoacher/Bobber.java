package vinneg.natpoacher;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.awt.image.BufferedImage;

import static vinneg.natpoacher.Log.log;

public class Bobber {

    public static final int SIDE = 2 * Seeker.R + 1;

    public final Clicker clicker;
    public final ImageView peep;

    public final int x;
    public final int y;
    public final Rectangle area;
    public int redness;

    public Bobber(Clicker clicker, ImageView peep) {
        this.clicker = clicker;
        this.peep = peep;

        Point cp = MouseInfo.getPointerInfo()
                .getLocation();

        this.x = cp.x;
        this.y = cp.y;

        this.area = new Rectangle(x - Seeker.R, y - Seeker.R, SIDE, SIDE);

        redness = getRedness();
    }

    private int getRedness() {
        BufferedImage image = clicker.bobber(this);
        peep.setImage(SwingFXUtils.toFXImage(image, null));

        int c = 0;

        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                c += Seeker.test(image.getRGB(x, y)) ? 1 : 0;
            }
        }

        return c;
    }

    public boolean still() {
        int cur = getRedness();
        double r = ((double) cur) / redness;
        boolean res = 0.5 <= r && r <= 1.5;

        if (!res) {
            log("Bobber triggered with redness %d", cur);
        }

        redness = cur;

        return res;
    }

}
