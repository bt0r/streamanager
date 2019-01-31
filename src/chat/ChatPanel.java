package chat;

import com.google.common.collect.ImmutableSortedSet;
import database.User;
import irc.StreamerBot;
import main.component.button.CommonButton;
import main.component.jpanel.ScrollablePanel;
import main.component.jxlabel.CommonTextField;
import org.pircbotx.Channel;
import services.ConfigService;
import services.DatabaseService;
import services.LanguageService;
import services.LogService;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Logger;

public class ChatPanel extends JPanel {
    String LEVEL_ALL  = "all";
    String LEVEL_1    = "100";
    String LEVEL_2    = "250";
    String LEVEL_3    = "500";
    String LEVEL_4    = "1000";
    String LEVEL_5    = "5000";
    String LEVEL_6    = "10000";
    String LEVEL_7    = "50000";
    String LEVEL_8    = "100000";
    String LEVEL_9    = "500000";
    String LEVEL_MODS = "mods";
    String LEVEL_SUBS = "subs";

    private ConfigService   config  = ConfigService.getInstance();
    private DatabaseService db      = DatabaseService.getInstance();
    private LogService      chatLog = LogService.getInstance();
    private LanguageService trans   = LanguageService.getInstance();
    private Logger          logger  = Logger.getLogger("streaManager");

    private String trollIconPath     = config.getProp("trollIcon");
    private String moderatorIconPath = config.getProp("moderatorIcon");
    private String sendIconPath      = config.getProp("chatIcon");
    private String followIconPath    = config.getProp("followIcon");

    private ImageIcon trollIcon      = new ImageIcon(getClass().getResource(trollIconPath));
    private ImageIcon moderatorIcon  = new ImageIcon(getClass().getResource(moderatorIconPath));
    private ImageIcon followIcon     = new ImageIcon(getClass().getResource(followIconPath));
    private ImageIcon points100Icon  = new ImageIcon(getClass().getResource(config.getProp("points-100")));
    private ImageIcon points250Icon  = new ImageIcon(getClass().getResource(config.getProp("points-250")));
    private ImageIcon points500Icon  = new ImageIcon(getClass().getResource(config.getProp("points-500")));
    private ImageIcon points1000Icon = new ImageIcon(getClass().getResource(config.getProp("points-1000")));
    private ImageIcon points5000Icon = new ImageIcon(getClass().getResource(config.getProp("points-5000")));
    private ImageIcon points10KIcon  = new ImageIcon(getClass().getResource(config.getProp("points-10K")));
    private ImageIcon points50KIcon  = new ImageIcon(getClass().getResource(config.getProp("points-50K")));
    private ImageIcon points100KIcon = new ImageIcon(getClass().getResource(config.getProp("points-100K")));
    private ImageIcon points500KIcon = new ImageIcon(getClass().getResource(config.getProp("points-500K")));

    private ScrollablePanel chatbox     = new ScrollablePanel();
    private JScrollPane     scrollPanel = new JScrollPane(chatbox);
    private JSlider         slider      = new JSlider();

