package main.component.jxlabel;

import services.ConfigService;

import javax.swing.*;
import java.awt.*;

/**
 * Created by btor on 20/11/2016.
 */
public class CommonTextField extends JTextField {
    private ConfigService config = ConfigService.getInstance();

    public CommonTextField() {
        setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));
        setBorder(BorderFactory.createLineBorder(new Color(Integer.decode(config.getProp("color.secondary")))));

    }
}
