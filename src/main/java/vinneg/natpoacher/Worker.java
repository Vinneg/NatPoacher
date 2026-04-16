package vinneg.natpoacher;

import java.awt.event.KeyEvent;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.currentTimeMillis;

public class Worker implements Runnable {

    private static final int BUFF_DURATION = 600_000;
    private static final int LURE_DURATION = 22_000;
    private static final int BUFF_CAST = 6_000 + 100;

    public Clicker clicker;
    public final MouseCursor mouseCursor;

    public Worker() throws NoSuchAlgorithmException {
        this.mouseCursor = new MouseCursor();
    }

    @Override
    public void run() {
        clicker.delay(2000);

        long nextLure = 0;
        long endAt = currentTimeMillis() + 6 * BUFF_DURATION;
        AtomicInteger noBobber = new AtomicInteger();

        while (!Thread.currentThread().isInterrupted() && endAt > currentTimeMillis()) {
            long seconds = (endAt - currentTimeMillis()) / 1_000;
            System.out.println("Time left: " + String.format("%d:%02d", seconds / 60, seconds % 60));

            if (nextLure < currentTimeMillis()) {
                // lure
                clicker.key(KeyEvent.VK_SEMICOLON);
                clicker.delay(BUFF_CAST);

                nextLure = currentTimeMillis() + BUFF_DURATION + 2_000;

                clicker.delay(1_000);
            }

            // fishing
            clicker.key(KeyEvent.VK_QUOTE);
            clicker.delay(1_000);

            // seek for bobber
            Seeker seeker = new Seeker(clicker.screen());
            seeker.fill();
            int attempt = 0;

            Seeker.Mass m;
            do {
                m = Optional.of(seeker)
                        .map(Seeker::central)
                        .map(seeker::pull)
                        .map(clicker::move)
                        .orElse(null);

                attempt++;
                clicker.delay(50);
            } while (m != null && !mouseCursor.isGear() && attempt < 5);

            if (m == null || attempt >= 5) {
                System.out.println("Bobber not found " + noBobber.incrementAndGet() + " times");

                clicker.delay(1_000);

                continue;
            } else {
                noBobber.set(0);
            }

            if (noBobber.get() > 25) {
                System.out.println("Bobber not found 25 times in a row. Exit");
                return;
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

            // clear bags
            clicker.delay(1_000);
            clicker.key(KeyEvent.VK_SLASH);
            clicker.delay(1_000);
        }

        System.out.println("Fishing finished");
    }

}