    public ChatPanel() {
        setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        setName("chatPanel");
        if (! config.getProp("bot.login").isEmpty() &&
                ! config.getProp("bot.password").isEmpty() &&
                ! config.getProp("streamer.login").isEmpty() &&
                ! config.getProp("streamer.password").isEmpty()) {
            BorderLayout ChatLayout = new BorderLayout();
            setLayout(ChatLayout);
            chatbox.setName("chatbox");
            chatbox.setLayout(new BoxLayout(chatbox, BoxLayout.Y_AXIS));
            chatbox.setVisible(true);
            chatbox.setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
            chatbox.setScrollableWidth(ScrollablePanel.ScrollableSizeHint.FIT);
            chatbox.setScrollableBlockIncrement(ScrollablePanel.VERTICAL, ScrollablePanel.IncrementType.PERCENT, 200);
            scrollPanel = new JScrollPane(chatbox);
            scrollPanel.setBorder(BorderFactory.createEmptyBorder());
            scrollPanel.getViewport().setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
            scrollPanel.setOpaque(false);

            final CommonTextField submitField  = new CommonTextField();
            CommonButton          submitButton = new CommonButton(trans.getProp("chatTab.submit"));
            ImageIcon             sendIcon     = new ImageIcon(getClass().getResource(sendIconPath));
            submitButton.setIcon(sendIcon);
            submitButton.setIcon(sendIcon);
            submitButton.setIcon(sendIcon);

            // Add one layout on bottom
            JPanel submitPanel  = new JPanel();
            JPanel commandPanel = new JPanel();
            commandPanel.setBackground(new Color(Integer.decode(config.getProp("color.primary"))));

            slider.setOrientation(SwingConstants.VERTICAL);
            slider.setMinimum(0);
            slider.setMaximum(11);
            slider.setPreferredSize(new Dimension(30, 300));
            slider.setToolTipText(trans.getProp("chatTab.slider"));
            slider.setMajorTickSpacing(1);
            slider.setMinorTickSpacing(1);
            slider.setValue(new Integer(config.getProp("chat.sliderValue")));
            slider.setSnapToTicks(true);
            slider.setValueIsAdjusting(true);
            slider.setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
            slider.setPaintLabels(true);

            Hashtable labelTable = new Hashtable();
            labelTable.put(new Integer(0), new JLabel(trollIcon));
            labelTable.put(new Integer(1), new JLabel(points100Icon));
            labelTable.put(new Integer(2), new JLabel(points250Icon));
            labelTable.put(new Integer(3), new JLabel(points500Icon));
            labelTable.put(new Integer(4), new JLabel(points1000Icon));
            labelTable.put(new Integer(5), new JLabel(points5000Icon));
            labelTable.put(new Integer(6), new JLabel(points10KIcon));
            labelTable.put(new Integer(7), new JLabel(points50KIcon));
            labelTable.put(new Integer(8), new JLabel(points100KIcon));
            labelTable.put(new Integer(9), new JLabel(points500KIcon));
            labelTable.put(new Integer(10), new JLabel(followIcon));
            labelTable.put(new Integer(11), new JLabel(moderatorIcon));
            slider.setLabelTable(labelTable);

            commandPanel.add(slider);
            submitPanel.setLayout(new BorderLayout());
            submitPanel.add(submitField, BorderLayout.CENTER);
            submitPanel.add(submitButton, BorderLayout.EAST);
            submitPanel.setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
            submitPanel.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));


            add(commandPanel, BorderLayout.EAST);
            add(scrollPanel, BorderLayout.CENTER);
            add(submitPanel, BorderLayout.SOUTH);


