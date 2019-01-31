package commands;

import listeners.CommandStateListener;
import main.component.border.CommonTitleBorder;
import main.component.jpanel.CommandStatePanel;
import main.component.jpanel.CommonPanel;
import services.ConfigService;
import services.LanguageService;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by btor on 24/08/2016.
 */
public class CommandsStatePanel extends CommonPanel {
    private ConfigService   config = ConfigService.getInstance();
    private LanguageService trans  = LanguageService.getInstance();


    public CommandsStatePanel() {
        // PANEL SETTINGS
        setLayout(new GridLayout(2, 2));


        /*
          STREAM
         */
        Border      streamBorder = new CommonTitleBorder(trans.getProp("command.title.stream"));
        CommonPanel streamPanel  = new CommonPanel();
        streamPanel.setBorder(streamBorder);
        streamPanel.setLayout(new BoxLayout(streamPanel, BoxLayout.Y_AXIS));

        // ON JOIN
        CommandStatePanel onJoinPanel = new CommandStatePanel(trans.getProp("command.state.onJoin"));
        onJoinPanel.setState(config.getProp("command.onJoin"));
        onJoinPanel.setWhisper(false);
        onJoinPanel.setForceWhisper(true);

        // FOLLOWERS
        CommandStatePanel followersPanel = new CommandStatePanel(trans.getProp("command.state.followers"));
        followersPanel.setWhisper(config.getProp("command.followers.whisper"));
        followersPanel.setForceWhisper(false);
        followersPanel.setState(config.getProp("command.followers"));

        // STREAMINFO
        CommandStatePanel streaminfoPanel = new CommandStatePanel(trans.getProp("command.state.streaminfo"));
        streaminfoPanel.setWhisper(config.getProp("command.streaminfo.whisper"));
        streaminfoPanel.setForceWhisper(false);
        streaminfoPanel.setState(config.getProp("command.streaminfo"));


        // QUESTION / VOTE
        CommandStatePanel questionPanel = new CommandStatePanel(trans.getProp("command.state.question"));
        questionPanel.setWhisper(false);
        questionPanel.setForceWhisper(true);
        questionPanel.setState(config.getProp("command.question"));

        streamPanel.add(onJoinPanel);
        streamPanel.add(followersPanel);
        streamPanel.add(streaminfoPanel);
        streamPanel.add(questionPanel);

        /*
            GAMES
         */
        Border      gameBorder = new CommonTitleBorder(trans.getProp("command.title.game"));
        CommonPanel gamePanel  = new CommonPanel();
        gamePanel.setBorder(gameBorder);
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));

        // 8BALL
        CommandStatePanel ballPanel = new CommandStatePanel(trans.getProp("command.state.8ball"));
        ballPanel.setWhisper(config.getProp("command.8ball.whisper"));
        ballPanel.setForceWhisper(false);
        ballPanel.setState(config.getProp("command.8ball"));

        // ROULETTE
        CommandStatePanel roulettePanel = new CommandStatePanel(trans.getProp("command.state.roulette"));
        roulettePanel.setWhisper(config.getProp("command.roulette.whisper"));
        roulettePanel.setForceWhisper(false);
        roulettePanel.setState(config.getProp("command.roulette"));

        // HOROSCOPE
        CommandStatePanel horoscopePanel = new CommandStatePanel(trans.getProp("command.state.horoscope"));
        horoscopePanel.setWhisper(true);
        horoscopePanel.setForceWhisper(true);
        horoscopePanel.setState(config.getProp("command.horoscope"));


        // LOVECHECK
        CommandStatePanel lovecheckPanel = new CommandStatePanel(trans.getProp("command.state.lovecheck"));
        lovecheckPanel.setWhisper(config.getProp("command.lovecheck.whisper"));
        lovecheckPanel.setForceWhisper(false);
        lovecheckPanel.setState(config.getProp("command.lovecheck"));

        // CHUCK
        CommandStatePanel chuckPanel = new CommandStatePanel(trans.getProp("command.state.chuck"));
        chuckPanel.setWhisper(config.getProp("command.chuck.whisper"));
        chuckPanel.setForceWhisper(false);
        chuckPanel.setState(config.getProp("command.chuck"));

        // SHIFUMI
        CommandStatePanel shifumiPanel = new CommandStatePanel(trans.getProp("command.state.shifumi"));
        shifumiPanel.setWhisper(config.getProp("command.shifumi.whisper"));
        shifumiPanel.setForceWhisper(false);
        shifumiPanel.setState(config.getProp("command.shifumi"));


        gamePanel.add(ballPanel);
        gamePanel.add(roulettePanel);
        gamePanel.add(horoscopePanel);
        gamePanel.add(lovecheckPanel);
        gamePanel.add(chuckPanel);
        gamePanel.add(shifumiPanel);

        /*
            USER
         */
        Border      userBorder = new CommonTitleBorder(trans.getProp("command.title.user"));
        CommonPanel userPanel  = new CommonPanel();
        userPanel.setBorder(userBorder);
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));


        // FC
        CommandStatePanel fcPanel = new CommandStatePanel(trans.getProp("command.state.fc"));
        fcPanel.setWhisper(config.getProp("command.fc.whisper"));
        fcPanel.setForceWhisper(false);
        fcPanel.setState(config.getProp("command.fc"));

        // USER INFO
        CommandStatePanel userinfoPanel = new CommandStatePanel(trans.getProp("command.state.userinfo"));
        userinfoPanel.setWhisper(config.getProp("command.userinfo.whisper"));
        userinfoPanel.setForceWhisper(false);
        userinfoPanel.setState(config.getProp("command.userinfo"));

        // POINTS / RANK
        CommandStatePanel pointsPanel = new CommandStatePanel(trans.getProp("command.state.points"));
        pointsPanel.setWhisper(config.getProp("command.points.whisper"));
        pointsPanel.setForceWhisper(false);
        pointsPanel.setState(config.getProp("command.points"));


        // TOP
        CommandStatePanel topPanel = new CommandStatePanel(trans.getProp("command.state.top"));
        topPanel.setWhisper(config.getProp("command.top.whisper"));
        topPanel.setForceWhisper(false);
        topPanel.setState(config.getProp("command.top"));


        userPanel.add(fcPanel);
        userPanel.add(userinfoPanel);
        userPanel.add(pointsPanel);
        userPanel.add(topPanel);

        /*
            EXTRA
         */
        Border      extraBorder = new CommonTitleBorder(trans.getProp("command.title.extra"));
        CommonPanel extraPanel  = new CommonPanel();
        extraPanel.setBorder(extraBorder);
        extraPanel.setLayout(new BoxLayout(extraPanel, BoxLayout.Y_AXIS));

        // SOUND / SOUNDLIST
        CommandStatePanel soundPanel = new CommandStatePanel(trans.getProp("command.state.sound"));
        soundPanel.setWhisper(config.getProp("command.sound.whisper"));
        soundPanel.setForceWhisper(false);
        soundPanel.setState(config.getProp("command.sound"));


        // POKE / INVITE
        CommandStatePanel pokePanel = new CommandStatePanel(trans.getProp("command.state.poke"));
        pokePanel.setWhisper(true);
        pokePanel.setForceWhisper(true);
        pokePanel.setState(config.getProp("command.poke"));


        extraPanel.add(soundPanel);
        extraPanel.add(pokePanel);
        extraPanel.add(new CommonPanel());
        extraPanel.add(new CommonPanel());

        add(streamPanel);
        add(userPanel);
        add(gamePanel);
        add(extraPanel);

        // LISTENERS
        // ComboBox
        fcPanel.getComboBox().addActionListener(new CommandStateListener(fcPanel.getComboBox(), fcPanel.getWhisperBox(), "fc"));
        followersPanel.getComboBox().addActionListener(new CommandStateListener(followersPanel.getComboBox(), followersPanel.getWhisperBox(), "followers"));
        streaminfoPanel.getComboBox().addActionListener(new CommandStateListener(streaminfoPanel.getComboBox(), streaminfoPanel.getWhisperBox(), "streaminfo"));
        questionPanel.getComboBox().addActionListener(new CommandStateListener(questionPanel.getComboBox(), questionPanel.getWhisperBox(), "question"));
        ballPanel.getComboBox().addActionListener(new CommandStateListener(ballPanel.getComboBox(), ballPanel.getWhisperBox(), "8ball"));
        roulettePanel.getComboBox().addActionListener(new CommandStateListener(roulettePanel.getComboBox(), roulettePanel.getWhisperBox(), "roulette"));
        horoscopePanel.getComboBox().addActionListener(new CommandStateListener(horoscopePanel.getComboBox(), horoscopePanel.getWhisperBox(), "horoscope"));
        lovecheckPanel.getComboBox().addActionListener(new CommandStateListener(lovecheckPanel.getComboBox(), lovecheckPanel.getWhisperBox(), "lovecheck"));
        chuckPanel.getComboBox().addActionListener(new CommandStateListener(chuckPanel.getComboBox(), chuckPanel.getWhisperBox(), "chuck"));
        shifumiPanel.getComboBox().addActionListener(new CommandStateListener(shifumiPanel.getComboBox(), shifumiPanel.getWhisperBox(), "shifumi"));
        pointsPanel.getComboBox().addActionListener(new CommandStateListener(pointsPanel.getComboBox(), pointsPanel.getWhisperBox(), "points"));
        topPanel.getComboBox().addActionListener(new CommandStateListener(topPanel.getComboBox(), topPanel.getWhisperBox(), "top"));
        soundPanel.getComboBox().addActionListener(new CommandStateListener(soundPanel.getComboBox(), soundPanel.getWhisperBox(), "sound"));
        pokePanel.getComboBox().addActionListener(new CommandStateListener(pokePanel.getComboBox(), pokePanel.getWhisperBox(), "poke"));
        onJoinPanel.getComboBox().addActionListener(new CommandStateListener(onJoinPanel.getComboBox(), onJoinPanel.getWhisperBox(), "onJoin"));
        userinfoPanel.getComboBox().addActionListener(new CommandStateListener(userinfoPanel.getComboBox(), userinfoPanel.getWhisperBox(), "userinfo"));


        // WhisperBox
        fcPanel.getWhisperBox().addActionListener(new CommandStateListener(fcPanel.getComboBox(), fcPanel.getWhisperBox(), "fc"));
        followersPanel.getWhisperBox().addActionListener(new CommandStateListener(followersPanel.getComboBox(), followersPanel.getWhisperBox(), "followers"));
        streaminfoPanel.getWhisperBox().addActionListener(new CommandStateListener(streaminfoPanel.getComboBox(), streaminfoPanel.getWhisperBox(), "streaminfo"));
        questionPanel.getWhisperBox().addActionListener(new CommandStateListener(questionPanel.getComboBox(), questionPanel.getWhisperBox(), "question"));
        ballPanel.getWhisperBox().addActionListener(new CommandStateListener(ballPanel.getComboBox(), ballPanel.getWhisperBox(), "8ball"));
        roulettePanel.getWhisperBox().addActionListener(new CommandStateListener(roulettePanel.getComboBox(), roulettePanel.getWhisperBox(), "roulette"));
        horoscopePanel.getWhisperBox().addActionListener(new CommandStateListener(horoscopePanel.getComboBox(), horoscopePanel.getWhisperBox(), "horoscope"));
        lovecheckPanel.getWhisperBox().addActionListener(new CommandStateListener(lovecheckPanel.getComboBox(), lovecheckPanel.getWhisperBox(), "lovecheck"));
        chuckPanel.getWhisperBox().addActionListener(new CommandStateListener(chuckPanel.getComboBox(), chuckPanel.getWhisperBox(), "chuck"));
        shifumiPanel.getComboBox().addActionListener(new CommandStateListener(shifumiPanel.getComboBox(), shifumiPanel.getWhisperBox(), "shifumi"));
        pointsPanel.getWhisperBox().addActionListener(new CommandStateListener(pointsPanel.getComboBox(), pointsPanel.getWhisperBox(), "points"));
        topPanel.getWhisperBox().addActionListener(new CommandStateListener(topPanel.getComboBox(), topPanel.getWhisperBox(), "top"));
        soundPanel.getWhisperBox().addActionListener(new CommandStateListener(soundPanel.getComboBox(), soundPanel.getWhisperBox(), "sound"));
        pokePanel.getWhisperBox().addActionListener(new CommandStateListener(pokePanel.getComboBox(), pokePanel.getWhisperBox(), "poke"));
        onJoinPanel.getWhisperBox().addActionListener(new CommandStateListener(onJoinPanel.getComboBox(), onJoinPanel.getWhisperBox(), "onJoin"));
        userinfoPanel.getWhisperBox().addActionListener(new CommandStateListener(userinfoPanel.getComboBox(), userinfoPanel.getWhisperBox(), "userinfo"));

        setVisible(true);
    }
}
