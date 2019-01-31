package main.component.jpanel;

import services.ConfigService;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

/**
 * Created by btor on 27/11/2016.
 */
public class CommonTabbedPane extends JTabbedPane{
    private ConfigService config = ConfigService.getInstance();

    public CommonTabbedPane() {
        setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));
        setOpaque(true);
        setUI(new BasicTabbedPaneUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                highlight = new Color(Integer.decode(config.getProp("color.primary")));
                lightHighlight = new Color(Integer.decode(config.getProp("color.secondary")));
                shadow = new Color(Integer.decode(config.getProp("color.secondary")));
                darkShadow = new Color(Integer.decode(config.getProp("color.primary")));
                focus = new Color(Integer.decode(config.getProp("color.primary")));
            }
        });
    }
}
