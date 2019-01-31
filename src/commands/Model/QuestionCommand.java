/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package commands.Model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import commands.AbstractCommand;
import database.Question;
import database.Vote;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by btor on 19/03/2017.
 */
public class QuestionCommand extends AbstractCommand {
    String COMMAND_NAME = "question";

    public QuestionCommand() {
        setName(this.COMMAND_NAME);
        canChooseWhisper(false);
    }

    @Override
    public Object doInBackground() {
        if (isEnabled() && (isAdmin() || isModerator() || isStreamer())) {
            String   content = getContent();
            String[] args    = content.split("\\s");
            if (args.length >= 1) {
                switch (args[0]) {
                    default:
                        create();
                        break;
                    case "stop":
                        stop();
                        break;
                    case "close":
                        stop();
                        break;
                }
            }
        }
        return true;
    }

    /**
     * Create a question with 3 args : Question, Choices, Duration
     *
     */
    private void create() {
        Pattern p = Pattern.compile("^([^\\[]+)\\[(.+)\\](?:\\s(\\d+))?$");
        Matcher m = p.matcher(getContent());
        if (m.matches()) {
            try {
                String userStr     = getSender().getUsername();
                String questionStr = m.group(1);
                String choices     = m.group(2);
                String choicesStr  = "";
                // Set default duration to 5 minutes
                int duration = m.group(3) != null ? new Integer(m.group(3)) : 5;

                // Create question
                Question question = new Question();
                question.setChoices(choices);
                question.setContent(questionStr);
                question.setUser(userStr);
                question.setIsEnable(true);
                question.setDuration(duration);
                int i = 0;
                for (String choice : question.getChoices()) {

                    if (i != question.getChoices().size() - 1) {
                        choicesStr = choicesStr + choice + ",";
                    } else {
                        choicesStr = choicesStr + choice;
                    }
                    i++;
                }


                Dao<Question, ?> questionDAO;
                try {
                    questionDAO = DaoManager.createDao(db.getConnectionSource(), Question.class);
                    QueryBuilder questQB = questionDAO.queryBuilder();
                    questQB.where().ge("endDate", new Date().getTime()).and().eq("isEnable", true);
                    if (questQB.countOf() == 0) {
                        // No question pending
                        questionDAO.create(question);
                        Map transMap = new HashMap<>();
                        transMap.put("user", getSender().getUsername());
                        transMap.put("question", questionStr);
                        transMap.put("choices", choicesStr);
                        transMap.put("duration", duration);

                        sendMessage(getName(), getEvent(), getSender().getUsername(), trans.replaceTrans(transMap, "question.new"));
                    } else {
                        sendMessage(getName(), getEvent(), getSender().getUsername(), trans.getProp("err.question.pending"));
                        logger.info(trans.getProp("err.question.pending"));
                    }
                } catch (SQLException e1) {
                    logger.warning(trans.getProp("err.question.create"));
                }

            } catch (Exception e) {
                logger.warning("Error with 'Question' command, error:" + e.getMessage());
            }
        }
    }

    /**
     * Stop current question
     */
    private void stop() {
    /*
     * STOP VOTE FOR LAST QUESTION OR FORCE QUESTION TO STOP
     */
        System.out.println("STOP");
        Dao<Vote, ?>     voteDAO;
        Dao<Question, ?> questionDAO;
        try {
            voteDAO = DaoManager.createDao(db.getConnectionSource(), Vote.class);
            questionDAO = DaoManager.createDao(db.getConnectionSource(), Question.class);
            QueryBuilder  questQB       = questionDAO.queryBuilder();
            Where         questionQB    = questQB.where().ge("endDate", new Date().getTime()).and().eq("isEnable", true);
            PreparedQuery preparedQuery = questionQB.prepare();
            List          questionList  = questionDAO.query(preparedQuery);
            Iterator      questionIt    = questionList.iterator();
            int           questionId    = 0;
            String        questionValue = "";
            logger.info(questionList.size() + " question(s) found");
            if (questionList.size() > 1) {
                sendMessage(getName(), getEvent(), getSender().getUsername(), trans.getProp("question.clone"));

            } else {
                while (questionIt.hasNext()) {
                    Question question = (Question) questionIt.next();
                    questionId = question.getId();
                    question.setIsEnable(false);
                    questionDAO.update(question);
                    questionValue = question.getContent();
                }
            }

            if (questionId != 0) {
                // A question has been found
                GenericRawResults<String[]> rawResults    = voteDAO.queryRaw("SELECT count(*) AS groupRes,value FROM vote WHERE question=" + questionId + " GROUP BY value ORDER BY groupRes DESC;");
                GenericRawResults<String[]> rawnbrResults = voteDAO.queryRaw("SELECT value FROM vote WHERE question=" + questionId);

                List<String[]> results   = rawResults.getResults();
                List<String[]> nbrResult = rawnbrResults.getResults();

                logger.info(nbrResult.size() + " votes");
                Iterator resultIt  = results.iterator();
                String   resultStr = "";

                if (results.size() > 0){
                    while (resultIt.hasNext()) {
                        String[] groupBy = (String[]) resultIt.next();
                        float longPourcent = (new Integer(groupBy[0]) * 100)/ (float) nbrResult.size();
                        int pourcent = Math.round(longPourcent);
                        if (resultStr.equals("")) {
                            resultStr = "⠆ " + groupBy[1] + "➜" + pourcent + "% ⠆";
                        } else {
                            resultStr = resultStr + " ⠆  " + groupBy[1] + "➜" + pourcent + "%⠆  ";
                        }

                    }
                    sendMessage("question", getEvent(), getSender().getUsername(), "\"" + questionValue + "\" " + trans.getProp("question.close") + resultStr);
                }else{
                    sendMessage("question", getEvent(), getSender().getUsername(), "\"" + questionValue + "\" " + trans.getProp("question.close_without_replies"));
                }
            }
        } catch (Exception errVote) {
            logger.warning(errVote.getMessage());
        }
    }

}
