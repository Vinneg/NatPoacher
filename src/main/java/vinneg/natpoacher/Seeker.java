package vinneg.natpoacher;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Seeker {

    public static final int R = 20;

    private static final double RATIO = 1.1;
    private static final int THRESHOLD = 50;
    private final List<Pixel> candidates = new LinkedList<>();
    private final BufferedImage image;
    private int cx;
    private int cy;

    public Seeker(String image) throws IOException {
        this(ImageIO.read(new File("C:\\Users\\vinogradovav\\downloads\\test.jpg")));
    }

    public Seeker(BufferedImage image) {
        this.image = image;
    }

    public static double square(double i) {
        return i * i;
    }

    public static int square(int i) {
        return i * i;
    }

    public static double dist(double x, double y) {
        return Math.sqrt(square(x) + square(y));
    }

    public void fill() {
        int width = image.getWidth();
        int height = image.getHeight();

        cx = width / 2;
        cy = height / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                if (red > THRESHOLD && red > green * RATIO && red > blue * RATIO) {
//                    image.setRGB(x,y, Color.RED.getRGB());
                    candidates.add(new Pixel(x, y));
                } else {
//                    image.setRGB(x,y, Color.LIGHT_GRAY.getRGB());
                }
            }
        }

//        ImageIO.write(image, "jpg", new File("C:\\Users\\vinogradovav\\Downloads\\test+.jpg"));
    }

    public Pixel central() {
        final Mass center = new Mass(cx, cy);
        double max = Double.MAX_VALUE;
        Pixel res = null;

        for (Pixel p : candidates) {
            double cur = center.dist(p);

            if (cur < max) {
                res = p;
                max = cur;
            }
        }

        return res;
    }

    public Mass pull(Pixel central) {
        return pull(new Mass(central));
    }

    public Mass pull(Mass central) {
        Mass cur = new Mass(central);

        for (Pixel p : candidates) {
            if (central.include(p)) {
                cur.add(p);
            }
        }

        cur.shift();

        if (central.dist(cur) > 1) {
            cur = pull(cur);
        }

        candidates.removeAll(cur.bundle);

        return cur;
    }

    public static class Pixel {

        public final int x;
        public final int y;

        public Pixel(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Pixel(Mass m) {
            this.x = (int) m.x;
            this.y = (int) m.y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o instanceof Pixel p) return this.x == p.x && this.y == p.y;

            return false;
        }

    }

    public static class Mass {

        public double x;
        public double y;
        public int m;
        public List<Pixel> bundle = new LinkedList<>();

        public Mass(double x, double y) {
            this.x = x;
            this.y = y;
            this.m = 1;
        }

        public Mass(Pixel source) {
            this(source.x, source.y);
        }

        public Mass(Mass source) {
            this(source.x, source.y);
        }

        public Mass add(Pixel p) {
            bundle.add(p);

            return this;
        }

        public boolean include(Pixel p) {
            double dx = x - p.x;
            double dy = y - p.y;

            return -R <= dx && dx <= R && -R <= dy && dy <= R;
        }

        public Mass shift() {
            double tx = 0;
            double ty = 0;

            for (Pixel p : bundle) {
                tx += p.x;
                ty += p.y;
            }

            this.x = tx / bundle.size();
            this.y = ty / bundle.size();

            return this;
        }

        public double dist(Pixel p) {
            return Seeker.dist(x - p.x, y - p.y);
        }

        public double dist(Mass m) {
            return Seeker.dist(x - m.x, y - m.y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o instanceof Mass mass) return this.x == mass.x && this.y == mass.y;

            return false;
        }

    }

}
