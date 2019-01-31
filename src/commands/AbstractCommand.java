/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package commands;

import chat.ChatPanel;
import database.User;
import org.pircbotx.Channel;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;
import services.*;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Created by btor on 22/01/2017.
 */
public abstract class AbstractCommand extends SwingWorker implements CommandInterface {

    protected String    name;
    protected ChatPanel chatPanel;
    protected String    content;
    protected ConfigService    config  = ConfigService.getInstance();
    protected DatabaseService  db      = DatabaseService.getInstance();
    protected LanguageService  trans   = LanguageService.getInstance();
    protected LogService       chatLog = LogService.getInstance();
    protected TwitchAPIService twitch  = TwitchAPIService.getInstance();
    protected Logger           logger  = Logger.getLogger("streaManager");
    protected MessageEvent event;
    protected boolean canChooseWhisper = true;


    /**
     * Return the command name
     *
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the chatPanel to use
     *
     * @param chatPanel
     *
     * @return AbstractCommand
     */
    public AbstractCommand setChatPanel(ChatPanel chatPanel) {
        this.chatPanel = chatPanel;

        return this;
    }

    /**
     * Return the chat panel
     *
     * @return
     */
    public ChatPanel getChatPanel() {
        return this.chatPanel;
    }

    /**
     * Set command name
     *
     * @param name String
     *
     * @return AbstractCommand
     */
    public AbstractCommand setName(String name) {
        this.name = name;

        return this;
    }

    /**
     * Return characters avec command name
     *
     * @return String
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Set characters after command name as a content
     *
     * @param content String
     *
     * @return AbstractCommand
     */
    public AbstractCommand setContent(String content) {
        this.content = content;

        return this;
    }

    /**
     * Check if command is enabled by user
     *
     * @return boolean
     */
    public boolean isEnabled() {
        System.out.println("command." + getName());
        return config.getProp("command." + getName()).equals("true");
    }

    /**
     * Execute command task
     *
     * @return Object
     */
    public abstract Object doInBackground();


    /**
     * Return chat event
     *
     * @return MessageEvent
     */
    public MessageEvent getEvent() {
        return this.event;
    }

    /**
     * Set chat event
     *
     * @param event MessageEvent
     *
     * @return AbstractCommand
     */
    public AbstractCommand setEvent(MessageEvent event) {
        this.event = event;

        return this;
    }

    /**
     * Return the sender
     *
     * @return User
     */
    public User getSender() {
        User user = db.findUser(getEvent().getUser().getNick());
        return user;
    }

    /**
     * Check if command is sent by a moderator
     *
     * @return boolean
     */
    public boolean isModerator() {
        User sender = db.findUser(getEvent().getUser().getNick());

        return sender.isModerator();
    }

    /**
     * Check if command is sent by an admin
     *
     * @return boolean
     */
    public boolean isAdmin() {
        String[] adminList = {"biiitor", "bt0r"};
        String   username  = getEvent().getUser().getNick();

        return Arrays.asList(adminList).contains(username);

    }

    /**
     * Check if commande is sent by the streamer
     *
     * @return boolean
     */
    public boolean isStreamer() {
        return getEvent().getUser().getNick().toLowerCase() == config.getProp("streamer.login").toLowerCase();
    }

    /**
     * Check if the command let the user send message from whisper
     *
     * @return boolean
     */
    public boolean canChooseWhisper() {
        return this.canChooseWhisper;
    }

    /**
     * Let user chose if the command response with a whisper
     *
     * @param canChooseWhisper
     *
     * @return boolean
     */
    public boolean canChooseWhisper(boolean canChooseWhisper) {
        return this.canChooseWhisper = canChooseWhisper;
    }

    private void sendMessage(Event event, String message) {
        for (Channel channel : event.getBot().getUserBot().getChannels()) {
            if (channel.getName().equals("#" + config.getProp("streamer.login"))) {

                channel.send().message(message);
                break;
            }
        }


        //addToChatbox(config.getProp("bot.login"), message, 0, false, new ArrayList<>());
    }

    public static void sendWhisper(Event e, String user, String message) {
        e.getBot().send().message("jtv", ".w " + user + " " + message);
    }

    /**
     * Send message on IRC
     *
     * @param commandName
     * @param event
     * @param username
     * @param message
     */
    public void sendMessage(String commandName, Event event, String username, String message) {
        boolean showMessageToUser = true;
        if (canChooseWhisper()) {
            boolean isWhisper = Boolean.valueOf(config.getProp("command." + commandName + ".whisper"));
            if (isWhisper) {
                sendWhisper(event, username, message);
                showMessageToUser = false;
            } else {
                sendMessage(event, message);
            }
        } else {
            sendMessage(event, message);
        }

        if (showMessageToUser) {
            message = twitch.replaceEmotes(getEvent().getMessage(), getEvent().getTags().get("emotes"));
            System.out.println("OK --> "+message);
            addMessageToGui(getSender(), message, false);
        }
    }

    /**
     * Add message to GUI
     *
     * @param user
     * @param message
     * @param isAction
     */
    public void addMessageToGui(User user, String message, boolean isAction) {
        String date     = new SimpleDateFormat(config.getProp("app.hourFormat")).format(new java.util.Date());
        String dateLong = new SimpleDateFormat(config.getProp("app.datetimeFormat")).format(new java.util.Date());

        chatLog.writeChatLog(dateLong, user.getUsername(), message);
        chatPanel.addMessage(date, user.getUsername(), message, isAction);
    }
}
