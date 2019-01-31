package services;

import Exception.UserNotFoundException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import database.*;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;


public class DatabaseService {
    private Logger           logger           = Logger.getLogger("streaManager");
    private ConnectionSource cs               = null;
    private ConfigService    config           = ConfigService.getInstance();
    private String           databaseDirPath  = System.getProperty("user.home") + File.separator + config.getProp("app.name");
    private String           databaseFilePath = databaseDirPath + File.separator + config.getProp("app.name") + ".db";

    /*
     * Private constructor
     */
    private DatabaseService() {
        logger.info("Trying to connect to database ...");
        process();
        update();
    }

    /*
     * Instance unique
     */
    private static DatabaseService DatabaseService = new DatabaseService();

    private static class DatabaseServiceHolder {
        private final static DatabaseService DatabaseService = new DatabaseService();
    }

    /*
     * Points d'accès pour l'instance unique du singleton
     */
    public static DatabaseService getInstance() {
        if (DatabaseService == null) {
            DatabaseService = new DatabaseService();
        }
        return DatabaseService.DatabaseService;
    }

    public boolean create() {
        boolean result = false;
        try {
            logger.info("Trying to drop database...");
            // Drop & Create tables
            TableUtils.dropTable(cs, Command.class, true);
            TableUtils.createTable(cs, Command.class);
            TableUtils.dropTable(cs, User.class, true);
            TableUtils.createTable(cs, User.class);
            TableUtils.dropTable(cs, Question.class, true);
            TableUtils.createTable(cs, Question.class);
            TableUtils.dropTable(cs, Vote.class, true);
            TableUtils.createTable(cs, Vote.class);
            TableUtils.dropTable(cs, Event.class, true);
            TableUtils.createTable(cs, Event.class);
            TableUtils.dropTable(cs, Battle.class, true);
            TableUtils.createTable(cs, Battle.class);

            logger.info("Database has been overwrited.");

        } catch (Exception e) {
            logger.warning("Can't delete database, error:" + e.getMessage());
        }

        return result;
    }

    public boolean process() {
        Boolean response = false;

        // Check if database file exist
        String dbFileExist = dbFileExist();
        if (!dbFileExist.equals("error")) {
            if (connect()) {
                if (dbFileExist.equals("created")) {
                    create();
                    response = true;
                }

            }
        }
        // Connect
        connect();

        return response;

    }

    public boolean connect() {
        // Connection to database
        boolean          connected        = false;
        ConnectionSource connectionSource = null;
        try {

            logger.info("Opening database ...");
            String databaseUrl = "jdbc:sqlite:" + databaseFilePath;

            connectionSource = new JdbcConnectionSource(databaseUrl);
            logger.info("Connected to database");
            connected = true;
            this.cs = connectionSource;

        } catch (Exception e) {
            e.printStackTrace();
            logger.warning("Can't connect to database , error: " + e.getMessage());
            connected = false;
        }
        return connected;
    }

    public String dbFileExist() {
        File   databaseDir  = new File(databaseDirPath);
        File   databaseFile = new File(databaseFilePath);
        String response     = "error";
        if (!databaseDir.isDirectory()) {
            try {
                databaseDir.mkdirs();
                logger.info("Database directory created successfully in " + databaseDirPath);
            } catch (Exception e) {
                logger.warning("Can't create database directory");
            }
        } else {
            logger.info("Database directory already exist");
        }
        if (!databaseFile.isFile()) {
            try {
                databaseFile.createNewFile();
                logger.info("Database file created successfully in " + databaseFilePath);
                response = "created";
            } catch (Exception e) {
                logger.warning("Can't create database file, error:" + e.getMessage());
            }
        } else {
            response = "nothing";
            logger.info("Database file already exist");

        }
        return response;
    }


