/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package services;

import animations.LoadingAnimation;
import com.tulskiy.keymaster.common.Provider;
import hotkeys.HotKeyPanel;
import hotkeys.HotKeysTab;
import listeners.CustomKeyListener;
import main.component.jxlabel.Label;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.logging.Logger;

/**
 * Created by btor on 11/02/2017.
 */
public class HotKeysService {
    private static ConfigService   config = ConfigService.getInstance();
    private static LanguageService trans  = LanguageService.getInstance();
    private static Logger          logger = Logger.getLogger("streaManager");

    public HotKeysService() {

    }

    public static void load(HotKeysTab panel) {
        config.startTask("hotkeys", trans.getProp("hotkeys.loading"), LoadingAnimation.LEVEL_INFO);
        try {
            Provider provider = Provider.getCurrentProvider(true);
            provider.reset();
            provider.stop();
            provider = Provider.getCurrentProvider(false);

            // Load all hotkeys configs
            for (int i = 1; i <= 10; i++) {
                String filepath = config.getProp("hotkey_" + i + "_path");
                String keys     = config.getProp("hotkey_" + i + "_keys");

                // Browse components and set values
                Component[] panelComponents = panel.getComponents();
                for (Component component : panelComponents) {
                    if (component instanceof HotKeyPanel) {
                        HotKeyPanel hotKeyPanel = (HotKeyPanel) component;
                        if (hotKeyPanel.getName().equals("hotkey_" + i)) {
                            Label filenameLabel = hotKeyPanel.getFilenameLabel();
                            Label hotkeyLabel   = hotKeyPanel.getHotkeyLabel();

                            if (filepath != null && !filepath.isEmpty()) {
                                File file = new File(filepath);
                                filenameLabel.setText(file.getName());
                            }
                            if(keys != null && !keys.isEmpty())
                                hotkeyLabel.setText(keys.toUpperCase());

                        }

                    }

                }

                if (!filepath.isEmpty() && !keys.isEmpty()) {
                    logger.info("Loading hotkey N°" + i);
                    provider.register(KeyStroke.getKeyStroke(keys), new CustomKeyListener(filepath));
                }
            }
            config.endTask("hotkeys", trans.getProp("hotkeys.loaded"), LoadingAnimation.LEVEL_SUCCESS);
        } catch (Exception e) {
            config.endTask("hotkeys", trans.getProp("hotkeys.loading.error"), LoadingAnimation.LEVEL_ERROR);
        }


    }
}
