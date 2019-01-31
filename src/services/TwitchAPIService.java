package services;

import Tools.Tools;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.vdurmont.emoji.Emoji;
import database.Event;
import database.User;
import main.component.SM_EmojiParser;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.logging.Logger;

public class TwitchAPIService {
    private Logger          logger                = Logger.getLogger("streaManager");
    private LanguageService trans                 = LanguageService.getInstance();
    private ConfigService   config                = ConfigService.getInstance();
    private String          CLIENT_ID             = "pw735xhre03i0otmsehbzqulyfsuize";
    private boolean         isAuthentified        = false;
    private String          WEBSITE               = "http://streamanager.net/user/home";
    private String          AUTH_URL              = "http://streamanager.net/bot/";
    private DatabaseService db                    = DatabaseService.getInstance();
    private Properties      emoteProperties       = new Properties();
    private ArrayList       emotesBTTVList        = new ArrayList();
    private String          emoticonDirectoryPath = config.getConfigSysDir() + File.separator + "emoticons";


    /*
     * Constructeur privé
     */
    private TwitchAPIService() {
        initEmotes();
    }

    /*
     * Instance unique
     */
    private static TwitchAPIService TwitchAPIService = new TwitchAPIService();

    private static class TwitchAPIServiceHolder {
        private final static TwitchAPIService TwitchAPIService = new TwitchAPIService();
    }

    /*
     * Points d'accès pour l'instance unique du singleton
     */
    public static TwitchAPIService getInstance() {
        if (TwitchAPIService == null) {
            TwitchAPIService = new TwitchAPIService();
        }
        return TwitchAPIService.TwitchAPIService;
    }

    private void initEmotes() {
        File emotesDirectory = new File(emoticonDirectoryPath);
        if (! emotesDirectory.exists()) {
            try {
                emotesDirectory.mkdir();
            } catch (Exception e) {
                logger.severe("Can't create emotes directory, error:" + e.getMessage());
            }

        }
    }

    /*
     * COMMANDS
     */
    public JSONObject getChatters(String user) {
        // Get chatters on a stream
        String     url      = "http://tmi.twitch.tv/group/user/" + user.toLowerCase() + "/chatters";
        JSONObject chatters = getJsonObjectFromUrl(url);
        return chatters;
    }


    public long getLastFollow(String user, String channel) {
        channel = channel.toLowerCase();
        user = user.toLowerCase();
        // Get last follow of a user on a channel
        String     url        = "https://api.twitch.tv/kraken/users/" + user + "/follows/channels/" + channel;
        long       endDate    = 0;
        JSONObject jsonResult = null;
        try {
            jsonResult = getJsonObjectFromUrl(url);
            endDate = Tools.dateToTimestamp(jsonResult.getString("created_at"));
        } catch (Exception e) {
            //endDate = trans.getProp("");
            e.getMessage();
            e.printStackTrace();
        }

        return endDate;
    }

    public String getTotalFollowers(String channel) throws JSONException {
        channel = channel.toLowerCase();
        // Get last follow of a user on a channel
        JSONObject jsonResult = getJsonObjectFromUrl("https://api.twitch.tv/kraken/channels/" + channel + "/follows");
        Integer    total      = jsonResult.getInt("_total");

        return total.toString();
    }

    public String getTotalFollow(String channel) throws JSONException {
        channel = channel.toLowerCase();
        // Get last follow of a user on a channel
        JSONObject jsonResult = getJsonObjectFromUrl("https://api.twitch.tv/kraken/users/" + channel + "/follows/channels");
        Integer    total      = jsonResult.getInt("_total");

        return total.toString();
    }

