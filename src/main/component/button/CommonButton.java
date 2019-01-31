package main.component.button;

import org.jdesktop.swingx.JXButton;
import services.ConfigService;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


/**
 * Created by btor on 31/10/2016.
 */
public class CommonButton extends JXButton {
    private ConfigService config = ConfigService.getInstance();

    public CommonButton() {
        super();
        this.applyTheme();
    }

    public CommonButton(String text) {
        super();
        setText(text);
        this.applyTheme();
    }

    public CommonButton(Icon icon) {
        super();
        setIcon(icon);
        this.applyTheme();
    }

    private void applyTheme(){
        Border border = BorderFactory.createLineBorder(new Color(Integer.decode(config.getProp("color.secondary"))));
        setBorder(BorderFactory.createCompoundBorder(border,BorderFactory.createEmptyBorder(2,2,2,2)));
        setBackground(new Color(Integer.decode(config.getProp("color.secondary"))));
        setForeground(new Color(Integer.decode(config.getProp("color.text.secondary"))));
        setOpaque(true);
    }
}
