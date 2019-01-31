package vendors.yeelightClient;

public enum DeviceCommand {
    SET_DEFAULT("set_default"),
    SET_POWER("set_power"),
    SET_CT_ABX("set_ct_abx"),
    SET_BRIGHT("set_bright"),
    SET_SCENE("set_scene"),
    SET_RGB("set_rgb"),
    SET_HSV("set_hsv"),
    SET_ADJUST("set_adjust"),
    SET_MUSIC("set_music"),
    SET_NAME("set_name"),

    GET_PROP("get_prop"),

    START_CF("start_cf"),
    STOP_CF("stop_cf"),

    CRON_ADD("cron_add"),
    CRON_GET("cron_get"),
    CRON_DEL("cron_del"),

    TOGGLE("toggle");

    private final String name;

    DeviceCommand(String value) {
        name = value;
    }

    public static DeviceCommand fromString(String value) {
        for (DeviceCommand command : values()) {
            if (command.name.toUpperCase().equals(value.toUpperCase())) {
                return command;
            }
        }

        throw new IllegalArgumentException(String.format("Unsupported value %s", value));
    }
}
