package chat;

import listeners.TimeOutListener;
import main.component.menu.Menu;
import main.component.menu.MenuItem;
import services.ConfigService;
import services.LanguageService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class UserMenu extends JPopupMenu {
    private ConfigService config = ConfigService.getInstance();

    public UserMenu(final String username) {
        Label    userLabel = new Label(username.toUpperCase());
        MenuItem info      = new MenuItem();
        Menu     timeout   = new Menu();
        MenuItem ban       = new MenuItem();

        userLabel.setBackground(Tools.Tools.darkerColor(new Color(Integer.decode(config.getProp("color.secondary")))));
        userLabel.setForeground(new Color(Integer.decode(config.getProp("color.text.secondary"))));

        // Timeout
        LanguageService trans = LanguageService.getInstance();
        timeout.setText(trans.getProp("userMenu.timeout"));
        timeout.setIcon(new ImageIcon(getClass().getResource(config.getProp("warningShieldIcon"))));
        MenuItem purge = new MenuItem();
        purge.setText(trans.getProp("userMenu.timeout.purge"));
        MenuItem to5s = new MenuItem();
        to5s.setText(trans.getProp("userMenu.timeout.5s"));
        MenuItem to30s = new MenuItem();
        to30s.setText(trans.getProp("userMenu.timeout.30s"));
        MenuItem to1m = new MenuItem();
        to1m.setText(trans.getProp("userMenu.timeout.1m"));
        MenuItem to5m = new MenuItem();
        to5m.setText(trans.getProp("userMenu.timeout.5m"));
        MenuItem to10m = new MenuItem();
        to10m.setText(trans.getProp("userMenu.timeout.10m"));
        MenuItem to15m = new MenuItem();
        to15m.setText(trans.getProp("userMenu.timeout.15m"));
        MenuItem to30m = new MenuItem();
        to30m.setText(trans.getProp("userMenu.timeout.30m"));
        MenuItem to1h = new MenuItem();
        to1h.setText(trans.getProp("userMenu.timeout.1h"));
        timeout.add(purge);
        timeout.add(to5s);
        timeout.add(to30s);
        timeout.add(to1m);
        timeout.add(to5m);
        timeout.add(to10m);
        timeout.add(to15m);
        timeout.add(to30m);
        timeout.add(to1h);


        info.setText(trans.getProp("userMenu.info"));
        info.setIcon(new ImageIcon(getClass().getResource(config.getProp("idIcon"))));

        ban.setText(trans.getProp("userMenu.ban"));
        ban.setIcon(new ImageIcon(getClass().getResource(config.getProp("crossShieldIcon"))));

        add(userLabel);
        add(info);
        add(timeout);
        add(ban);
        setVisible(true);


        /*
            Listeners
         */

        info.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                new UserInformations(username);
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        purge.addMouseListener(new TimeOutListener(1));
        /*to5s.addMouseListener(new TimeOutListener(5));
        to30s.addMouseListener(new TimeOutListener(30));
        to1m.addMouseListener(new TimeOutListener(60));
        to5m.addMouseListener(new TimeOutListener(300));
        to10m.addMouseListener(new TimeOutListener(600));
        to15m.addMouseListener(new TimeOutListener(900));
        to30m.addMouseListener(new TimeOutListener(1800));
        to1h.addMouseListener(new TimeOutListener(3600));

        ban.addMouseListener(new TimeOutListener(0));*/


    }


}
