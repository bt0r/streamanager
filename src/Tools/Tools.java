package Tools;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import database.User;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.tz.UTCProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import services.ConfigService;
import services.DatabaseService;
import services.LanguageService;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final public class Tools {
    static ConfigService   config = ConfigService.getInstance();
    static LanguageService trans  = LanguageService.getInstance();
    static DatabaseService db     = DatabaseService.getInstance();
    static Logger          logger = Logger.getLogger("streaManager");

    private Tools() {

    }

    public static int compareDate(long startTimestamp) {

        DateTime LFDate     = new DateTime(startTimestamp);
        DateTime actualDate = new DateTime();

        int nbrDays = Days.daysBetween(LFDate.withTimeAtStartOfDay(), actualDate.withTimeAtStartOfDay()).getDays();

        return nbrDays;
    }

    public static long dateToTimestamp(String dateStr) {
        // Convert date to timestamp
        dateStr = dateStr.replaceAll("(\\+\\d+):(\\d+)$", "");
        long   result     = 0;
        String dateFormat = trans.getProp("date.format");

        Pattern p = Pattern.compile(".+Z$");
        Matcher m = p.matcher(dateStr);
        Instant instant;
        if (m.matches()) {
            instant = Instant.parse(dateStr);
        } else {
            instant = Instant.parse(dateStr + "Z");
        }
        Date date = java.util.Date.from(instant);
        result = date.getTime();

        return result;
    }

    public static String timestampToDateStr(long date) {
        SimpleDateFormat sf       = new SimpleDateFormat(trans.getProp("date.format"));
        String           lastDate = sf.format(date);
        return lastDate;

    }

    public static void openWebpage(String uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URI(uri));
            } catch (Exception e) {
                logger.severe("Can't load requested URI : " + uri);
            }
        }
    }


    public static String timestampToStr(long timestamp) {
        String endStr = "";

        Calendar calendar = Calendar.getInstance();
        Date     date     = new Date();

        date.setTime(timestamp);
        calendar.set(Calendar.SECOND, 0);
        calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        Integer month = calendar.get(Calendar.MONTH) + 1;

        if (calendar.get(Calendar.MINUTE) < 10) {
            String minute = "0" + Calendar.MINUTE;
        }

        endStr = calendar.get(Calendar.YEAR) + "/" + addZero(month) + "/"
                + addZero(calendar.get(Calendar.DAY_OF_MONTH)) + " "
                + addZero(calendar.get(Calendar.HOUR_OF_DAY)) + ":"
                + addZero(calendar.get(Calendar.MINUTE)) + ":"
                + addZero(calendar.get(Calendar.SECOND));

        return endStr;
    }


    public static String addZero(Integer i) {
        String end = i.toString();
        if (i < 10) {
            end = String.format("%02d", i);
        }
        return end;
    }

    public static JSONObject getJsonObjectFromUrl(String url) {
        JSONObject json = null;
        try {
            json = new JSONObject(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
        } catch (JSONException e) {
            logger.warning("Can't get JSON from url " + url + " error:" + e.getMessage());
        } catch (Exception e) {
            logger.warning("Error occured when trying to get a JSON Object from " + url + ", error:" + e.getMessage());
        }
        return json;
    }

    public static JSONArray getJsonArrayFromUrl(String url) {
        JSONArray json = null;
        try {
            json = new JSONArray(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
        } catch (JSONException e) {
            logger.warning("Can't get JSON from url " + url + " error:" + e.getMessage());
        } catch (Exception e) {
            logger.warning("Error occured when trying to get a JSON Object from " + url + ", error:" + e.getMessage());
        }
        return json;
    }


    public static Integer lovecheck(String username, String username2) {
        Integer result = 0;

        username = username.toLowerCase();
        username2 = username2.toLowerCase();

        char[]  user2Chars = username2.toCharArray();
        char[]  userChars  = username.toCharArray();
        char[]  shortUser  = null;
        String  longUser;
        Integer matches    = 0;

        if (username.length() <= username2.length()) {
            shortUser = username.toCharArray();
            longUser = username2;
        } else {
            shortUser = username2.toCharArray();
            longUser = username;
        }

        for (char letter : shortUser) {
            if (longUser.indexOf(letter) != -1) {
                matches++;
            }
        }
        result = (matches * 100) / longUser.length();

        return result;

    }

    public static String[] isCompatible(String username, String username2) {
        String[] result = {"0", "0"};


        return result;
    }

    public static Integer diffBetween(String username, String username2) {
        Integer end = Math.abs(username.length() - username2.length());

        return end;
    }

    public static Map<String, Integer> horoscope(String username) {
        DateTimeZone.setProvider(new UTCProvider());
        DateTime date       = new DateTime();
        int      day        = date.getDayOfMonth();
        int      dayInt     = date.getDayOfWeek();
        int      hour       = date.getHourOfDay();
        int      pseudo     = username.length();
        int      charNumber = username.codePointAt(username.length() - 1) % 10 + 1;
        float    charDec    = ((((float) charNumber) * 10) / 100) + 1;


        int health = (int) (100 - day - (pseudo * (charDec * 1.5)));
        int money  = (int) ((pseudo + charNumber + hour) * 1.5);
        int love   = (pseudo + charNumber * 2) + day;


        // Change value depending on the day

        if (hour > 8 && hour < 18 && dayInt < 6) {
            // Working time
            health = health - 20;
            money = money + 23;
            love = love - 19;
            if (charNumber <= 5) {
                health = health - 25;
                love = love + 5;
            } else {
                health = health + 10;
                love = love - 23;
            }
        } else {
            // Lazy time
            health = health + 15;
            money = money - 10;
            love = love + 15;
            if (charNumber <= 5) {
                money = money - 17;
                love = love + 18;
            } else {
                money = money + 10;
                love = love - 10;
            }
        }

        // Check out of bound error
        if (health < 0) {
            health = 0;
        }
        if (health > 100) {
            health = 100;
        }
        if (money < 0) {
            money = 0;
        }
        if (money > 100) {
            money = 100;
        }
        if (love < 0) {
            love = 0;
        }
        if (love > 100) {
            love = 100;
        }

        // Return result
        Map<String, Integer> end = new HashMap<String, Integer>();
        end.put("health", health);
        end.put("money", money);
        end.put("love", love);

        return end;
    }

    public static boolean fight(String username, String username2) {
        boolean              result     = false;
        Dao<User, ?>         userDAO    = null;
        Map<String, Integer> horoscope  = horoscope(username);
        Integer              userHealth = horoscope.get("health");
        Integer              userScore  = 0;
        try {
            userDAO = DaoManager.createDao(db.getConnectionSource(), User.class);
            String[]            points   = userDAO.queryRaw("SELECT MAX(points) as max FROM user").getResults().get(0);
            PreparedQuery<User> userPQ   = userDAO.queryBuilder().where().eq("username", username.toLowerCase()).prepare();
            List<User>          userList = userDAO.query(userPQ);
            Iterator            userIT   = userList.iterator();
            User                user     = new User();
            while (userIT.hasNext()) {
                user = (User) userIT.next();
            }
            Integer userPoints = user.getPoints();
            userScore = new Integer(points[0]);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static int chance(String username) {
        DateTime date       = new DateTime();
        int      day        = date.getDayOfMonth();
        int      dayInt     = date.getDayOfWeek();
        int      hour       = date.getHourOfDay();
        int      pseudo     = username.length();
        int      charNumber = username.codePointAt(username.length() - 1) % 10 + 1;
        float    charDec    = ((((float) charNumber) * 10) / 100) + 1;
        int      chance     = (int) (100 - day - (pseudo * (charDec * 1.5)));


        return chance;
    }

    public static int money(String username) {
        DateTime date       = new DateTime();
        int      day        = date.getDayOfMonth();
        int      dayInt     = date.getDayOfWeek();
        int      hour       = date.getHourOfDay();
        int      pseudo     = username.length();
        int      charNumber = username.codePointAt(username.length() - 1) % 10 + 1;
        float    charDec    = ((((float) charNumber) * 10) / 100) + 1;
        int      money      = (int) ((pseudo + charNumber + hour) * 1.5);


        return money;
    }

    public static int penis(String username, String channel) {
        int     userLength = username.length();
        Integer endResult  = 0;
        if (username.equals(channelToUser(channel))) {
            endResult = 31;
        } else if (userLength <= 16) {
            endResult = 26 - userLength;
        } else if (userLength >= 17 && userLength <= 18) {
            endResult = 2;
        } else {
            endResult = 1;
        }
        return endResult;
    }

    public static int vagin(String username) {
        Integer endResult = 30 - username.length();
        return endResult;
    }

    public static String channelToUser(String channel) {
        return channel.replace("#", "");
    }

    public static Color lighterColor(Color color) {
        if (config.getProp("color.theme").equals("dark") && color.getBlue() < 90 && color.getRed() < 90 && color.getGreen() < 90) {
            int blue  = (int) (color.getBlue() * 1.5);
            int red   = (int) (color.getRed() * 1.5);
            int green = (int) (color.getGreen() * 1.5);
            blue = blue > 255 ? 255 : blue;
            red = red > 255 ? 255 : red;
            green = green > 255 ? 255 : green;

            color = new Color(red, green, blue);
        }

        return color;
    }

    public static Color darkerColor(Color color) {

        int blue  = (int) (color.getBlue() / 1.5);
        int red   = (int) (color.getRed() / 1.5);
        int green = (int) (color.getGreen() / 1.5);
        blue = blue > 255 ? 255 : blue;
        red = red > 255 ? 255 : red;
        green = green > 255 ? 255 : green;

        color = new Color(red, green, blue);


        return color;
    }


}
