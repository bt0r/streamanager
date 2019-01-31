package chat;

import services.ConfigService;
import services.LanguageService;

import javax.swing.*;
import java.awt.*;

/**
 * Created by btor on 08/10/2016.
 */
public class HardwarePanel extends JPanel {
    private JLabel cpuLabel = new JLabel();
    private JLabel memLabel = new JLabel();
    private JPanel hddPanel = new JPanel();
    private JLabel netLabel = new JLabel();
    private ConfigService  config = ConfigService.getInstance();
    private LanguageService trans  = LanguageService.getInstance();

    public HardwarePanel() {
        setMinimumSize(new Dimension(150,0));
        setPreferredSize(new Dimension(150,25));
        setMaximumSize(new Dimension(300,0));
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder());
        //setLayout(new BorderLayout());
        setBackground(new Color(Integer.decode(config.getProp("color.primary"))));

        ImageIcon cpuIcon = new ImageIcon(getClass().getResource(config.getProp("cpuIcon")));
        cpuLabel.setIcon(cpuIcon);
        cpuLabel.setToolTipText(trans.getProp("chatTab.cpu"));
        cpuLabel.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));

        Font font = new Font(cpuLabel.getFont().getName(),cpuLabel.getFont().getStyle(),9);
        cpuLabel.setFont(font);

        ImageIcon memIcon = new ImageIcon(getClass().getResource(config.getProp("memIcon")));
        memLabel.setIcon(memIcon);
        memLabel.setFont(font);
        memLabel.setToolTipText(trans.getProp("chatTab.mem"));
        memLabel.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));

        ImageIcon networkIcon = new ImageIcon(getClass().getResource(config.getProp("networkIcon")));
        netLabel.setIcon(networkIcon);
        netLabel.setFont(font);
        netLabel.setToolTipText(trans.getProp("chatTab.net"));
        netLabel.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));

        hddPanel.setLayout(new BoxLayout(hddPanel,BoxLayout.Y_AXIS));
        hddPanel.setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        hddPanel.setToolTipText(trans.getProp("chatTab.hdd"));
        hddPanel.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));


        add(cpuLabel);
        add(memLabel);
        add(netLabel);
        add(hddPanel);

        setVisible(true);
        // CRONTASK
        CronHardware cronTask = new CronHardware(memLabel,cpuLabel,netLabel,hddPanel);
        cronTask.setInterval(1000);
        cronTask.start();
    }
}
