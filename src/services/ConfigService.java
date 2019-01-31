package services;

import animations.LoadingAnimation;
import configuration.LogFormat;
import main.component.frame.ErrorDialog;
import main.component.frame.RestartDialog;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;


public class ConfigService {

    private Logger logger = Logger.getLogger("streaManager");

    private Properties            prop             = new Properties();
    private LanguageService       trans            = LanguageService.getInstance();
    private InternalConfigService internalConfig   = InternalConfigService.getInstance();
    private String                configPath       = "/configuration/config.properties";
    private String                configSysDir     = System.getProperty("user.home") + File.separator + "StreaManager";
    private String                configSysPath    = configSysDir + File.separator + "config.properties";
    private String                soundPath        = configSysDir + File.separator + "sounds";
    private String                versionPath      = configSysDir + File.separator + "versions";
    private String                APP_VERSION      = "0.0.8";
    private String                libSysDirPath    = configSysDir + File.separator + "libs";
    private String                libDirPath       = "/libs";
    private LoadingAnimation      loadingAnimation = null;
    private boolean               isLibsLoaded     = false;
    private int                   openedTasks      = 0;
    private int                   closedTasks      = 0;
    private ArrayList<String>     taskList         = new ArrayList();
    private String                lastReleaseURL   = null;
    private String                lastRelease      = null;

    /*
     * Constructeur privé
     */
    private ConfigService() {
        ConsoleHandler logHandler = new ConsoleHandler();
        LogFormat      logFormat  = new LogFormat();
        logHandler.setFormatter(logFormat);
        logger.addHandler(logHandler);
        createLibDir();
        getConfig();
        setProp("app.version", APP_VERSION);
    }

    /*
     * Instance unique
     */
    private static ConfigService ConfigService = new ConfigService();

    private static class ConfigServiceHolder {
        private final static ConfigService ConfigService = new ConfigService();
    }

    /*
     * Points d'accès pour l'instance unique du singleton
     */
    public static ConfigService getInstance() {
        if (ConfigService == null) {
            ConfigService = new ConfigService();
        }
        return ConfigService.ConfigService;
    }


