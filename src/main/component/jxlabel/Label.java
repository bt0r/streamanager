package main.component.jxlabel;

import org.jdesktop.swingx.JXLabel;
import services.ConfigService;

import javax.swing.*;
import java.awt.*;

/**
 * Created by btor on 31/10/2016.
 */
public class Label extends JXLabel{
    private ConfigService config = ConfigService.getInstance();

    public Label(String text) {
        super(text);
        setOpaque(true);
        setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));
    }

    public Label() {
        setOpaque(true);
        setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));
    }

    public Label(Icon icon) {
        setIcon(icon);
        setOpaque(true);
        setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));
    }
    public Label(String text, Icon icon, int horizontalAlignment){
        super();
    }
}
