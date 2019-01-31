package main.component.border;

import services.ConfigService;

import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by btor on 24/11/2016.
 */
public class CommonTitleBorder extends TitledBorder {
    private ConfigService config = ConfigService.getInstance();


    public CommonTitleBorder(String title) {
        super(title);
        setTitleColor(new Color(Integer.decode(config.getProp("color.text.primary"))));
    }
}
