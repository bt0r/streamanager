package chat;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import database.Event;
import services.ConfigService;
import services.DatabaseService;
import services.EventService;
import services.TwitchAPIService;

import java.util.*;
import java.util.logging.Logger;


public class CronEvent extends Thread {
    private ConfigService    config = ConfigService.getInstance();
    private TwitchAPIService twitch = TwitchAPIService.getInstance();
    private DatabaseService  db     = DatabaseService.getInstance();
    private Logger           logger = Logger.getLogger("streaManager");
    private EventService     eventS = EventService.getInstance();
    int interval = 60000;

    public CronEvent() {}

    public void run() {
        final Logger logger = Logger.getLogger("streaManager");
        logger.fine("Starting cron event task...");

        long  startTime = 0;
        Timer timer     = new Timer();
        TimerTask tache = new TimerTask() {
            @Override
            public void run() {
                try {

                    ArrayList result     = twitch.getLatestFollowers(config.getProp("streamer.login"));
                    Iterator eventIT = result.iterator();

                    while (eventIT.hasNext()) {
                        Event                  event    = (Event) eventIT.next();
                        Dao<Event, ?>          eventDAO = DaoManager.createDao(db.getConnectionSource(), Event.class);
                        QueryBuilder<Event, ?> eventQB  = eventDAO.queryBuilder();
                        eventQB.where().eq("type", "follow").and().eq("user_id", event.getUser().getId());
                        PreparedQuery<Event> eventPQ        = eventQB.prepare();
                        List<Event>          eventDBResults = eventDAO.query(eventPQ);
                        Iterator             eventDBIT      = eventDBResults.iterator();

                        while (eventDBIT.hasNext()) {
                            Event eventDB = (Event) eventDBIT.next();
                            long actualDate = System.currentTimeMillis();

                            // ADD EVENT IN GUI
                            eventS.addFollow(event.getUser().getUsername());
                        }
                        //logger.fine(viewer + " added to the list");

                    }

                } catch (Exception e) {
                    logger.warning("Error when trying to refresh event list, error:" + e.getMessage());
                }


            }
        };
        timer.scheduleAtFixedRate(tache, startTime, (long) getInterval());

    }

    public int getInterval() {
        return this.interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

}
