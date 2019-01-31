package chat;

import animations.LoadingAnimation;
import irc.StreamerBot;
import irc.ircBot;
import main.component.jpanel.CommonPanel;
import services.LanguageService;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import java.awt.*;

public class ChatTab extends CommonPanel {

    private EventPanel      eventPanel = new EventPanel();
    private ChatPanel       chatPanel  = new ChatPanel();
    private UserPanel       userPanel  = new UserPanel();
    private LanguageService trans      = LanguageService.getInstance();
    private StreamerBot streamerBot;
    private ircBot      ircBot;


    public ChatTab() {
        setName("chatTab");
        setLayout(new BorderLayout());

        // SPLITPANE
        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, eventPanel, chatPanel);
        sp.setOneTouchExpandable(true);
        sp.setContinuousLayout(true);
        sp.setBackground(new Color(Integer.decode(config.getProp("color.secondary"))));
        sp.setForeground(new Color(Integer.decode(config.getProp("color.text.secondary"))));
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setOpaque(true);

        JSplitPane sp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp, userPanel);
        sp2.setOneTouchExpandable(true);
        sp2.setContinuousLayout(true);
        sp2.setResizeWeight(.9d);
        sp2.setBorder(BorderFactory.createEmptyBorder());
        sp2.setBackground(new Color(Integer.decode(config.getProp("color.secondary"))));
        sp2.setForeground(new Color(Integer.decode(config.getProp("color.text.secondary"))));
        sp2.setOpaque(true);

        // DIVIDER OF SPLITPANE
        BasicSplitPaneDivider div1 = (BasicSplitPaneDivider) sp.getComponent(2);
        div1.setBorder(null);
        div1.setBackground(new Color(Integer.decode(config.getProp("color.secondary"))));
        div1.setForeground(new Color(Integer.decode(config.getProp("color.text.secondary"))));

        BasicSplitPaneDivider div2 = (BasicSplitPaneDivider) sp2.getComponent(2);
        div2.setBorder(null);
        div2.setBackground(new Color(Integer.decode(config.getProp("color.secondary"))));
        div2.setForeground(new Color(Integer.decode(config.getProp("color.text.secondary"))));

        add(sp2, BorderLayout.CENTER);
        setVisible(true);


        // Bots
        ircBot ircBot = new ircBot(chatPanel, userPanel);
        ircBot.start();
        this.ircBot = ircBot;


        StreamerBot streamerBot = new StreamerBot(chatPanel);
        streamerBot.start();
        this.streamerBot = streamerBot;

        config.startTask("twitch_streamer_connect", trans.getProp("chatTab.connection"), LoadingAnimation.LEVEL_INFO);
        config.startTask("twitch_bot_connect", trans.getProp("chatTab.connection"), LoadingAnimation.LEVEL_INFO);
    }

    public StreamerBot getStreamerBot() {
        return this.streamerBot;
    }


}
