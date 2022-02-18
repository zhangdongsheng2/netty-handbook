package server.yto.constant;


public enum TianMaiBodyType {

    lngHeight(1, "lngHeight"),
    lngLow(2, "lngLow"),
    latHeight(3, "latHeight"),
    latLow(4, "latLow"),
    height(5, "height"),
    speed(6, "speed"),
    directionAngle(7, "directionAngle"),
    dateTime(8, "dateTime"),
    dateTimeType(9, "dateTimeType"),
    reissueMark(10, "reissueMark"),
    avgSpeed(11, "avgSpeed"),
    trainNumberType(12, "trainNumberType"),
    subLineNumber(13, "subLineNumber"),
    stationUnifiedNumber(14, "stationUnifiedNumber"),
    trainNumberTypeSign(15, "trainNumberTypeSign"),
    turnAround(16, "turnAround"),
    accumulatedMileage(17, "accumulatedMileage"),
    accumulatedMileageBySensor(18, "accumulatedMileageBySensor"),
    remainingOil(19, "remainingOil"),
    setGps(160, "setGps");

    private int code;
    private String type;

    private TianMaiBodyType(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public int code() {
        return code;
    }

    public String type() {
        return type;
    }

    public static String getValue(int code) {
        TianMaiBodyType[] typeEnums = values();
        for (TianMaiBodyType typeEnum : typeEnums) {
            if (typeEnum.code() == code) {
                return typeEnum.type();
            }
        }
        return null;
    }
}
