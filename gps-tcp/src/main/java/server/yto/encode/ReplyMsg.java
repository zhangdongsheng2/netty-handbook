package server.yto.encode;

import lombok.Data;
import server.yto.utils.ByteUtil;
import server.yto.utils.EndianUtil;

import java.io.UnsupportedEncodingException;

/**
 * <pre>
 * 名称: ReplyMsg
 * 描述: ReplyMsg
 * </pre>
 *
 * @author lcyuan(01490324 @ yto.net.cn)
 * @version 2018/9/6 16:42
 * @since 1.0.0
 */
@Data
public class ReplyMsg extends MsgHead {
    public static final int BODY_LEN = 5;
    private Integer serial;
    private Integer replyId;
    private Short result;

    public ReplyMsg(int msgId) {
        super(MsgType.parseOf(msgId));
    }

    public ReplyMsg(MsgType msgType) {
        super(msgType);
    }

    @Override
    protected byte[] toBytes() throws UnsupportedEncodingException {
        this.getAttr().setBodyLen(BODY_LEN);
        byte[] head = super.toBytes();
        byte[] buf = new byte[head.length + BODY_LEN];
        byte[] serial = ByteUtil.getBytes(EndianUtil.convert(this.serial.shortValue()));
        byte[] replyId = ByteUtil.getBytes(EndianUtil.convert(this.replyId.shortValue()));
        byte[] result = ByteUtil.getBytes(EndianUtil.convert(this.result));
        System.arraycopy(head, 0, buf, 0, head.length);
        System.arraycopy(serial, 0, buf, head.length, serial.length);
        System.arraycopy(replyId, 0, buf, head.length + 2, replyId.length);
        System.arraycopy(result, 0, buf, head.length + 4, 1);
        return buf;
    }
}
