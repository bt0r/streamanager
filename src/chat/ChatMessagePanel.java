package chat;

import database.User;
import listeners.UserMouseListener;
import main.component.jpanel.CommonPanel;
import main.component.jxlabel.BadgeLabel;
import main.component.jxlabel.UserLabel;
import org.jdesktop.swingx.JXEditorPane;
import org.jdesktop.swingx.JXPanel;
import services.ConfigService;
import services.LanguageService;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;


/**
 * Created by btor on 29/08/2016.
 */
public class ChatMessagePanel extends JXPanel {
    private String    date;
    private User      user;
    private ArrayList badges;
    private long      points;
    private String    message;
    private JLabel          dateLabel       = new JLabel();
    private UserLabel       userLabel       = new UserLabel();
    private JPanel          otherUsersPanel = new CommonPanel();
    private JLabel          otherUsersCount = new JLabel();
    private JXEditorPane    messagePanel    = new JXEditorPane("text/html", "");
    private JPanel          badgesPanel     = new JPanel();
    private JPanel          header          = new JPanel();
    private ConfigService   config          = ConfigService.getInstance();
    private LanguageService trans           = LanguageService.getInstance();


    public ChatMessagePanel(String date, User user, String message) {
        Border LRBorder  = BorderFactory.createEmptyBorder(0, 10, 0, 10);
        Border ALLBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        this.user = user;
        this.message = message;
        setLayout(new BorderLayout());
        setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));
        setBorder(ALLBorder);
        setName("ChatMessagePanel");
        setPoints(user.getPoints());

        // HEADER
        header.setLayout(new BoxLayout(header, BoxLayout.X_AXIS));
        header.setBackground(new Color(Integer.decode(config.getProp("color.table.secondary"))));

        dateLabel.setText(date);
        dateLabel.setSize(new Dimension(100, 20));
        dateLabel.setBorder(LRBorder);
        dateLabel.setName("date");
        dateLabel.setForeground(new Color(Integer.decode(config.getProp("color.table.text"))));

        badgesPanel.setLayout(new BoxLayout(badgesPanel, BoxLayout.X_AXIS));
        badgesPanel.setSize(new Dimension(200, 20));
        badgesPanel.setName("badges");
        badgesPanel.setBackground(new Color(Integer.decode(config.getProp("color.table.secondary"))));


        if (user.getUsername().equals(config.getProp("streamer.login"))) {
            ImageIcon  broadcasterIcon  = new ImageIcon(getClass().getResource(config.getProp("broadcasterBadge")));
            BadgeLabel broadcasterLabel = new BadgeLabel(broadcasterIcon);
            broadcasterLabel.setToolTipText(trans.getProp("badge.broadcaster"));
            badgesPanel.add(broadcasterLabel);
        } else {
            if (user.isModerator()) {
                ImageIcon  moderatorIcon  = new ImageIcon(getClass().getResource(config.getProp("moderatorBadge")));
                BadgeLabel moderatorLabel = new BadgeLabel(moderatorIcon);
                moderatorLabel.setToolTipText(trans.getProp("badge.moderator"));
                badgesPanel.add(moderatorLabel);
            }
        }
        if (user.isSubscriber()) {
            ImageIcon  subIcon  = new ImageIcon(getClass().getResource(config.getProp("subscriberBadge")));
            BadgeLabel subLabel = new BadgeLabel(subIcon);
            subLabel.setToolTipText(trans.getProp("badge.subscriber"));

            badgesPanel.add(subLabel);
        }
        if (user.isPremium()) {
            ImageIcon  premiumIcon  = new ImageIcon(getClass().getResource(config.getProp("premiumBadge")));
            BadgeLabel premiumLabel = new BadgeLabel(premiumIcon);
            premiumLabel.setToolTipText(trans.getProp("badge.premium"));
            badgesPanel.add(premiumLabel);
        }
        if (user.isTurbo()) {
            ImageIcon  turboIcon  = new ImageIcon(getClass().getResource(config.getProp("turboBadge")));
            BadgeLabel turboLabel = new BadgeLabel(turboIcon);
            turboLabel.setToolTipText(trans.getProp("badge.broadcaster"));

            badgesPanel.add(turboLabel);
        }
        if (user.isGamewispSubscriber()) {
            ImageIcon  gamewispIcon  = new ImageIcon(getClass().getResource(config.getProp("gamewispBadge")));
            BadgeLabel gamewispLabel = new BadgeLabel(gamewispIcon);
            gamewispLabel.setToolTipText(trans.getProp("badge.gamewisp"));

            badgesPanel.add(gamewispLabel);
        }


        userLabel.setName("username");
        userLabel.setText(user.getUsername());
        userLabel.setTwitchUsername(user.getUsername());
        userLabel.setForeground(Tools.Tools.lighterColor(new Color(user.getHexColor())));
        userLabel.setBackground(new Color(Integer.decode(config.getProp("color.table.secondary"))));
        userLabel.setFont(new Font(userLabel.getFont().getName(), Font.BOLD, userLabel.getFont().getSize()));
        userLabel.setSize(new Dimension(150, 20));
        userLabel.setBorder(LRBorder);
        userLabel.addMouseListener(new UserMouseListener());

        // Other users
        ImageIcon heartIcon = new ImageIcon(getClass().getResource(config.getProp("heartIcon")));
        otherUsersCount.setIcon(heartIcon);
        otherUsersCount.setForeground(new Color(0x038F0A));
        otherUsersCount.setFont(new Font(otherUsersCount.getFont().getName(), Font.BOLD, userLabel.getFont().getSize()));
        otherUsersCount.setVisible(false);

        otherUsersPanel.setName("otherUsers");
        otherUsersPanel.setBorder(LRBorder);
        otherUsersPanel.setVisible(true);
        otherUsersPanel.setBackground(new Color(Integer.decode(config.getProp("color.table.secondary"))));
        otherUsersPanel.setLayout(new BoxLayout(otherUsersPanel, BoxLayout.X_AXIS));

        header.add(dateLabel);
        header.add(badgesPanel);
        header.add(userLabel);
        header.add(otherUsersCount);
        header.add(otherUsersPanel);

        // MESSAGE
        //messageLabel.setTextAlignment(JXLabel.TextAlignment.LEFT);

        messagePanel.setOpaque(true);
        messagePanel.setBorder(ALLBorder);
        messagePanel.setBackground(new Color(Integer.decode(config.getProp("color.table.primary"))));
        messagePanel.setForeground(new Color(Integer.decode(config.getProp("color.table.text"))));

        Border messageBorder = BorderFactory.createLineBorder(new Color(Integer.decode(config.getProp("color.table.secondary"))));
        messagePanel.setBorder(BorderFactory.createCompoundBorder(messageBorder, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        messagePanel.setText("<html>" + message + "</html>");

        messagePanel.setEditable(false);

        // Add header panel and message label
        add(header, BorderLayout.NORTH);
        add(messagePanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public void setAction(boolean isAction) {
        if (isAction) {
            messagePanel.setForeground(new Color(user.getHexColor()));
            messagePanel.setFont(new Font(userLabel.getFont().getName(), Font.BOLD, userLabel.getFont().getSize()));
            messagePanel.setVisible(true);
        }
    }

    public void setWhisper(boolean isWhisper) {
        if (isWhisper) {
            ImageIcon   whisperIcon    = new ImageIcon(getClass().getResource(config.getProp("whisperBadge")));
            JLabel      whisperLabel   = new JLabel(whisperIcon);
            Component[] headComponents = header.getComponents();
            for (int i = 0; i < headComponents.length; i++) {
                if (headComponents[i].getName().equals("badges")) {
                    whisperLabel.setToolTipText(trans.getProp("badge.whisper"));
                    badgesPanel.add(whisperLabel);
                }
                badgesPanel.setBackground(new Color(Integer.decode(config.getProp("color.table.whisper"))));
            }

            header.setForeground(new Color(Integer.decode(config.getProp("color.table.text"))));
            header.setBackground(new Color(Integer.decode(config.getProp("color.table.whisper"))));
            Border blueBorder = BorderFactory.createLineBorder(new Color(Integer.decode(config.getProp("color.table.whisper"))));
            messagePanel.setBorder(BorderFactory.createCompoundBorder(blueBorder, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            messagePanel.setFont(new Font(userLabel.getFont().getName(), Font.BOLD, userLabel.getFont().getSize()));
            messagePanel.setVisible(true);
        }
    }

    public void forceLineWrap() {
        Component[] messagesLabels    = messagePanel.getComponents();
        long        totalLabelsWidth  = 0;
        double      result            = 1;
        int         resultCeil        = 0;
        int         lineHeight        = 45;
        int         headerHeight      = header.getHeight();
        int         totalBordersWitdh = 0;

        for (Component message : messagesLabels) {
            JLabel messageLabel = (JLabel) message;
            totalLabelsWidth += messageLabel.getWidth();
            totalBordersWitdh += 10;


        }
        Dimension messageSize = this.getPreferredSize();


        if (totalLabelsWidth > (messageSize.getWidth() - 200)) {
            result = (totalLabelsWidth / (messageSize.getWidth() - 200));
            resultCeil = (int) Math.ceil(result);

            double height = lineHeight * resultCeil;
            messageSize.height = (int) height + headerHeight;


            //setPreferredSize(messageSize);


        } else {
            Dimension size = getPreferredSize();
            size.height = lineHeight + headerHeight;
            setPreferredSize(size);
        }

    }

    /**
     * Return the chat message
     *
     * @return String
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Return points of user
     *
     * @return
     */
    public long getPoints() {
        return this.points;
    }

    /**
     * Set points for current message panel
     *
     * @param points
     *
     * @return
     */
    public ChatMessagePanel setPoints(long points) {
        this.points = points;
        return this;
    }

    /**
     * Return user
     *
     * @return
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Return other users panel
     *
     * @return JPanel
     */
    public JPanel getOtherUsers() {
        return this.otherUsersPanel;
    }

    /**
     * Return other users count label
     *
     * @return
     */
    private JLabel getOtherUsersCount() {
        return this.otherUsersCount;
    }

    /**
     * Add other user in group message
     *
     * @param username
     *
     * @return
     */
    public ChatMessagePanel addOtherUser(String username) {
        JPanel      otherUsersPanel  = getOtherUsers();
        boolean     createUser       = true;
        Component[] otherUsersLabels = otherUsersPanel.getComponents();
        if (otherUsersLabels.length > 0) {
            // Check if username already exists
            for (Component component : otherUsersPanel.getComponents()) {
                UserLabel userLabel = (UserLabel) component;
                userLabel.setBackground(new Color(Integer.decode(config.getProp("color.table.secondary"))));
                if (userLabel.getTwitchUsername().toLowerCase().equals(username.toLowerCase())) {
                    createUser = false;
                    break;
                }
            }
        }
        if (createUser && !getUser().getUsername().equals(username)) {
            if (otherUsersLabels.length < 5) {
                // Enough space to add it
                UserLabel otherUser = new UserLabel(username);
                otherUser.setTwitchUsername(username);
                otherUser.addMouseListener(new UserMouseListener());
                otherUser.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));
                otherUser.setBackground(new Color(Integer.decode(config.getProp("color.table.secondary"))));
                otherUser.setFont(new Font(userLabel.getFont().getName(), Font.BOLD, userLabel.getFont().getSize()));
                otherUser.setSize(new Dimension(150, 20));
                otherUser.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

                otherUsersPanel.add(otherUser);
            } else if (otherUsersLabels.length == 5) {
                // Too many user, just count it
                JLabel moreLabel = new JLabel("...");
                otherUsersPanel.add(moreLabel);
            }
            int otherUsersCountValue = otherUsersCount.getText() == null || otherUsersCount.getText().isEmpty() ? 1 : Integer.parseInt(otherUsersCount.getText()) + 1;
            otherUsersCount.setText(String.valueOf(otherUsersCountValue));
            otherUsersCount.revalidate();
            otherUsersCount.setVisible(true);
        }

        return this;
    }
}
