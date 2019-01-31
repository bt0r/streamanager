package settings;


import animations.LoadingAnimation;
import org.json.JSONException;
import org.json.JSONObject;
import main.component.border.CommonTitleBorder;
import main.component.button.CommonButton;
import main.component.frame.mainFrame;
import main.component.jpanel.CommonPanel;
import main.component.jxlabel.CommonTextField;
import main.component.jxlabel.Label;
import services.TwitchAPIService;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class accountsFrame extends mainFrame {
    private TwitchAPIService twitchAPI = TwitchAPIService.getInstance();


    public accountsFrame() {
        setResizable(false);
        setTitle(trans.getProp("menu.accounts"));
        setSize(new Dimension(410, 200));
        setPreferredSize(new Dimension(410, 200));
        setAutoLocation();
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        CommonPanel settingsPanel = new CommonPanel();
        settingsPanel.setLayout(new BorderLayout());


        // LOGIN PANEL
        final CommonTextField usernameTextfield = new CommonTextField();
        final Label           usernameLabel     = new Label(trans.getProp("settings.username"));
        final Label           messageLabel      = new Label();
        messageLabel.setOpaque(true);
        CommonPanel loginPanel = new CommonPanel();
        Border      title      = new CommonTitleBorder(trans.getProp("settings.username"));
        loginPanel.setBorder(title);
        loginPanel.setLayout(new FlowLayout());

        usernameLabel.setPreferredSize(new Dimension(200, 20));
        usernameTextfield.setPreferredSize(new Dimension(200, 20));
        messageLabel.setPreferredSize(new Dimension(400, 40));
        messageLabel.setLineWrap(true);

        Border paddingBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        messageLabel.setBorder(BorderFactory.createCompoundBorder(paddingBorder, paddingBorder));

        loginPanel.add(usernameLabel, BorderLayout.WEST);
        loginPanel.add(usernameTextfield, BorderLayout.WEST);
        loginPanel.add(messageLabel, BorderLayout.SOUTH);
        settingsPanel.add(loginPanel);


        // SECOND CONTAINER
        CommonPanel  savePanel  = new CommonPanel();
        CommonButton saveButton = new CommonButton(trans.getProp("common.save"));
        savePanel.add(saveButton);


        settingsPanel.add(savePanel, BorderLayout.SOUTH);
        add(settingsPanel);
        setVisible(true);


        // Listeners
        saveButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e)  {
                // Save informations to config file
                config.startTask("save_accounts",trans.getProp("settings.saving"), LoadingAnimation.LEVEL_INFO);
                String username = usernameTextfield.getText();
                if (username.length() > 0) {

                    JSONObject response = twitchAPI.getJsonWebsite(username);
                    try{
                        if (!response.getBoolean("error")) {
                            JSONObject data = response.getJSONObject("data");
                            config.setProp("streamer.login", data.getString("twitchUsername"));
                            config.setProp("streamer.password", "oauth:" + data.getString("twitchToken"));
                            config.setProp("bot.login", data.getString("twitchBotUsername"));
                            config.setProp("bot.password", "oauth:" + data.getString("twitchBotToken"));
                            if(!data.isNull("gamewispToken") && !data.getString("gamewispToken").equals("")){
                                config.setProp("gamewisp.token", data.getString("gamewispToken"));
                            }

                            messageLabel.setBackground(new Color(10, 181, 8));
                            messageLabel.setForeground(new Color(255, 255, 255));
                            config.endTask("save_accounts",trans.getProp("settings.saved"),LoadingAnimation.LEVEL_SUCCESS);
                            config.closeLoadingAndRestart();

                        } else {
                            config.endTask("save_accounts",trans.getProp("settings.save.error"),LoadingAnimation.LEVEL_WARNING);
                            messageLabel.setBackground(new Color(139, 42, 18));
                            messageLabel.setForeground(new Color(255, 255, 255));
                        }
                        messageLabel.setText(response.getString("message"));
                    }catch(JSONException jsonExc){
                        config.endTask("save_accounts",trans.getProp("settings.save.error")+",error: "+jsonExc.getMessage(),LoadingAnimation.LEVEL_ERROR);
                        jsonExc.printStackTrace();
                    }

                }
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
}
