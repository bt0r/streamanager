package database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@DatabaseTable(tableName = "user")
public class User {

    @DatabaseField(generatedId = true, allowGeneratedIdInsert = true, canBeNull = false)
    private int    id;
    @DatabaseField(unique = true, canBeNull = false)
    private String username;
    @DatabaseField
    private long   twitchId;
    @DatabaseField
    private long   joinDate;
    @DatabaseField
    private long   lastConn;
    @DatabaseField
    private int points = 0;
    @DatabaseField
    private boolean isModerator;
    @DatabaseField
    private long    followDate;
    @DatabaseField
    private long    lastFc;
    @DatabaseField
    private String  color;
    @DatabaseField
    private boolean isSubscriber;
    @DatabaseField
    private boolean isGamewispSubscriber;
    @DatabaseField
    private long    gamewispSubDate;
    @DatabaseField
    private boolean isPremium;
    @DatabaseField
    private boolean isTwitchStaff;
    @DatabaseField
    private boolean isTwitchGlobalMod;
    @DatabaseField
    private boolean isTwitchAdmin;
    @DatabaseField
    private String  badges;
    @DatabaseField
    private boolean isTurbo;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTwitchId() {
        return twitchId;
    }

    public void setTwitchId(long twitchId) {
        this.twitchId = twitchId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(long joinDate) {
        this.joinDate = joinDate;
    }

    public long getLastConn() {
        return lastConn;
    }

    public void setLastConn(long lastConn) {
        this.lastConn = lastConn;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int points) {
        this.points = getPoints() + points;
    }

    public void delPoints(int points) {
        if (getPoints() - points < 0) {
            this.points = 0;
        } else {
            this.points = getPoints() - points;
        }

    }

    public boolean isModerator() {
        return isModerator;
    }

    public void setModerator(boolean isModerator) {
        this.isModerator = isModerator;
    }

    public void setModerator(String isModerator) {
        if (isModerator.equals("0")) {
            setModerator(false);
        } else {
            setModerator(true);
        }
    }

    public boolean isTwitchStaff() {
        return isTwitchStaff;
    }

    public void setTwitchStaff(boolean twitchStaff) {
        isTwitchStaff = twitchStaff;
    }

    public boolean isTwitchGlobalMod() {
        return isTwitchGlobalMod;
    }

    public void setTwitchGlobalMod(boolean twitchGlobalMod) {
        isTwitchGlobalMod = twitchGlobalMod;
    }

    public boolean isTwitchAdmin() {
        return isTwitchAdmin;
    }

    public void setTwitchAdmin(boolean twitchAdmin) {
        isTwitchAdmin = twitchAdmin;
    }

    public void setTurbo(boolean turbo) {
        isTurbo = turbo;
    }

    public long getFollowDate() {
        return followDate;
    }

    public void setFollowDate(long followDate) {
        this.followDate = followDate;
    }

    public long getLastFc() {
        return lastFc;
    }

    public void setLastFc(long lastFc) {
        this.lastFc = lastFc;
    }

    public String getColor() {
        return color;
    }

    public int getHexColor() {
        int result = 0;
        try {
            if (Integer.decode("0x" + color) != null) {
                result = Integer.decode("0x" + color);
            }
        } catch (Exception e) {
        }

        return result;
    }

    public void createColor() {
        // 10 differents colors
        String[] colors = {"C74E46","BB8738","FE62FB","1F3A89","C86058","6A2AC3","80A56D","1CB75C","038F0A","3274B6","8B2A12","A73266","2E2E2E","B4B533","EE81B2"};
        int randomNumber = (int) (Math.random()*14);
        setColor(colors[randomNumber]);
    }

    public void setColor(String color) {
        this.color = color.replace("#", "");
    }

    public boolean isSubscriber() {
        return isSubscriber;
    }

    public void setSubscriber(boolean subscriber) {
        isSubscriber = subscriber;
    }

    public void setSubscriber(String subscriber) {
        if (subscriber.equals("0")) {
            isSubscriber = false;
        } else {
            isSubscriber = true;
        }
    }

    public boolean isGamewispSubscriber() {
        return isGamewispSubscriber;
    }

    public void setGamewispSubscriber(boolean GamewispSubscriber) {
        isGamewispSubscriber = GamewispSubscriber;
    }

    public void setGamewispSubscriber(String GamewispSubscriber) {
        if (GamewispSubscriber.equals("0")) {
            isGamewispSubscriber = false;
        } else {
            isGamewispSubscriber = true;
        }
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public void setPremium(String premium) {
        if (premium.equals("0")) {
            isPremium = false;
        } else {
            isPremium = true;
        }
    }

    public boolean isPremium() {
        return isPremium;
    }

    public String getBadges() {


        return badges;
    }

    public void setBadges(String badges) {
        Map      badgesMap = formatBadges(badges);
        Iterator badgesIT  = badgesMap.entrySet().iterator();
        while (badgesIT.hasNext()) {
            Map.Entry badgeEntry = (Map.Entry) badgesIT.next();
            String    badgeName  = badgeEntry.getKey().toString();
            int       badgeValue = (int) badgeEntry.getValue();
            switch (badgeName) {
                case "staff":
                    if (badgeValue == 1) {
                        setTwitchStaff(true);
                    }
                    break;
                case "admin":
                    if (badgeValue == 1) {
                        setTwitchAdmin(true);
                    }
                    break;
                case "global_mod":
                    if (badgeValue == 1) {
                        setTwitchGlobalMod(true);
                    }
                    break;
                case "moderator":
                    if (badgeValue == 1) {
                        setModerator(true);
                    }
                    break;
                case "subscriber":
                    if (badgeValue == 1) {
                        setSubscriber(true);
                    }
                    break;
                case "premium":
                    if (badgeValue == 1) {
                        setPremium(true);
                    }
                    break;
            }
        }
        this.badges = badges;
    }

    public long getGamewispSubDate() {
        return gamewispSubDate;
    }

    public void setGamewispSubDate(long gamewispSubDate) {
        this.gamewispSubDate = gamewispSubDate;
    }


    public boolean isTurbo() {
        return isTurbo;
    }

    public void setTurbo(String turbo) {
        if (turbo.equals("0")) {
            isTurbo = false;
        } else {
            isTurbo = true;
        }
    }

    private Map<String, String> formatBadges(String badges) {
        Map finalBadges = new HashMap();

        String[] badgesArray = badges.split(",");
        for (String badge : badgesArray) {

            String[] badgeArray = badge.split("/");
            String   badgeName  = badgeArray[0];
            Integer  badgeValue = new Integer(badgeArray[1]);
            finalBadges.put(badgeName, badgeValue);
        }
        return finalBadges;
    }

    public String toString() {
        String str = "username: " + getUsername() +
                ", color: " + getColor() +
                ", points:" + getPoints() +
                ", isSub:" + isSubscriber() +
                ", isGWSub:" + isGamewispSubscriber() +
                ", isPremium:" + isPremium() +
                ", isTurbo:" + isTurbo() +
                ", badge:" + getBadges() +
                ", fc:" + getFollowDate();

        return str;
    }
}

    
	