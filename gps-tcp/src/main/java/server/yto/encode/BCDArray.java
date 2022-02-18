package server.yto.encode;

import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * <pre>
 * 名称: BCDArray
 * 描述: BCDArray
 * </pre>
 *
 * @author lcyuan(01490324 @ yto.net.cn)
 * @version 2018/9/6 16:33
 * @since 1.0.0
 */
@Data
public class BCDArray {
    private byte[] bytes;

    public BCDArray(int bytesLen) {
        this.bytes = new byte[bytesLen];
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            byte b = this.bytes[i];
            buffer.append((b >>> 4) & 0x0f);
            buffer.append(b & 0x0f);
        }
        return buffer.toString();
    }

    public void setString(String str) throws UnsupportedEncodingException {
        byte[] buf = str.getBytes(StandardCharsets.UTF_8);
        int pos = 11;
        for (int i = 1; i <= 6; i++) {
            this.bytes[6 - i] = (byte) ((buf[pos - 1] << 4) | (buf[pos] & 0x0f));
            pos -= 2;
        }
    }
}
