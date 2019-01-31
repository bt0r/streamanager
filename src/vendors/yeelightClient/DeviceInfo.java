package vendors.yeelightClient;

import java.util.List;

public class DeviceInfo {
    private String ip;
    private int servicePort;
    String deviceId;
    private String model;
    private String name;
    private String firmwareVersion;
    private List<DeviceCommand> supportedActions;
    private String power;
    private DeviceColorMode colorMode;
    private int bright;
    private int colorTemperature;
    private int rgb;
    private int hue;
    private int sat;

    public DeviceInfo(String ip, int servicePort, String deviceId, String model, String name, String firmwareVersion, List<DeviceCommand> supportedActions, String power, DeviceColorMode colorMode, int bright, int colorTemperature, int rgb, int hue, int sat) {
        this.ip = ip;
        this.servicePort = servicePort;
        this.deviceId = deviceId;
        this.model = model;
        this.name = name;
        this.firmwareVersion = firmwareVersion;
        this.supportedActions = supportedActions;
        this.power = power;
        this.colorMode = colorMode;
        this.bright = bright;
        this.colorTemperature = colorTemperature;
        this.rgb = rgb;
        this.hue = hue;
        this.sat = sat;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public List<DeviceCommand> getSupportedActions() {
        return supportedActions;
    }


}
