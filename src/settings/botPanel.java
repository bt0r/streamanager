package settings;

import services.ConfigService;
import services.LanguageService;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by btor on 28/02/2016.
 */
public class botPanel extends JPanel {
    private JTextField     login;
    private JPasswordField password;


    public botPanel(String titlePanel) {
        Border title = BorderFactory.createTitledBorder(titlePanel);
        setBorder(title);
        setLayout(new BorderLayout());
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new FlowLayout());

        // Login
        LanguageService trans      = LanguageService.getInstance();
        JLabel          loginLabel = new JLabel(trans.getProp("settings.login"));
        JTextField      login      = new JTextField();
        setLogin(login);
        login.setMaximumSize(new Dimension(300, 20));
        login.setPreferredSize(new Dimension(200, 20));

        // Password
        //JLabel passwordLabel = new JLabel(trans.getProp("settings.password"));

        ConfigService config        = ConfigService.getInstance();
        ImageIcon     icon          = new ImageIcon(getClass().getResource(config.getProp("helpIcon")));
        JLabel        passwordLabel = new JLabel(trans.getProp("settings.password"));
        passwordLabel.setIcon(icon);
        passwordLabel.setToolTipText(trans.getProp("settings.password.help"));

        JPasswordField password = new JPasswordField();
        setPassword(password);
        password.setMaximumSize(new Dimension(300, 20));
        password.setPreferredSize(new Dimension(200, 20));

        contentPanel.add(loginLabel, BorderLayout.WEST);
        contentPanel.add(login, BorderLayout.WEST);
        contentPanel.add(passwordLabel, BorderLayout.EAST);
        contentPanel.add(password, BorderLayout.EAST);
        add(contentPanel);

        passwordLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Tools.Tools.openWebpage("http://www.twitchapps.com/tmi");
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    // Get login textfield
    public JTextField getLogin() {
        return this.login;
    }

    // Get password passwordField
    public JPasswordField getPassword() {
        return this.password;
    }

    // Set login textfield
    public void setLogin(JTextField login) {
        this.login = login;
    }

    // Set login passwordField
    public void setPassword(JPasswordField password) {
        this.password = password;
    }
}
