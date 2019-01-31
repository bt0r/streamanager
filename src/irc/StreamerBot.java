package irc;

import chat.ChatPanel;
import listeners.StreamerListener;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.EnableCapHandler;
import services.ConfigService;

import java.nio.charset.Charset;

public class StreamerBot extends Thread {
    private ConfigService config = ConfigService.getInstance();
    private PircBotX  bot;
    private ChatPanel chatbox;

    public void run() {
        connect();
    }

    public StreamerBot(ChatPanel chatbox) {
        this.chatbox = chatbox;
    }

    public void connect() {

        Configuration configBot = new Configuration.Builder()
                .setEncoding(Charset.forName("UTF-8"))
                .setAutoReconnect(true)
                .setName(config.getProp("streamer.login"))
                .setLogin(config.getProp("streamer.login"))
                .setServer("irc.twitch.tv", 6667)
                .setOnJoinWhoEnabled(false)
                .addCapHandler(new EnableCapHandler("twitch.tv/membership"))
                .addCapHandler(new EnableCapHandler("twitch.tv/commands"))
                .addCapHandler(new EnableCapHandler("twitch.tv/tags"))
                .setServerPassword(config.getProp("streamer.password"))
                .addAutoJoinChannel("#" + config.getProp("streamer.login"))
                .addListener(new StreamerListener(chatbox))
                .buildConfiguration();


        // Create our bot with the configuration
        PircBotX bot = new PircBotX(configBot);

        // Connect to the server
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
