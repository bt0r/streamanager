/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package commands.Model;

import commands.AbstractCommand;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by btor on 05/02/2017.
 */
public class StreaminfoCommand extends AbstractCommand {
    String COMMAND_NAME = "streaminfo";

    public StreaminfoCommand() {
        setName(COMMAND_NAME);
    }

    @Override
    public Object doInBackground() {
        if (isEnabled() || isAdmin()) {
            try {
                String              user     = getEvent().getChannel().getName().replace("#","");
                JSONObject          chatters = twitch.getChatters(user);
                Map<String, String> stream   = twitch.getStreamInfo(user);
                if (chatters != null && stream.get("status").equals("online")) {
                    // Streamer is online
                    JSONArray moderators = chatters.getJSONObject("chatters").getJSONArray("moderators");
                    JSONArray viewers    = chatters.getJSONObject("chatters").getJSONArray("viewers");
                    JSONArray staffs     = chatters.getJSONObject("chatters").getJSONArray("staff");
                    JSONArray admins     = chatters.getJSONObject("chatters").getJSONArray("admins");

                    String totalViewer = stream.get("viewers");
                    String game        = stream.get("game");
                    String title       = stream.get("title");

                    Map transMap = new HashMap<>();
                    transMap.put("title", title);
                    transMap.put("game", game);
                    transMap.put("admins", admins.length());
                    transMap.put("staff", staffs.length());
                    transMap.put("mods", moderators.length());
                    transMap.put("viewers", totalViewer);
                    transMap.put("chatters", viewers.length());
                    String message = trans.replaceTrans(transMap, "chatTab.streamInfo");

                    sendMessage(COMMAND_NAME, getEvent(), getEvent().getUser().getNick(), message);
                } else {
                    // Stream is offline
                    sendMessage(COMMAND_NAME, getEvent(), getEvent().getUser().getNick(), trans.getProp("chatTab.noStream"));
                }
            } catch (Exception e) {
                logger.severe("Can't execute "+COMMAND_NAME+" command, error: "+e.getMessage());
            }

        }


        return true;
    }
}
