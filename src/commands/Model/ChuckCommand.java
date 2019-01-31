/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package commands.Model;

import commands.AbstractCommand;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;

/**
 * Created by btor on 22/01/2017.
 */
public class ChuckCommand extends AbstractCommand {
    String COMMAND_NAME = "chuck";
    String API_URL_FR   = "http://www.chucknorrisfacts.fr/api/get?data=tri:alea;type:txt;nb:1";
    String API_URL_EN   = "https://api.chucknorris.io/jokes/random";

    public ChuckCommand() {
        setName(this.COMMAND_NAME);
    }

    @Override
    public Object doInBackground() {
        if (isEnabled()) {
            try {
                String fact;

                if (config.getProp("app.lang").equals("fr")) {
                    JSONArray json = twitch.getJsonArrayFromUrl(API_URL_FR);
                    fact = StringEscapeUtils.unescapeHtml4(json.getJSONObject(0).getString("fact"));
                } else {
                    JSONArray json = twitch.getJsonArrayFromUrl(API_URL_EN);
                    fact = StringEscapeUtils.unescapeHtml4(json.getJSONObject(0).getString("fact"));
                }
                if (!fact.equals("")) {
                    sendMessage("chuck", getEvent(), getEvent().getUser().getNick(), fact);
                } else {
                    sendMessage("chuck", getEvent(), getEvent().getUser().getNick(), trans.getProp("err.chuck.fact"));
                }
            } catch (Exception e2) {
                sendMessage("chuck", getEvent(), getEvent().getUser().getNick(), trans.getProp("err.chuck.fact"));
            }
            return true;
        }
        return false;
    }

}
