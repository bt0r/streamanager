package animations;

import main.component.frame.mainFrame;
import main.component.jxlabel.Label;
import org.jdesktop.swingx.JXLabel;
import services.ConfigService;

import javax.swing.*;
import java.awt.*;

/**
 * Created by btor on 31/10/2016.
 */
public class LoadingAnimation extends mainFrame {
    private      ConfigService config        = ConfigService.getInstance();
    private      Label         taskLabel     = new Label(" ");
    public static final String        LEVEL_INFO    = "INFO";
    public static final String        LEVEL_ERROR   = "ERROR";
    public static final String        LEVEL_WARNING = "WARNING";
    public static final String        LEVEL_SUCCESS = "SUCCESS";

    public LoadingAnimation() {
        setUndecorated(true);
        setLayout(new BorderLayout());
        ImageIcon icon = new ImageIcon(this.getClass().getResource(config.getProp("loadingAnimation")));

        JXLabel label = new JXLabel();
        label.setIcon(icon);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setBackground(new Color(0x303030));
        taskLabel.setForeground(Color.white);
        taskLabel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        getContentPane().setBackground(new Color(0x303030));
        add(label, BorderLayout.CENTER);
        add(taskLabel, BorderLayout.SOUTH);
        setSize(new Dimension(200, 140));
        setLocationRelativeTo(null);

        setVisible(true);
    }

    public LoadingAnimation setTask(String task, String level) {
        taskLabel.setText(task);
        switch (level) {
            case LEVEL_ERROR:
                taskLabel.setBackground(new Color(Integer.decode(config.getProp("color.level.error"))));
                taskLabel.setIcon(new ImageIcon(getClass().getResource(config.getProp("levelError"))));
                break;
            case LEVEL_INFO:
                taskLabel.setBackground(new Color(Integer.decode(config.getProp("color.level.info"))));
                taskLabel.setIcon(new ImageIcon(getClass().getResource(config.getProp("levelInfo"))));
                break;
            case LEVEL_WARNING:
                taskLabel.setBackground(new Color(Integer.decode(config.getProp("color.level.warning"))));
                taskLabel.setIcon(new ImageIcon(getClass().getResource(config.getProp("levelWarning"))));
                break;
            case LEVEL_SUCCESS:
                taskLabel.setBackground(new Color(Integer.decode(config.getProp("color.level.success"))));
                taskLabel.setIcon(new ImageIcon(getClass().getResource(config.getProp("levelSuccess"))));
                break;
        }

        return this;
    }
}