    public ArrayList<Event> getLatestFollowers(String channel) throws JSONException {
        channel = channel.toLowerCase();
        JSONObject       jsonResult = getJsonObjectFromUrl("https://api.twitch.tv/kraken/channels/" + channel + "/follows");
        JSONArray        jsonArray  = jsonResult.getJSONArray("follows");
        ArrayList<Event> result     = new ArrayList<Event>();

        // BROWSE TWITCH FOLLOWS
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject userAllInfos = jsonArray.getJSONObject(i);
            JSONObject userInfos    = userAllInfos.getJSONObject("user");
            long       fc           = Tools.dateToTimestamp(userAllInfos.getString("created_at"));


            try {
                // CREATE/UPDATE USER
                Dao<User, String> userDAO = DaoManager.createDao(db.getConnectionSource(), User.class);
                User              user    = db.findUser(userInfos.getString("name"));

                user.setFollowDate(fc);
                userDAO.update(user);

                // CHECK EVENTS ON DB
                Dao<Event, String>          eventDAO = DaoManager.createDao(db.getConnectionSource(), Event.class);
                QueryBuilder<Event, String> eventQB  = eventDAO.queryBuilder();
                eventQB.orderBy("date", false).where().eq("user_id", user.getId()).and().eq("type", "follow");
                PreparedQuery<Event> eventPQ        = eventQB.prepare();
                List<Event>          eventDBResults = eventDAO.query(eventPQ);
                Iterator<Event>      eventDBIT      = eventDBResults.iterator();
                long                 actualDate     = System.currentTimeMillis();
                boolean              isNew          = true;

                // EVENTS FOUND IN DB
                while (eventDBIT.hasNext()) {
                    Event eventDB = eventDBIT.next();
                    if (fc == eventDB.getDate() || fc < (actualDate - 86400000)) {
                        // EVENT IS OLDER THAN 1 DAY -> FORGOT IT
                        isNew = false;
                        //logger.info("User " + user.getUsername() + " already follow.");
                        break;
                    }
                }
                // CREATE EVENT
                if (isNew) {
                    Event event = new Event();
                    event.setType("follow");
                    event.setUser(user);
                    event.setDate(fc);
                    eventDAO.create(event);
                    result.add(event);
                    db.addPointsToUser(user, 100);
                }


            } catch (Exception e) {
                e.printStackTrace();
                logger.warning("Can't create event");
            }

        }


