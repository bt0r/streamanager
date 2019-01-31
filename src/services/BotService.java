package services;

import chat.ChatPanel;
import chat.UserPanel;
import listeners.CommandListener;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

import java.nio.charset.Charset;
import java.util.logging.Logger;


public class BotService  {
    private static Logger        logger = Logger.getLogger("streaManager");
    private static ConfigService config = ConfigService.getInstance();
    private static ChatPanel chatbox;
    private static PircBotX  bot;
    private static UserPanel userPanel;
    // 199.9.253.119 --> Server IP for whisper

    /*
     * Constructeur privé
     */
    public BotService(ChatPanel chatbox, UserPanel userPanel){
        this.chatbox = chatbox;
        this.userPanel = userPanel;
        connection();
    }

    /*
     * Instance unique
     */
    private static BotService BotService = new BotService(chatbox,userPanel);



    private static class BotServiceHolder {
        private final static BotService BotService = new BotService(chatbox,userPanel);
    }

    /*
     * Points d'accés pour l'instance unique du singleton
     */
    public static BotService  getInstance(ChatPanel chatbox,UserPanel userPanel) {
        if (BotService == null) {
            synchronized (BotService.class){
                BotService = new BotService(chatbox,userPanel);
            }

        }
        return BotService.BotService;
    }

    public static void connection() {
        Configuration configBot = new Configuration.Builder()
                .setEncoding(Charset.forName("UTF-8"))
                .setAutoReconnect(true)
                .setName(config.getProp("bot.login"))
                .setLogin(config.getProp("bot.login"))
                .setAutoNickChange(true)
                .setServer("irc.twitch.tv", 6667)
                .setServerPassword(config.getProp("bot.password"))
                .addAutoJoinChannel("#matvanis")
                .addAutoJoinChannel("#jtv")
                .addListener(new CommandListener(chatbox,userPanel))
                .buildConfiguration();
        PircBotX bot = new PircBotX(configBot);
        try {
            setBot(bot);
            bot.startBot();

        } catch (Exception e) {
            logger.severe(e.getMessage());

        }
    }

    public static void setBot(PircBotX bot) {
        BotService.bot = bot;
    }

    public static PircBotX getBot(){
        return BotService.bot;
    }

}