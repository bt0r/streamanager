/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package commands.Model;

import commands.AbstractCommand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by btor on 22/01/2017.
 */
public class BanCommand extends AbstractCommand {
    String COMMAND_NAME = "ban";

    public BanCommand() {
        setName(this.COMMAND_NAME);
        canChooseWhisper(false);
    }

    @Override
    public Object doInBackground() {
        /*
         * Unban user , work only for moderator and admins
         */
        if(isEnabled() && (isModerator() || isStreamer() || isAdmin())){
            Pattern             p          = Pattern.compile("(\\w+)$");
            Matcher  m          = p.matcher(getContent());
            if (m.matches()) {
                String username = m.group(1);
                sendMessage("unban",getEvent(), username, ".ban " + username);
                sendMessage("ban",getEvent(), username, username + " " + trans.getProp("ban.banned"));            }
        }

        return true;

    }
}
