/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package commands.Model;

import commands.AbstractCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by btor on 05/02/2017.
 */
public class FollowersCommand extends AbstractCommand {
    String COMMAND_NAME = "followers";

    public FollowersCommand() {
        setName(COMMAND_NAME);
    }

    @Override
    public Object doInBackground() {
        if (isEnabled() || isAdmin()) {
            try {
                Pattern p              = Pattern.compile("^([\\w]+)$");
                Matcher m              = p.matcher(getContent());
                String  totalFollowers = null;
                if (m.matches()) {
                    String username = m.group(1);
                    totalFollowers = twitch.getTotalFollowers(username);

                    Map transMap = new HashMap<>();
                    transMap.put("user", username);
                    transMap.put("total", totalFollowers);
                    sendMessage(COMMAND_NAME, getEvent(), getSender().getUsername(), trans.replaceTrans(transMap, "chatTab.followers"));
                } else {
                    String channel = getEvent().getChannel().getName().replace("#", "");
                    String username = getSender().getUsername();
                    totalFollowers = twitch.getTotalFollowers(channel);
                    Map transMap = new HashMap<>();
                    transMap.put("user", username);
                    transMap.put("total", totalFollowers);
                    sendMessage(COMMAND_NAME, getEvent(), getSender().getUsername(), trans.replaceTrans(transMap, "chatTab.followers"));
                }
                if (totalFollowers == null) {
                    sendMessage(COMMAND_NAME, getEvent(), getSender().getUsername(), trans.getProp("chatTab.followers.error"));
                }
            } catch (Exception e) {

            }
        }

        return null;
    }
}
