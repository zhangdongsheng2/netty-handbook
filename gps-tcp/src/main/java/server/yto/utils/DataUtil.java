package server.yto.utils;

import com.google.common.primitives.Bytes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName DataUtil
 * @Description T0DO
 * @Aurhor yto 吴建伟
 * @Date 2018/9/14 14:01
 */
public class DataUtil {
    /**
     * 数据校验生产校验码
     * @param b
     * @return
     */
    public static int checkBytes(byte[] b) {
        if (b.length > 1) {
            int checkCode = b[0];
            for (int i = 1; i < b.length - 1; i++) {//从第一位开始与后面的每个字节进行异或运算
                checkCode = checkCode ^ b[i];
            }
            return checkCode;
        } else {
            return 0;
        }
    }

    /**
     * 生产鉴权码
     * @return
     */
    public static String authorizeCode() {

        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}
