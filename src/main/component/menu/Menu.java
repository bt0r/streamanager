package main.component.menu;

import services.ConfigService;

import javax.swing.*;
import java.awt.*;

/**
 * Created by btor on 19/08/2016.
 */
public class Menu extends JMenu {
    private ConfigService config = ConfigService.getInstance();

    public Menu(String text){
        super();
        setBorderPainted(false);
        setText(text);
        setOpaque(true);
        setBackground(new Color(Integer.decode(config.getProp("color.secondary"))));
        setForeground(new Color(Integer.decode(config.getProp("color.text.secondary"))));
    }
    public Menu(){
        super();
        setBorderPainted(false);
        setOpaque(true);
        setBackground(new Color(Integer.decode(config.getProp("color.secondary"))));
        setForeground(new Color(Integer.decode(config.getProp("color.text.secondary"))));
    }
}
