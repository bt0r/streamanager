package chat;

import Tools.Tools;
import animations.LoadingAnimation;
import database.User;
import main.component.frame.mainFrame;
import main.component.jpanel.CommonPanel;
import main.component.jxlabel.BadgeLabel;
import main.component.jxlabel.Label;
import org.json.JSONObject;
import services.ConfigService;
import services.DatabaseService;
import services.LanguageService;
import services.TwitchAPIService;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Created by btor on 07/03/2016.
 */
public class UserInformations extends mainFrame {
    private ConfigService   config = ConfigService.getInstance();
    private DatabaseService db     = DatabaseService.getInstance();
    private String username;
    private Color borderColor = new Color(0xaaaaaa);

    private Label     follow      = new Label();
    private Label     date        = new Label();
    private Label     followers   = new Label();
    private JTextArea description = new JTextArea();

    public UserInformations(String username) {
        Label profilePicture = new Label();
        profilePicture.setPreferredSize(new Dimension(150, 150));
        profilePicture.setVerticalAlignment(SwingUtilities.CENTER);
        profilePicture.setHorizontalAlignment(SwingUtilities.CENTER);
        profilePicture.setHorizontalTextPosition(SwingUtilities.CENTER);
        setUsername(username);
        LanguageService trans = LanguageService.getInstance();
        setTitle(trans.getProp("userInfo.title"));
        int screenHeight = 250;
        int screenWidth  = 600;
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setResizable(false);
        String smallIconPath = config.getProp("logoSmall");
        try {
            setIconImage(ImageIO.read(getClass().getResourceAsStream(smallIconPath)));
        } catch (IOException e1) {
            Logger logger = Logger.getLogger("streaManager");
            logger.warning("Can't load icon image from " + smallIconPath);
        }


        // SCREEN SIZE
        Dimension screenSize      = getToolkit().getScreenSize();
        Integer   screenMidHeight = (int) (screenSize.getHeight() / 2);
        Integer   screenMidWidth  = (int) (screenSize.getWidth() / 2);
        setLocation(screenMidWidth - (screenWidth / 2), screenMidHeight - (screenHeight / 2));

        // LAYOUT
        setLayout(new BorderLayout());

        // PANELS
        CommonPanel leftPanel = new CommonPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension((int) getPreferredSize().getWidth() / 3, (int) getPreferredSize().getHeight()));

        CommonPanel rightPanel = new CommonPanel();
        rightPanel.setLayout(new BorderLayout());
        Color backgroundColor = new Color(Integer.decode(config.getProp("color.primary")));
        rightPanel.setBackground(backgroundColor);

        /*
         * Left Panel
         */
        // Pseudo
        Label pseudo   = new Label();
        Font  boldFont = new Font(pseudo.getFont().getFontName(), Font.BOLD, pseudo.getFont().getSize());
        pseudo.setFont(boldFont);
        pseudo.setText(getUsername().toUpperCase());
        pseudo.setHorizontalAlignment(SwingConstants.CENTER);
        pseudo.setVerticalTextPosition(SwingConstants.CENTER);
        pseudo.setVerticalAlignment(SwingConstants.CENTER);
        // Badges
        CommonPanel badgesPanel = new CommonPanel();
        badgesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Points
        Label pointsLabel = new Label();

        CommonPanel leftBottomPanel = new CommonPanel();
        leftBottomPanel.setLayout(new BoxLayout(leftBottomPanel,BoxLayout.X_AXIS));
        leftBottomPanel.add(badgesPanel);
        leftBottomPanel.add(pointsLabel);
        // Add / Remove Points


        leftPanel.add(pseudo, BorderLayout.NORTH);
        leftPanel.add(profilePicture, BorderLayout.CENTER);
        leftPanel.add(leftBottomPanel, BorderLayout.SOUTH);


        /*
         * Right Panel
         */

        	/* TOP */
        CommonPanel topPanel = new CommonPanel();
        topPanel.setPreferredSize(new Dimension(rightPanel.getWidth(), 110));
        topPanel.setLayout(new BorderLayout());
        topPanel.setOpaque(false);

