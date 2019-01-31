package vendors.yeelightClient;

public enum DeviceAttribute {

    LOCATION("Location"),
    ID("id"),
    MODEL("model"),
    FW("fw_ver"),
    SUPPORT("support"),
    POWER("power"),
    BRIGHT("bright"),
    COLOR_MODE("color_mode"),
    COLOR_TEMP("ct"),
    RGB("rgb"),
    HUE("hue"),
    SAT("sat"),
    NAME("name");

    private final String name;

    DeviceAttribute(String value) {
        name = value;
    }

    public static DeviceAttribute fromString(String value) {
        for (DeviceAttribute attribute : values()) {
            if (attribute.name.toUpperCase().equals(value.toUpperCase())) {
                return attribute;
            }
        }

        throw new IllegalArgumentException(String.format("Unknown value %s", value));
    }
}
