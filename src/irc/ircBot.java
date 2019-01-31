package irc;

import chat.ChatPanel;
import chat.UserPanel;
import listeners.CommandListener;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.EnableCapHandler;
import services.ConfigService;

import java.nio.charset.Charset;

public class ircBot extends Thread {
    private ConfigService config = ConfigService.getInstance();
    private ChatPanel chatbox;
    private PircBotX  bot;
    private UserPanel userPanel;

    public void run() {
        connect();
    }

    public ircBot(ChatPanel chatbox, UserPanel userPanel) {
        this.chatbox = chatbox;
        this.userPanel = userPanel;
    }

    public void connect() {

        // 199.9.253.119 --> Server IP for whisper
        Configuration configBot = new Configuration.Builder()
                .setEncoding(Charset.forName("UTF-8"))
                .setAutoReconnect(true)
                .setName(config.getProp("bot.login"))
                .setLogin(config.getProp("bot.login"))
                .setServer("irc.twitch.tv", 6667)
                .setServerPassword(config.getProp("bot.password"))
                .setOnJoinWhoEnabled(false)
                .addCapHandler(new EnableCapHandler("twitch.tv/membership"))
                .addCapHandler(new EnableCapHandler("twitch.tv/commands"))
                .addCapHandler(new EnableCapHandler("twitch.tv/tags"))
                .addAutoJoinChannel("#" + config.getProp("streamer.login"))
                .addListener(new CommandListener(chatbox,userPanel))
                .buildConfiguration();


        // Create our bot with the configuration
        PircBotX bot = new PircBotX(configBot);
        try {
            setBot(bot);
            bot.startBot();
        } catch (Exception e) {
            e.printStackTrace();

            this.bot = bot;

        }

    }

    public PircBotX getBot() {
        return this.bot;
    }

    public void setBot(PircBotX bot) {
        this.bot = bot;
    }
}
