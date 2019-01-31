package services;

import configuration.LogFormat;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;


public class InternalConfigService {

    private Logger     logger = Logger.getLogger("streaManager");
    private Properties prop   = new Properties();

    /*
     * Constructeur privé
     */
    private InternalConfigService() {
        ConsoleHandler logHandler = new ConsoleHandler();
        LogFormat      logFormat  = new LogFormat();
        logHandler.setFormatter(logFormat);
        logger.addHandler(logHandler);
        load();
    }

    /*
     * Instance unique
     */
    private static InternalConfigService InternalConfigService = new InternalConfigService();

    private static class InternalConfigServiceHolder {
        private final static InternalConfigService InternalConfigService = new InternalConfigService();
    }

    /*
     * Points d'accès pour l'instance unique du singleton
     */
    public static InternalConfigService getInstance() {
        if (InternalConfigService == null) {
            InternalConfigService = new InternalConfigService();
        }
        return InternalConfigService.InternalConfigService;
    }

    public String getProp(String property) {
        return prop.getProperty(property);
    }

    private void load(){
        try {
            InputStream input = getClass().getResourceAsStream("/configuration/internal_config.properties");
            prop.load(input);

        } catch (Exception e) {
            logger.warning("Can't load Language.properties file");
            e.printStackTrace();
        }
    }

}