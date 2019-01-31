package listeners;


import animations.LoadingAnimation;
import chat.ChatPanel;
import database.User;
import org.pircbotx.Channel;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.events.UnknownEvent;
import services.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StreamerListener extends ListenerAdapter {
    private ConfigService   config  = ConfigService.getInstance();
    private LanguageService trans   = LanguageService.getInstance();
    private EventService    eventS  = EventService.getInstance();
    private LogService      chatLog = LogService.getInstance();
    private DatabaseService db      = DatabaseService.getInstance();

    private ChatPanel chatbox;

    public StreamerListener(ChatPanel chatbox) {
        this.chatbox = chatbox;
    }


    @Override
    public void onUnknown(UnknownEvent e) throws Exception {

        String  line = e.getLine();
        Pattern p    = Pattern.compile(".+\\w+!(\\w+)@[\\w\\.]+\\s(\\w+)\\s(\\w+)\\s:(.+)");
        Matcher m    = p.matcher(line);
        if (m.matches()) {
            switch (m.group(2)) {
                case "WHISPER":
                    String username = m.group(1);
                    String message = m.group(4);
                    User sender = db.findUser(username);
                    addToChatbox(username, message, sender.getPoints(), true);
                    break;
            }
        }

    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent e) throws Exception {
        if (e.getMessage().contains("host")) {
            eventS.addHost(e.getUser().getNick(), e.getMessage());
        }

    }

    @Override
    public void onConnect(ConnectEvent e) throws Exception {
        config.endTask("twitch_streamer_connect",trans.getProp("chatTab.connected.streamer"), LoadingAnimation.LEVEL_SUCCESS);

    }

    @Override
    public void onDisconnect(DisconnectEvent event) throws Exception {
        super.onDisconnect(event);
        config.endTask("twitch_streamer_connect",trans.getProp("chatTab.disconnected.streamer"), LoadingAnimation.LEVEL_WARNING);

    }

    public void sendMessage(Event event, String message) {
        Iterator<Channel> channels = event.getBot().getUserBot().getChannels().iterator();
        while (channels.hasNext()) {
            Channel channel = channels.next();
            if (channel.getName().equals("#" + config.getProp("streamer.login"))) {
                channel.send().message(message);
            }
        }


    }

    public void addToChatbox(String user, String message, int points, boolean isWhisper) {
        String date = new SimpleDateFormat(config.getProp("app.hourFormat")).format(new java.util.Date());
        chatLog.writeChatLog(date, user, message);
        chatbox.addWhisper(date, user, message);
    }
}
