package commands;


import main.component.frame.mainFrame;
import main.component.jpanel.CommonTabbedPane;
import services.ConfigService;
import services.DatabaseService;
import services.LanguageService;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
 * Created by btor on 26/02/2016.
 */
public class CommandsFrame extends mainFrame {
    private ConfigService   config  = ConfigService.getInstance();
    private LanguageService trans   = LanguageService.getInstance();
    private DatabaseService db      = DatabaseService.getInstance();
    private ImageIcon       addIcon = new ImageIcon(getClass().getResource(config.getProp("addIcon")));
    private Logger          logger  = Logger.getLogger("streaManager");


    public CommandsFrame() {
        // FRAME SETTINGS
        setTitle(trans.getProp("menu.commands"));
        setSize(new Dimension(990, 500));
        setPreferredSize(new Dimension(990, 500));
        setAutoLocation();
        setResizable(false);
        getContentPane().setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        getContentPane().setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));

        ImageIcon commandIcon = new ImageIcon(getClass().getResource(config.getProp("commandsIcon")));
        ImageIcon customIcon  = new ImageIcon(getClass().getResource(config.getProp("customCommands")));


        CommonTabbedPane tabs = new CommonTabbedPane();
        // TABS
        CustomPanel        customPanel        = new CustomPanel();
        CommandsStatePanel commandsStatePanel = new CommandsStatePanel();

        tabs.addTab(trans.getProp("command.custom.labels"), customIcon, customPanel);
        tabs.addTab(trans.getProp("command.manage"), commandIcon, commandsStatePanel);

        setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));

        add(tabs);

        setVisible(true);

    }
}
