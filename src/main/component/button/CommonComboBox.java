package main.component.button;

import org.jdesktop.swingx.JXComboBox;
import services.ConfigService;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;

/**
 * Created by btor on 26/11/2016.
 */
public class CommonComboBox extends JXComboBox {
    private ConfigService config = ConfigService.getInstance();

    CommonComboBox() {
        super();
        UIManager.put("ComboBox.background", new ColorUIResource(new Color(Integer.decode(config.getProp("color.primary")))));
        UIManager.put("ComboBox.foreground", new ColorUIResource(new Color(Integer.decode(config.getProp("color.text.primary")))));
        UIManager.put("ComboBox.selectionBackground", new ColorUIResource(new Color(Integer.decode(config.getProp("color.table.whisper")))));
        UIManager.put("ComboBox.selectionForeground", new ColorUIResource(new Color(Integer.decode(config.getProp("color.text.primary")))));
        setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        setForeground(new Color(Integer.decode(config.getProp("color.black"))));
    }
}
