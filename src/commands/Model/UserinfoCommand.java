/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package commands.Model;

import Tools.Tools;
import commands.AbstractCommand;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by btor on 01/02/2017.
 */
public class UserinfoCommand extends AbstractCommand {
    String COMMAND_NAME = "userinfo";

    public UserinfoCommand() {
        setName(COMMAND_NAME);
    }

    @Override
    public Object doInBackground() {
        if (isEnabled()) {
            Pattern p = Pattern.compile("^(\\w+)$");
            Matcher m = p.matcher(getContent());
            if (m.matches()) {
                String username = m.group(1);
                try {
                    JSONObject userInfo       = twitch.getUserInfo(username);
                    String     totalFollowers = twitch.getTotalFollowers(username);
                    String     totalFollow    = twitch.getTotalFollow(username);
                    String     bio            = "";
                    if (! userInfo.isNull("bio")) {
                        bio = userInfo.getString("bio");
                    }
                    String                  createdDate = Tools.timestampToDateStr(Tools.dateToTimestamp(userInfo.getString("created_at")));
                    HashMap<String, String> messageMap  = new HashMap<String, String>();
                    messageMap.put("username", username);
                    messageMap.put("bio", bio);
                    messageMap.put("date", createdDate);
                    messageMap.put("follows", totalFollow);
                    messageMap.put("followers", totalFollowers);
                    String message = trans.replaceTrans(messageMap, "userInfo.command");
                    sendMessage(COMMAND_NAME, getEvent(), getSender().getUsername(), message);


                } catch (Exception e) {
                    e.printStackTrace();
                    HashMap<String, String> messageMap = new HashMap<String, String>();
                    messageMap.put("user", username);

                    sendMessage(COMMAND_NAME, getEvent(), getSender().getUsername(), trans.replaceTrans(messageMap, "userInfo.userNotFound"));
                    logger.severe("Error with command " + COMMAND_NAME + " error:" + e.getMessage());
                }
            }
        }

        return this;
    }
}
