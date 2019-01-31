/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package commands;

import org.pircbotx.hooks.events.MessageEvent;

/*
 * Created by btor on 22/01/2017.
 */
interface CommandInterface {
    String getName();

    CommandInterface setName(String name);

    String getContent();

    CommandInterface setContent(String content);

    MessageEvent getEvent();

    CommandInterface setEvent(MessageEvent event);

    boolean isEnabled();

    boolean canChooseWhisper();

}
