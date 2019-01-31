/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package listeners;

import main.component.jpanel.CommonPanel;
import main.component.jxlabel.Label;
import services.ConfigService;
import services.LanguageService;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by btor on 11/02/2017.
 */
public class HotKeyMouseListener implements MouseListener {
    private CommonPanel panel;
    private Label       label;
    private Label       message;
    private ConfigService   config = ConfigService.getInstance();
    private LanguageService trans  = LanguageService.getInstance();

    public HotKeyMouseListener(CommonPanel panel, Label label, Label messageBox) {
        this.panel = panel;
        this.label = label;
        this.message = messageBox;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Panel must be focusable and is waiting hotkeys
        message.setText(trans.getProp("hotkeys.editing"));
        panel.setFocusable(true);
        panel.requestFocusInWindow();
        panel.addKeyListener(new CaptureKeyListener(label, panel,message));
        panel.setOpaque(true);
        panel.setBackground(new Color(Integer.decode(config.getProp("color.secondary"))));
        panel.setForeground(new Color(Integer.decode(config.getProp("color.text.secondary"))));
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
        message.setText(trans.getProp("hotkeys.edit"));
        panel.setFocusable(false);
        panel.setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        panel.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));
    }
}
