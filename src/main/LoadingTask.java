package main;

import animations.LoadingAnimation;
import org.jdesktop.swingx.JXLabel;
import org.json.JSONException;
import services.ConfigService;
import services.LanguageService;
import services.TwitchAPIService;
import updater.ConfirmUpdate;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by btor on 28/09/2016.
 */
public class LoadingTask extends Thread {
    LanguageService trans  = LanguageService.getInstance();
    ConfigService   config = ConfigService.getInstance();
    JXLabel taskName;

    public LoadingTask(JXLabel taskName) {
        super();
        this.taskName = taskName;
    }

    @Override
    public synchronized void start() {
        super.start();

        // Emotes
        taskName.setForeground(new Color(0xFFFFFF));
        taskName.setText(trans.getProp("task.emotes.bttv"));
        downloadEmotesBTTV();
        taskName.setText(trans.getProp("task.emotes.bttv") + trans.getProp("task.end"));

        // CREATE LIBS DIRECTORY
        taskName.setText(trans.getProp("task.libs.create"));

        // Load SIGAR API
        try {
            config.moveLibs();
            taskName.setText(trans.getProp("task.libs.create") + trans.getProp("task.end"));
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            taskName.setText(trans.getProp("task.libs.create") + trans.getProp("task.error"));
            taskName.setOpaque(true);
            taskName.setBackground(new Color(Integer.decode(config.getProp("color.red"))));
        }

        // Checking new update
        if(config.checkNewVersion()){
            config.startTask("update.check", trans.getProp("update.task"), LoadingAnimation.LEVEL_INFO);
            try{
                SwingUtilities.invokeLater(() -> new ConfirmUpdate());
                config.endTask("update.check", trans.getProp("update.task.done"), LoadingAnimation.LEVEL_SUCCESS);
            }catch(Exception e){
                config.endTask("update.check", trans.getProp("err.update.task"), LoadingAnimation.LEVEL_ERROR);
            }
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    public void downloadEmotesBTTV() {
        try {
            File    emoteFile     = new File(config.getConfigSysDir() + File.separator + "emoticonsBTTV.json");
            boolean forceDownload = false;
            if (emoteFile.exists() && emoteFile.isFile()) {
                long emoteFileDate = emoteFile.lastModified();
                long actualDate    = System.currentTimeMillis() + 86400000;

                if ((emoteFileDate - actualDate) > 0) {
                    forceDownload = true;
                }
            } else {
                forceDownload = true;
            }

            if (forceDownload) {
                if (!config.getProp("streamer.login").equals("")) {
                    TwitchAPIService.getInstance().downloadBTTVEmotesDB(config.getProp("streamer.login"));
                }
            }

            TwitchAPIService.getInstance().setBTTVEmotesList();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
