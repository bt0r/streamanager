package chat;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import database.User;
import listeners.UserMouseListener;
import main.component.jxlabel.UserLabel;
import org.json.JSONArray;
import org.json.JSONObject;
import services.*;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;


public class CronTask extends Thread {
    private ConfigService      config     = ConfigService.getInstance();
    private TwitchAPIService   twitch     = TwitchAPIService.getInstance();
    private DatabaseService    db         = DatabaseService.getInstance();
    private Logger             logger     = Logger.getLogger("streaManager");
    private LogService         streamFile = LogService.getInstance();


    int interval = 60000;
    JPanel userList, userCount;

    public CronTask(JPanel userList, JPanel userCount) {
        this.userList = userList;
        this.userCount = userCount;
    }

    public void run() {
        final Logger logger = Logger.getLogger("streaManager");
        logger.fine("Starting cron task...");


        long  startTime = 0;
        Timer timer     = new Timer();
        TimerTask tache = new TimerTask() {
            @Override
            public void run(){
                logger.info("Checking twitch API !");
                try {
                    ImageIcon moderatorIcon = new ImageIcon(getClass().getResource(config.getProp("moderatorIcon")));
                    ImageIcon viewerIcon    = new ImageIcon(getClass().getResource(config.getProp("userIcon")));

                    JSONObject          result       = twitch.getChatters(config.getProp("streamer.login"));
                    JSONObject          chatters     = result.getJSONObject("chatters");
                    JSONArray           moderators   = chatters.getJSONArray("moderators");
                    JSONArray           viewers      = chatters.getJSONArray("viewers");
                    Map<String, String> streamInfo   = twitch.getStreamInfo(config.getProp("streamer.login"));
                    String              totalViewers = streamInfo.get("viewers");


                    if (streamInfo.get("status").equals("online")) {
                        totalViewers = streamInfo.get("viewers");
                        streamFile.writeCountTotal(new Integer(totalViewers));
                    }


                    Component[] jlComponents = userCount.getComponents();
                    for (Component component : jlComponents) {
                        JLabel jlabel = (JLabel) component;
                        if (jlabel.getName().equals("totalViewers")) {
                            jlabel.setText(totalViewers);

                        } else if (jlabel.getName().equals("totalModerators")) {
                            jlabel.setText(Integer.toString(moderators.length()));
                            streamFile.writeCountModerators(moderators.length());
                        } else if (jlabel.getName().equals("totalNormalViewers")) {
                            jlabel.setText(Integer.toString(viewers.length()));
                            streamFile.writeCountNormalViewers(viewers.length());
                        }
                    }

                    logger.info(viewers.length() + " viewers found");
                    userList.removeAll();

                    for (int m = 0; m < moderators.length(); m++) {
                        // ADD MODERATOR JLABEL
                        String moderator      = moderators.getString(m);
                        UserLabel moderatorLabel = new UserLabel(moderatorIcon);
                        moderatorLabel.setText(moderator);
                        moderatorLabel.setTwitchUsername(moderator);
                        moderatorLabel.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));
                        moderatorLabel.addMouseListener(new UserMouseListener());
                        userList.add(moderatorLabel);

                        // SET USER AS MODERATOR

                        try {
                            User user = db.findUser(moderator);
                            user.setModerator(true);
                            Dao<User, ?> userDAO = DaoManager.createDao(db.getConnectionSource(), User.class);
                            userDAO.update(user);

                        } catch (Exception e) {

                        }
                        //logger.fine(moderator + " added to the list of moderators");

                    }
                    for (int v = 0; v < viewers.length(); v++) {
                        // ADD MODERATOR JLABEL
                        String viewer      = viewers.getString(v);
                        UserLabel viewerLabel = new UserLabel(viewerIcon);
                        viewerLabel.setText(viewer);
                        viewerLabel.setTwitchUsername(viewer);
                        viewerLabel.addMouseListener(new UserMouseListener());
                        viewerLabel.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));
                        userList.add(viewerLabel);

                        // SET USER AS A NORMAL USER
                        try {
                            User user = db.findUser(viewer);
                            user.setModerator(false);
                            Dao<User, ?> userDAO = DaoManager.createDao(db.getConnectionSource(), User.class);
                            userDAO.update(user);

                        } catch (Exception e) {

                        }
                        //logger.fine(viewer + " added to the list");

                    }
                    userList.revalidate();
                    userList.repaint();
                } catch (Exception e) {
                    logger.warning("Error when trying to refresh user list, error:" + e.getMessage());
                }


            }
        };
        timer.scheduleAtFixedRate(tache, startTime, (long) getInterval());

    }

    public int getInterval() {
        return this.interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

}
