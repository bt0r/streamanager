package chat;

import org.hyperic.sigar.*;
import services.ConfigService;
import services.LogService;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;


public class CronHardware extends Thread {
    private ConfigService config     = ConfigService.getInstance();
    private LogService    streamFile = LogService.getInstance();
    private Logger        logger     = Logger.getLogger("streaManager");
    private JLabel memLabel;
    private JLabel cpuLabel;
    private JPanel hddPanel;
    private JLabel networkLabel;
    private Sigar sigar    = null;
    private int   interval = 5000;
    private long  rxBytes  = 0;
    private long  txBytes  = 0;
    private long  rxSpeed  = 0;
    private long  txSpeed  = 0;

    public CronHardware(JLabel memLabel, JLabel cpuLabel, JLabel networkLabel, JPanel hddPanel) {
        this.memLabel = memLabel;
        this.cpuLabel = cpuLabel;
        this.hddPanel = hddPanel;
        this.networkLabel = networkLabel;
    }

    public void run() {
        final Logger logger = Logger.getLogger("streaManager");
        logger.fine("Starting cron hardware task...");

        long  startTime = 0;
        Timer timer     = new Timer();
        TimerTask tache = new TimerTask() {
            @Override
            public void run() {
                if (config.isLibsLoaded()) {
                    // WAIT UNTIL LIBS ARE LOADED
                    try {
                        sigar = new Sigar();
                        Mem          mem    = sigar.getMem();
                        FileSystem[] fsList = sigar.getFileSystemList();

                        // NETWORK
                        String[] netInts = sigar.getNetInterfaceList();
                        for (String netInt : netInts) {
                            NetInterfaceStat netInterface = sigar.getNetInterfaceStat(netInt);
                            if (netInterface.getRxBytes() > rxBytes) {
                                // NEW VALUE MUST BE GREATER THAN RXBYTES
                                rxSpeed = (netInterface.getRxBytes() - rxBytes) * 8;
                                txSpeed = (netInterface.getTxBytes() - txBytes) * 8;
                                rxBytes = netInterface.getRxBytes();
                                txBytes = netInterface.getTxBytes();
                                break;
                            }
                        }

                        String tcpReceived = Sigar.formatSize(rxSpeed);
                        String tcpSent     = Sigar.formatSize(txSpeed);
                        networkLabel.setText("↓" + tcpReceived + " ↑" + tcpSent);
                        streamFile.writeNetwork(tcpReceived, tcpSent);

                        // FILESYSTEM
                        hddPanel.removeAll();
                        int i = 0;
                        for (FileSystem fs : fsList) {
                            try {

                                FileSystemUsage usage      = sigar.getFileSystemUsage(fs.getDirName());
                                String          devName    = fs.getDevName();
                                String          hddPercent = (int) Math.ceil(usage.getUsePercent() * 100) + "%";

                                JLabel    hddLabel = new JLabel(devName + " " + hddPercent);
                                ImageIcon hddIcon  = new ImageIcon(getClass().getResource(config.getProp("hddIcon")));
                                Font      font     = new Font(hddLabel.getFont().getName(), hddLabel.getFont().getStyle(), 9);

                                if (i == 0) {
                                    // Write percent in log file
                                    streamFile.writeHDD(hddPercent);
                                    i++;
                                }

                                hddLabel.setIcon(hddIcon);
                                hddLabel.setFont(font);
                                hddLabel.setOpaque(true);
                                hddLabel.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));
                                if ((int) Math.ceil(usage.getUsePercent() * 100) > 90) {
                                    hddLabel.setBackground(new Color(Integer.decode(config.getProp("color.red.light"))));
                                } else {
                                    hddLabel.setBackground(null);
                                }

                                hddPanel.add(hddLabel);
                            } catch (Exception e) {
                            }

                        }
                        hddPanel.revalidate();
                        hddPanel.repaint();

                        // CPU
                        int cpuLoad = (int) Math.ceil(sigar.getCpuPerc().getCombined() * 100);
                        streamFile.writeCPU(String.valueOf(cpuLoad) + "%");
                        cpuLabel.setText(cpuLoad + "%");
                        cpuLabel.setOpaque(true);

                        if (cpuLoad >= 90) {
                            cpuLabel.setBackground(new Color(Integer.decode(config.getProp("color.red.light"))));
                        } else if (cpuLoad > 80 && cpuLoad < 90) {
                            cpuLabel.setBackground(new Color(Integer.decode(config.getProp("color.orange"))));
                        } else {
                            cpuLabel.setBackground(null);
                        }

                        // MEMORY
                        int    memPercent = (int) Math.ceil(mem.getUsedPercent());
                        String memUsed    = Sigar.formatSize(mem.getActualUsed());
                        String memTotal   = Sigar.formatSize(mem.getTotal());
                        streamFile.writeRAM(String.valueOf(memPercent) + "%");
                        memLabel.setText(String.valueOf(memPercent) + "%");


                    } catch (Exception e) {
                        logger.warning("Error when trying to refresh hardware monitoring, error:" + e.getMessage());
                    }

                }


            }
        };
        timer.scheduleAtFixedRate(tache, startTime, (long) getInterval());

    }

    public int getInterval() {
        return this.interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

}
