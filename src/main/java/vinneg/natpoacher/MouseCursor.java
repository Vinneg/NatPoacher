package vinneg.natpoacher;

public class MouseCursor {

    static {
        System.load("D:\\Java\\NatPoacher\\src\\main\\resources\\jni\\MouseCursorLib.dll");
    }

    // Нативный метод возвращает байтовый массив с изображением курсора
    public native String getCursorHash();

    static void main(String[] args) {
        final String imageData = new MouseCursor().getCursorHash();

        System.out.println("Cursor MD5=" + imageData);
    }

}
