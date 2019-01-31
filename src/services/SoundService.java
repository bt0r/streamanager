package services;

import javazoom.jl.player.Player;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SoundService {
    private Logger          logger = Logger.getLogger("streaManager");
    private LanguageService trans  = LanguageService.getInstance();
    private ConfigService   config = ConfigService.getInstance();

    /*
     * Constructeur privé
     */
    private SoundService() {

    }

    /*
     * Instance unique
     */
    private static SoundService SoundService = new SoundService();

    private static class SoundServiceHolder {
        private final static SoundService SoundService = new SoundService();
    }

    /*
     * Points d'accès pour l'instance unique du singleton
     */
    public static SoundService getInstance() {
        if (SoundService == null) {
            SoundService = new SoundService();
        }
        return SoundService.SoundService;
    }

    public synchronized void play(final String path, boolean isLocal) {

        try {
            InputStream fileB;
            if (isLocal) {
                fileB = this.getClass().getResourceAsStream(path);
            } else {
                fileB = new FileInputStream(path);

            }

            if(path.endsWith(".mp3") || path.endsWith(".MP3")){
                Player playMP3 = new Player(fileB);
                playMP3.play();

            }else{
                File             soundFile        = new File(path);
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile );
                //AudioStream as = new AudioStream(audioInputStream);
                //AudioPlayer.player.start(as);

                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();//This plays the audio
            }

           /* Media sound = new Media(path);
            MediaPlayer mp = new MediaPlayer(sound);
            mp.play();*/



        } catch (Exception exc) {
            exc.printStackTrace();
            logger.warning(trans.getProp("err.file.notfound") + ": " + path);
        }
    }

    public String[] getSoundFiles() {

        File   soundDir = new File(config.getSoundPath());
        File[] sounds   = soundDir.listFiles();

        List<String> soundsList = new ArrayList<String>();
        for (File sound : sounds) {
            String  soundName = sound.getName();
            Pattern p         = Pattern.compile("^(\\w+)\\.(\\w+)$");
            Matcher m         = p.matcher(soundName);
            if (m.matches() && (m.group(2).equals("mp3") || m.group(2).equals("MP3") || m.group(2).equals("wav") || m.group(2).equals("WAV"))) {
                soundsList.add(soundName);
            }


        }

        String[] simpleArray = new String[soundsList.size()];
        String[] soundsArray = soundsList.toArray(simpleArray);

        return soundsArray;
    }


}