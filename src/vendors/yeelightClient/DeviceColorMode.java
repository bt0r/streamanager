package vendors.yeelightClient;

public enum DeviceColorMode {
    COLOR(1),
    TEMPERATURE(2),
    HSV(3);

    private final int value;

    DeviceColorMode(int value) {
        this.value = value;
    }

    public static DeviceColorMode fromString(String value) {
        for (DeviceColorMode colorMode : values()) {
            if (colorMode.value == Integer.valueOf(value)) {
                return colorMode;
            }
        }

        throw new IllegalArgumentException(String.format("Unsupported value %s", value));
    }
}
