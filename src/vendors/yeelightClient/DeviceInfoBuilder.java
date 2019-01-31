package vendors.yeelightClient;

import java.util.List;

public class DeviceInfoBuilder {
    private String ip;
    private int servicePort;
    private String deviceId;
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

    public DeviceInfoBuilder setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public DeviceInfoBuilder setServicePort(int servicePort) {
        this.servicePort = servicePort;
        return this;
    }

    public DeviceInfoBuilder setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public DeviceInfoBuilder setModel(String model) {
        this.model = model;
        return this;
    }

    public DeviceInfoBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public DeviceInfoBuilder setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
        return this;
    }

    public DeviceInfoBuilder setSupportedActions(List<DeviceCommand> supportedActions) {
        this.supportedActions = supportedActions;
        return this;
    }

    public DeviceInfoBuilder setPower(String power) {
        this.power = power;
        return this;
    }

    public DeviceInfoBuilder setColorMode(DeviceColorMode colorMode) {
        this.colorMode = colorMode;
        return this;
    }

    public DeviceInfoBuilder setBright(int bright) {
        this.bright = bright;
        return this;
    }

    public DeviceInfoBuilder setColorTemperature(int colorTemperature) {
        this.colorTemperature = colorTemperature;
        return this;
    }

    public DeviceInfoBuilder setRgb(int rgb) {
        this.rgb = rgb;
        return this;
    }

    public DeviceInfoBuilder setHue(int hue) {
        this.hue = hue;
        return this;
    }

    public DeviceInfoBuilder setSat(int sat) {
        this.sat = sat;
        return this;
    }

    public DeviceInfo createDeviceInfo() {
        return new DeviceInfo(ip, servicePort, deviceId, model, name, firmwareVersion, supportedActions, power, colorMode, bright, colorTemperature, rgb, hue, sat);
    }
}