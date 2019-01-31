package listeners;

import animations.LoadingAnimation;
import animations.TestObservable;
import animations.TestObserver;
import chat.ChatPanel;
import chat.UserPanel;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import commands.AbstractCommand;
import commands.Model.EightBallCommand;
import database.Command;
import database.User;
import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONException;
import org.pircbotx.Channel;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;
import services.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandListener extends ListenerAdapter {

    private ChatPanel chatbox;
    private LanguageService  trans     = LanguageService.getInstance();
    private LogService       chatLog   = LogService.getInstance();
    private ConfigService    config    = ConfigService.getInstance();
    private TwitchAPIService twitchAPI = TwitchAPIService.getInstance();
    private EventService     eventS    = EventService.getInstance();
    private DatabaseService  db        = DatabaseService.getInstance();
    private Logger           logger    = Logger.getLogger("streaManager");
    private UserPanel        userList  = null;


    public CommandListener(ChatPanel chatbox, UserPanel userList) {
        this.chatbox = chatbox;
        this.userList = userList;

    }

    @Override
    public void onMode(ModeEvent event) throws Exception {
        String mode      = event.getModeParsed().get(0);
        String recipient = event.getModeParsed().get(1);
        User   user      = db.findUser(recipient);
        try {
            Dao<User, ?> senderDAO = DaoManager.createDao(db.getConnectionSource(), User.class);
            if (mode.equals("+o")) {
                // User is modded
                user.setModerator(true);
            }
            if (mode.equals("-o")) {
                // User is unmodded
                user.setModerator(false);
            }
            senderDAO.update(user);
        } catch (Exception e) {
        }
    }

    @Override
    public void onUnknown(UnknownEvent e) throws Exception {
        // Trigger for all events
        String  line = e.getLine();
        Pattern p    = Pattern.compile(".+\\w+!(\\w+)@[\\w\\.]+\\s(\\w+)\\s(\\w+)\\s:(.+)");
        Matcher m    = p.matcher(line);
        System.out.println("LINE "+line);
        if (m.matches()) {
            switch (m.group(2)) {
                case "NOTICE":
                    System.out.println(m.group(0));
                    System.out.println(m.group(1));

                break;
                case "WHISPER":
                    System.out.println(m.group(0));
                    System.out.println(m.group(1));

                    break;
            }
        }

    }

    @Override
    public void onNotice(NoticeEvent e) throws Exception {
        //INFOS: @badges=moderator/1,subscriber/0;color=#DAA520;display-name=StreaManager;emotes=;id=6cc58bb2-1e0e-462b-a919-41e1b51a4ec7;login=streamanager;mod=1;msg-id=sub;msg-param-months=0;msg-param-sub-plan-name=Junior\s(\s4.99$\s);msg-param-sub-plan=1000;room-id=57651881;subscriber=1;system-msg=StreaManager\sjust\ssubscribed\swith\sa\s$4.99\ssub!;tmi-sent-ts=1498991844460;turbo=0;user-id=92211876;user-type=mod :tmi.twitch.tv USERNOTICE #bt0r
        /*Event                        rawEvent     = (Event) e;
        MessageEvent                 messageEvent = new MessageEvent(e.getBot(),e.getChannel(),e.getChannelSource(),e.getUserHostmask(),e.getUser(),e.getMessage(),e.);
        ImmutableMap<String, String> tags         = messageEvent.getTags();
        Boolean                      isSub        = tags.get("msg-id").equals("sub");
        long                         twitchId     = Long.parseLong(e.getTags().get("user-id"));
        User                         sender       = null;
        Dao<User, ?>                 senderDAO    = null;
        try {
            senderDAO = DaoManager.createDao(db.getConnectionSource(), User.class);
            // Find user by his ID or username
            sender = db.findUser(e.getUser().getNick(), twitchId);
            if (isSub) {
                int    months  = Integer.parseInt(tags.get("msg-param-months"));
                String subPlan = tags.get("msg-param-sub-plan-name");
                sender.setSubscriber(true);
            }
            String systemMessage = tags.get("system-msg");


        } catch (Exception err) {

        }
    */

    }

    @Override
    public void onConnect(ConnectEvent e) throws Exception {
       config.endTask("twitch_bot_connect", trans.getProp("chatTab.connected.bot"), LoadingAnimation.LEVEL_SUCCESS);
    }

    @Override
    public void onDisconnect(DisconnectEvent event) throws Exception {
        config.endTask("twitch_bot_connect", trans.getProp("chatTab.disconnected"), LoadingAnimation.LEVEL_WARNING);

    }

    @Override
    public void onJoin(JoinEvent e) throws Exception {
        userList.addUser(e.getUser().getNick());
        eventS.addConnect(e.getUser().getNick());
        // BOT JOIN
        if (! e.getUser().getNick().toLowerCase().equals(config.getProp("bot.login").toLowerCase()) && config.getProp("command.onJoin").equals("true")) {
            Map transMap = new HashMap<String, String>();
            transMap.put("user", e.getUser().getNick());
            String quitSTR = trans.replaceTrans(transMap, "chatTab.joined");
            e.getChannel().send().action(quitSTR);
        }

        TestObserver testObs = new TestObserver();

        TestObservable testObservable = new TestObservable();
        testObservable.setId(4);
        testObservable.addObserver(testObs);
        testObs.setId(5);
    }

    @Override
    public void onPart(PartEvent e) throws Exception {
        userList.removeUser(e.getUser().getNick());
        eventS.addDisconnect(e.getUser().getNick());
        if (! e.getUser().getNick().toLowerCase().equals(config.getProp("bot.login").toLowerCase()) && config.getProp("command.onPart").equals("true")) {
            Map transMap = new HashMap<>();
            transMap.put("user", e.getUser().getNick());
            String quitSTR = trans.replaceTrans(transMap, "chatTab.quit");
            e.getChannel().send().action(quitSTR);
        }


    }

    @Override
    public void onAction(ActionEvent e) throws Exception {
        User              user        = db.findUser(e.getUser().getNick().toLowerCase());

        addToChatbox(user.getUsername(), e.getMessage(), user.getPoints(), true);
    }

    @Override
    public void onMessage(MessageEvent e) throws JSONException {
        // RETRIEVE USER FROM DB
        User         sender    = null;
        Dao<User, ?> senderDAO = null;
        try {
            senderDAO = DaoManager.createDao(db.getConnectionSource(), User.class);
            String color    = e.getTags().get("color");
            String mod      = e.getTags().get("mod");
            String sub      = e.getTags().get("subscriber");
            String turbo    = e.getTags().get("turbo");
            String badges   = e.getTags().get("badges");
            long   twitchId = Long.parseLong(e.getTags().get("user-id"));
            // Find user by his ID or username
            sender = db.findUser(e.getUser().getNick(), twitchId);

            if (color != null) {
                sender.setColor(color);
            } else {
                sender.createColor();
            }
            if (mod != null) {
                sender.setModerator(mod);
            }
            if (sub != null) {
                sender.setSubscriber(sub);
            }
            if (turbo != null) {
                sender.setTurbo(turbo);
            }
            if (badges != null && ! badges.equals("")) {
                sender.setBadges(badges);
            }

            senderDAO.update(sender);
        } catch (SQLException e1) {
            logger.warning("Can't retrieve user from database");
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.warning("Error when trying to set user information (turbo/badge etc..)");
        }

        // SEARCH IF COMMAND EXIST
        try {
            String  commandNameRegex   = "^\\!(\\w+)(?:\\s(.+))?$";
            Pattern commandNamePattern = Pattern.compile(commandNameRegex);
            Matcher commandMatch       = commandNamePattern.matcher(e.getMessage());
            if (commandMatch.matches()) {
                String commandName    = commandMatch.group(1);
                String commandContent = commandMatch.group(2) == null ? "" : commandMatch.group(2);

                if (commandName.equals("8ball")) {
                    EightBallCommand eightBall = new EightBallCommand();
                    eightBall.setContent(commandContent);
                    eightBall.setEvent(e);
                    eightBall.setChatPanel(chatbox);
                    eightBall.doInBackground();

                } else {
                    // Command exist
                    Class<?>        abstractClass   = Class.forName("commands.Model." + WordUtils.capitalize(commandName) + "Command");
                    AbstractCommand abstractCommand = (AbstractCommand) abstractClass.newInstance();
                    abstractCommand.setContent(commandContent);
                    abstractCommand.setEvent(e);
                    abstractCommand.setChatPanel(chatbox);
                    abstractCommand.doInBackground();
                }

            }

        } catch (ClassNotFoundException e1) {
            // Command doesn't exist, try to find in database
            Dao<Command, ?> commandDAO;
            try {
                commandDAO = DaoManager.createDao(db.getConnectionSource(), Command.class);
                QueryBuilder           questQB       = commandDAO.queryBuilder();
                Where                  commandQB     = questQB.where().eq("isEnable", true);
                PreparedQuery<Command> preparedQuery = commandQB.prepare();
                List<Command>          commandList   = commandDAO.query(preparedQuery);
                Iterator               commandIt     = commandList.iterator();
                while (commandIt.hasNext()) {
                    Command command = (Command) commandIt.next();
                    if (e.getMessage().equals("!" + command.getName())) {
                        sendMessage(e, command.getMessage());
                        // TODO : Traiter le whisper sur les commandes personalisées
                    }
                }
            } catch (Exception err) {
                logger.severe("Command not found");
                sendMessage(e, trans.getProp("err.command.notfound"));
            }

        } catch (InstantiationException e1) {
            // Can't instantiate class
            logger.warning("Cannot instantiate class, error:" + e1.getMessage());
        } catch (IllegalAccessException e1) {
            // Can't call method
            logger.warning("Cannot call method, error:" + e1.getMessage());
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        /*
        * CULBUTE !CULBUTE

        else if (e.getMessage().startsWith("!culbute ") && config.getProp("command.culbute").equals("true")) {

            Pattern regexp = Pattern.compile("!culbute\\s(\\w+)");
            Matcher match  = regexp.matcher(e.getMessage());

            if (match.matches()) {
                String resSTR;
                int    penis  = Tools.penis(e.getUser().getNick(), e.getChannel().getName());
                int    vagin  = Tools.vagin(match.group(1));
                int    result = vagin - penis;
                Map    sexMap = new HashMap<String, String>();
                if (!match.group(1).equals(e.getUser().getNick())) {
                    if (result < 5 && result > 0) {
                        // Sex is normal
                        sexMap.put("user", e.getUser().getNick());
                        sexMap.put("user2", match.group(1));
                        resSTR = trans.replaceTrans(sexMap, "culbute.normal");

                    } else if (result <= 0) {
                        sexMap.put("user", e.getUser().getNick());
                        sexMap.put("user2", match.group(1));
                        resSTR = trans.replaceTrans(sexMap, "culbute.hard");

                    } else if (result >= 5 && result < 10) {
                        // Sex is boring
                        sexMap.put("user", e.getUser().getNick());
                        sexMap.put("user2", match.group(1));
                        resSTR = trans.replaceTrans(sexMap, "culbute.bad");
                    } else {
                        // Sex ?
                        sexMap.put("user", e.getUser().getNick());
                        sexMap.put("user2", match.group(1));
                        resSTR = trans.replaceTrans(sexMap, "culbute.impossible");
                    }
                } else {
                    sexMap.put("user", e.getUser().getNick());
                    resSTR = trans.replaceTrans(sexMap, "culbute.error");
                }


                sendMessage("culbute",e, resSTR);
            }


        }*/


        /*
         * PENIS !PENIS

        else if (config.getProp("command.penis").equals("true") && e.getMessage().equals("!penis")) {
            int    penisSize = Tools.penis(e.getUser().getNick(), e.getChannel().getName());
            String penis     = "";
            if (penisSize == 2) {
                penis = trans.getProp("penis.micro");
                penis = penis.replaceAll("\\.user\\.", e.getUser().getNick());
            } else if (penisSize == 1) {
                penis = trans.getProp("penis.nano");
                penis = penis.replaceAll("\\.user\\.", e.getUser().getNick());
            } else {
                penis = trans.getProp("penis.normal");
                penis = penis.replaceAll("\\.user\\.", e.getUser().getNick()).replaceAll("\\.size\\.", Integer.toString(penisSize));
            }
            sendMessage("penis",e, penis);
        }*/
        /*
         * VAGIN !VAGIN

        else if (config.getProp("command.vagin").equals("true")
                && e.getMessage().equals("!vagin")) {
            String vagin = trans.getProp("vagin");
            vagin = vagin.replaceAll("\\.user\\.", e.getUser().getNick()).replaceAll("\\.size\\.", Integer.toString(Tools.vagin(e.getUser().getNick())));

            sendMessage(e, vagin);
        }*/

         /*
         * ADD POINTS TO USER
         */
        try {

            // User ...
            sender = db.findUser(sender.getUsername());
            sender.setLastConn(new Date().getTime());
            db.addPointsToUser(sender, 1);
            senderDAO.update(sender);
        } catch (SQLException e1) {
            logger.warning("Can't update user " + sender.getUsername() + " in database");
        }


        String message = twitchAPI.replaceEmotes(e.getMessage(), e.getTags().get("emotes"));

        addToChatbox(e.getUser().getNick(), message, sender.getPoints(), false);

    }

    public void sendMessage(Event event, String message) {
        /*try {
            long timestamp = new Timestamp(new java.util.Date().getTime()).getTime();
            //long timestamp = new DateTime().getMillis();
            long cooldown = 0;
            if (config.getProp("cooldown") != null) {
                cooldown = Long.parseLong(config.getProp("cooldown"));
            }
            if (timestamp - cooldown > 5000) {
                event.getChannel().send().message(message + trans.getProp("command.cooldown"));
                long   ts = new Timestamp(new java.util.Date().getTime()).getTime();
                String cd = Long.toString(ts);
                config.setProp("cooldown", cd);
            }
        } finally {
            // Command has been sent

        }*/
        Iterator<Channel> channels = event.getBot().getUserBot().getChannels().iterator();
        while (channels.hasNext()) {
            Channel channel = channels.next();
            if (channel.getName().equals("#" + config.getProp("streamer.login"))) {
                channel.send().message(message);
            }
        }


        addToChatbox(config.getProp("bot.login"), message, 0, false);
    }

    public static void sendWhisper(Event e, String user, String message) {
        e.getBot().send().message("jtv", ".w " + user + " " + message);
    }


    public void addToChatbox(String user, String message, int points, boolean isAction) {
        // NOTIFICATION HIGHLIGHT
        if (message.contains(config.getProp("streamer.login").toLowerCase())
                && config.getProp("notification.state").equals("true")
                && ! message.startsWith("!")) {
        }
        String date     = new SimpleDateFormat(config.getProp("app.hourFormat")).format(new java.util.Date());
        String dateLong = new SimpleDateFormat(config.getProp("app.datetimeFormat")).format(new java.util.Date());

        /*DefaultTableModel model = (DefaultTableModel) chatbox.getModel();
        String            date  = new SimpleDateFormat(config.getProp("app.datetimeFormat")).format(new java.util.Date());

        Object[] row = {date, points, user, message};
        // APPLY CHAT HISTORY
        int maxRow = new Integer(config.getProp("chat.maxRow"));
        if (model.getRowCount() > maxRow) {
            int rowToDelete = model.getRowCount() - (maxRow + 1);
            model.removeRow(rowToDelete);
        }
        model.addRow(row);*/

        /*
        int charPixel   = 7;
        int heightRatio = 20;
        int res         = charPixel * message.length() / chatbox.getColumn(2).getWidth();

        if (res > 0) {
            chatbox.setRowHeight(model.getRowCount() - 1, heightRatio * res);
        } else {
            chatbox.setRowHeight(model.getRowCount() - 1, 20);
        }


        // SCROLL TO BOTTOM
        chatbox.scrollRectToVisible(new Rectangle(chatbox.getCellRect(model.getRowCount() - 1, 0, true)));
        chatbox.repaint();
        */

        chatLog.writeChatLog(dateLong, user, message);
        chatbox.addMessage(date, user, message, isAction);
    }


}
