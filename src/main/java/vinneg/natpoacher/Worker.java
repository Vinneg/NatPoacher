package vinneg.natpoacher;

import java.awt.event.KeyEvent;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.currentTimeMillis;

public class Worker implements Runnable {

    public Clicker clicker;
    public final MouseCursor mouseCursor;

    public Worker() {
        this.mouseCursor = new MouseCursor();
    }

    @Override
    public void run() {
        clicker.delay(2000);

        AtomicInteger counter = new AtomicInteger();
        long nextLure = 0;
        long endAt = currentTimeMillis() + 60_000;

        while (!Thread.currentThread().isInterrupted() && endAt > currentTimeMillis() && counter.getAndIncrement() < 2) {
//            if (nextLure < currentTimeMillis()) {
//                // lure
//                clicker.key(KeyEvent.VK_SEMICOLON);
//                clicker.delay(5000);
//
//                nextLure = currentTimeMillis() + 10 * 60 * 1_000;
//            }

            // fishing
            clicker.key(KeyEvent.VK_QUOTE);
            clicker.delay(2000);

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

                clicker.delay(5_000);

                continue;
            }

            // bobber found
//            Bobber bobber = new Bobber(clicker);
//
//            long et = currentTimeMillis() + 30_000;
//            boolean bite = false;
//
//            for (long t = 0; t < et && !bite; t = currentTimeMillis()) {
//                clicker.delay(25);
//                bite = !bobber.still();
//
//                if (!bite) {
//                    System.out.println("Bobber not triggered");
//                }
//            }
//
//            if (bite) {
//                clicker.click();
//            }

            clicker.delay(5_000);
        }
    }

}
