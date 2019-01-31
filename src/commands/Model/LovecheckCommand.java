/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package commands.Model;

import Tools.Tools;
import commands.AbstractCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by btor on 05/02/2017.
 */
public class LovecheckCommand extends AbstractCommand {
    String COMMAND_NAME = "lovecheck";

    public LovecheckCommand() {
        setName(COMMAND_NAME);
    }

    @Override
    public Object doInBackground() {
        if (isEnabled() || isAdmin()) {
            try {
                Pattern p = Pattern.compile("^([\\w]+)$");
                Matcher m = p.matcher(getContent());
                if (m.matches()) {
                    String  username        = m.group(1);
                    Integer lovecheckResult = Tools.lovecheck(username, getSender().getUsername());
                    Map     transMap        = new HashMap<>();
                    transMap.put("user", getSender().getUsername());
                    transMap.put("user2", username);
                    transMap.put("result", lovecheckResult);
                    sendMessage(COMMAND_NAME, getEvent(), getSender().getUsername(), trans.replaceTrans(transMap, "chatTab.lovecheck"));
                }
            } catch (Exception e) {
                logger.severe("Error with " + COMMAND_NAME + " command, error:" + e.getMessage());
            }

        }
        return true;
    }
}
