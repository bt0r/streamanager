/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package listeners;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import services.SoundService;


/**
 * Created by btor on 08/02/2017.
 */
public class CustomKeyListener implements HotKeyListener {
    private SoundService sound = SoundService.getInstance();
    private String filepath;

    public CustomKeyListener(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public void onHotKey(HotKey hotKey) {
        sound.play(filepath,false);
    }
}
