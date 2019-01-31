package main;

import services.ConfigService;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;


public class StreaManager {
    private static ConfigService config = ConfigService.getInstance();
    private static Logger        logger = Logger.getLogger("streaManager");


    public static void main(String[] args) {

        // Launch splashScreen
        new Thread() {
            public void run() {
                JFrame load = new loadingFrame();
                load.dispatchEvent(new WindowEvent(load, WindowEvent.WINDOW_CLOSING));
            }
        }.start();
        // Connection to database
        try {
            UIManager.setLookAndFeel(config.getProp("app.theme"));
        }catch (Exception e) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            } catch (Exception e2) {
                logger.severe("Can't change look and feel, not a linux/window OS.");

            }
        }
        new appFrame();

    }

}
