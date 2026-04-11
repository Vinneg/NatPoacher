package vinneg.natpoacher;

public class MouseCursor {

    static {
        System.load("D:\\Java\\NatPoacher\\src\\main\\resources\\jni\\MouseCursorLib.dll");
    }

//    public static final String GEAR = "600cc700cd0100000f0000ffffffffff";
//    public static final String GEAR = "0000359c670200000f0000ffffffffff";
    public static final String GEAR = "0000b021630200000f0000ffffffffff";

    // Нативный метод возвращает байтовый массив с изображением курсора
    public native String getCursorHash();

    public boolean isGear() {
        String hash = getCursorHash();

        System.out.println("Cursor hash " + hash);

        return GEAR.equalsIgnoreCase(hash);
    }

    static void main(String[] args) {
        final String imageData = new MouseCursor().getCursorHash();

        System.out.println("Cursor MD5=" + imageData);
    }

}
