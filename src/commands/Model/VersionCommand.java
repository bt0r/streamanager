/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package commands.Model;

import commands.AbstractCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by btor on 01/02/2017.
 */
public class VersionCommand extends AbstractCommand {
    String COMMAND_NAME = "version";

    public VersionCommand() {
        setName(COMMAND_NAME);
        canChooseWhisper(false);
    }

    @Override
    public Object doInBackground() {
        Map transMap = new HashMap<>();
        transMap.put("version", config.getProp("app.version"));
        sendWhisper(getEvent(), getSender().getUsername(), trans.replaceTrans(transMap, "version"));

        return this;
    }
}
