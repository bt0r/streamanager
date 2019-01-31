/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package commands.Model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import commands.AbstractCommand;
import database.User;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by btor on 05/02/2017.
 */
public class FcCommand extends AbstractCommand {
    String COMMAND_NAME = "fc";

    public FcCommand() {
        setName(COMMAND_NAME);
    }

    @Override
    public Object doInBackground() {
        if (isEnabled() || isAdmin()) {
            try {
                // Match without param , one or two params
                Pattern p = Pattern.compile("^(?:@?(\\w+)\\s?@?(\\w+)?|@?(\\w+))?$");
                Matcher m = p.matcher(getContent());
                if (m.matches()) {
                    String usernameToCheck = m.group(1);
                    String channelToCheck  = m.group(2);

                    if (usernameToCheck == null && channelToCheck == null) {
                        fc();
                    } else if (usernameToCheck != null && channelToCheck == null) {
                        fc(usernameToCheck);
                    } else if (usernameToCheck != null && channelToCheck != null) {
                        fc(usernameToCheck, channelToCheck);
                    }
                }


            } catch (Exception e) {
                logger.severe("Can't execute " + COMMAND_NAME + " command, error: " + e.getMessage());
            }
        }


        return true;
    }

    /**
     * Check if sender already follow the channel
     */
    public void fc() {
        String usernameToCheck = getSender().getUsername();
        String channelToCheck  = getEvent().getChannel().getName().replace("#", "");

        doFc(usernameToCheck, channelToCheck);
    }

    /**
     * Check if a specific username already follow the channel
     * @param usernameToCheck
     */
    public void fc(String usernameToCheck) {
        String channelToCheck = getEvent().getChannel().getName().replace("#", "");

        doFc(usernameToCheck, channelToCheck);
    }

    /**
     * Check if a specific user follow a specific channel
     * @param usernameToCheck
     * @param channelToCheck
     */
    public void fc(String usernameToCheck, String channelToCheck) {
        doFc(usernameToCheck, channelToCheck);
    }

    private void doFc(String usernameToCheck, String channelToCheck) {
        try {
            long         lastFollow = twitch.getLastFollow(usernameToCheck, channelToCheck);
            Dao<User, ?> senderDAO  = DaoManager.createDao(db.getConnectionSource(), User.class);

            DateTime LFDate     = new DateTime(lastFollow);
            DateTime actualDate = DateTime.now();

            int nbrDays = Days.daysBetween(LFDate.withTimeAtStartOfDay(), actualDate.withTimeAtStartOfDay()).getDays();


            // Channel asked is the same
            if (channelToCheck.equals(getEvent().getChannel().getName().replace("#", ""))) {
                User sender = getSender();
                sender.setLastConn(actualDate.getMillis());
                sender.setLastFc(actualDate.getMillis());
                sender.setFollowDate(LFDate.getMillis());
                logger.info("update de l'utilisateur : " + actualDate.getMillis());
                senderDAO.update(sender);
            }
            SimpleDateFormat sf        = new SimpleDateFormat(trans.getProp("date.format"));
            String           lastFL    = sf.format(lastFollow);
            String           followStr = "";

            if (lastFollow != 0) {
                Map transMap = new HashMap<>();
                transMap.put("user", usernameToCheck);
                transMap.put("channel", channelToCheck);
                transMap.put("followDate", lastFL);
                transMap.put("followDays", nbrDays);
                followStr = trans.replaceTrans(transMap, "chatTab.follow");
            } else {
                Map transMap = new HashMap<String, String>();
                transMap.put("user", usernameToCheck);
                transMap.put("channel", channelToCheck);
                followStr = trans.replaceTrans(transMap, "chatTab.notFollow");

            }

            sendMessage(COMMAND_NAME, getEvent(), getSender().getUsername(), followStr);
        } catch (Exception e) {
            logger.warning(trans.getProp("err.user"));
        }


    }
}
