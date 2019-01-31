/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package commands.Model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import commands.AbstractCommand;
import database.Battle;
import database.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by btor on 22/01/2017.
 */
public class RouletteCommand extends AbstractCommand {
    String COMMAND_NAME = "roulette";

    public RouletteCommand() {
        setName(this.COMMAND_NAME);
    }

    @Override
    public Object doInBackground() {
        /*
         * Roulette game ! One chance on six to die :D
         */
        if (isEnabled()) {
            int    result   = (int) (Math.random() * 6);
            String username = getEvent().getUser().getNick();
            User   sender   = getSender();

            try {
                Dao<User, ?> senderDAO = DaoManager.createDao(db.getConnectionSource(), User.class);
                if (sender.getPoints() > 70) {
                    // User can send roulette command

                    Battle                      battle       = new Battle();
                    Dao<Battle, ?>              battleDAO    = DaoManager.createDao(db.getConnectionSource(), Battle.class);
                    GenericRawResults<String[]> winnerResult = battleDAO.queryRaw("SELECT COUNT(*) AS totalWin FROM battle WHERE (player1_id=" + sender.getId() + " OR player2_id=" + sender.getId() + ") AND winner_id =" + sender.getId());
                    GenericRawResults<String[]> loserResult  = battleDAO.queryRaw("SELECT COUNT(*) AS totalWin FROM battle WHERE (player1_id=" + sender.getId() + " OR player2_id=" + sender.getId() + ") AND (winner_id !=" + sender.getId() + " OR winner_id IS NULL)");
                    String[]                    winList      = winnerResult.getFirstResult();
                    String[]                    loseList     = loserResult.getFirstResult();
                    int                         totalWin     = Integer.parseInt(winList[0]);
                    int                         totalLose    = Integer.parseInt(loseList[0]);

                    battle.setName("roulette");
                    battle.setIsVersusBot(true);
                    battle.setDate(System.currentTimeMillis());
                    battle.setPlayer1(sender);
                    battle.setPlayer2(null);

                    Map transMap = new HashMap<>();
                    transMap.put("user", sender.getUsername());
                    if (result == 1) {
                        // DIED -70 points
                        battle.setWinner(null);
                        sender.setPoints(sender.getPoints() - 70);
                        transMap.put("totalWin", totalWin);
                        transMap.put("totalLose", totalLose+1);

                        sendMessage("roulette", getEvent(), username, trans.replaceTrans(transMap, "roulette.died"));
                        sendMessage("roulette", getEvent(), username, ".timeout " + getEvent().getUser().getNick() + " 60");
                    } else {
                        // SURVIVED + 10pts
                        battle.setWinner(sender);
                        battle.setEarnedPoints(10);
                        sender.setPoints(sender.getPoints() + 10);
                        transMap.put("totalWin", totalWin+1);
                        transMap.put("totalLose", totalLose);
                        sendMessage("roulette", getEvent(), username, trans.replaceTrans(transMap, "roulette.survived"));
                    }
                    battleDAO.create(battle);
                    senderDAO.update(sender);
                } else {
                    // User can't send roulette command
                    sendMessage("roulette", getEvent(), username, username + " " + trans.getProp("roulette.right"));
                }


            } catch (SQLException e) {
                e.printStackTrace();
                logger.warning("Can't check user from database, action : !roulette");
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                logger.warning("Unknown error with command !roulette, error: " + e.getMessage());
                return false;
            }
        }

        return true;

    }
}
