package server.yto.entity;

import lombok.Data;

import java.util.Arrays;

/**
 * 解析消息
 */
public class DataGPSFormat {

    /**
     * 整包长
     */
    private int msgLength;

    /**
     * 版本号
     */
    private int msgVersion;

    /**
     * 包类型
     */
    private int msgType;

    /**
     * 头长度
     */
    private int msgHeaderLength;

    /**
     * 包头
     */
    private byte[] msgHeader;

    /**
     * 包体
     */
    private byte[] msgBody;





    public int getMsgLength() {
        return msgLength;
    }

    public void setMsgLength(int msgLength) {
        this.msgLength = msgLength;
    }

    public int getMsgVersion() {
        return msgVersion;
    }

    public void setMsgVersion(int msgVersion) {
        this.msgVersion = msgVersion;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getMsgHeaderLength() {
        return msgHeaderLength;
    }

    public void setMsgHeaderLength(int msgHeaderLength) {
        this.msgHeaderLength = msgHeaderLength;
    }

    public byte[] getMsgHeader() {
        return msgHeader;
    }

    public void setMsgHeader(byte[] msgHeader) {
        this.msgHeader = msgHeader;
    }

    public byte[] getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(byte[] msgBody) {
        this.msgBody = msgBody;
    }

    @Override
    public String toString() {
        return "DataGPSFormat{" +
                "msgLength=" + msgLength +
                ", msgVersion=" + msgVersion +
                ", msgType=" + msgType +
                ", msgHeaderLength=" + msgHeaderLength +
                ", msgHeader=" + Arrays.toString(msgHeader) +
                ", msgBody=" + Arrays.toString(msgBody) +
                '}';
    }
}
