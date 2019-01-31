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

import java.util.Iterator;
import java.util.List;

/**
 * Created by btor on 05/02/2017.
 */
public class TopCommand extends AbstractCommand {
    String COMMAND_NAME = "top";

    public TopCommand() {
        setName(COMMAND_NAME);
    }

    @Override
    public Object doInBackground() {
        if (isEnabled() || isAdmin()) {
            try {
                Dao<User, ?> senderDAO = DaoManager.createDao(db.getConnectionSource(), User.class);
                QueryBuilder<User, ?> queryBuilder2 = senderDAO.queryBuilder();
                queryBuilder2.orderBy("points", false).limit(10);
                PreparedQuery<User> preparedQuery2 = queryBuilder2.prepare();
                List<User>          topUsers       = senderDAO.query(preparedQuery2);
                Iterator            topUserIt      = topUsers.iterator();
                boolean             cantPlaySounds = true;

                String  topUsersSTR = "";
                Integer i           = 1;
                while (topUserIt.hasNext()) {
                    User user = (User) topUserIt.next();
                    topUsersSTR += " #" + i.toString() + " " + user.getUsername() + " [" + user.getPoints() + "] ";
                    i++;
                }
                sendMessage("top", getEvent(), getSender().getUsername(), trans.getProp("top.list") + topUsersSTR);

            } catch (Exception err) {
                logger.warning(trans.getProp("err.top"));
            }
        }


        return true;
    }
}
