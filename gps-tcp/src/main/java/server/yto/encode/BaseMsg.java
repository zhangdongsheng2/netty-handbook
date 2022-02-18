package server.yto.encode;

import lombok.Data;
import server.yto.utils.ByteUtil;
import server.yto.utils.EndianUtil;
import server.yto.utils.SimulatorUtil;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * <pre>
 * 名称: BaseMsg
 * 描述: BaseMsg
 * </pre>
 *
 * @author lcyuan(01490324 @ yto.net.cn)
 * @version 2018/9/6 10:20
 * @since 1.0.0
 */
@Data
public abstract class BaseMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    private MsgType msgType;    // 消息ID
    private String clientID;    // 客户端ID

    public BaseMsg() {
        this.clientID = SimulatorUtil.getClientID();
    }

    public BaseMsg(MsgType msgType) {
        this.msgType = msgType;
        this.clientID = SimulatorUtil.getClientID();
    }

    protected abstract byte[] toBytes() throws UnsupportedEncodingException;

    public byte[] serialize() throws UnsupportedEncodingException {
        byte[] bytes = this.toBytes();
        int bytesLen = bytes.length;
        int bufLen = bytesLen + 3;
        byte checkCode = bytes[0];
        for (int i = 0; i < bytesLen; i++) {
            byte b = bytes[i];
            if (b == 0x7e || b == 0x7d) {
                bufLen++;
            }
            if (i > 0) checkCode ^= b;
        }
        int pos = 1;
        byte[] buf = new byte[bufLen];
        for (int i = 0; i < bytesLen; i++) {
            byte b = bytes[i];
            if (b == 0x7e) {
                buf[pos] = 0x7d;
                pos++;
                buf[pos] = 0x02;
            } else if (b == 0x7d) {
                buf[pos] = b;
                pos++;
                buf[pos] = 0x01;
            } else {
                buf[pos] = b;
            }
            pos++;
        }
        buf[0] = 0x7e;
        buf[bufLen - 2] = checkCode;
        buf[bufLen - 1] = 0x7e;
        return buf;
    }

    public enum MsgType {
        CLIENT_REPLAY(0X0001, "终端通用应答"),
        CLIENT_HEART(0X0002, "终端心跳"),
        CLIENT_LOC_REPORT(0x0200, "位置信息汇报"),
        RIGISTE_REPLAY(0x8100, "终端注册应答"),
        SERVER_REPLAY(0x8001, "平台通用应答"),
        UNKNOWN(0xffff, "未知消息");

        private Short code;
        private String desc;

        MsgType(int code, String desc) {
            this.code = (short) code;
            this.desc = desc;
        }

        public Short code() {
            return this.code;
        }

        public String desc() {
            return this.desc;
        }

        public byte[] bytes() {
            return ByteUtil.getBytes(EndianUtil.convert(this.code));
        }

        public static MsgType parseOf(int code) {
            for (MsgType msgType : MsgType.values()) {
                if (msgType.code() == (short) code) return msgType;
            }
            return MsgType.UNKNOWN;
        }
    }

    public enum CryptType {

        NO(0, "消息体不加密"),
        RSA(1, "消息体经过RSA 算法加密"),
        UNKNOWN(0xff, "数据错误");

        private int code;
        private String desc;

        CryptType(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int code() {
            return this.code;
        }

        public String desc() {
            return this.desc;
        }

        public static CryptType parseOf(int code) {
            for (CryptType cryptType : CryptType.values()) {
                if (cryptType.code() == code) return cryptType;
            }
            return CryptType.UNKNOWN;
        }
    }
}
