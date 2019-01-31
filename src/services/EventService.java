package services;

import listeners.UserMouseListener;
import main.component.jxlabel.UserLabel;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;


public class EventService {

    private static JPanel eventPanel;
    private Logger        logger = Logger.getLogger("streaManager");
    private ConfigService config = ConfigService.getInstance();
    private LogService streamFile = LogService.getInstance();



    /*
     * Constructeur privé
     */
    public EventService(JPanel eventPanel) {
        this.eventPanel = eventPanel;
    }

    /*
     * Instance unique
     */
    private static EventService EventService = new EventService(eventPanel);

    private static class EventServiceHolder {
        private static JPanel eventPanel;
        private final static EventService EventService = new EventService(eventPanel);
    }

    /*
     * Points d'accès pour l'instance unique du singleton
     */
    public static EventService getInstance() {
        if (EventService == null) {
            EventService = new EventService(eventPanel);
        }
        return EventService.EventService;
    }

    public void addFollow(String username) {
        addEvent("follow", username,null);
        streamFile.writeLastFollower(username);
    }

    public void addConnect(String username) {
        addEvent("connect", username,null);
    }

    public void addHost(String username, String message) {
        addEvent("host", username,message);
    }

    public void addGamewispSub(String username, String message) {

        addEvent("sub_gamewisp", username,message);
    }

    public void addGamewispReSub(String username, String message) {

        addEvent("resub_gamewisp", username,message);
    }

    public void addGamewispSubAnniversary(String username, String message) {

        addEvent("resub_gamewisp", username,message);
    }

    public void addDisconnect(String username) {
        addEvent("disconnect", username,null);
    }

    /*
     * ADD GENERIC EVENT
     */
    public void addEvent(String type, String username, String message) {
        // ADD EVENT IN JLABEL
        try {
            ImageIcon icon = null;

            switch (type) {
                case "follow":
                    icon = new ImageIcon(getClass().getResource(config.getProp("followIcon")));
                    break;
                case "unfollow":
                    icon = new ImageIcon(getClass().getResource(config.getProp("unfollowIcon")));
                    break;
                case "connect":
                    icon = new ImageIcon(getClass().getResource(config.getProp("connectIcon")));
                    break;
                case "disconnect":
                    icon = new ImageIcon(getClass().getResource(config.getProp("disconnectIcon")));
                    break;
                case "host":
                    icon = new ImageIcon(getClass().getResource(config.getProp("hostIcon")));
                    username = message.split("\\s")[0];
                    streamFile.writeLastHost(username);
                break;
                case "sub_gamewisp":
                    icon = new ImageIcon(getClass().getResource(config.getProp("gamewispBadge")));
                break;
                case "resub_gamewisp":
                    icon = new ImageIcon(getClass().getResource(config.getProp("gamewispBadge")));
                break;
            }

            UserLabel eventLabel = new UserLabel(icon);
            if(username != null && !username.isEmpty() && !username.equals("jtv")){
                // There is a twitch username
                eventLabel.setTwitchUsername(username);
                eventLabel.addMouseListener(new UserMouseListener());
            }
            if(message == null || message.isEmpty()){
                // No message but we have a username
                eventLabel.setText(username);
            }else{
                // Only message
                eventLabel.setText(message);
            }

            eventLabel.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));

            eventPanel.add(eventLabel, 0);
            eventPanel.revalidate();
            eventPanel.repaint();
            //sound.play("connect",true);

        } catch (Exception e) {
            logger.warning("Can't add event on GUI.");
        }


    }

}