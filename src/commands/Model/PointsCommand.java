/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package commands.Model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import commands.AbstractCommand;
import database.User;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by btor on 01/02/2017.
 */
public class PointsCommand extends AbstractCommand {
    String COMMAND_NAME = "points";

    public PointsCommand() {
        setName(this.COMMAND_NAME);
    }

    @Override
    public Object doInBackground() {
        if (getContent().isEmpty()) {
            points();
        } else {
            Pattern regexp = Pattern.compile("^(\\w+)\\s(\\w+)\\s(\\d+)$");
            Matcher m      = regexp.matcher(getContent());
            if (m.matches()) {
                String subCommand = m.group(1);
                String username   = m.group(2);
                int    points     = Integer.parseInt(m.group(3));
                switch (subCommand) {
                    case "give":
                        givePoints(username, points);
                        break;
                    case "take":
                        takePoints(username, points);
                        break;
                }
            }
        }


        return this;
    }

    /**
     * Give points to a user
     *
     * @param username     String
     * @param pointsToGive int
     *
     * @return PointsCommand
     */
    private PointsCommand givePoints(String username, int pointsToGive) {
        if (isModerator() || isAdmin() || isStreamer()) {
            try {
                Dao<User, ?> senderDAO   = DaoManager.createDao(db.getConnectionSource(), User.class);
                User         user        = db.findUser(username);
                Integer      points      = 0;
                Integer      pointsToAdd = new Integer(pointsToGive);
                points = pointsToAdd > 1000 ? 1000 : pointsToAdd;

                user.addPoints(points);
                senderDAO.update(user);
                Map transMap = new HashMap<String, String>();
                transMap.put("user", username);
                transMap.put("points", pointsToAdd);
                transMap.put("totalPoints", user.getPoints());
                sendMessage(this.COMMAND_NAME, getEvent(), getSender().getUsername(), trans.replaceTrans(transMap, "points.give"));

            } catch (Exception e) {
                logger.warning("Cannot execute commande " + COMMAND_NAME + ",error: " + e.getMessage());

            }
        }
        return this;
    }

    /**
     * Take points to a user
     *
     * @param username     String
     * @param pointsToTake int
     *
     * @return PointsCommand
     */
    private PointsCommand takePoints(String username, int pointsToTake) {
        if (isAdmin() || isModerator() || isStreamer()) {
            try {
                Dao<User, ?>          senderDAO    = DaoManager.createDao(db.getConnectionSource(), User.class);
                QueryBuilder<User, ?> queryBuilder = senderDAO.queryBuilder();
                queryBuilder.where().eq("username", username.toLowerCase());
                PreparedQuery<User> preparedQuery = queryBuilder.prepare();
                List<User>          userList      = senderDAO.query(preparedQuery);
                Iterator<User>      userIt        = userList.iterator();
                if (!userList.isEmpty()) {
                    Integer points      = 0;
                    Integer pointsToDel = new Integer(pointsToTake);
                    points = pointsToDel > 1000 ? 1000 : pointsToDel;
                    User user = userIt.next();

                    user.delPoints(points);
                    senderDAO.update(user);
                    Map transMap = new HashMap<>();
                    transMap.put("user", username);
                    transMap.put("points", pointsToDel);
                    transMap.put("totalPoints", user.getPoints());
                    sendMessage(this.COMMAND_NAME, getEvent(), getSender().getUsername(), trans.replaceTrans(transMap, "points.take"));
                }
            } catch (Exception e) {
                logger.warning("Cannot execute commande " + COMMAND_NAME + ",error: " + e.getMessage());
            }
        }

        return this;
    }

    private PointsCommand points() {
        if (isEnabled()) {
            try {
                User user = getSender();
                Map transMap = new HashMap<>();
                transMap.put("channel", getEvent().getChannel().getName().replace("#", ""));
                transMap.put("points", Integer.toString(user.getPoints()));
                sendMessage(this.COMMAND_NAME, getEvent(), getSender().getUsername(), trans.replaceTrans(transMap, "points"));

            } catch (Exception e) {
                logger.warning("Cannot execute commande " + this.COMMAND_NAME + ",error: " + e.getMessage());
            }
        }

        return this;
    }
}
