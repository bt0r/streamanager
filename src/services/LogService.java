package services;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogService {
    private Logger        logger             = Logger.getLogger("streaManager");
    private ConfigService config             = ConfigService.getInstance();
    private String        logFileDirPath     = config.getConfigSysDir() + File.separator + "logs";
    private String        logFilePath        = logFileDirPath + File.separator + config.getProp("streamer.login") + "_backup.csv";
    private String        lastFollower       = "lastFollower";
    private String        lastHost           = "lastHost";
    private String        totalHost          = "totalHost";
    private String        totalFollower      = "totalFollower";
    private String        cpuUsage           = "cpuUsage";
    private String        ramUsage           = "ramUsage";
    private String        hddUsage           = "hddUsage";
    private String        networkUsage       = "networkUsage";
    private String        totalNormalViewers = "totalNormalViewers";
    private String        totalModerators    = "totalModerators";
    private String        totalViewers       = "totalViewers";
    private ArrayList     filesList          = new ArrayList();


    /*
     * Constructeur privé
     */
    private LogService() {
        init();
    }

    /*
     * Instance unique
     */
    private static LogService LogService = new LogService();

    private static class LogServiceHolder {
        private final static LogService LogService = new LogService();
    }

    /*
     * Points d'accès pour l'instance unique du singleton
     */
    public static LogService getInstance() {
        if (LogService == null) {
            LogService = new LogService();
        }
        return LogService.LogService;
    }


    /**
     * Initialize logs files
     */
    private void init() {
        // INITIALIZE CHAT LOG FILES
        if (!config.getProp("streamer.login").equals("") && config.getProp("streamer.login") != null) {
            File logFileDir  = new File(logFileDirPath);
            File chatLogFile = new File(this.logFilePath);
            try {
                if (!logFileDir.exists()) {
                    logFileDir.mkdir();
                    logger.info("Logs directory created");
                }
                if (!chatLogFile.exists() && !chatLogFile.isFile()) {
                    chatLogFile.createNewFile();
                    logger.info("ChatLog file created");
                }

            } catch (Exception e) {
                logger.warning("Can't create logs directory or chatLog file.");
            }

            // STREAMING FILES
            filesList.add(lastFollower);
            filesList.add(lastHost);
            filesList.add(totalHost);
            filesList.add(totalFollower);
            filesList.add(cpuUsage);
            filesList.add(ramUsage);
            filesList.add(hddUsage);
            filesList.add(networkUsage);
            filesList.add(totalNormalViewers);
            filesList.add(totalModerators);
            filesList.add(totalViewers);

            Iterator filesListIT = filesList.iterator();
            while (filesListIT.hasNext()) {
                String fileName = (String) filesListIT.next();
                String filePath = logFileDirPath + File.separator + config.getProp("streamer.login") + "_" + fileName + ".txt";
                File   logFile  = new File(filePath);
                logFile.delete();
                try {
                    logFile.createNewFile();
                    logger.finest(fileName + " created.");
                    if (fileName.equals(totalFollower) || fileName.equals(totalHost)) {
                        Files.write(Paths.get(filePath), ("0").getBytes(), StandardOpenOption.WRITE);
                    }
                } catch (Exception e) {
                    logger.severe("Can't create " + fileName + ", error:" + e.getMessage());
                }

            }
        }
    }

    /**
     * Write log in chat log file
     *
     * @param date    Parsed date
     * @param user    Username of the user
     * @param message Message to add
     */
    public void writeChatLog(String date, String user, String message) {
        try {
            Files.write(Paths.get(this.logFilePath), (date + ";" + user + ";" + message + "\r\n").getBytes(), StandardOpenOption.APPEND);
        } catch (FileNotFoundException e) {
            logger.warning("ChatLog File was not found, can't write on it, please check your rights");
        } catch (IOException e) {
            logger.warning("Can't write chatlog, maybe right or bad ?");
        }

    }

    /**
     * Write last follower
     *
     * @param username Username of the last follow
     */
    public void writeLastFollower(String username) {
        writeLog(lastFollower, username);
        writeCountFollower();
    }

    /**
     * Write last person who host the stream
     *
     * @param username Username of the last user who host the stream
     */
    public void writeLastHost(String username) {
        writeLog(lastHost, username);
        writeCountHost();
    }

    /**
     * Write number of host during the session
     */
    public void writeCountHost() {
        if (!config.getProp("streamer.login").equals("") && config.getProp("streamer.login") != null) {
            String filePath = logFileDirPath + File.separator + config.getProp("streamer.login") + "_" + totalHost + ".txt";
            try {
                FileInputStream in   = new FileInputStream(filePath);
                BufferedReader  bis  = new BufferedReader(new InputStreamReader(in));
                String          line = bis.readLine();
                Pattern         p    = Pattern.compile("^(\\d+)$");
                Matcher         m    = p.matcher(line);
                if (m.matches()) {
                    int count = new Integer(m.group(1));
                    count++;
                    writeLog(totalHost, String.valueOf(count));
                }
                bis.close();
                in.close();


            } catch (Exception e) {
                logger.severe("Can't read file , error:" + e.getMessage());
            }
        }
    }

    /**
     * Write number of follows during the session
     */
    public void writeCountFollower() {
        if (!config.getProp("streamer.login").equals("") && config.getProp("streamer.login") != null) {
            String filePath = logFileDirPath + File.separator + config.getProp("streamer.login") + "_" + totalFollower + ".txt";
            try {
                FileInputStream in   = new FileInputStream(filePath);
                BufferedReader  bis  = new BufferedReader(new InputStreamReader(in));
                String          line = bis.readLine();
                Pattern         p    = Pattern.compile("^(\\d+)$");
                Matcher         m    = p.matcher(line);
                if (m.matches()) {
                    int count = new Integer(m.group(1));
                    count++;
                    writeLog(totalFollower, String.valueOf(count));
                }
                bis.close();
                in.close();

            } catch (Exception e) {
                logger.severe("Can't read file , error:" + e.getMessage());
            }
        }
    }

    /**
     * Write CPU usage
     *
     * @param cpu CPU usage in percent
     */
    public void writeCPU(String cpu) {
        writeLog(cpuUsage, cpu);
    }

    /**
     * Write RAM usage
     *
     * @param ram RAM usage in percent
     */
    public void writeRAM(String ram) {
        writeLog(ramUsage, ram);
    }

    /**
     * Write HDD usage
     *
     * @param hdd HDD usage in percent
     */
    public void writeHDD(String hdd) {
        writeLog(hddUsage, hdd);
    }

    /**
     * Write network bandwidth
     *
     * @param received Bytes received
     * @param sent     Bytes sent
     */
    public void writeNetwork(String received, String sent) {
        writeLog(networkUsage, received + "/" + sent);
    }

    /**
     * Write number of "normal" viewers
     */
    public void writeCountNormalViewers(int viewers) {
        writeLog(totalNormalViewers, String.valueOf(viewers));
    }

    /**
     * Write number of moderators
     */
    public void writeCountModerators(int moderators) {
        writeLog(totalModerators, String.valueOf(moderators));
    }

    /**
     * Write total viewers
     */
    public void writeCountTotal(int total) {
        writeLog(totalViewers, String.valueOf(total));
    }

    private void writeLog(String fileName, String data) {
        if (!config.getProp("streamer.login").equals("") && config.getProp("streamer.login") != null) {
            try {
                String      filePath   = logFileDirPath + File.separator + config.getProp("streamer.login") + "_" + fileName + ".txt";
                File        file       = new File(filePath);
                PrintWriter fileWriter = new PrintWriter(file);
                fileWriter.print("");
                fileWriter.close();

                Files.write(Paths.get(filePath), (data).getBytes(), StandardOpenOption.WRITE);
            } catch (FileNotFoundException e) {
                logger.warning(fileName + " file was not found, can't write on it, please check your rights");
            } catch (IOException e) {
                e.printStackTrace();
                logger.warning("Can't write " + fileName + ", maybe right or bad ?");
            }
        }
    }


}