package main.component.button;

import services.ConfigService;

import javax.swing.*;
import java.awt.*;

/**
 * Created by btor on 26/11/2016.
 */
public class CommonCheckBox extends JCheckBox {
    private ConfigService config = ConfigService.getInstance();

    public CommonCheckBox(String text){
        super(text);

        setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));
    }

    @Override
    public void setEnabled(boolean isEnabled) {
        super.setEnabled(isEnabled);
        if(isEnabled){
            setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
            setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));
        }else{
            setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
            setForeground(new Color(0x4A4A4A));
        }
    }


}
