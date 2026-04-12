package vinneg.natpoacher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MouseCursor {

    public final MessageDigest MD5;

    static {
        System.load("D:\\Java\\NatPoacher\\src\\main\\resources\\jni\\MouseCursorLib.dll");
    }

    public static final String GEAR = "8418d5732c16c2de99b84c79b4c05a6b";

    // Нативный метод возвращает байтовый массив с изображением курсора
    public native byte[] getCursor();

    public MouseCursor() throws NoSuchAlgorithmException {
        MD5 = MessageDigest.getInstance("MD5");
    }

    public boolean isGear() {
        return GEAR.equals(getMD5Hash());
    }

    private String getMD5Hash() {
        byte[] cd = getCursor();
        if (cd == null) {
            return "none";
        }
        byte[] bbs = MD5.digest(cd);

        StringBuilder sb = new StringBuilder();
        for (byte b : bbs) {
            sb.append(String.format("%02x", b & 0xff)); // & 0xff чтобы избежать отрицательного значения
        }
        return sb.toString();
    }

    static void main(String[] args) throws NoSuchAlgorithmException {
        MouseCursor cursor = new MouseCursor();
        String hash = cursor.getMD5Hash();

        System.out.println(hash);
    }

}
