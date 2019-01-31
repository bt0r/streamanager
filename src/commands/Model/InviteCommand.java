/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package commands.Model;

import commands.AbstractCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by btor on 22/01/2017.
 */
public class InviteCommand extends AbstractCommand {
    String COMMAND_NAME = "invite";

    public InviteCommand() {
        setName(this.COMMAND_NAME);
    }

    @Override
    public Object doInBackground() {
        /*
         * Work only with whisper
         */
        if (isEnabled()) {
            String[] content    = getContent().split("\\s");
            String   userToInvite = content[0];

            Map transMap = new HashMap<>();
            transMap.put("user", getEvent().getUser().getNick());
            transMap.put("channel", getEvent().getChannel().getName().replace("#", ""));
            sendMessage("invite", getEvent(), userToInvite, trans.replaceTrans(transMap, "invite"));
        }

        return true;

    }
}
