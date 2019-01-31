package chat;

import database.User;
import listeners.UserMouseListener;
import main.component.jpanel.CommonPanel;
import main.component.jxlabel.UserLabel;
import services.ConfigService;
import services.DatabaseService;
import services.LanguageService;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;


public class UserPanel extends JPanel {
    ConfigService   config = ConfigService.getInstance();
    LanguageService trans  = LanguageService.getInstance();
    DatabaseService db     = DatabaseService.getInstance();
    private String usersIconPath = config.getProp("usersIcon");

    private CommonPanel moderatorsPanel  = new CommonPanel();
    private CommonPanel subscribersPanel = new CommonPanel();
    private CommonPanel usersPanel       = new CommonPanel();
    private CommonPanel userList         = new CommonPanel();

    public UserPanel() {
		/*
         * Layout
		 */
        setMinimumSize(new Dimension(150, 0));
        setSize(new Dimension(200, 0));
        setBorder(BorderFactory.createEmptyBorder());
        setName("userPanel");
        setLayout(new BorderLayout());
        ImageIcon usersIcon = new ImageIcon(getClass().getResource(usersIconPath));
        JLabel    userTitle = new JLabel(trans.getProp("chatTab.userTitle"));
        userTitle.setIcon(usersIcon);

        // USERS LIST
        userList.setLayout(new BoxLayout(userList, BoxLayout.Y_AXIS));
        userList.setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        userList.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));

        // UserPanels
        moderatorsPanel.setLayout(new BoxLayout(moderatorsPanel, BoxLayout.Y_AXIS));
        subscribersPanel.setLayout(new BoxLayout(subscribersPanel, BoxLayout.Y_AXIS));
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));

        // User List Panel
        JScrollPane sc = new JScrollPane();
        sc.getViewport().add(userList);
        sc.setBorder(BorderFactory.createEmptyBorder());
        sc.setVisible(true);

        // TITLE
        userTitle.setHorizontalAlignment(SwingConstants.CENTER);
        userTitle.setVerticalAlignment(SwingConstants.CENTER);
        userTitle.setBackground(new Color(Integer.decode(config.getProp("color.secondary"))));
        userTitle.setForeground(new Color(Integer.decode(config.getProp("color.text.secondary"))));
        userTitle.setOpaque(true);


        // USER STATS
        JPanel userStatPanel = new JPanel();
        userStatPanel.setLayout(new GridLayout(0, 3));
        userStatPanel.setPreferredSize(new Dimension(150, 20));
        userStatPanel.setBackground(new Color(Integer.decode(config.getProp("color.secondary"))));

        ImageIcon moderatorIcon     = new ImageIcon(getClass().getResource(config.getProp("moderatorIcon")));
        ImageIcon viewerIcon        = new ImageIcon(getClass().getResource(config.getProp("chattersIcon")));
        ImageIcon allViewersIcon    = new ImageIcon(getClass().getResource(config.getProp("usersIcon")));
        JLabel    totalViewersLabel = new JLabel();
        totalViewersLabel.setName("totalViewers");
        totalViewersLabel.setToolTipText(trans.getProp("chatTab.totalViewers"));
        totalViewersLabel.setIcon(allViewersIcon);
        totalViewersLabel.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));

        JLabel totalModeratorsLabel = new JLabel();
        totalModeratorsLabel.setName("totalModerators");
        totalModeratorsLabel.setToolTipText(trans.getProp("chatTab.totalModerators"));
        totalModeratorsLabel.setIcon(moderatorIcon);
        totalModeratorsLabel.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));

        JLabel totalNormalViewersLabel = new JLabel();
        totalNormalViewersLabel.setName("totalNormalViewers");
        totalNormalViewersLabel.setToolTipText(trans.getProp("chatTab.totalNormalViewers"));
        totalNormalViewersLabel.setIcon(viewerIcon);
        totalNormalViewersLabel.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));

        userStatPanel.add(totalViewersLabel);
        userStatPanel.add(totalModeratorsLabel);
        userStatPanel.add(totalNormalViewersLabel);

        userList.add(moderatorsPanel);
        userList.add(subscribersPanel);
        userList.add(usersPanel);

        /* CRONTASK
        CronTask cronTask = new CronTask(userList, userStatPanel);
        cronTask.setInterval(10000);
        cronTask.start();
        */
        add(userTitle, BorderLayout.NORTH);
        add(sc, BorderLayout.CENTER);
        add(userStatPanel, BorderLayout.SOUTH);
        setVisible(true);

    }

    /**
     * Entry point to add an user
     *
     * @param username
     */
    public void addUser(String username) {
        // Check user in DB
        User user = db.findUser(username);
        // Create label
        UserLabel userLabel = new UserLabel();
        userLabel.setText(username);
        userLabel.setTwitchUsername(username);
        userLabel.addMouseListener(new UserMouseListener());

        userLabel.setName(username);

        if (user.isModerator()) {
            ImageIcon moderatorIcon = new ImageIcon(getClass().getResource(config.getProp("moderatorIcon")));
            userLabel.setIcon(moderatorIcon);

            // Try to find moderator, if he doesn't exists, add it in panel
            Component[] moderatorsComponents = this.moderatorsPanel.getComponents();
            boolean     found                = false;

            for (Component moderatorComponent : moderatorsComponents) {
                if (moderatorComponent.getName().equals(userLabel.getTwitchUsername())) {
                    found = true;
                    break;
                }
            }
            if (! found) {
                this.moderatorsPanel.add(userLabel);
            }
        } else if (user.isSubscriber()) {
            ImageIcon subIcon = new ImageIcon(getClass().getResource(config.getProp("subscriberBadge")));
            userLabel.setIcon(subIcon);

            // Try to find subscriber, if he doesn't exists, add it in panel
            Component[] subscribersComponents = this.subscribersPanel.getComponents();
            boolean     found                 = false;
            for (Component subscriberComponent : subscribersComponents) {
                if (subscriberComponent.getName().equals(userLabel.getTwitchUsername())) {
                    found = true;
                    break;
                }
            }
            if (! found) {
                this.subscribersPanel.add(userLabel);
            }

            this.subscribersPanel.add(userLabel);
        } else {
            ImageIcon userIcon = new ImageIcon(getClass().getResource(config.getProp("userIcon")));
            userLabel.setIcon(userIcon);

            // Try to find user, if he does'nt exists, add it in panel
            Component[] usersComponents = this.usersPanel.getComponents();
            boolean     found           = false;
            for (Component usersPanel : usersComponents) {
                if (usersPanel.getName().equals(userLabel.getTwitchUsername())) {
                    found = true;
                    break;
                }
            }
            if (! found) {
                this.usersPanel.add(userLabel);
            }

            this.usersPanel.add(userLabel);
        }
        userList.revalidate();
        userList.repaint();

    }

    public void removeUser(String username) {
        boolean found = false;
        // Try to find if it's a moderator
        Component[] moderatorsComponents = this.moderatorsPanel.getComponents();
        for (Component moderatorComponent : moderatorsComponents) {
            if (moderatorComponent.getName().equals(username)) {
                moderatorsPanel.remove(moderatorComponent);
                found = true;
                break;
            }
        }
        // Try to find if it's a subscriber
        if(!found){
            Component[] subscribersComponents = this.subscribersPanel.getComponents();
            for (Component subscriberComponent : subscribersComponents) {
                if (subscriberComponent.getName().equals(username)) {
                    subscribersPanel.remove(subscriberComponent);
                    found = true;
                    break;
                }
            }
        }

        // Try to find if it's a normal viewer
        if(!found){
            Component[] usersComponents = this.usersPanel.getComponents();
            for (Component userComponent : usersComponents) {
                if (userComponent.getName().equals(username)) {
                    usersPanel.remove(userComponent);
                    break;
                }
            }
        }

        userList.revalidate();
        userList.repaint();
    }

    private void reorder() {
        // Reorder usersPanel

        // Reorder moderatorsPanel
    }

}
