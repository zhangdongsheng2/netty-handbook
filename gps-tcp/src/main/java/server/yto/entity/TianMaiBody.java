package server.yto.entity;

import lombok.Data;

import java.util.Set;

@Data
public class TianMaiBody {

    /**
     * 经度
     */
    private double lng;
    /**
     * 纬度
     */
    private double lat;

    /**
     * 高度
     */
    private int height;

    /**
     * 即时速度
     */
    private int speed;

    /**
     * 方向角
     */
    private int directionAngle;

    /**
     * 时间
     */
    private int dateTime;

    /**
     * 时间类型
     */
    private byte dateTimeType;

    /**
     * 补发标志
     */
    private byte reissueMark;

    /**
     * 平均速度
     */
    private int avgSpeed;

    /**
     * 车次类型
     */
    private byte trainNumberType;

    /**
     * 子线号
     */
    private int subLineNumber;

    /**
     * 站点统一编号
     */
    private int stationUnifiedNumber;

    /**
     * 车次类型标志
     */
    private byte trainNumberTypeSign;

    /**
     * 车辆掉头
     */
    private byte turnAround;

    /**
     * 累计里程
     */
    private int accumulatedMileage;

    /**
     * 累计里程(传感器)
     */
    private int accumulatedMileageBySensor;

    /**
     * 剩余油量
     */
    private int remainingOil;

    /**
     * 定位信息集
     */
    private Set setGps;

}
