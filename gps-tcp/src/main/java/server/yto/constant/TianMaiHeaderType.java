package server.yto.constant;

public enum TianMaiHeaderType {

    sourceAddressOrganization(1,"sourceAddressOrganization"),
    sourceType(2,"sourceType"),
    sourceAddress(3,"sourceAddress"),
    targetType(4,"targetType"),
    destinationAddress(5,"destinationAddress"),
    responseField(6,"responseField"),
    priority(7,"priority"),
    sequenceNumber(8,"sequenceNumber"),
    timestamp(9,"timestamp"),
    endFlag(10,"endFlag");

    private int code;
    private String type;

    private TianMaiHeaderType(int code,String type) {
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
        TianMaiHeaderType[] carTypeEnums = values();
        for (TianMaiHeaderType carTypeEnum : carTypeEnums) {
            if (carTypeEnum.code() == code) {
                return carTypeEnum.type();
            }
        }
        return null;
    }
}
