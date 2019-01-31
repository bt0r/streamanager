package main.component.jpanel;

import org.jdesktop.swingx.JXPanel;
import services.ConfigService;

import java.awt.*;

/**
 * Created by btor on 31/10/2016.
 */
public class CommonPanel extends JXPanel{
    protected ConfigService config = ConfigService.getInstance();

    public CommonPanel(){
        super();
        setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));
    }

}
