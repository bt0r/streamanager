/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package commands.Model;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import commands.AbstractCommand;
import database.User;
import services.SoundService;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Created by btor on 05/02/2017.
 */
public class SoundCommand extends AbstractCommand {
    String COMMAND_NAME = "sound";
    private SoundService sound = SoundService.getInstance();

    public SoundCommand() {
        setName(COMMAND_NAME);
    }

    @Override
    public Object doInBackground() {
            if (getName().equals("soundlist")) {
                setName(COMMAND_NAME);
                if(isEnabled() || isAdmin()){
                    list();
                }

            } else if (getName().equals("sound") && !getContent().isEmpty() && (isEnabled() || isAdmin())) {
                String soundname = getContent();
                play(soundname);
            }

        return true;
    }

    private void play(String soundname) {
        try {
            Dao<User, ?> senderDAO = DaoManager.createDao(db.getConnectionSource(), User.class);
            QueryBuilder<User, ?> queryBuilder2 = senderDAO.queryBuilder();
            queryBuilder2.orderBy("points", false).limit(50);
            PreparedQuery<User> preparedQuery2 = queryBuilder2.prepare();
            List<User>          topUsers       = senderDAO.query(preparedQuery2);
            Iterator            topUserIt      = topUsers.iterator();
            boolean             cantPlaySounds = true;
            while (topUserIt.hasNext()) {
                User user = (User) topUserIt.next();
                if ((user.getUsername().equals(getSender().getUsername()) && user.getPoints() > 10) || isAdmin() || isStreamer()) {
                    cantPlaySounds = false;
                    // User can play a sound
                    String[] soundlist    = sound.getSoundFiles();
                    String   soundName    = "";
                    boolean  soundFounded = false;
                    if (soundlist.length > 0) {
                        for (int i = 0; i < soundlist.length; i++) {
                            if (soundlist[i].startsWith(soundname)) {
                                soundName = soundlist[i];
                                soundFounded = true;
                                break;
                            }
                        }
                        if (soundFounded) {
                            String soundPath = config.getSoundPath();
                            soundPath = soundPath + File.separator + soundName;
                            logger.info(trans.getProp("sound.playing") + " " + soundName);
                            sound.play(soundPath, false);
                            user.setPoints(user.getPoints() - 10);
                            senderDAO.update(user);
                            sendWhisper(getEvent(), getSender().getUsername(), trans.getProp("sound.played"));
                        } else {
                            sendWhisper(getEvent(), getSender().getUsername(), trans.getProp("sound.notfound"));
                        }
                    } else {
                        sendMessage("sound", getEvent(), getSender().getUsername(), trans.getProp("sound.empty"));
                    }
                    break;
                }
            }
            if (cantPlaySounds) {
                sendMessage("sound", getEvent(), getSender().getUsername(), trans.getProp("sound.right"));
            }
        } catch (Exception exUser) {
            logger.warning(trans.getProp("err.sound.play"));
        }
    }

    private void list() {
        String[] sounds    = sound.getSoundFiles();
        String   soundsStr = "";

        for (int i = 0; i < sounds.length; i++) {
            sounds[i] = sounds[i].replaceAll("\\..+$", "");
            soundsStr += sounds[i] + " ";
        }
        if (sounds.length > 0) {
            sendMessage("sound", getEvent(), getSender().getUsername(), trans.getProp("sound.list") + " :" + soundsStr);
            //sendMessage(e, trans.getProp("sound.list") + " :" + soundsStr);
        } else {
            sendMessage("sound", getEvent(), getSender().getUsername(), trans.getProp("sound.empty") + " :" + soundsStr);
            //sendMessage(e, trans.getProp("sound.empty"));
        }
    }
}
