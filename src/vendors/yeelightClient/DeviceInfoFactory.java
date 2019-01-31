package vendors.yeelightClient;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class DeviceInfoFactory {
    private static final Logger log = Logger.getLogger(DeviceInfoFactory.class.getSimpleName());

    public static DeviceInfo createFromDeviceResponse(String deviceResponse) {
        DeviceInfoBuilder builder = new DeviceInfoBuilder();

        String[] headers = deviceResponse.split("\n");
        for (String header : headers) {
            if (!header.contains(": ")) { //skipping response status
                continue;
            }
            String[] values = header.split(": ");
            String key = values[0];
            String value = values.length == 2 ? values[1] : "";

            try {
                matchKeyWithDeviceInfo(builder, key, value);
            } catch (IllegalArgumentException e) {
                //Unsupported feature
            }
        }

        return builder.createDeviceInfo();
    }

    private static void matchKeyWithDeviceInfo(DeviceInfoBuilder builder, String key, String value) {
        switch (DeviceAttribute.fromString(key)) {
            case LOCATION:
                String[] values = value.substring(11).split(":");
                builder.setIp(values[0]);
                builder.setServicePort(Integer.valueOf(values[1]));
                break;
            case ID:
                builder.setDeviceId(value);
                break;
            case MODEL:
                builder.setModel(value);
                break;
            case FW:
                builder.setFirmwareVersion(value);
                break;
            case SUPPORT:
                String[] commands = value.split(" ");
                List<DeviceCommand> commandList = new ArrayList<>();
                for (String command : commands) {
                    commandList.add(DeviceCommand.fromString(command));
                }
                builder.setSupportedActions(commandList);
                break;
            case POWER:
                builder.setPower(value);
                break;
            case BRIGHT:
                builder.setBright(Integer.valueOf(value));
                break;
            case COLOR_MODE:
                builder.setColorMode(DeviceColorMode.fromString(value));
                break;
            case COLOR_TEMP:
                builder.setColorTemperature(Integer.valueOf(value));
                break;
            case RGB:
                builder.setRgb(Integer.valueOf(value));
                break;
            case HUE:
                builder.setHue(Integer.valueOf(value));
                break;
            case SAT:
                builder.setSat(Integer.valueOf(value));
                break;
            case NAME:
                builder.setName(value);
                break;
        }
    }
}
