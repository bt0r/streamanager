package main.component.jxlabel;

import org.jdesktop.swingx.JXLabel;
import services.ConfigService;

import javax.swing.*;
import java.awt.*;

/**
 * Created by btor on 31/10/2016.
 */
public class BadgeLabel extends JXLabel {
    private ConfigService config = ConfigService.getInstance();

    public BadgeLabel(){
        super();
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));


    }
    public BadgeLabel(Icon icon){
        super();
        setIcon(icon);
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

    }
}