        // Date
        Border       lineBorder = new LineBorder(borderColor);
        TitledBorder dateTitle  = new TitledBorder(lineBorder, trans.getProp("userInfo.created"));
        dateTitle.setTitleColor(borderColor);
        date.setBorder(dateTitle);

        // Follow
        TitledBorder followTitle = new TitledBorder(lineBorder, trans.getProp("userInfo.follow"));
        followTitle.setTitleColor(borderColor);
        follow.setBorder(followTitle);


        // Followers
        TitledBorder followersTitle = new TitledBorder(lineBorder, trans.getProp("userInfo.followers"));
        followersTitle.setTitleColor(borderColor);
        followers.setBorder(followersTitle);

        topPanel.add(date, BorderLayout.NORTH);
        topPanel.add(follow, BorderLayout.CENTER);
        topPanel.add(followers, BorderLayout.SOUTH);


        rightPanel.add(topPanel, BorderLayout.NORTH);

	        /* BOTTOM */
        CommonPanel bottomPanel = new CommonPanel();
        bottomPanel.setLayout(new BorderLayout());

        TitledBorder descriptionTitle = new TitledBorder(lineBorder, trans.getProp("userInfo.description"));
        descriptionTitle.setTitleColor(borderColor);
        description.setBorder(descriptionTitle);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEditable(false);
        description.setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        description.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));

        bottomPanel.add(description, BorderLayout.CENTER);

        rightPanel.add(bottomPanel, BorderLayout.CENTER);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        setVisible(true);
        pack();


        /**
         * Load informations from twitch & DB
         */
        SwingUtilities.invokeLater(() -> {
            try {
                config.startTask("twitch.loading", trans.getProp("task.data.load"), LoadingAnimation.LEVEL_INFO);
                // GET INFO FROM TWITCH
                TwitchAPIService twitch         = TwitchAPIService.getInstance();
                JSONObject       informations   = twitch.getUserInfo(username);
                String           totalFollow    = twitch.getTotalFollow(username);
                String           totalFollowers = twitch.getTotalFollowers(username);
                if (informations.has("logo") && !informations.isNull("logo") && !informations.getString("logo").isEmpty()) {
                    String        profilePictureURL = informations.getString("logo").replace("300x300", "150x150");
                    BufferedImage myPicture         = ImageIO.read(new URL(profilePictureURL));
                    ImageIcon     image             = new ImageIcon(myPicture);
                    profilePicture.setIcon(image);
                }

                // GET INFO FROM DB
                User user = db.findUser(username);

                String createdDate = Tools.timestampToDateStr(Tools.dateToTimestamp(informations.getString("created_at")));
                date.setText(createdDate);
                follow.setText(totalFollow + " personnes suivies");
                followers.setText(totalFollowers + " followers");

                // Description
                String informationText = trans.getProp("userInfo.bio.null");
                if (informations.has("bio") && !informations.isNull("bio") && !informations.getString("bio").isEmpty()) {
                    informationText = informations.getString("bio");
                }
                description.setText(informationText);

                // Badges
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
                if (user.isSubscriber()) {
                    ImageIcon  subIcon  = new ImageIcon(getClass().getResource(config.getProp("subscriberBadge")));
                    BadgeLabel subLabel = new BadgeLabel(subIcon);
                    subLabel.setToolTipText(trans.getProp("badge.subscriber"));

                    badgesPanel.add(subLabel);
                }
                if (user.isGamewispSubscriber()) {
                    ImageIcon  gamewispIcon  = new ImageIcon(getClass().getResource(config.getProp("gamewispBadge")));
                    BadgeLabel gamewispLabel = new BadgeLabel(gamewispIcon);
                    gamewispLabel.setToolTipText(trans.getProp("badge.gamewisp"));

                    badgesPanel.add(gamewispLabel);
                }

                // Points
                pointsLabel.setText(user.getPoints()+" pts");


                config.endTask("twitch.loading", trans.getProp("task.end"), LoadingAnimation.LEVEL_SUCCESS);
            } catch (Exception e) {
                config.endTask("twitch.loading", trans.getProp("err.task.data"), LoadingAnimation.LEVEL_ERROR);
                logger.severe(e.getMessage());
            }

        });


    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