            /*
             * Listeners
            */
            submitButton.addMouseListener(new MouseListener() {

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (! submitField.getText().isEmpty()) {
                        sendMessage(submitField.getText());
                        submitField.setText(null);
                    }
                }
            });
            submitField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendMessage(submitField.getText());
                    submitField.setText(null);
                }
            });

            slider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSlider slider      = (JSlider) e.getSource();
                    int     sliderValue = slider.getValue();
                    config.setProp("chat.sliderValue", String.valueOf(sliderValue));

                    switch (sliderValue) {
                        case 11:
                            // Only mods
                            config.setProp("chat.neededPoints", LEVEL_MODS);
                            displayMessagePanels();
                            break;
                        case 10:
                            // Only mods  & Subs
                            config.setProp("chat.neededPoints", LEVEL_SUBS);
                            displayMessagePanels();
                            break;
                        case 9:
                            // Users with more than 500 000 points & mods  & Subs
                            config.setProp("chat.neededPoints", LEVEL_9);
                            displayMessagePanels();
                            break;
                        case 8:
                            // Users with more than 100 000 points & mods  & Subs
                            config.setProp("chat.neededPoints", LEVEL_8);
                            displayMessagePanels();
                            break;
                        case 7:
                            // Users with more than 50 000 points & mods  & Subs
                            config.setProp("chat.neededPoints", LEVEL_7);
                            displayMessagePanels();
                            break;
                        case 6:
                            // Users with more than 10 000 points & mods  & Subs
                            config.setProp("chat.neededPoints", LEVEL_6);
                            displayMessagePanels();
                            break;
                        case 5:
                            // Users with more than 5 000 points & mods  & Subs
                            config.setProp("chat.neededPoints", LEVEL_5);
                            displayMessagePanels();
                            break;
                        case 4:
                            // Users with more than 1 000 points & mods  & Subs
                            config.setProp("chat.neededPoints", LEVEL_4);
                            displayMessagePanels();
                            break;
                        case 3:
                            // Users with more than 500 points & mods  & Subs
                            config.setProp("chat.neededPoints", LEVEL_3);
                            displayMessagePanels();
                            break;
                        case 2:
                            // Users with more than 250 points & mods  & Subs
                            config.setProp("chat.neededPoints", LEVEL_2);
                            displayMessagePanels();
                            break;
                        case 1:
                            // Users with more than 100 points & mods  & Subs
                            config.setProp("chat.neededPoints", LEVEL_1);
                            displayMessagePanels();
                            break;
                        case 0:
                            // All
                            config.setProp("chat.neededPoints", LEVEL_ALL);
                            displayMessagePanels();
                            break;
                    }
                }
            });

            setVisible(true);

        } else {
            add(new ErrorPanel());
        }
    }

    /**
     * Show/Hide messagePanel if anti-troll system is active
     */
    private void displayMessagePanels() {
        Component[] messagePanels = getMessagesPanels();
        for (Component component : messagePanels) {
            ChatMessagePanel messagePanel = (ChatMessagePanel) component;
            displayMessagePanel(messagePanel);
        }
    }

    /**
     * Show/Hide messagePanel if anti-troll system is active
     *
     * @param messagePanel
     */
    private void displayMessagePanel(ChatMessagePanel messagePanel) {
        String level = config.getProp("chat.neededPoints");
        User   user  = messagePanel.getUser();

        if (level.equals(LEVEL_ALL)) {
            // All
            messagePanel.setVisible(true);
            messagePanel.setEnabled(true);
        } else if (level.equals(LEVEL_MODS)) {
            // mods
            if (user.isModerator()) {
                messagePanel.setVisible(true);
                messagePanel.setEnabled(true);
            } else {
                messagePanel.setVisible(false);
                messagePanel.setEnabled(false);
            }
        } else if (level.equals(LEVEL_SUBS)) {
            // mods & subs
            if (user.isModerator() || user.isGamewispSubscriber() || user.isSubscriber()) {
                messagePanel.setVisible(true);
                messagePanel.setEnabled(true);
            } else {
                messagePanel.setVisible(false);
                messagePanel.setEnabled(false);
            }
        } else {
            // Filter by points, if lower -> disable messagePanel else enable it
            if (messagePanel.getPoints() <= Integer.parseInt(level) && (! user.isModerator() && ! user.isGamewispSubscriber() && ! user.isSubscriber())) {
                messagePanel.setVisible(false);
                messagePanel.setEnabled(false);
            } else {
                messagePanel.setVisible(true);
                messagePanel.setEnabled(true);
            }
        }
        messagePanel.repaint();

    }


    public void sendMessage(String message) {
        ChatTab chatTab = (ChatTab) (getParent().getParent().getParent());

        StreamerBot streamerBot = chatTab.getStreamerBot();
        ImmutableSortedSet<Channel> channels        = streamerBot.getBot().getUserBot().getChannels();
        String                      streamerChannel = config.getProp("streamer.login");
        for (Channel channel : channels) {
            if (channel.getName().equals("#" + streamerChannel)) {
                try {
                    channel.send().message(message);
                } catch (Exception e) {
                    logger.severe("Can't send message to IRC chat");
                }
                break;
            }
        }
    }


    public void addMessage(String date, String username, String message, boolean isAction) {
        User             user         = db.findUser(username);
        ChatMessagePanel messagePanel = new ChatMessagePanel(date, user, message);

        try {
            int countComponents = chatbox.getComponents().length;
            if (countComponents > 0) {
                ChatMessagePanel previousChatMessage = (ChatMessagePanel) chatbox.getComponent(countComponents - 1);
                if (previousChatMessage.getMessage().toLowerCase().equals(message.toLowerCase())) {
                    // Remove last message panel and replace it by a message group panel
                    previousChatMessage.addOtherUser(username);
                    previousChatMessage.revalidate();
                } else {
                    addMessagePanel(messagePanel, isAction);
                }
            } else {
                addMessagePanel(messagePanel, isAction);
            }

            displayMessagePanel(messagePanel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addWhisper(String date, String username, String message) {
        User             user         = db.findUser(username);
        ChatMessagePanel messagePanel = new ChatMessagePanel(date, user, message);
        messagePanel.setWhisper(true);
        addMessagePanel(messagePanel, false);
    }

    private void addMessagePanel(ChatMessagePanel messagePanel, boolean isAction) {
        if (isAction) {
            messagePanel.setAction(true);
        }
        chatbox.add(messagePanel);
        scrollDown();

    }

    public Component[] getMessagesPanels() {
        return chatbox.getComponents();
    }

    public JPanel getChatbox() {
        return this.chatbox;
    }

    /**
     * Force to scroll down scrollbar
     */
    public void scrollDown() {
        JScrollBar verticalBar = scrollPanel.getVerticalScrollBar();
        AdjustmentListener downScroller = new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                Adjustable adjustable = e.getAdjustable();
                adjustable.setValue(adjustable.getMaximum());
                verticalBar.removeAdjustmentListener(this);
            }
        };
        verticalBar.addAdjustmentListener(downScroller);
        repaint();
        revalidate();
    }
}
