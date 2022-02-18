package server.yto.encode;

/**
 * <pre>
 * 名称: HeartBeat
 * 描述: HeartBeat
 * </pre>
 *
 * @author lcyuan(01490324 @ yto.net.cn)
 * @version 2018/9/6 16:30
 * @since 1.0.0
 */
public class HeartBeat extends MsgHead {
    public HeartBeat() {
        super(BaseMsg.MsgType.CLIENT_HEART);
    }
}
