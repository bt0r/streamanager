package main.component.frame;

import org.jdesktop.swingx.JXFrame;
import services.ConfigService;
import services.LanguageService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Logger;

public class mainFrame extends JXFrame {
    protected ConfigService       config  = ConfigService.getInstance();
    protected LanguageService     trans   = LanguageService.getInstance();
    protected Logger              logger  = Logger.getLogger("streaManager");

    public mainFrame() {
        String applicationName = config.getProp("app.title") + " - v" + config.getProp("app.version");
        String smallIconPath = config.getProp("logoSmall");
        try {
            setIconImage(ImageIO.read(getClass().getResourceAsStream(smallIconPath)));
        } catch (IOException e1) {
            Logger logger = Logger.getLogger("streaManager");
            logger.warning("Can't load icon image from " + smallIconPath);
        }

        getContentPane().setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        getContentPane().setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));
        setResizable(true);
        setUndecorated(false);
        setTitle(applicationName);

    }

    protected void setHalfSize() {
        Dimension screenSize = getToolkit().getScreenSize();
        Integer   height     = (int) (screenSize.getHeight() / 2);
        Integer   width      = (int) (screenSize.getWidth() / 2);
        Dimension frameSize  = new Dimension(width, height);
        setPreferredSize(frameSize);
        setSize(frameSize);
    }

    protected void setAutoLocation(){
        Dimension screenSize      = getToolkit().getScreenSize();
        Integer   screenMidHeight = (int) (screenSize.getHeight() / 2);
        Integer   screenMidWidth  = (int) (screenSize.getWidth() / 2);
        int       w2    = (int) (getPreferredSize().getWidth() / 2);
        int       h2   = (int) (getPreferredSize().getHeight() / 2);
        setLocation(screenMidWidth - w2, screenMidHeight - h2);

    }

}