    public void update() {
        try {
            // From 0.0.8 , save the previous version number. It will help for sql migrations
            String previousVersion = config.getPropRaw("app.version.previous") != null ? config.getPropRaw("app.version.previous") :"0.0.7" ;
            String currentVersion = config.getPropRaw("app.version");
            config.setProp("app.version.previous",previousVersion);
            // Previous Version
            String[] versionArray = previousVersion.split("\\.");
            int previousVersionInt  = Integer.parseInt(versionArray[0]);
            int previousFunctionInt = Integer.parseInt(versionArray[1]);
            int previousRevisionInt = Integer.parseInt(versionArray[2]);

            if (currentVersion.equals("0.0.2c") || currentVersion.equals("0.0.2a") || currentVersion.equals("0.0.2b")) {
                logger.info("Updating database to 0.0.3");
                TableUtils.createTableIfNotExists(cs, Event.class);
                Dao<User, ?> userDAO = DaoManager.createDao(cs, User.class);
                userDAO.executeRaw("ALTER TABLE `user` ADD color varchar(255);");
                userDAO.executeRaw("ALTER TABLE `user` ADD badges varchar(255);");
                userDAO.executeRaw("ALTER TABLE `user` ADD isTurbo boolean;");
                userDAO.executeRaw("ALTER TABLE `user` ADD isSubscriber boolean;");
                userDAO.executeRaw("ALTER TABLE `user` ADD isTwitchAdmin boolean;");
                userDAO.executeRaw("ALTER TABLE `user` ADD isTwitchGlobalMod boolean;");
                userDAO.executeRaw("ALTER TABLE `user` ADD isTwitchStaff boolean;");
            }
            if (previousVersionInt <= 0 && previousFunctionInt <= 0 && previousRevisionInt <= 5 ) {
                logger.info("Updating database to 0.0.5");
                Dao<User, ?> userDAO = DaoManager.createDao(cs, User.class);
                userDAO.executeRaw("ALTER TABLE `user` ADD isPremium boolean;");
            }

            if (previousVersionInt <= 0 && previousFunctionInt <= 0 && previousRevisionInt <= 6) {
                logger.info("Updating database to 0.0.6");
                Dao<User, ?> userDAO = DaoManager.createDao(cs, User.class);
                userDAO.executeRaw("ALTER TABLE `user` ADD isGamewispSubscriber boolean;");
                userDAO.executeRaw("ALTER TABLE `user` ADD gamewispSubDate bigint;");
            }
            if (previousVersionInt <= 0 && previousFunctionInt <= 0 && previousRevisionInt <= 7) {
                logger.info("Updating database to 0.0.7");
                TableUtils.createTableIfNotExists(cs, Battle.class);
                if (config.getProp("command.shifumi").isEmpty()) {
                    config.setProp("command.shifumi", "true");
                    config.setProp("command.shifumi.whisper", "false");
                }
                Dao<User, ?> userDAO = DaoManager.createDao(cs, User.class);
                userDAO.executeRaw("ALTER TABLE `user` ADD twitchId bigint;");
            }

            config.setProp("app.version.previous",config.getProp("app.version"));
        } catch (SQLException e) {
            logger.severe("Can't update tables, error:"+e.getMessage());
        }

    }

    public User addUser(User user) {
        try {

            // Check if user exist
            Dao<User, String> userDAO = DaoManager.createDao(cs, User.class);
            logger.info("Trying to create user " + user.getUsername());
            user.setJoinDate(new Date().getTime());
            user.setLastConn(new Date().getTime());
            userDAO.create(user);
            int userId = user.getId();
            user.setId(userId);
            logger.info("User " + user.getUsername() + " created with id " + user.getId());

        } catch (SQLException e) {
            logger.warning("Can't create User, SQL error:" + e.getMessage());
        } catch (Exception e) {
            logger.warning("Can't create User, error:" + e.getMessage());
        }
        return user;
    }

    public boolean addPointsToUser(User user, Integer points) {
        boolean error = false;
        try {

            Dao<User, String> userDAO = DaoManager.createDao(cs, User.class);
            user.addPoints(points);
            user.setLastConn(new Date().getTime());
            userDAO.update(user);
            logger.info("Adding " + points + " to user " + user.getUsername());
        } catch (Exception e) {
            logger.warning("Can't add " + points + " points to user " + user.getUsername() + ",error: " + e.getMessage());
            error = true;
        }
        return error;
    }

    public User findUser(String username) {
        // Force username to be in lowercase in database
        username = username.toLowerCase();

        Dao<User, String> userDAO;
        User              user = new User();
        user.setUsername(username);
        try {
            userDAO = DaoManager.createDao(cs, User.class);
            QueryBuilder<User, String> builder = userDAO.queryBuilder();
            builder.where().eq("username", username);
            List<User> list = userDAO.query(builder.prepare());
            for (int i = 0; i < list.size(); i++) {
                user = list.get(i);
            }
            if (user.getId() == 0) {
                user = addUser(user);
            }
            //logger.info("User " + username + " retrieved from database");
        } catch (SQLException e) {
            logger.warning("Can't check user " + username + ", error:" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.warning("Can't check user " + username + ", error:" + e.getMessage());
        }

        return user;
    }

    /**
     * Find a user by the twitch id or his username
     *
     * @param id
     *
     * @return
     */
    public User findUser(String username, long id) throws UserNotFoundException {
        Dao<User, String> userDAO;
        User              user = new User();
        user.setTwitchId(id);
        user.setUsername(username);
        try {
            userDAO = DaoManager.createDao(cs, User.class);
            QueryBuilder<User, String> builder = userDAO.queryBuilder();
            builder.where().eq("twitchId", id);
            List<User> list = userDAO.query(builder.prepare());
            for (int i = 0; i < list.size(); i++) {
                user = list.get(i);
            }
            if (user.getId() == 0) {
                // User not found by his twitch ID, try to find it with his username
                builder.where().eq("username", username);
                List<User> listUsername = userDAO.query(builder.prepare());
                for (int i = 0; i < listUsername.size(); i++) {
                    // User found in database
                    user = listUsername.get(i);
                    user.setTwitchId(id);
                }
                if (user.getId() != 0) {
                    user = addUser(user);
                }
            }
            //logger.info("User " + username + " retrieved from database");
        } catch (Exception e) {
            e.printStackTrace();
            logger.warning("Can't check user_id " + id + ", error:" + e.getMessage());
        }

        return user;
    }


    public ConnectionSource getConnectionSource() {
        return this.cs;
    }

    public String getDatabasePath() {
        return databaseFilePath;
    }

}
