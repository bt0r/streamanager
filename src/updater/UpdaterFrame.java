package updater;

import main.component.frame.mainFrame;
import main.component.jpanel.ImagePanel;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import services.ConfigService;
import services.LanguageService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class UpdaterFrame extends mainFrame {

    private Properties      prop           = new Properties();

    private LanguageService trans          = LanguageService.getInstance();
    private ConfigService   config         = ConfigService.getInstance();
    private JProgressBar    progressBar    = new JProgressBar(0, 100);
    private String          configSysDir   = config.getConfigSysDir();

    public UpdaterFrame() {

        Dimension  frameSize = new Dimension(400, 200);
        ImagePanel mainPanel = new ImagePanel(config.getProp("updaterIcon"));
        mainPanel.setBackground(new Color(0x303030));
        mainPanel.setOpaque(true);
        setContentPane(mainPanel);
        setLayout(new BorderLayout());
        setBackground(new Color(0x303030));
        setTitle("StreaManager Updater");
        setSize(frameSize);
        setPreferredSize(frameSize);
        setResizable(false);
        this.setLocationRelativeTo(null);

        // Bottom bar
        JXPanel bottomBar = new JXPanel();
        bottomBar.setLayout(new BorderLayout());

        // Text bar
        final JXLabel textBar = new JXLabel(trans.getProp("update.equal"));
        textBar.setBackground(new Color(0x151F34));
        textBar.setForeground(new Color(255, 255, 255));
        textBar.setOpaque(true);


        // ProgressBar
        progressBar.setValue(0);
        progressBar.setForeground(new Color(0x17477E));
        progressBar.setBackground(new Color(255, 255, 255));
        progressBar.setStringPainted(true);

        // Launch update button
        JXButton validateButton = new JXButton(trans.getProp("update.button"));
        validateButton.setEnabled(false);

        bottomBar.add(textBar, BorderLayout.NORTH);
        bottomBar.add(progressBar, BorderLayout.CENTER);
        bottomBar.add(validateButton, BorderLayout.EAST);

        add(bottomBar, BorderLayout.SOUTH);
        setVisible(true);

        SwingUtilities.invokeLater(() -> {
            // Check if a new version is available and enable button
            if(config.checkNewVersion()){
                validateButton.setEnabled(true);
                textBar.setText(trans.getProp("update.intro"));
            }
        });
        validateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                /**
                 * LAUNCH UPDATE
                 */
                if(config.checkNewVersion()) {
                    Map transMap = new HashMap<String, String>();
                    transMap.put("version", config.getNewVersion());
                    textBar.setText(trans.replaceTrans(transMap, "update.downloading"));

                    // Download new version
                    Thread downloadWorker = new Thread(new DownloadWorker(progressBar, textBar));
                    downloadWorker.start();

                }else {
                    // Same version
                    textBar.setText(trans.getProp("update.equal"));
                }
            }
        });
    }
}
