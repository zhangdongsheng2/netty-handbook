package server.yto.utils;

import java.nio.ByteOrder;

/**
 * <pre>
 * 名称: EndianUtil
 * 描述: EndianUtil
 * </pre>
 *
 * @author lcyuan(01490324 @ yto.net.cn)
 * @version 2018/9/6 11:28
 * @since 1.0.0
 */
public class EndianUtil {

    /**
     * 大小端模式标识
     * 大端: true
     * 小端: false
     */
    private static boolean ENDIAN = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;

    /**
     * 两字节转换
     *
     * @param value
     * @param <T>
     * @return
     */
    public static <T extends Number> T convert(T value) {
        if (EndianUtil.ENDIAN) return value;
        if (value instanceof Short) {
            value = (T) ByteUtil.getShort(reverseByteArray(ByteUtil.getBytes((Short) value)));
        } else if (value instanceof Integer) {
            value = (T) ByteUtil.getInt(reverseByteArray(ByteUtil.getBytes((Integer) value)));
        } else if (value instanceof Long) {
            value = (T) ByteUtil.getLong(reverseByteArray(ByteUtil.getBytes((Long) value)));
        } else if (value instanceof Float) {
            value = (T) ByteUtil.getFloat(reverseByteArray(ByteUtil.getBytes((Float) value)));
        } else if (value instanceof Double) {
            value = (T) ByteUtil.getDouble(reverseByteArray(ByteUtil.getBytes((Double) value)));
        } else {
            throw new IllegalArgumentException("Unknown param class type");
        }
        return value;
    }

    /**
     * 反转字节数组
     *
     * @param buf
     * @return
     */
    private static byte[] reverseByteArray(byte[] buf) {
        int len = buf.length;
        for (int i = len >>> 1, j = i - 1; i < len; i++, j--) {
            byte tmp = buf[j];
            buf[j] = buf[i];
            buf[i] = tmp;
        }
        return buf;
    }

    public static void main(String[] args) {
        if (EndianUtil.ENDIAN) {
            System.out.print("big endian");
        } else {
            System.out.print("little endian");
        }
        long a = EndianUtil.convert((long) 0x11223344);
        System.out.print(String.format("%02x", a));
    }
}
