/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */
/*
 * Created by btor on 22/01/2017.
 */
package commands.Model;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.vdurmont.emoji.EmojiManager;
import commands.AbstractCommand;
import database.Battle;
import database.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class ShifumiCommand extends AbstractCommand {
    String COMMAND_NAME = "shifumi";

    public ShifumiCommand() {
        setName(this.COMMAND_NAME);
    }

    @Override
    public Object doInBackground() {

        if (isEnabled()) {
            User     player1       = db.findUser(getEvent().getUser().getNick());
            String[] content       = getContent().split("\\s");
            String   playerChoice  = content[0];
            String[] scissorChoice = {"scissor", "ciseaux"};
            String[] paperChoice   = {"paper", "feuille"};
            String[] rockChoice    = {"rock", "pierre"};

            String  player2Name = getEvent().getBot().getNick();
            User    player2     = db.findUser(player2Name);
            boolean isVersusBot = true;

            if (content.length > 1) {
                player2Name = content[1];
                player2 = db.findUser(player2Name);
                isVersusBot = false;
            }

            if (! Arrays.asList(paperChoice).contains(playerChoice) &&
                    ! Arrays.asList(rockChoice).contains(playerChoice) &&
                    ! Arrays.asList(scissorChoice).contains(playerChoice)) {
                Map transMap = new HashMap<>();
                transMap.put("user", getEvent().getUser().getNick());
                sendMessage(getName(), getEvent(), getEvent().getUser().getNick(), trans.replaceTrans(transMap, "shifumi.error"));
                return true;
            }

            int     randomNumber = (int) (Math.random() * 3);
            boolean playerWin    = false;
            boolean isDraw       = false;
            String  botchoice    = "";
            switch (randomNumber) {
                case 0:
                    // Rock, Paper break rock
                    playerWin = Arrays.asList(paperChoice).contains(playerChoice);
                    isDraw = Arrays.asList(rockChoice).contains(playerChoice);
                    botchoice = config.getProp("app.lang").equals("en") ? rockChoice[0] : rockChoice[1];
                    break;
                case 1:
                    // Paper, Scissor break paper
                    playerWin = Arrays.asList(scissorChoice).contains(playerChoice);
                    isDraw = Arrays.asList(paperChoice).contains(playerChoice);
                    botchoice = config.getProp("app.lang").equals("en") ? paperChoice[0] : paperChoice[1];
                    break;
                case 2:
                    // Scissor , Rock break scissor
                    playerWin = Arrays.asList(rockChoice).contains(playerChoice);
                    isDraw = Arrays.asList(scissorChoice).contains(playerChoice);
                    botchoice = config.getProp("app.lang").equals("en") ? scissorChoice[0] : scissorChoice[1];
                    break;
            }
            String winner       = player2.getUsername();
            String loser        = player1.getUsername();
            String winnerChoice = botchoice;
            String loserChoice  = playerChoice;

            Map transMap = new HashMap<>();
            Battle battle = new Battle();
            if (isDraw) {
                // It's a draw
                transMap.put("choice", winnerChoice);
                transMap.put("player1", player1.getUsername());
                transMap.put("player2", player2.getUsername());

                // Save battle
                battle.setDate(System.currentTimeMillis());
                battle.setEarnedPoints(0);
                battle.setIsVersusBot(isVersusBot);
                battle.setPlayer1(player1);
                battle.setPlayer2(player2);
                battle.setName("shifumi");

                battle.setWinner(null);


            } else {
                // One player win
                if (playerWin) {
                    winner = player1.getUsername();
                    loser = player2.getUsername();
                    winnerChoice = playerChoice;
                    loserChoice = botchoice;
                }
                transMap.put("winner", winner);
                transMap.put("loser", loser);
                transMap.put("winnerChoice", winnerChoice);
                transMap.put("loserChoice", loserChoice);

                // Save result as battle
                battle.setDate(System.currentTimeMillis());
                battle.setEarnedPoints(10);
                battle.setIsVersusBot(isVersusBot);
                battle.setPlayer1(player1);
                battle.setPlayer2(player2);
                battle.setName("shifumi");

                User winnerObj = null;
                if (! isDraw) {
                    // It's not a draw
                    winnerObj = player1.getUsername().toLowerCase().equals(winner.toLowerCase()) ? player1 : player2;
                }

                battle.setWinner(winnerObj);
                db.addPointsToUser(winnerObj,10);
            }

            try {
                // Save Battle in database
                Dao<Battle, ?> battleDAO = DaoManager.createDao(db.getConnectionSource(), Battle.class);
                battleDAO.create(battle);
                String emoji = playerWin ? EmojiManager.getForAlias("trophy").getUnicode() : EmojiManager.getForAlias("-1").getUnicode();

                if(!isDraw){
                    sendMessage(getName(), getEvent(), getEvent().getUser().getNick(), emoji + " " + trans.replaceTrans(transMap, "shifumi.result"));
                }else{
                    sendMessage(getName(), getEvent(), getEvent().getUser().getNick(), EmojiManager.getForAlias("scales").getUnicode() + " " + trans.replaceTrans(transMap, "shifumi.draw"));
                }

            } catch (Exception e) {
                sendMessage(getName(), getEvent(), getEvent().getUser().getNick(), trans.replaceTrans(transMap, "shifumi.error"));
                return true;
            }

            return true;
        }


        return false;
    }
}
