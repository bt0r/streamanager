package services;

import com.vdurmont.emoji.EmojiManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageService {
    private Logger     logger     = Logger.getLogger("streaManager");
    private Properties prop       = new Properties();
    private String     configPath = "/configuration/lang.fr.properties";

    /*
     * Constructeur privé
     */
    private LanguageService() {
        getConfig();
    }

    /*
     * Instance unique
     */
    private static LanguageService LanguageService = new LanguageService();

    private static class LanguageServiceHolder {
        private final static LanguageService ConfigService = new LanguageService();
    }

    /*
     * Points d'accès pour l'instance unique du singleton
     */
    public static LanguageService getInstance() {
        if (LanguageService == null) {
            LanguageService = new LanguageService();
        }
        return LanguageService.LanguageService;
    }

    public void getConfig() {
        try {
            InputStream input = getClass().getResourceAsStream(configPath);
            prop.load(input);

        } catch (Exception e) {
            logger.warning("Can't load Language.properties file");
            e.printStackTrace();
        }
    }

    public String getProp(String propertie) {
        String transSTR = prop.getProperty(propertie);
        transSTR = replaceEmoji(transSTR);

        return transSTR;
    }

    public void setProp(String key, String value) {
        this.prop.setProperty(key, value);
        try {
            OutputStream output = new FileOutputStream(configPath);
            prop.store(output, "");
        } catch (IOException e) {
            logger.warning("Can't save properties in " + configPath + ", error: " + e.getMessage());
        }
    }

    public String replaceTrans(Map values, String properties) {
        // Replace keys by its value in trans properties
        String   transSTR = this.getProp(properties);
        Iterator valuesIT = values.entrySet().iterator();
        while (valuesIT.hasNext()) {
            Map.Entry pair  = (Map.Entry) valuesIT.next();
            String    key   = pair.getKey().toString();
            String    value = pair.getValue().toString();
            transSTR = transSTR.replaceAll("\\." + key + "\\.", value);
        }
        transSTR = replaceEmoji(transSTR);

        return transSTR;
    }

    /**
     * Replace emojis
     *
     * @param transSTR
     */
    private String replaceEmoji(String transSTR) {
        /* Search for emojis */
        Pattern p = Pattern.compile("%(\\w+)%");
        Matcher m = p.matcher(transSTR);
        while (m.find()) {
            String emoteName = m.group(1);
            transSTR = transSTR.replaceAll("%" + emoteName + "%", EmojiManager.getForAlias(emoteName).getUnicode());
        }
        return transSTR;
    }
}