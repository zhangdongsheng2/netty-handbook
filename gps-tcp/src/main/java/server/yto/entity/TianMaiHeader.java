package server.yto.entity;

import lombok.Data;

@Data
public class TianMaiHeader {

    /**
     * 源地址的组织，如线路编号
     */
    private int sourceAddressOrganization;

    /**
     * 源地址类型
     */
    private byte sourceType;

    /**
     * 源地址编号，如车载机编号
     */
    private int sourceAddress;

    /**
     * 目标地址类型
     */
    private byte targetType;

    /**
     * 目标地址编号
     */
    private int destinationAddress;

    /**
     * 是否需要回应和回应是否成功
     */
    private byte responseField;

    /**
     * 数据包的优先级
     */
    private byte priority;

    /**
     * 消息流水号,顺序累加,步长为 1,循环使
     * 用（一对请求和应答消息的流水号必须
     * 相同）由应用端维护此消息号。
     */
    private byte sequenceNumber;

    /**
     * 通信包时间戳
     */
    private int timestamp;

    /**
     * 标识分段数据包是否结束
     */
    private byte endFlag;

}
