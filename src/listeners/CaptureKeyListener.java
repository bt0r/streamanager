/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package listeners;

import hotkeys.HotKeysTab;
import main.component.jpanel.CommonPanel;
import main.component.jxlabel.Label;
import services.ConfigService;
import services.HotKeysService;
import services.LanguageService;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by btor on 09/02/2017.
 */
public class CaptureKeyListener implements KeyListener {
    private Label       label;
    private CommonPanel panel;
    private ConfigService   config = ConfigService.getInstance();
    private LanguageService trans  = LanguageService.getInstance();
    private Label message;

    public CaptureKeyListener(Label label, CommonPanel panel, Label message) {
        this.label = label;
        this.panel = panel;
        this.message = message;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) || ((e.getModifiers() & KeyEvent.SHIFT_MASK) != 0) || ((e.getModifiers() & KeyEvent.ALT_MASK) != 0)) {
            String modifiers = "";
            if (((e.getModifiers() & KeyEvent.CTRL_MASK) != 0))
                modifiers += "ctrl ";
            if (((e.getModifiers() & KeyEvent.SHIFT_MASK) != 0))
                modifiers += "shift ";
            if (((e.getModifiers() & KeyEvent.ALT_MASK) != 0))
                modifiers += "alt ";

            this.label.setText(modifiers.toUpperCase() + KeyEvent.getKeyText(e.getKeyCode()));
            panel.setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
            panel.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));
            message.setText(trans.getProp("hotkeys.edit"));
            System.out.println(modifiers + KeyEvent.getKeyText(e.getKeyCode()));
            if (e.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD) {
                // Key is from numpad
                config.setProp(panel.getName() + "_keys", modifiers + "NUMPAD"+e.getKeyChar());
            } else {
                config.setProp(panel.getName() + "_keys", modifiers + KeyEvent.getKeyText(e.getKeyCode()));
            }

            panel.setFocusable(false);

            // Load hotkeys
            HotKeysService.load((HotKeysTab) panel.getParent());
            //this.label.setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        }
    }
}
