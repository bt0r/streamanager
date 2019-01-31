package services;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import database.Event;
import database.User;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class GamewispAPIService {
    private Logger          logger         = Logger.getLogger("streaManager");
    private LanguageService trans          = LanguageService.getInstance();
    private ConfigService   config         = ConfigService.getInstance();
    private DatabaseService db             = DatabaseService.getInstance();
    private String          subscribersURL = "https://api.gamewisp.com/pub/v1/channel/subscribers";
    private EventService    eventService   = EventService.getInstance();
    public  String          CLIENT_ID      = "d49667d57043f1c0229e1093f0979a63b274e7d";
    public  String          CLIENT_SECRET  = "73bae1617ba4285d1736567d5f4d40971f1bbb4";

    /*
     * Constructeur privé
     */
    private GamewispAPIService() {

    }

    /*
     * Instance unique
     */
    private static GamewispAPIService GamewispAPIService = new GamewispAPIService();

    private static class GamewispAPIServiceHolder {
        private final static GamewispAPIService GamewispAPIService = new GamewispAPIService();
    }

    /*
     * Points d'accès pour l'instance unique du singleton
     */
    public static GamewispAPIService getInstance() {
        if (GamewispAPIService == null) {
            GamewispAPIService = new GamewispAPIService();
        }
        return GamewispAPIService.GamewispAPIService;
    }

    public void addSubscriber(String username, long subDate) {
        try {
            Dao<User, String>  userDAO  = DaoManager.createDao(db.getConnectionSource(), User.class);
            Dao<Event, String> eventDAO = DaoManager.createDao(db.getConnectionSource(), Event.class);

            // Set user as a subscriber
            User user = db.findUser(username);
            user.setGamewispSubDate(subDate);
            user.setLastConn(new Date().getTime());
            user.setGamewispSubscriber(true);
            user.setPoints(user.getPoints()+200);

            // Update user
            userDAO.update(user);

        } catch (SQLException e) {
            logger.warning("Can't add user as a gamewisp subscriber");
        }
    }

    public void getSubscribers() throws JSONException {
        if (config.getProp("gamewisp.token") != null) {
            String     api_url  = this.subscribersURL + "?access_token=" + config.getProp("gamewisp.token") + "&limit=50&include=user";
            JSONObject response = this.getJsonObjectFromUrl(api_url);
            JSONArray  subArray = response.getJSONArray("data");
            int        subTotal = subArray.length();
            for (int i = 0; i < subArray.length(); i++) {
                try {
                    JSONObject gamewispSub  = subArray.getJSONObject(i);
                    JSONObject gamewispUser = gamewispSub.getJSONObject("user");
                    String     subUsername  = gamewispUser.getJSONObject("data").getString("username");
                    DateFormat format       = new SimpleDateFormat("Y-M-d k:m:s");
                    long       subDate      = format.parse(gamewispSub.getString("created_at")).getTime();


                    Dao<User, String> userDAO = DaoManager.createDao(db.getConnectionSource(), User.class);
                    User              user    = db.findUser(subUsername);
                    userDAO.update(user);

                    // CHECK EVENTS ON DB
                    Dao<Event, String>          eventDAO = DaoManager.createDao(db.getConnectionSource(), Event.class);
                    QueryBuilder<Event, String> eventQB  = eventDAO.queryBuilder();
                    eventQB.orderBy("date", false).where().eq("user_id", user.getId()).and().eq("type", "sub_gamewisp");
                    PreparedQuery<Event> eventPQ        = eventQB.prepare();
                    List<Event>          eventDBResults = eventDAO.query(eventPQ);
                    Iterator<Event>      eventDBIT      = eventDBResults.iterator();
                    long                 actualDate     = System.currentTimeMillis();
                    boolean              isNew          = true;

                    while (eventDBIT.hasNext()) {
                        Event eventDB = eventDBIT.next();
                        if (subDate == eventDB.getDate() || subDate < (actualDate - 86400000)) {
                            // EVENT IS OLDER THAN 1 DAY -> FORGOT IT
                            isNew = false;
                            logger.info("User " + user.getUsername() + " already sub with gamewisp.");
                            break;
                        }
                    }
                    // CREATE EVENT
                    if (isNew) {
                        Event event = new Event();
                        event.setType("sub_gamewisp");
                        event.setUser(user);
                        event.setDate(subDate);
                        eventDAO.create(event);
                        eventService.addGamewispSub(user.getUsername(), null);
                        db.addPointsToUser(user, 200);
                        user.setGamewispSubDate(subDate);
                        user.setGamewispSubscriber(true);
                        userDAO.update(user);
                    }
                    System.out.println("Total sub " + subTotal);

                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    /**
     * TOOLS
     *
     * @param url
     *
     * @return
     */
    public JSONObject getJsonObjectFromUrl(String url) {
        // Get JSON string from API
        JSONObject json = null;

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

    /**
     * @param url
     *
     * @return
     */
    public JSONArray getJsonArrayFromUrl(String url) {
        JSONArray json = null;
        try {
            json = new JSONArray(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
        } catch (JSONException e) {
            logger.warning("Can't get JSON from url " + url + " error:" + e.getMessage());
        } catch (Exception e) {
            logger.warning("Error occured when trying to get a JSON Array from " + url + ", error:" + e.getMessage());
        }
        return json;
    }


}