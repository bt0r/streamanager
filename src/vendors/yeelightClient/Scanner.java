package vendors.yeelightClient;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class Scanner implements Runnable {

    private final Logger log = Logger.getLogger("StreaManager");
    private static final String DISCOVERY_MSG =
            "M-SEARCH * HTTP/1.1\r\n" +
                    "HOST:239.255.255.250:1982\r\n" +
                    "MAN:\"ssdp:discover\"\r\n" +
                    "ST:wifi_bulb\r\n";
    private static final int MULTICAST_PORT = 1982;
    private static final String MULTICAST_ADDR = "239.255.255.250";
    private final int SOCKET_TIMEOUT = 1000;

    private HashMap<String, DeviceInfo> deviceList = new HashMap<>();

    private AtomicBoolean scanning = new AtomicBoolean(false);

    @Override
    public void run() {
        scanning.set(true);
        log.info("Starting scanner");

        try {
            process();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Scanner stopped.");
        scanning.set(false);
    }

    private void process() throws IOException {
        byte[] searchPacket = DISCOVERY_MSG.getBytes();
        log.finest("Creating multicast socket.");
        MulticastSocket socket = new MulticastSocket(MULTICAST_PORT);
        socket.setTimeToLive(100);
        socket.setSoTimeout(SOCKET_TIMEOUT);
        socket.joinGroup(InetAddress.getByName(MULTICAST_ADDR));
        DatagramPacket packet;
        while (!Thread.currentThread().isInterrupted()) {
            byte[] buf = new byte[2048];
            packet = new DatagramPacket(buf, buf.length);
            try {
                log.finest("Sending discovery message.");
                socket.send(new DatagramPacket(
                        searchPacket,
                        searchPacket.length,
                        InetAddress.getByName(MULTICAST_ADDR),
                        MULTICAST_PORT)
                );
                while (!Thread.currentThread().isInterrupted()) {
                    log.finest("Waiting for devices...");
                    socket.receive(packet);
                    String lightData = new String(packet.getData());
                    if (lightData.contains("HTTP/1.1 200 OK")) {
                        //Discovery response
                        DeviceInfo deviceInfo = DeviceInfoFactory.createFromDeviceResponse(lightData);
                        deviceList.put(deviceInfo.deviceId, deviceInfo);
                        log.info("Found device. Id: " + deviceInfo.deviceId);
                    } else if (lightData.contains("NOTIFY * HTTP/1.1")) {
                        //Notify message
                        DeviceInfo deviceInfo = DeviceInfoFactory.createFromDeviceResponse(lightData);
                        deviceList.put(deviceInfo.deviceId, deviceInfo);
                        log.info("Notification from device. Id: " + deviceInfo.deviceId);
                    } else if (lightData.contains("M-SEARCH * HTTP/1.1")) {
                        //Our own message, just drop
                    } else {
                        log.info("Unknown header. Header value: " + lightData.split("\n")[0]);
                        log.info(lightData);
                    }
                }
            } catch (SocketTimeoutException ignored) {
            }
        }
        socket.leaveGroup(InetAddress.getByName(MULTICAST_ADDR));
        socket.close();
    }

    public boolean isActive() {
        return scanning.get();
    }

    public HashMap<String, DeviceInfo> getFoundDevices() {
        HashMap<String, DeviceInfo> foundDevices = deviceList;
        deviceList.clear();
        return foundDevices;
    }

}
