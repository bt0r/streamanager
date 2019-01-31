package chat;

import com.google.common.collect.ImmutableSortedSet;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import database.User;
import irc.StreamerBot;
import org.jdesktop.swingx.JXPanel;
import org.pircbotx.Channel;
import services.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.logging.Logger;


public class ChatPanelOld extends JXPanel {
    private ConfigService    config    = ConfigService.getInstance();
    private TwitchAPIService twitchAPI = TwitchAPIService.getInstance();
    private DatabaseService  db        = DatabaseService.getInstance();
    private LogService       chatLog   = LogService.getInstance();
    private Logger           logger    = Logger.getLogger("streaManager");
    private LanguageService  trans     = LanguageService.getInstance();


    private StreamerBot streamerBot;
    private String sendIconPath      = config.getProp("chatIcon");
    private String sendingIconPath   = config.getProp("sendingIcon");
    private String trollIconPath     = config.getProp("trollIcon");
    private String moderatorIconPath = config.getProp("moderatorIcon");

    private JSlider     slider  = new JSlider();
    private ChatJXTable chatbox = new ChatJXTable();

    private ImageIcon trollIcon     = new ImageIcon(getClass().getResource(trollIconPath));
    private ImageIcon moderatorIcon = new ImageIcon(getClass().getResource(moderatorIconPath));

    public ChatPanelOld() {
        /*
         * Vars
		 */

		/*
         * Layout
		 */
        if (!config.getProp("bot.login").isEmpty() &&
                !config.getProp("bot.password").isEmpty() &&
                !config.getProp("streamer.login").isEmpty() &&
                !config.getProp("streamer.password").isEmpty()) {
            BorderLayout ChatLayout = new BorderLayout();
            setLayout(ChatLayout);
            JScrollPane scrollPanel = new JScrollPane();

            scrollPanel.getViewport().add(chatbox);
            scrollPanel.setBorder(BorderFactory.createEmptyBorder());
            scrollPanel.setVisible(true);
            scrollPanel.setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
            final JTextField submitField  = new JTextField();
            JButton          submitButton = new JButton(trans.getProp("chatTab.submit"));


            ImageIcon sendIcon = new ImageIcon(getClass().getResource(sendIconPath));
            submitButton.setIcon(sendIcon);
            submitButton.setIcon(sendIcon);
            submitButton.setIcon(sendIcon);


            add(scrollPanel, BorderLayout.CENTER);
            // Add one layout on bottom
            JPanel submitPanel  = new JPanel();
            JPanel commandPanel = new JPanel();
            commandPanel.setBackground(new Color(Integer.decode(config.getProp("color.secondary"))));

            slider.setOrientation(SwingConstants.VERTICAL);
            slider.setMinimum(0);
            slider.setMaximum(10);
            slider.setPreferredSize(new Dimension(30, 300));
            slider.setToolTipText(trans.getProp("chatTab.slider"));
            slider.setMajorTickSpacing(1);
            slider.setMinorTickSpacing(1);
            slider.setValue(new Integer(config.getProp("app.chat.neededPoints")));
            slider.setSnapToTicks(true);
            slider.setValueIsAdjusting(true);
            slider.setBackground(new Color(Integer.decode(config.getProp("color.secondary"))));
            slider.setPaintLabels(true);

            Hashtable labelTable = new Hashtable();
            labelTable.put(new Integer(0), new JLabel(trollIcon));
            labelTable.put(new Integer(10), new JLabel(moderatorIcon));
            slider.setLabelTable(labelTable);

            commandPanel.add(slider);
            submitPanel.setLayout(new BorderLayout());
            submitPanel.add(submitField, BorderLayout.CENTER);
            submitPanel.add(submitButton, BorderLayout.EAST);

            add(submitPanel, BorderLayout.SOUTH);
            add(commandPanel, BorderLayout.EAST);


            /*ircBot ircBot = new ircBot(chatbox);
            ircBot.start();


            StreamerBot streamerBot = new StreamerBot(chatbox);
            streamerBot.start();
            this.streamerBot = streamerBot;
            */

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
                    if (!submitField.getText().isEmpty()) {
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
            slider.addMouseListener(new MouseListener() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    filterRow();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    filterRow();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    filterRow();
                }
            });
        } else {
            add(new ErrorPanel());
        }
    }

    public void filterRow() {
        Dao<User, ?> userDAO;
        Long         max        = new Long(0);
        Integer      min        = 0;
        String       sql        = "SELECT MAX(points) as max FROM user";
        Long         limitValue = new Long(0);

        try {
            userDAO = DaoManager.createDao(db.getConnectionSource(), User.class);
            String[] result = userDAO.queryRaw(sql).getResults().get(0);
            if (result[0] != null && !result[0].isEmpty()) {
                max = new Long(result[0]);
                slider.setMajorTickSpacing(10);
                slider.setMinorTickSpacing(10);
                slider.setMaximum(100);
                Hashtable labelTable = new Hashtable();
                labelTable.put(0, new JLabel(trollIcon));
                labelTable.put(100, new JLabel(moderatorIcon));
                slider.setLabelTable(labelTable);
                logger.info("Setting needed point to " + slider.getValue());
                config.setProp("app.chat.neededPoints", Integer.toString(slider.getValue()));
                //limitValue = );
                //System.out.println(max +"--"+slider.getValue()+"--"+(new Float(slider.getValue())/100));
                limitValue = new Long(Math.round(max * (new Float(slider.getValue()) / 100)));

            } else {
                slider.setValue(0);
            }
        } catch (SQLException e) {
            logger.warning("Can't find max and min points values, error :" + e.getMessage());
        }
        final Long endLimit = limitValue;
        RowFilter<Object, Object> filter = new RowFilter<Object, Object>() {
            @Override
            public boolean include(Entry<? extends Object, ? extends Object> entry) {
                Integer points = (Integer) entry.getValue(1);
                return points >= endLimit;
            }
        };
        chatbox.setRowFilter(filter);
    }

    public void sendMessage(String message) {
        ImmutableSortedSet<Channel> channels = streamerBot.getBot().getUserBot().getChannels();
        for (Channel channel : channels) {
            if (channel.getName().equals("#" + config.getProp("streamer.login"))) {
                try {
                    channel.send().message(message);
                } catch (Exception e) {
                    logger.severe("Can't send message to IRC chat");
                }

            }

        }


    }

}
