package vinneg.natpoacher;

import java.awt.event.KeyEvent;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.currentTimeMillis;

public class Worker implements Runnable {

    private static Thread main;
    private static long nextLure = 0;

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
        clicker.delay(2_000);

        AtomicInteger noBobber = new AtomicInteger();

        while (!Thread.currentThread().isInterrupted()) {
            if (nextLure < currentTimeMillis()) {
                // lure
                clicker.key(KeyEvent.VK_SEMICOLON);
                clicker.delay(BUFF_CAST);

                nextLure = currentTimeMillis() + BUFF_DURATION + 3_000;

                clicker.delay(1_000);
            }

            // fishing
            clicker.key(KeyEvent.VK_QUOTE);
            clicker.delay(1_500);

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
                clicker.delay(25);
            } while (m != null && !mouseCursor.isGear() && attempt < 15);

            if (m == null || attempt >= 15) {
                System.out.println("Bobber not found " + noBobber.incrementAndGet() + " times");

                clicker.delay(500);

                continue;
            } else {
                noBobber.set(0);
            }

            if (noBobber.getPlain() > 25) {
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

        clicker.delay(2_000);
        clicker.key(KeyEvent.VK_SLASH);

        System.out.println("Fishing finished");
    }

    public static void start(Clicker clicker) throws NoSuchAlgorithmException {
        if (main == null) {
            Worker worker = new Worker();
            worker.clicker = clicker;

            main = new Thread(worker, UUID.randomUUID().toString());
        }

        main.start();
    }

    public static void stop() {
        if (main == null) {
            return;
        }

        try {
            main.interrupt();
            main.join(3 * 60 * 1_000); // ждём до 3 минут
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        } finally {
            main = null;
        }
    }

}
