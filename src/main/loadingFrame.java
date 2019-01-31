package main;

import main.component.jpanel.ImagePanel;
import org.jdesktop.swingx.JXLabel;
import services.ConfigService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class loadingFrame extends JFrame {

    ConfigService config = ConfigService.getInstance();
    private JXLabel taskName = new JXLabel();

    public loadingFrame() {

        Dimension frameSize = new Dimension(400, 200);

        //BufferedImage imageBackground = ImageIO.read(getClass().getResourceAsStream(config.getProp("loading")));
        ImagePanel mainPanel = new ImagePanel(config.getProp("loading"));
        mainPanel.add(taskName);
        // Set your Image Here.
        setContentPane(mainPanel);
        setTitle(config.getProp("app.name"));
        setSize(frameSize);
        setPreferredSize(frameSize);
        setUndecorated(true);
        setResizable(false);
        setVisible(true);
        setAlwaysOnTop(true);

        this.setLocationRelativeTo(null);
        try {
            setIconImage(ImageIO.read(getClass().getResource(config.getProp("logoSmall"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        processTask();
    }

    private void processTask(){
        new LoadingTask(this.taskName).start();
    }

}