    public void getConfig() {
        try {
            // Check if configs file exist in filesystem
            File configSysFile = new File(configSysPath);
            File soundPathFile = new File(soundPath);
            if (! configSysFile.exists()) {
                createNewConfig();
            }
            if (! soundPathFile.exists()) {
                soundPathFile.mkdir();
            }
            InputStream input = new FileInputStream(configSysPath);

            prop.load(input);
            input.close();
        } catch (Exception e) {
            logger.warning("Can't load config.properties file,error:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Reset config file
     */
    public void resetConfig() {
        try {
            // Check is it's an update
            String streamerLogin    = prop.getProperty("streamer.login");
            String streamerPassword = prop.getProperty("streamer.password");
            String botLogin         = prop.getProperty("bot.login");
            String botPassword      = prop.getProperty("bot.password");

            createNewConfig();
            InputStream configIn = getClass().getResourceAsStream(configPath);
            prop.load(configIn);
            if (! streamerLogin.isEmpty() && ! streamerPassword.isEmpty() && ! botLogin.isEmpty() && ! botPassword.isEmpty()) {
                setProp("streamer.login", streamerLogin);
                setProp("streamer.password", streamerPassword);
                setProp("bot.login", botLogin);
                setProp("bot.password", botPassword);
            }
            new RestartDialog();

        } catch (Exception e) {
            logger.severe(e.getMessage());
            new ErrorDialog(e.getMessage());
        }
    }

    public String getProp(String property) {
        if (property.equals("app.version")) return APP_VERSION;
        String propStr = prop.getProperty(property);
        if (propStr == null) {
            // Check if it's present in internalConfig
            String intProperty = internalConfig.getProp(property);
            if(intProperty != null){
                return intProperty;
            }else{
                logger.warning("Property " + property + " don't found, creating a new config file");
                new ErrorDialog(trans.getProp("err.propertie"));
                propStr = "";
            }
        }
        return propStr;
    }

    /**
     * Get a property without any special check
     * @param property
     * @return
     */
    public String getPropRaw(String property){
        return prop.getProperty(property);
    }


    public void setProp(String key, String value) {
        this.prop.setProperty(key, value);
        try {
            OutputStream output = new FileOutputStream(configSysPath);
            prop.store(output, "");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.warning("Can't save properties in " + configPath + ", error: " + e.getMessage());
        }
    }

    public void delete() {
        File configuration = new File(configSysPath);
        File database      = new File(DatabaseService.getInstance().getDatabasePath());
        try {
            configuration.delete();
            database.delete();
            logger.info("Configuration file and database succesfully deleted");
        } catch (Exception e) {
            logger.warning("Can't delete configuration or database file");
        }

    }

    public String getConfigSysDir() {
        return this.configSysDir;
    }

    public String getSoundPath() {
        return this.soundPath;
    }

    public String getVersionsPath() {
        return this.versionPath;
    }

    public void createNewConfig() {
        try {
            // Tell user that it's not normal

            // Do the job
            File configSysDir = new File(this.configSysDir);
            configSysDir.mkdir();
            FileOutputStream configSysFileOut = new FileOutputStream(configSysPath);
            InputStream      configIn         = getClass().getResourceAsStream(configPath);

            //InputStreamReader configInSR = new InputStreamReader(configIn, "UTF-8");
            StringWriter writer = new StringWriter();
            IOUtils.copy(configIn, writer, "UTF-8");
            configSysFileOut.write(writer.toString().getBytes("UTF-8"));
            configSysFileOut.flush();

            configIn.close();
            configSysFileOut.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getLibDir() {
        return libSysDirPath;
    }

    public void createLibDir() {
        File libDir = new File(libSysDirPath);
        if (! libDir.exists()) {
            try {
                libDir.mkdir();
            } catch (Exception e) {
                logger.warning("Can't create libs directory, error: " + e.getMessage());
            }
        }
    }

    public void moveLibs() {
        String model = System.getProperty("sun.arch.data.model");
        try {
            File libDir = new File(libSysDirPath);
            if (libDir.isDirectory()) {
                File[] libSysFiles = libDir.listFiles();
                for (File libSysFile : libSysFiles) {
                    libSysFile.delete();
                }
            }

            ArrayList libFiles = new ArrayList();
            if (SystemUtils.IS_OS_WINDOWS) {
                if (model.equals("32")) {
                    // WINDOWS AND 32bits
                    libFiles.add("sigar-x86-winnt.dll");
                } else {
                    // WINDOWS AND 64bits
                    libFiles.add("sigar-amd64-winnt.dll");
                }
            } else if (SystemUtils.IS_OS_LINUX) {
                if (model.equals("32")) {
                    // LINUX AND 32bits
                    libFiles.add("libsigar-x86-linux.so");
                } else {
                    // LINUX AND 64bits
                    libFiles.add("libsigar-amd64-linux.so");
                }

            }
            Iterator libIT      = libFiles.iterator();
            int      nbrLibs    = libFiles.size();
            int      libsLoaded = 0;
            while (libIT.hasNext()) {
                String libname = (String) libIT.next();
                try {
                    InputStream  in     = getClass().getResourceAsStream(libDirPath + "/" + libname);
                    OutputStream out    = new FileOutputStream(libSysDirPath + File.separator + libname);
                    byte[]       buffer = new byte[4096];
                    int          bytes_read;
                    while ((bytes_read = in.read(buffer)) != - 1) {
                        out.write(buffer, 0, bytes_read);
                    }
                    out.close();
                    in.close();
                    logger.info("Library file " + libname + " correctly copied in libs directory");
                    try {
                        System.load(getLibDir() + File.separator + libname);
                    } catch (Exception e) {
                        logger.severe("Can't load library file " + libname + ", error: " + e.getMessage());
                        System.exit(1);
                        libsLoaded++;
                    }


                } catch (Exception e) {
                    logger.info("Library file " + libname + " cannot been copied in libs directory");
                }
            }
            if (libsLoaded == (nbrLibs - 1)) {
                isLibsLoaded(true);
            }
        } catch (Exception e) {
            logger.severe("Cannot flush and/or install libraries, error:" + e.getMessage());
        }

    }

    private void isLibsLoaded(boolean isLibsLoaded) {
        this.isLibsLoaded = isLibsLoaded;
    }

    public boolean isLibsLoaded() {
        return this.isLibsLoaded;
    }

    private void openLoading() {
        if (! isLoading()) {
            loadingAnimation = new LoadingAnimation();
        }
    }

    private void closeLoading() {
        if (isLoading() && taskList.size() == 0) {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            loadingAnimation.dispose();
                            loadingAnimation = null;
                        }
                    }, 2000
            );
        }
    }

    public void closeLoadingAndRestart() {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        new RestartDialog();
                    }
                }, 2000
        );
    }

    private boolean isLoading() {
        if (loadingAnimation != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if a new version is available
     *
     * @return
     */
    public boolean checkNewVersion() {
        String filesDirectory   = "http://streamanager.net/files/";
        String botDirectory     = filesDirectory + "bot/";
        String lastRelease      = filesDirectory + "lastRelease";
        URL    lastReleasePath  = null;
        String currentVersion   = "0";
        String lastReleaseValue = "0";

        try {
            // Check if local directory "Versions" exists
            String directoryPath  = this.versionPath;
            File   destinationDir = new File(directoryPath);
            if (! destinationDir.exists()) {
                destinationDir.mkdir();
            }

            // Check last release
            lastReleasePath = new URL(lastRelease);
            InputStream    is = lastReleasePath.openStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            lastReleaseValue = in.readLine();
            currentVersion = getProp("app.version");
            this.lastReleaseURL = botDirectory + "StreaManager_" + lastReleaseValue + ".zip";
            this.lastRelease = lastReleaseValue;

        } catch (Exception e) {
            logger.severe("Can't find last version, error:" + e.getMessage());
        }
        // Compare current version and last release
        if (! currentVersion.equals(lastReleaseValue)) {
            return true;
        } else {
            return false;
        }
    }

    public String getNewVersionURL() {
        return this.lastReleaseURL;
    }

    public String getNewVersion() {
        return this.lastRelease;
    }

    /**
     * Add task to loading frame
     *
     * @param message
     * @param severity
     */
    public void startTask(String taskName, String message, String severity) {
        // If loading frame is not launched, launch it
        openLoading();
        loadingAnimation.setTask(message, severity);
        if (! taskList.contains(taskName)) {
            taskList.add(taskName);
        }
    }


    /**
     * Remove a task to loading frame
     *
     * @param message
     * @param severity
     */
    public void endTask(String taskName, String message, String severity) {

        loadingAnimation.setTask(message, severity);
        if (taskName != null && taskList.contains(taskName)) {
            for (int i = 0; i < taskList.size(); i++) {
                String tempName = taskList.get(i);
                if (tempName.equals(taskName)) {
                    taskList.remove(i);
                }
            }
        }
        closeLoading();
    }

    /**
     * Close app
     */
    public void shutdown() {
        System.exit(0);
    }

}