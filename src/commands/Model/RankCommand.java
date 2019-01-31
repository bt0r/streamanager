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

/**
 * Created by btor on 05/02/2017.
 */
public class RankCommand extends AbstractCommand {
    String COMMAND_NAME = "rank";

    public RankCommand() {
        setName(COMMAND_NAME);
    }

    @Override
    public Object doInBackground() {
        if (isAdmin() || isEnabled()) {
            try {
                Dao<User, ?>          senderDAO     = DaoManager.createDao(db.getConnectionSource(), User.class);
                QueryBuilder<User, ?> queryBuilder2 = senderDAO.queryBuilder();
                queryBuilder2.orderBy("points", false);
                PreparedQuery<User> preparedQuery2 = queryBuilder2.prepare();
                List<User>          topUsers       = senderDAO.query(preparedQuery2);
                Iterator            topUserIt      = topUsers.iterator();

                String  topUsersSTR = "";
                Integer i           = 1;
                while (topUserIt.hasNext()) {
                    User user = (User) topUserIt.next();
                    if (user.getUsername().equals(getSender().getUsername())) {
                        Map topMap = new HashMap<String, String>();
                        topMap.put("user", getSender().getUsername());
                        topMap.put("rank", i);
                        topMap.put("points", Integer.toString(user.getPoints()));

                        topUsersSTR = trans.replaceTrans(topMap, "rank");
                        break;
                    }
                    i++;
                }
                sendMessage("rank", getEvent(), getSender().getUsername(), topUsersSTR);
            } catch (Exception e) {
                logger.warning(trans.getProp("err.top"));
            }
        }

        return true;
    }
}
