package main.component.menu;

import services.ConfigService;

import javax.swing.*;
import java.awt.*;

/**
 * Created by btor on 19/08/2016.
 */
public class MenuItem extends JMenuItem {
    private ConfigService config = ConfigService.getInstance();

    public MenuItem(String text){
        super();
        setText(text);
        setBorder(BorderFactory.createEmptyBorder());
        setOpaque(true);
        setBackground(new Color(Integer.decode(config.getProp("color.secondary"))));
        setForeground(new Color(Integer.decode(config.getProp("color.text.secondary"))));

    }
    public MenuItem(){
        super();
        setBorder(BorderFactory.createEmptyBorder());
        setOpaque(true);
        setBackground(new Color(Integer.decode(config.getProp("color.secondary"))));
        setForeground(new Color(Integer.decode(config.getProp("color.text.secondary"))));

    }
}