        return result;
    }

    public JSONObject getUserInfo(String user) throws JSONException {
        user = user.toLowerCase();
        // Get user informations
        JSONObject jsonResult = getJsonObjectFromUrl("https://api.twitch.tv/kraken/users/" + user);
        return jsonResult;
    }

    public Map<String, String> getStreamInfo(String channel) throws JSONException {
        channel = channel.toLowerCase();
        // Get Stream information (only if streamer is online)
        JSONObject          jsonResult = getJsonObjectFromUrl("https://api.twitch.tv/kraken/streams/" + channel);
        Map<String, String> streamInfo = new HashMap<String, String>();

        if (! jsonResult.isNull("stream")) {
            JSONObject stream        = jsonResult.getJSONObject("stream");
            JSONObject channelObject = stream.getJSONObject("channel");
            streamInfo.put("game", stream.getString("game"));
            streamInfo.put("viewers", Integer.toString(stream.getInt("viewers")));
            streamInfo.put("date", stream.getString("created_at"));
            streamInfo.put("title", channelObject.getString("status"));
            streamInfo.put("status", "online");
            streamInfo.put("follow", Integer.toString(channelObject.getInt("followers")));
            //streamInfo.put("partner", channelObject.getBoolean("partner"));
            //streamInfo.put("mature", channelObject.getString("mature"));
        } else {
            streamInfo.put("game", "");
            streamInfo.put("viewers", "0");
            streamInfo.put("date", "0");
            streamInfo.put("title", "");
            streamInfo.put("status", "offline");
            streamInfo.put("follow", "0");
        }
        return streamInfo;
    }

    public void startCommercial(long channelId) {
        // TODO : https://dev.twitch.tv/docs/v5/reference/channels/#start-channel-commercial

    }

    public void updatechannel() {
        // TODO : https://dev.twitch.tv/docs/v5/reference/channels/#update-channel

    }

    public void getSubscribers(long channelId) {
        // https://dev.twitch.tv/docs/v5/reference/channels/#get-channel-subscribers
        JSONObject          response    = getJsonObjectFromUrl("https://api.twitch.tv/kraken/channels/" + channelId + "/subscriptions");
        Map<String, String> subscribers = new HashMap<String, String>();
        try {
            JSONArray subscribersArray = response.getJSONArray("subscriptions");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList getBTTVEmotesList() {
        return this.emotesBTTVList;
    }

    public void setBTTVEmotesList() throws JSONException {
        try {
            String filePath  = config.getConfigSysDir() + File.separator + "emoticonsBTTV.json";
            File   emoteFile = new File(filePath);
            if (emoteFile.exists()) {
                InputStream in           = new FileInputStream(filePath);
                String      JSON         = IOUtils.toString(in);
                JSONObject  emotesObject = new JSONObject(JSON);
                JSONArray   emotesArray  = emotesObject.getJSONArray("emotes");

                for (int i = 0; i < emotesArray.length(); i++) {
                    JSONObject emoteObj = emotesArray.getJSONObject(i);
                    emotesBTTVList.add(emoteObj.get("code"));
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBTTVEmoteURL(String emoteSTR) {
        String emoticonFilePath = config.getConfigSysDir() + File.separator + "emoticonsBTTV.json";
        String cachePath        = null;
        try {
            InputStream emoteIN      = new FileInputStream(emoticonFilePath);
            String      JSON         = IOUtils.toString(emoteIN);
            JSONObject  emotesObject = new JSONObject(JSON);
            String      urlTemplate  = emotesObject.getString("urlTemplate");

            JSONArray emotesArray = emotesObject.getJSONArray("emotes");
            for (int i = 0; i < emotesArray.length(); i++) {
                JSONObject emoteObject = emotesArray.getJSONObject(i);
                if (emoteObject.getString("code").equals(emoteSTR)) {
                    String distantURL = "https:" + urlTemplate.replace("{{id}}", emoteObject.getString("id")).replace("{{image}}", "1x");
                    cachePath = emoticonDirectoryPath + File.separator + emoteObject.getString("id") + "." + emoteObject.getString("imageType");

                    //IS EMOTE IN CACHE ?
                    File cacheEmote = new File(cachePath);
                    long actualDate = System.currentTimeMillis() + 86400000;
                    // REDOWNLOAD EMOTE IF OLDER THAN 1 DAY
                    if (! cacheEmote.exists() || (cacheEmote.exists() && (cacheEmote.lastModified() - actualDate) > 0)) {
                        try {
                            InputStream  in  = new BufferedInputStream(new URL(distantURL).openStream());
                            OutputStream out = new BufferedOutputStream(new FileOutputStream(cachePath));
                            for (int j; (j = in.read()) != - 1; ) {
                                out.write(j);
                            }
                            in.close();
                            out.close();

                            logger.info("BTTV Emote downloaded : " + emoteObject.getString("id") + " , real name :" + emoteObject.getString("code"));

                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.severe("Can't download BTTV emote : " + emoteObject.getString("id") + " , real name :" + emoteObject.getString("code"));
                        }

                        break;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cachePath = "file:///" + cachePath.replace("\\", "/");
        return cachePath;

    }

    public void downloadBTTVEmotesDB(String channel) {
        String url  = "https://api.betterttv.net/2/emotes";
        String url2 = "https://api.betterttv.net/2/channels/" + channel;

        try {

            JSONObject   globalEmotes      = new JSONObject(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
            String       emoteURL          = globalEmotes.getString("urlTemplate");
            JSONArray    globalEmotesArray = globalEmotes.getJSONArray("emotes");
            String       emoticonFilePath  = config.getConfigSysDir() + File.separator + "emoticonsBTTV.json";
            OutputStream emoteOS           = new FileOutputStream(emoticonFilePath);

            JSONObject emoteBTTV   = new JSONObject();
            JSONArray  emotesArray = new JSONArray();
            emoteBTTV.put("urlTemplate", emoteURL);
            for (int i = 0; i < globalEmotesArray.length(); i++) {
                JSONObject emote = globalEmotesArray.getJSONObject(i);
                emotesArray.put(emote);

            }
            try {
                JSONObject channelEmotes      = new JSONObject(IOUtils.toString(new URL(url2), Charset.forName("UTF-8")));
                JSONArray  channelEmotesArray = channelEmotes.getJSONArray("emotes");
                for (int i = 0; i < channelEmotesArray.length(); i++) {
                    JSONObject emote = channelEmotesArray.getJSONObject(i);
                    emotesArray.put(emote);
                }
            } catch (Exception e) {
                logger.warning("Error when trying to download betterTTV channel emotes file");
            }
            emoteBTTV.put("emotes", emotesArray);
            emoteOS.write(emoteBTTV.toString().getBytes());
            emoteOS.close();

        } catch (Exception e) {
            e.printStackTrace();
            logger.warning("Error when trying to download betterTTV emotes file");

        }


    }

    public String replaceBTTVEmotes(String message) {
        ArrayList bttvEmotes   = getBTTVEmotesList();
        Iterator  bttvEmotesIT = bttvEmotes.iterator();
        while (bttvEmotesIT.hasNext()) {
            String bttvEmote = (String) bttvEmotesIT.next();
            if (message.contains(bttvEmote)) {
                message = message.replace(bttvEmote, "<img src=\"" + getBTTVEmoteURL(bttvEmote) + "\">");
            }
        }
        return message;
    }

    public String replaceEmotes(String message, String emotes) {
        // Prevent malicious viewers :')
        message = StringEscapeUtils.escapeHtml4(message);

        String[] words      = message.split("\\s");
        String   endMessage = "";

        for (String word : words) {
            boolean found = false;
            if (emotes != null && ! emotes.equals("")) {
                String[] emotesArray = emotes.split("/");
                for (String emote : emotesArray) {
                    // Browse Emotes
                    String[] emoteArray      = emote.split(":");
                    String   emoteID         = emoteArray[0];
                    String[] emoteOccurences = emoteArray[1].split(",");

                    String   emoteUrl  = getEmoteUrl(emoteID);
                    String[] emotePos  = emoteOccurences[0].split("-");
                    int      start     = new Integer(emotePos[0]);
                    int      end       = new Integer(emotePos[1]);
                    String   emoteName = message.substring(start, end + 1);
                    System.out.println("EmoteName: " + emoteName);
                    System.out.println("EmoteUrl : " + emoteUrl);

                    if (word.equals(emoteName)) {
                        // Twitch emotes
                        endMessage += "<img src=\"" + emoteUrl + "\"> ";
                        found = true;
                        break;
                    }
                }
            }
            if(!found){
                endMessage += word + " ";
            }
        }
        // BTTV Emotes
        message = replaceBTTVEmotes(endMessage);

        // Emojis
        try {
            SM_EmojiParser        emojiParser    = new SM_EmojiParser();
            java.util.List<Emoji> emotesList     = emojiParser.detectEmotes(message);
            Iterator<Emoji>       emojisIterator = emotesList.iterator();
            while (emojisIterator.hasNext()) {
                Emoji emoji = emojisIterator.next();
                //message = message.replace(emoji.getUnicode(),"XXX");
                try {

                    String emojiCode = emoji.getHtmlHexadecimal().replace("&#x", "").replace(";", "");
                    String emojiPath = getClass().getResource("/img/emojis/20/" + emojiCode + ".png").toString();
                    message = message.replace(emoji.getUnicode(), "<img src=\"" + emojiPath + "\">");
                } catch (Exception e) {
                    System.out.println("error: " + e.getMessage());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }




                /*for (String emoteOcc : emoteOccurences) {
                    // Get position of emotes and get the string value of emote
                    if(!emotesDone.contains(emoteID)){
                        String[] emotePos  = emoteOcc.split("-");
                        int      start     = new Integer(emotePos[0]);
                        int      end       = new Integer(emotePos[1]);
                        String emoteName   = message.substring(start, end + 1);



                        String   emoteName = message.substring(start, end + 1);
                        if (!emoteName.equals("")) {
                            // Chars or special chars ?
                            Pattern p = Pattern.compile("\\w+");
                            Matcher m = p.matcher(emoteName);
                            if (m.matches()) {
                                emotesEnd.add(emoteName);
                            } else {
                                Properties     emotesProperties = getTwitchEmotesProperties();
                                Enumeration<?> emotesNames      = emotesProperties.propertyNames();
                                while (emotesNames.hasMoreElements()) {
                                    // Browse properties keys
                                    String  emoteRegex = (String) emotesNames.nextElement();
                                    Pattern p2         = Pattern.compile(emoteRegex);
                                    Matcher m2         = p2.matcher(emoteName);
                                    if (m2.matches()) {
                                        emotesEnd.add(emoteRegex);
                                        break;
                                    }
                                }
                            }
                        }

                        emotesDone.add(emoteID);
                    }else{
                        break;
                    }


                }
*/


        return message;
    }

    public String getEmoteUrl(String emoteId) {
        String emoteURL = "https://static-cdn.jtvnw.net/emoticons/v1/" + emoteId + "/1.0";


        String emoticonDirectoryPath = config.getConfigSysDir() + File.separator + "emoticons";
        //String   emoteURL              = emoteProperties.getProperty(emote);
        String[] imageURLSplit = emoteURL.split("/");
        String   imageName     = imageURLSplit[imageURLSplit.length - 1];
        //String[] imageNameSplit        = imageName.split("\\.");
        //String   imageExtension        = imageNameSplit[imageNameSplit.length - 1];
        String cacheName = DigestUtils.sha1Hex(emoteId) + ".png";
        String cachePath = emoticonDirectoryPath + File.separator + cacheName;

        //IS EMOTE IN CACHE ?
        File cacheEmote = new File(cachePath);
        long actualDate = System.currentTimeMillis() + 86400000;
        // REDOWNLOAD EMOTE IF OLDER THAN 1 DAY
        if (! cacheEmote.exists() || (cacheEmote.exists() && (cacheEmote.lastModified() - actualDate) > 0)) {
            // DOWNLOADING EMOTE
            try {
                InputStream  in  = new BufferedInputStream(new URL(emoteURL).openStream());
                OutputStream out = new BufferedOutputStream(new FileOutputStream(cachePath));

                for (int i; (i = in.read()) != - 1; ) {
                    out.write(i);
                }
                in.close();
                out.close();

                logger.info("Emote downloaded : " + cacheName + " , real emoteId :" + emoteId);

            } catch (Exception e) {
                e.printStackTrace();
                logger.severe("Can't download emote : " + cacheName + " , real emoteId:" + emoteId);
            }
        } else {
            logger.info("Emote " + emoteId + " already exist in " + cacheName);
        }


        cachePath = "file:///" + cachePath.replace("\\", "/");
        return cachePath;
    }

    /*
     * TOOLS
     */
    public JSONObject getJsonObjectFromUrl(String url) {
        // Get JSON string from API
        JSONObject json = null;
        url = url + "?oauth_token=" + config.getProp("bot.password").replace("oauth:", "") + "&client_id=" + CLIENT_ID + "&nocache";
        try {
            json = new JSONObject(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
        } catch (JSONException e) {
            logger.warning("Can't get JSON from url " + url + " error:"
                                   + e.getMessage());
        } catch (Exception e) {
            logger.warning("Error occured when trying to get a JSON Object from "
                                   + url + ", error:" + e.getMessage());
        }


        return json;
    }

    public JSONArray getJsonArrayFromUrl(String url) {
        JSONArray json = null;
        url = url + "?oauth_token=" + config.getProp("bot.password").replace("oauth:", "") + "&client_id=" + CLIENT_ID;
        try {
            json = new JSONArray(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
        } catch (JSONException e) {
            logger.warning("Can't get JSON from url " + url + " error:" + e.getMessage());
        } catch (Exception e) {
            logger.warning("Error occured when trying to get a JSON Array from " + url + ", error:" + e.getMessage());
        }
        return json;
    }

    public JSONObject getJsonWebsite(String username) {
        JSONObject json = null;
        try {
            json = new JSONObject(IOUtils.toString(new URL(this.AUTH_URL + username), Charset.forName("UTF-8")));
        } catch (JSONException e) {
            logger.warning("Can't get JSON from url " + this.AUTH_URL + username + " error:" + e.getMessage());
        } catch (Exception e) {
            logger.warning("Error occured when trying to get a JSON Array from " + this.AUTH_URL + username + ", error:" + e.getMessage());
        }
        return json;
    }


}