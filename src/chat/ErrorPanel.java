package chat;

import services.LanguageService;
import settings.accountsFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by btor on 04/03/2016.
 */
public class ErrorPanel extends JPanel {

    public ErrorPanel() {
        setAlignmentX(JComponent.CENTER_ALIGNMENT);
        setPreferredSize(new Dimension(600, 200));

        LanguageService trans    = LanguageService.getInstance();
        JButton         settings = new JButton(trans.getProp("settings.account.open"));

        add(new JLabel(trans.getProp("chatTab.botError")));
        add(settings);
        settings.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                new accountsFrame();
            }

        });
    }
}
