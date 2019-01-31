package listeners;

import org.jdesktop.swingx.JXButton;
import services.ConfigService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by btor on 29/10/2016.
 */
public class ColorThemeListener extends MouseAdapter {
    private ConfigService config = ConfigService.getInstance();
    private String   properties;
    private JXButton button;

    public ColorThemeListener(String properties, JXButton button) {
        this.properties = properties;
        this.button = button;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Color color = JColorChooser.showDialog(null, config.getProp("theme.choose"), Color.WHITE);
        config.setProp(properties, String.format("#%06X", 0xFFFFFF & color.getRGB()).replace("#","0x"));
        button.setBackground(color);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
