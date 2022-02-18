package server.yto.utils;

/**
 * <pre>
 * 名称: ByteUtil
 * 描述: ByteUtil
 * </pre>
 *
 * @author lcyuan(01490324 @ yto.net.cn)
 * @version 2018/9/6 13:36
 * @since 1.0.0
 */
public class ByteUtil {

    public static byte[] getBytes(Short data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >>> 8);
        return bytes;
    }

    public static byte[] getBytes(Integer data) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >>> 8);
        bytes[2] = (byte) ((data & 0xff0000) >>> 16);
        bytes[3] = (byte) ((data & 0xff000000) >>> 24);
        return bytes;
    }

    public static byte[] getBytes(Long data) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data >>> 8) & 0xff);
        bytes[2] = (byte) ((data >>> 16) & 0xff);
        bytes[3] = (byte) ((data >>> 24) & 0xff);
        bytes[4] = (byte) ((data >>> 32) & 0xff);
        bytes[5] = (byte) ((data >>> 40) & 0xff);
        bytes[6] = (byte) ((data >>> 48) & 0xff);
        bytes[7] = (byte) ((data >>> 56) & 0xff);
        return bytes;
    }

    public static byte[] getBytes(Float data) {
        int intBits = Float.floatToIntBits(data);
        return getBytes(intBits);
    }

    public static byte[] getBytes(Double data) {
        long intBits = Double.doubleToLongBits(data);
        return getBytes(intBits);
    }

    public static Short getShort(byte[] bytes) {
        return (short) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    public static Integer getInt(byte[] bytes) {
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16))
                | (0xff000000 & (bytes[3] << 24));
    }

    public static Long getLong(byte[] bytes) {
        return (0xffL & (long) bytes[0]) | (0xff00L & ((long) bytes[1] << 8)) | (0xff0000L & ((long) bytes[2] << 16))
                | (0xff000000L & ((long) bytes[3] << 24)) | (0xff00000000L & ((long) bytes[4] << 32))
                | (0xff0000000000L & ((long) bytes[5] << 40)) | (0xff000000000000L & ((long) bytes[6] << 48))
                | (0xff00000000000000L & ((long) bytes[7] << 56));
    }

    public static Float getFloat(byte[] bytes) {
        return Float.intBitsToFloat(getInt(bytes));
    }

    public static Double getDouble(byte[] bytes) {
        long l = getLong(bytes);
        System.out.println(l);
        return Double.longBitsToDouble(l);
    }

    public static String getString(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length);
        String sTmp;

        for (int i = 0; i < bytes.length; i++) {
            sTmp = Integer.toHexString(0xFF & bytes[i]);
            sb.append("0x");
            if (sTmp.length() < 2)
                sb.append(0);
            sb.append(sTmp.toUpperCase()).append(" ");
        }

        return sb.toString();
    }
}
