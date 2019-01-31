/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package main.component;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by btor on 13/02/2017.
 */
public class SM_EmojiParser extends EmojiParser {
    public List<Emoji> detectEmotes(String input) {
        char[]    inputCharArray = input.toCharArray();
        ArrayList emotesList     = new ArrayList();
        for (int i = 0; i < input.length(); ++i) {
            int emojiEnd = getEmojiEndPos(inputCharArray, i);
            if (emojiEnd != -1) {
                // Detect Emoji
                Emoji emoji = EmojiManager.getByUnicode(input.substring(i, emojiEnd));
                emotesList.add(emoji);
            }
        }
        return emotesList;
    }
}
