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
public class PokeCommand extends AbstractCommand {
    String COMMAND_NAME = "poke";

    public PokeCommand() {
        setName(this.COMMAND_NAME);
    }

    @Override
    public Object doInBackground() {
        /*
         * Work only with whisper
         */
        if(isEnabled()){
            String[] content      = getContent().split("\\s");
            String   userToPoke = content[0];

            Map    transMap = new HashMap<>();
            transMap.put("user", getEvent().getUser().getNick());
            sendMessage("poke", getEvent(), userToPoke, trans.replaceTrans(transMap, "poke"));
        }

        return true;

    }
}
