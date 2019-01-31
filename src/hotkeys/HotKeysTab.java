/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package hotkeys;

import main.component.jpanel.CommonPanel;
import services.HotKeysService;

import java.awt.*;

/**
 * Created by btor on 08/02/2017.
 */
public class HotKeysTab extends CommonPanel {


    public HotKeysTab() {
        setLayout(new GridLayout(0,2));
        setName("hotkeys_tab");
        add(new HotKeyPanel(1));
        add(new HotKeyPanel(2));
        add(new HotKeyPanel(3));
        add(new HotKeyPanel(4));
        add(new HotKeyPanel(5));
        add(new HotKeyPanel(6));
        add(new HotKeyPanel(7));
        add(new HotKeyPanel(8));
        add(new HotKeyPanel(9));
        add(new HotKeyPanel(10));

        setVisible(true);

        HotKeysService.load(this);
    }

}
