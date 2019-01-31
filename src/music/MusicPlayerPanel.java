package music;

import services.ConfigService;
import services.LanguageService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MusicPlayerPanel extends JPanel implements ActionListener {
    LanguageService trans  = LanguageService.getInstance();
    ConfigService   config = ConfigService.getInstance();
    FlowLayout layout;

    JLabel    songName  = new JLabel(trans.getProp("musicTab.songName"));
    JButton   addSong   = new JButton();
    JButton   playSong  = new JButton();
    JButton   stopSong  = new JButton();
    JButton   hotkey    = new JButton();
    ImageIcon addIcon   = new ImageIcon(getClass().getResource(config.getProp("addIcon")));
    ImageIcon editIcon  = new ImageIcon(getClass().getResource(config.getProp("editIcon")));
    ImageIcon keyIcon   = new ImageIcon(getClass().getResource(config.getProp("key2Icon")));
    ImageIcon stopIcon  = new ImageIcon(getClass().getResource(config.getProp("stopIcon")));
    ImageIcon pauseIcon = new ImageIcon(getClass().getResource(config.getProp("pauseIcon")));
    ImageIcon playIcon  = new ImageIcon(getClass().getResource(config.getProp("playIcon")));


    MusicPlayerPanel() {
        layout = new FlowLayout(FlowLayout.LEFT, 0, 20);
        addSong.setIcon(addIcon);
        playSong.setIcon(playIcon);
        playSong.addActionListener(this);
        stopSong.setIcon(stopIcon);
        hotkey.setIcon(keyIcon);

        setLayout(layout);
        add(songName);
        add(playSong);
        add(stopSong);
        add(hotkey);
        add(addSong);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // Button is clicked
        try {
            //Tools.playSound("C:\\Users\\XooVooX\\Downloads\\dqzdqzdqzd qzd qz dq.mp3",false);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }


}




