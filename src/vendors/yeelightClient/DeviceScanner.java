package vendors.yeelightClient;


import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class DeviceScanner {

    private final Logger log = Logger.getLogger("StreaManager");
    private HashMap<String, DeviceInfo> devices;

    private       ExecutorService executor = null;
    private final Scanner         scanner  = new Scanner();

    public void scanDevices() {
        System.out.println("TEST SCAN");
        if (scanner.isActive()) {
            System.out.println("SCAN ACTIVE");
            log.info("Scanning is active.");
            return;
        }
        executor = Executors.newSingleThreadExecutor(r -> new Thread(r, "DeviceScannerThread"));
        executor.execute(scanner);
    }

    public void stopScanning() {
        try {
            executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.warning("Thread failed to shutdown. Killing it.");
        }
        executor.shutdownNow();
        int max_sleeps = 50;
        while (! executor.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (-- max_sleeps < 0) break;
        }
    }

}
