package vinneg.natpoacher;

import java.awt.event.KeyEvent;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static java.lang.System.currentTimeMillis;

public class Worker implements Runnable {

    private static final int BUFF_DURATION = 600_000;
    private static final int LURE_DURATION = 22_000;
    private static final int BUFF_CAST = 6_000;

    public Clicker clicker;
    public final MouseCursor mouseCursor;

    public Worker() throws NoSuchAlgorithmException {
        this.mouseCursor = new MouseCursor();
    }

    @Override
    public void run() {
        clicker.delay(2000);

        long nextLure = currentTimeMillis() + BUFF_DURATION;
        long endAt = currentTimeMillis() + 9 * 60_000;

        while (!Thread.currentThread().isInterrupted() && endAt > currentTimeMillis()) {
            if (nextLure < currentTimeMillis()) {
                // lure
                clicker.key(KeyEvent.VK_SEMICOLON);
                clicker.delay(BUFF_CAST);

                nextLure = currentTimeMillis() + BUFF_DURATION;
            }

            // fishing
            clicker.key(KeyEvent.VK_QUOTE);
            clicker.delay(2_000);

            // seek for bobber
            Seeker seeker = new Seeker(clicker.screen());
            seeker.fill();

            Seeker.Mass m;
            do {
                m = Optional.of(seeker)
                        .map(Seeker::central)
                        .map(seeker::pull)
                        .map(clicker::move)
                        .orElse(null);

                clicker.delay(50);
            } while (m != null && !mouseCursor.isGear());

            if (m == null) {
                System.out.println("Bobber not found");

                clicker.delay(2_000);

                continue;
            }

            // bobber found
            Bobber bobber = new Bobber(clicker);

            System.out.println("Bobber found with redness " + bobber.redness);

            long et = currentTimeMillis() + LURE_DURATION;
            boolean bite = false;

            for (long t = 0; t < et && !bite; t = currentTimeMillis()) {
                clicker.delay(25);
                bite = !bobber.still();
            }

            if (bite) {
                clicker.click();
            } else {
                System.out.println("Bobber not triggered");
            }

            clicker.delay(2_000);
        }

        System.out.println("Fishing finished");
    }

}
