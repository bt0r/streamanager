package notifications;


import services.ConfigService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NotificationFrame extends javax.swing.JFrame {

    ConfigService config = ConfigService.getInstance();

    public NotificationFrame(String notificationText,String fontSize) {

        Dimension frameSize = new Dimension(400, 146);
        setUndecorated(true);
        setTitle(config.getProp("app.name"));
        setSize(frameSize);
        setPreferredSize(frameSize);
        setResizable(false);
        //setAlwaysOnTop(true);
        try {
            BufferedImage imageBackground = ImageIO.read(getClass().getResourceAsStream(config.getProp("notificationIcon")));
            JLabel notificationContent = new JLabel();
            notificationContent.setOpaque(true);
            if(fontSize.equals("small")){
                notificationContent.setFont(new Font(notificationContent.getFont().getName(), Font.PLAIN, 15));
                notificationContent.setMaximumSize(new Dimension(350,140));
                notificationContent.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
                notificationContent.setText("<html>"+notificationText+"</html>");
            }else{
                notificationContent.setFont(new Font(notificationContent.getFont().getName(), Font.PLAIN, 20));
                notificationContent.setText(notificationText);
            }

            notificationContent.setForeground(new Color(Integer.decode(config.getProp("color.white"))));
            notificationContent.setIcon(new ImageIcon(imageBackground));
            notificationContent.setAlignmentX(SwingConstants.CENTER);
            notificationContent.setAlignmentY(SwingConstants.CENTER);
            notificationContent.setHorizontalTextPosition(JLabel.CENTER);
            notificationContent.setVerticalTextPosition(JLabel.CENTER);

            setContentPane(notificationContent);

        } catch (IOException e) {

        }
        setVisible(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                dispose();
            }
        });

    }

}
