/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package commands.Model;

import Tools.Tools;
import commands.AbstractCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by btor on 31/01/2017.
 */
public class HoroscopeCommand extends AbstractCommand {
    String COMMAND_NAME = "horoscope";

    public HoroscopeCommand() {
        setName(this.COMMAND_NAME);
        canChooseWhisper(false);
    }

    @Override
    public Object doInBackground() {
        /*
         * Horoscope command, only on whisper !
         */
        if (isEnabled()) {
            Map<String, Integer> horoscope = Tools.horoscope(getEvent().getUser().getNick());
            Map                  transMap  = new HashMap<>();
            transMap.put("love", horoscope.get("love"));
            transMap.put("health", horoscope.get("health"));
            transMap.put("money", horoscope.get("money"));

            sendWhisper(getEvent(), getEvent().getUser().getNick(), trans.replaceTrans(transMap, "horoscope"));

        }

        return true;

    }
}
