package main;

import about.AboutFrame;
import commands.CommandsFrame;
import main.component.menu.Menu;
import main.component.menu.MenuItem;
import services.ConfigService;
import services.LanguageService;
import settings.accountsFrame;
import settings.settingsFrame;
import updater.UpdaterFrame;

import javax.swing.*;
import javax.swing.plaf.basic.BasicMenuBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;


public class menuBar extends JMenuBar {
    private ConfigService config = ConfigService.getInstance();
    private Logger        logger = Logger.getLogger("streaManager");


    public menuBar() {
        setUI(new BasicMenuBarUI() {
            public void paint(Graphics g, JComponent c) {
                g.setColor(new Color(Integer.decode(config.getProp("color.secondary"))));
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
            }
        });
        UIManager.put("PopupMenu.border", BorderFactory.createLineBorder(new Color(Integer.decode(config.getProp("color.primary"))), 1));

        //Configuration menuBar
        setOpaque(true);
        setBackground(new Color(Integer.decode(config.getProp("color.secondary"))));
        setForeground(new Color(Integer.decode(config.getProp("color.text.secondary"))));
        setBorder(BorderFactory.createEmptyBorder());


        // MENU
        LanguageService trans        = LanguageService.getInstance();
        Menu            menu         = new Menu(trans.getProp("menu.label"));
        MenuItem        settings     = new MenuItem(trans.getProp("menu.settings"));
        MenuItem        commands     = new MenuItem(trans.getProp("menu.commands"));
        MenuItem        accounts     = new MenuItem(trans.getProp("menu.accounts"));
        ImageIcon       settingsIcon = new ImageIcon(getClass().getResource(config.getProp("settingsIcon")));
        ImageIcon       commandsIcon = new ImageIcon(getClass().getResource(config.getProp("commandsIcon")));
        ImageIcon       accountIcon  = new ImageIcon(getClass().getResource(config.getProp("accountIcon")));

        settings.setIcon(settingsIcon);
        commands.setIcon(commandsIcon);
        settings.setIcon(settingsIcon);
        accounts.setIcon(accountIcon);

        menu.add(settings);
        menu.add(commands);
        menu.add(accounts);

        add(menu);

        // CONTACT
        Menu      menuContact = new Menu(trans.getProp("menu.help"));
        MenuItem  menuAbout   = new MenuItem(trans.getProp("menu.about"));
        ImageIcon aboutIcon   = new ImageIcon(getClass().getResource(config.getProp("aboutIcon")));
        menuAbout.setIcon(aboutIcon);
        MenuItem  menuThanks = new MenuItem(trans.getProp("menu.thanks"));
        ImageIcon thanksIcon = new ImageIcon(getClass().getResource(config.getProp("thanksIcon")));
        menuThanks.setIcon(thanksIcon);
        MenuItem  menuUpdater = new MenuItem(trans.getProp("menu.updater"));
        ImageIcon cloudIcon   = new ImageIcon(getClass().getResource(config.getProp("cloudIcon")));
        menuUpdater.setIcon(cloudIcon);

        menuContact.add(menuUpdater);
        menuContact.add(menuAbout);
        menuContact.add(menuThanks);


        Menu language = new Menu(trans.getProp("menu.languages"));

        // LANGUAGE
        MenuItem  langFr     = new MenuItem(trans.getProp("menu.lang.fr"));
        ImageIcon frenchFlag = new ImageIcon(getClass().getResource(config.getProp("frenchFlag")));
        langFr.setIcon(frenchFlag);
        MenuItem  langEn      = new MenuItem(trans.getProp("menu.lang.en"));
        ImageIcon englishFlag = new ImageIcon(getClass().getResource(config.getProp("englishFlag")));
        langEn.setIcon(englishFlag);
        language.add(langFr);
        language.add(langEn);

        add(menuContact);
        add(Box.createHorizontalGlue());
        add(language);
        setVisible(true);
        /*
         * Listeners
		 */
        menuUpdater.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UpdaterFrame();
            }
        });
       
        menuAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AboutFrame();
            }

        });
        accounts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new accountsFrame();
            }

        });
        commands.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CommandsFrame();
            }

        });
        settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new settingsFrame();
            }

        });

    }
}
