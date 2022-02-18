package server.yto.utils;

/**
 * @ClassName DataConversion
 * @Description T0DO
 * @Aurhor yto 吴建伟
 * @Date 2018/9/5 8:45
 */
public class ByteDecodeUtil {
    /**
     * shot 转 byte[]
     *
     * @param s
     * @return
     */
    public static byte[] short2bytes(short s) {
        byte[] shortBuf = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = (shortBuf.length - 1 - i) * 8;
            shortBuf[i] = (byte) ((s >>> offset) & 0xff);
        }
        return shortBuf;
    }

    /**
     * int 转 byte[]
     *
     * @param num
     * @return
     */
    public static byte[] int2Bytes(int num) {
        byte[] byteNum = new byte[4];
        for (int ix = 0; ix < 4; ++ix) {
            int offset = 32 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    /**
     * byte[] 转 int
     *
     * @param byteNum
     * @return
     */
    public static int bytes2Int(byte[] byteNum) {
        int num = 0;
        for (int ix = 0; ix < 4; ++ix) {
            num <<= 8;
            num |= (byteNum[ix] & 0xff);
        }
        return num;
    }

    /**
     * int 转 byte
     *
     * @param num
     * @return
     */
    public static byte int2OneByte(int num) {
        return (byte) (num & 0x000000ff);
    }

    /**
     * byt 转 int
     *
     * @param byteNum
     * @return
     */
    public static int oneByte2Int(byte byteNum) {
        return byteNum > 0 ? byteNum : (128 + (128 + byteNum));
    }

    /**
     * long 转 byte[]
     *
     * @param num
     * @return
     */
    public static byte[] long2Bytes(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

}
