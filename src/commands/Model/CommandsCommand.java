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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by btor on 05/02/2017.
 */
public class CommandsCommand extends AbstractCommand {
    String COMMAND_NAME = "commands";

    public CommandsCommand() {
        setName(COMMAND_NAME);
        canChooseWhisper(false);
    }

    @Override
    public Object doInBackground() {
        String commandsMSG       = trans.getProp("command.list");
        String commandsCustomMSG = null;


        Dao<Command, ?> commandDAO;
        try {
            commandDAO = DaoManager.createDao(db.getConnectionSource(), Command.class);
            QueryBuilder questQB   = commandDAO.queryBuilder();
            Where        commandQB = questQB.where().eq("isEnable", true);
            logger.info(questQB.prepareStatementString());
            PreparedQuery<Command> preparedQuery = commandQB.prepare();
            List<Command>          commandList   = commandDAO.query(preparedQuery);
            Iterator               commandIt     = commandList.iterator();
            logger.info(commandList.size() + " commands found");
            if (commandList.size() > 0) {
                String commandsList = "";
                while (commandIt.hasNext()) {
                    Command command = (Command) commandIt.next();
                    commandsList += "!" + command.getName() + " ";
                }

                HashMap<String, String> customCommandMap = new HashMap<String, String>();
                customCommandMap.put("commands", commandsList);
                commandsCustomMSG = trans.replaceTrans(customCommandMap, "command.custom.list");
            }
        } catch (Exception e2) {
            logger.severe("Error when trying to find custom commands");
        }
        if (commandsCustomMSG == null) {
            sendMessage(COMMAND_NAME, getEvent(), getSender().getUsername(), commandsMSG);
        } else {
            sendMessage(COMMAND_NAME, getEvent(), getSender().getUsername(), commandsMSG + commandsCustomMSG);
        }

        return true;
    }
}
