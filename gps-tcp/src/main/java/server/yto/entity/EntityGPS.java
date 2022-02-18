package server.yto.entity;

import lombok.Data;

import java.util.Date;

@Data
public class EntityGPS {

    /// 线路ID
    public String ROUTEID;

    /// 车载机编号
    public String PRODUCTID;

    /// 经度
    public Double LONGITUDE;

    /// 纬度
    public Double LATITUDE;

    /// 高度
    public Integer ALTITUDE;

    /// 即时速度
    public Integer GPSSPEED;

    /// 方向角
    public Integer ROTATIONANGLE;

    /// 业务时间
    public String ACTDATETIME;

    /// 时间戳，记录时间
    public String RECDATETIME;

    /// 时间类型
    public Integer ACTDATETIMEType;

    /// 是否补录 0",直接上传;1,GPRS补发;3,场站DSRC补发;5,站台上报到离站;8,第二链路实时上传;9,"第二链路补发上传
    public Integer ISAPPEND;

    /// 平均速度
    public Integer Averagepeed;

    /// 车次类型 3",GPS;4,到离站;55,违规;47,DSRC检到离场;71,GPS到离场;53,"开关门
    public Integer BUSSID;

    /// 数据类型
    public String DATATYPE;

    /// 子线号
    public String SUBROUTEID;

    /// 站点统一编号
    public String SiteNo;

    /// 累计里程（传感器）
    public Integer SENSORMILEAGE;

    /// GPS数据包
    public String GPSByte;

}
