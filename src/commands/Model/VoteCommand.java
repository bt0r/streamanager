/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package commands.Model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import commands.AbstractCommand;
import database.Question;
import database.Vote;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by btor on 19/03/2017.
 */
public class VoteCommand extends AbstractCommand {
    String COMMAND_NAME = "question";

    public VoteCommand() {
        setName(this.COMMAND_NAME);
        canChooseWhisper(false);
    }

    @Override
    public Object doInBackground() {
        if (isEnabled() || isAdmin()) {
            vote();
        }

        return true;
    }

    private void vote() {
        // Checker en BDD qu'une question existe déjà (en fonction des
        // réponses et des dates)
        String userStr   = getSender().getUsername();
        String voteValue = getContent();

        // Check if question exist and still enable
        try {
            Dao<Question, ?> questionDAO;
            questionDAO = DaoManager.createDao(db.getConnectionSource(), Question.class);
            QueryBuilder<Question, ?> queryBuilder = questionDAO.queryBuilder();
            queryBuilder.where().ge("endDate", new Date().getTime());
            PreparedQuery<Question> preparedQuery = queryBuilder.prepare();
            List<Question>          questionList  = questionDAO.query(preparedQuery);
            Iterator                questionIt    = questionList.iterator();
            while (questionIt.hasNext()) {
                Question          question = (Question) questionIt.next();
                ArrayList<String> choices  = question.getChoices();
                for (String choice : choices) {
                    if (choice.equals(voteValue)) {
                        // Check if vote already exist for this user
                        Dao<Vote, ?> voteDAO;
                        voteDAO = DaoManager.createDao(db.getConnectionSource(), Vote.class);
                        QueryBuilder<Vote, ?> queryBuilderVote = voteDAO.queryBuilder();
                        queryBuilderVote.where().eq("user", userStr).and().eq("question", question.getId()).prepare();
                        long resCount = queryBuilderVote.countOf();
                        if (resCount == 0) {
                            // Create vote
                            Vote vote = new Vote();
                            vote.setUser(userStr);
                            vote.setValue(choice);
                            vote.setQuestion(question.getId());

                            // Adding the vote into database
                            try {
                                voteDAO.create(vote);
                                sendWhisper(getEvent(), getSender().getUsername(), trans.getProp("vote.success"));
                                logger.info("Vote '" + choice + "' for user " + userStr + " added in database.");
                            } catch (SQLException e1) {
                                logger.warning(trans.getProp("err.vote.create"));
                            }
                        } else {
                            sendWhisper(getEvent(), getSender().getUsername(), trans.getProp("err.vote.duplicate"));
                        }
                    }
                }
            }

        } catch (SQLException e1) {
            logger.warning(trans.getProp("err.question.notexist"));
            sendWhisper(getEvent(), getSender().getUsername(), trans.getProp("err.question.notexist"));

        } catch (Exception e) {
            logger.warning("Error with 'Vote' command, error:" + e.getMessage());
        }


    }
}
