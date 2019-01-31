package main;

import chat.ChatTab;
import giveAway.GiveAwayTab;
import hotkeys.HotKeysTab;
import main.component.frame.mainFrame;
import main.component.jpanel.CommonPanel;
import main.component.jpanel.CommonTabbedPane;
import music.MusicTab;
import services.ConfigService;
import services.LanguageService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class appFrame extends mainFrame {
    private ConfigService   config = ConfigService.getInstance();

    private LanguageService trans  = LanguageService.getInstance();


    private ChatTab chatTab = new ChatTab();

    public appFrame() {
        String giveAwayIconPath = config.getProp("giveAwayIcon");
        String chatIconPath = config.getProp("chatIcon");
        String musicIconPath = config.getProp("musicIcon");
        String hotkeysIconPath = config.getProp("hotkeysIcon");
        BorderLayout     layout      = new BorderLayout();
        JMenuBar         menuBar     = new menuBar();
        CommonTabbedPane tabs        = new CommonTabbedPane();
        CommonPanel      giveAwayTab = new GiveAwayTab();

        giveAwayTab.setName("giveAwayTab");
        chatTab.setName("chatTab");
        chatTab.setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        chatTab.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));
        chatTab.setOpaque(true);

        HotKeysTab hotKeysTab = new HotKeysTab();

        MusicTab musicTab = new MusicTab();
        musicTab.setName("musicTab");

        setHalfSize();
        setAutoLocation();
        setLayout(layout);
        add(menuBar, BorderLayout.PAGE_START);
        ImageIcon giveAwayIcon = new ImageIcon(getClass().getResource(giveAwayIconPath));
        ImageIcon musicIcon    = new ImageIcon(getClass().getResource(musicIconPath));
        ImageIcon chatIcon     = new ImageIcon(getClass().getResource(chatIconPath));
        ImageIcon hotkeysIcon     = new ImageIcon(getClass().getResource(hotkeysIconPath));

        tabs.addTab(trans.getProp("chatTab.title"), chatIcon, chatTab);
        tabs.addTab(trans.getProp("hotkeys.label"), hotkeysIcon, hotKeysTab);

        //tabs.addTab(trans.getProp("giveAwayTab.title"), giveAwayIcon, giveAwayTab);
        //tabs.addTab(trans.getProp("musicTab.title"), musicIcon, musicTab);

        add(tabs);
        setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));

        setVisible(true);
        /*
         * Listeners
		 */
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Runtime.getRuntime().halt(0);
            }
        });

    }

    public ChatTab getChatTab(){
        return this.chatTab;
    }


}
