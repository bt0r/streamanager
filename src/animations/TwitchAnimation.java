package animations;

import main.component.frame.mainFrame;
import org.jibble.simplewebserver.SimpleWebServer;

import java.io.File;


/**
 * Created by btor on 22/10/2016.
 */
public class TwitchAnimation extends mainFrame {


    public TwitchAnimation() {
        try {
            SimpleWebServer server  = new SimpleWebServer(new File("C:\\Users\\btor\\StreaManager\\html"), 80);
            String          destUri = "ws://localhost";


            setSize(800, 600);
            setLocationByPlatform(true);
            setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        setVisible(true);
        setAlwaysOnTop(true);
        animate();
    }

    private void animate() {


    }
}
