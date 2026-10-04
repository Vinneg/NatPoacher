package vinneg.natpoacher;

public class Log {

    public static boolean enable = false;

    public static void log(String s) {
        if (!enable) return;

        System.out.println(s);
    }

    public static void log(String s, Object... a) {
        if (!enable) return;

        System.out.println(s.formatted(a));
    }

    public static void log(Integer s) {
        if (!enable) return;

        System.out.println(s);
    }

    public static void log(Double s) {
        if (!enable) return;

        System.out.println(s);
    }

}
