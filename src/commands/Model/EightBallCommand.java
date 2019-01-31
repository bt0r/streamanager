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
public class EightBallCommand extends AbstractCommand {
    String COMMAND_NAME = "8ball";

    public EightBallCommand() {
        setName(COMMAND_NAME);
    }

    @Override
    public Object doInBackground() {
        if (isEnabled()) {
            String question = getContent();
            String answer   = "";
            int    result   = (int) (Math.random() * 12);
            switch (result) {
                case 0:
                    answer = trans.getProp("answer.yes");
                    break;
                case 1:
                    answer = trans.getProp("answer.no");
                    break;
                case 2:
                    answer = trans.getProp("answer.maybe");
                    break;
                case 3:
                    answer = trans.getProp("answer.tomorrow");
                    break;
                case 4:
                    answer = trans.getProp("answer.wait");
                    break;
                case 5:
                    answer = trans.getProp("answer.impossible");
                    break;
                case 6:
                    answer = trans.getProp("answer.dumb");
                    break;
                case 7:
                    answer = trans.getProp("answer.sure");
                    break;
                case 8:
                    answer = trans.getProp("answer.noDoubt");
                    break;
                case 9:
                    answer = trans.getProp("answer.probably");
                    break;
                case 10:
                    answer = trans.getProp("answer.cantReply");
                    break;
                case 11:
                    answer = trans.getProp("answer.probablyNot");
                    break;
            }
            String username = getEvent().getUser().getNick();
            Map    transMap = new HashMap<>();
            transMap.put("user", username);
            transMap.put("answer", answer);

            sendMessage("8ball", getEvent(), username, trans.replaceTrans(transMap, "8ball.answer"));
        }


        return true;
    }
}
