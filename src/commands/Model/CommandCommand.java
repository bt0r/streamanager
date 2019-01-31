/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package commands.Model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import commands.AbstractCommand;
import database.Command;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by btor on 09/03/2017.
 */
public class CommandCommand extends AbstractCommand {
    String COMMAND_NAME = "command";

    public CommandCommand() {
        setName(COMMAND_NAME);
        canChooseWhisper(false);
    }

    @Override
    public Object doInBackground() {
        if (isAdmin() || isStreamer() || isModerator()) {
            Pattern p = Pattern.compile("^(\\w+)\\s(\\w+).*");
            Matcher m = p.matcher(getContent());
            if (m.matches()) {
                String subCommand  = m.group(1);
                String commandName = m.group(2);
                System.out.println("subCommand:" + subCommand + " command:" + commandName);
                String[] disableValues = {"0", "disable"};
                String[] enableValues  = {"1", "enable"};
                // Enable / Disable commands
                if (Arrays.asList(disableValues).contains(commandName)) {
                    changeState(subCommand, false);
                } else if (Arrays.asList(enableValues).contains(commandName)) {
                    changeState(subCommand, true);
                } else {
                    switch (subCommand) {
                        case "create":
                            create(commandName);
                            break;
                        case "delete":
                            delete(commandName);
                            break;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Create a command
     *
     * @param commandName
     */
    public void create(String commandName) {
        Pattern p = Pattern.compile("^create\\s(\\w+)\\s(.+)");
        Matcher m = p.matcher(getContent());
        if (m.matches()) {
            String commandMessage = m.group(2);
            // Create command
            Dao<Command, ?> commandDAO;
            Command         command = new Command();
            command.setIsEnable(true);
            command.setMessage(commandMessage);
            command.setName(commandName);

            try {
                // Check if command exist
                commandDAO = DaoManager.createDao(db.getConnectionSource(), Command.class);
                QueryBuilder<Command, ?> questQB = commandDAO.queryBuilder();
                Where                    sql     = questQB.where().eq("name", command.getName());

                if (sql.countOf() > 0) {
                    sendMessage(getName(), getEvent(), getEvent().getUser().getNick(), trans.getProp("command.exist"));
                } else {
                    // Creating command
                    commandDAO.create(command);
                    sendMessage(getName(), getEvent(), getEvent().getUser().getNick(), trans.getProp("command.created"));
                }

            } catch (Exception e) {
                logger.warning("Can't create command " + command.getName() + " ,error:" + e.getMessage());
            }
        }
    }

    /**
     * Delete a command
     *
     * @param commandName
     */
    public void delete(String commandName) {
        // Deleting command
        Dao<Command, ?> commandDAO;
        try {
            commandDAO = DaoManager.createDao(db.getConnectionSource(), Command.class);
            QueryBuilder questQB = commandDAO.queryBuilder();
            Where        sql     = questQB.where().eq("name", commandName);
            if (sql.countOf() > 0) {
                PreparedQuery<Command> preparedQuery = sql.prepare();
                List<Command>          commandList   = commandDAO.query(preparedQuery);
                Iterator               commandIt     = commandList.iterator();
                while (commandIt.hasNext()) {
                    Command command = (Command) commandIt.next();
                    commandDAO.delete(command);
                    logger.info(trans.getProp("command.deleted") + " [" + command.getName() + "]");
                    sendMessage(getName(), getEvent(), getEvent().getUser().getNick(), trans.getProp("command.deleted"));
                }
            } else {
                logger.warning(trans.getProp("err.command.notfound") + " [" + commandName + "]");
                sendMessage(getName(), getEvent(), getEvent().getUser().getNick(), trans.getProp("err.command.notfound") + " [" + commandName + "]");
            }
        } catch (Exception e) {
            logger.warning("Error while trying to find a custom command [" + commandName + "]");
        }
    }

    /**
     * Disable/Enable a command
     *
     * @param commandName
     * @param state
     */
    public void changeState(String commandName, boolean state) {
        // Enable / Disable command
        String boolState;
        String transState;
        if (state) {
            boolState = "true";
            transState = "enable";
        } else {
            boolState = "false";
            transState = "disable";
        }
        if (config.getProp("command." + commandName) != null) {
            config.setProp("command." + commandName, boolState);
            sendMessage(getName(), getEvent(), getEvent().getUser().getNick(), trans.getProp("command." + transState) + commandName);
        } else {
            sendMessage(getName(), getEvent(), getEvent().getUser().getNick(), trans.getProp("err.command.notfound"));
        }
    }
}
