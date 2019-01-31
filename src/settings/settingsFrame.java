package settings;


import animations.LoadingAnimation;
import main.component.border.CommonTitleBorder;
import main.component.button.CommonButton;
import main.component.frame.mainFrame;
import main.component.jpanel.CommonPanel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class settingsFrame extends mainFrame {
    protected ImageIcon removeIcon = new ImageIcon(getClass().getResource(config.getProp("removeIcon")));
    protected ImageIcon addIcon    = new ImageIcon(getClass().getResource(config.getProp("addIcon")));

    public settingsFrame() {
        setTitle(trans.getProp("menu.settings"));
        setSize(new Dimension(500, 140));
        setPreferredSize(new Dimension(500, 140));
        setResizable(false);
        setAutoLocation();


        // MAIN PANEL
        CommonPanel settingsPanel = new CommonPanel();
        settingsPanel.setLayout(new GridLayout(2, 0));
        settingsPanel.setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        settingsPanel.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));
        settingsPanel.setOpaque(true);
        add(settingsPanel);


        // Notifications
        CommonPanel notifPanel = new CommonPanel();
        notifPanel.setLayout(new GridLayout(0, 3));
        TitledBorder notifTitle = new CommonTitleBorder(trans.getProp("notification.label"));
        notifTitle.setTitleColor(new Color(Integer.decode(config.getProp("color.text.primary"))));
        notifPanel.setBorder(notifTitle);

        // STATE
        JPanel                     statePanel      = new JPanel();
        JLabel                     notifState      = new JLabel(trans.getProp("notification.state"));
        final DefaultComboBoxModel notifStateModel = new DefaultComboBoxModel();
        notifStateModel.addElement(trans.getProp("common.enable"));
        notifStateModel.addElement(trans.getProp("common.disable"));
        JComboBox notifStateField = new JComboBox(notifStateModel);
        statePanel.add(notifState);
        statePanel.add(notifStateField);


        // DURATION
        JPanel           durationPanel = new JPanel();
        JLabel           notifDuration = new JLabel(trans.getProp("notification.duration"));
        final JTextField durationField = new JTextField();
        durationField.setPreferredSize(new Dimension(50, 20));
        durationPanel.add(notifDuration);
        durationPanel.add(durationField);


        // POSITION
        JPanel           positionPanel  = new JPanel();
        JLabel           positionLabel  = new JLabel(trans.getProp("notification.position"));
        final JTextField positionXField = new JTextField();
        positionXField.setPreferredSize(new Dimension(50, 20));
        final JTextField positionYField = new JTextField();
        positionYField.setPreferredSize(new Dimension(50, 20));
        positionXField.setSize(new Dimension());

        positionPanel.add(positionLabel);
        positionPanel.add(positionXField);
        positionPanel.add(positionYField);

        // THEMES
        JPanel themePanel = new JPanel();
        themePanel.setLayout(new GridLayout(0, 2));
        themePanel.setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        themePanel.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));
        themePanel.setOpaque(true);
        Border themeTitle = new CommonTitleBorder(trans.getProp("theme.label"));
        themePanel.setBorder(themeTitle);

        Label                      themeLabel = new Label(trans.getProp("theme.label"));
        final DefaultComboBoxModel themeModel = new DefaultComboBoxModel();
        themeModel.addElement(trans.getProp("theme.dark"));
        themeModel.addElement(trans.getProp("theme.light"));
        //themeModel.addElement(trans.getProp("theme.custom"));

        JComboBox themeField = new JComboBox(themeModel);
        themePanel.add(themeLabel, BorderLayout.WEST);
        themePanel.add(themeField, BorderLayout.CENTER);


        // CUSTOM COLORS
        /*JXPanel  customThemePanel = new JXPanel();
        JXButton primaryBGButton  = new JXButton(trans.getProp("theme.primary.background"));
        primaryBGButton.setPreferredSize(new Dimension(200, 50));
        JXButton secondaryBGButton = new JXButton(trans.getProp("theme.secondary.background"));
        secondaryBGButton.setPreferredSize(new Dimension(200, 50));
        JXButton primaryTXTButton = new JXButton(trans.getProp("theme.primary.text"));
        primaryTXTButton.setPreferredSize(new Dimension(200, 50));
        JXButton secondaryTXTButton = new JXButton(trans.getProp("theme.secondary.text"));
        secondaryTXTButton.setPreferredSize(new Dimension(200, 50));
        JXButton tablePrimaryButton = new JXButton(trans.getProp("theme.table.primary"));
        tablePrimaryButton.setPreferredSize(new Dimension(200, 50));
        JXButton tableSecondaryButton = new JXButton(trans.getProp("theme.table.secondary"));
        tableSecondaryButton.setPreferredSize(new Dimension(200, 50));
        JXButton tableWhisperButton = new JXButton(trans.getProp("theme.table.whisper"));
        tableWhisperButton.setPreferredSize(new Dimension(200, 50));

        themePanel.add(primaryBGButton);
        themePanel.add(primaryTXTButton);
        themePanel.add(secondaryBGButton);
        themePanel.add(secondaryTXTButton);
        themePanel.add(tablePrimaryButton);
        themePanel.add(tableSecondaryButton);
        themePanel.add(tableWhisperButton);*/


        // SAVE PANEL
        JPanel savePanel = new JPanel();
        savePanel.setLayout(new BorderLayout());
        savePanel.setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        savePanel.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));

        // SAVE
        CommonButton saveButton = new CommonButton(trans.getProp("common.save"));
        saveButton.setIcon(addIcon);
        CommonButton resetButton = new CommonButton(trans.getProp("menu.reset"));
        resetButton.setIcon(removeIcon);
        savePanel.add(resetButton, BorderLayout.WEST);
        savePanel.add(saveButton, BorderLayout.EAST);

        notifPanel.add(statePanel);
        notifPanel.add(durationPanel);
        notifPanel.add(positionPanel);

        //settingsPanel.add(notifPanel);
        settingsPanel.add(themePanel);
        settingsPanel.add(savePanel);

        if (! config.getProp("notification.state").equals("true")) {
            notifStateModel.setSelectedItem(trans.getProp("common.disable"));
        } else {
            notifStateModel.setSelectedItem(trans.getProp("common.enable"));
        }
        durationField.setText(config.getProp("notification.duration"));
        positionYField.setText(config.getProp("notification.y"));
        positionXField.setText(config.getProp("notification.x"));
        setVisible(true);

        // Listeners
        /*primaryBGButton.addMouseListener(new ColorThemeListener("color.primary", primaryBGButton));
        secondaryBGButton.addMouseListener(new ColorThemeListener("color.secondary", secondaryBGButton));
        primaryTXTButton.addMouseListener(new ColorThemeListener("color.text.primary", primaryTXTButton));
        secondaryTXTButton.addMouseListener(new ColorThemeListener("color.text.secondary", secondaryTXTButton));
        tablePrimaryButton.addMouseListener(new ColorThemeListener("color.table.primary", tablePrimaryButton));
        tableSecondaryButton.addMouseListener(new ColorThemeListener("color.table.secondary", tableSecondaryButton));
        tableWhisperButton.addMouseListener(new ColorThemeListener("color.table.whisper", tableWhisperButton));
    */


        saveButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                config.startTask("save_settings", trans.getProp("settings.saving"), LoadingAnimation.LEVEL_INFO);
                // THEMES
                try {
                    if (themeModel.getSelectedItem().equals("Dark")) {
                        config.setProp("color.theme", "dark");
                        config.setProp("color.primary", config.getProp("theme.dark.primary"));
                        config.setProp("color.secondary", config.getProp("theme.dark.secondary"));
                        config.setProp("color.text.primary", config.getProp("theme.dark.text.primary"));
                        config.setProp("color.text.secondary", config.getProp("theme.dark.text.secondary"));
                        config.setProp("color.table.primary", config.getProp("theme.dark.table.primary"));
                        config.setProp("color.table.secondary", config.getProp("theme.dark.table.secondary"));
                        config.setProp("color.table.whisper", config.getProp("theme.dark.table.whisper"));
                        config.setProp("color.table.text", config.getProp("theme.dark.table.text"));
                        config.setProp("color.table.focus", config.getProp("theme.dark.table.focus"));

                        // IMAGES
                        config.setProp("usersIcon", config.getProp("usersDarkIcon"));
                        config.setProp("userIcon", config.getProp("userDarkIcon"));

                    } else if (themeModel.getSelectedItem().equals("Light")) {
                        config.setProp("color.theme", "light");
                        config.setProp("color.primary", config.getProp("theme.light.primary"));
                        config.setProp("color.secondary", config.getProp("theme.light.secondary"));
                        config.setProp("color.text.primary", config.getProp("theme.light.text.primary"));
                        config.setProp("color.text.secondary", config.getProp("theme.light.text.secondary"));
                        config.setProp("color.table.primary", config.getProp("theme.light.table.primary"));
                        config.setProp("color.table.secondary", config.getProp("theme.light.table.secondary"));
                        config.setProp("color.table.whisper", config.getProp("theme.light.table.whisper"));
                        config.setProp("color.table.text", config.getProp("theme.light.table.text"));
                        config.setProp("color.table.focus", config.getProp("theme.light.table.focus"));

                        // IMAGES
                        config.setProp("usersIcon", config.getProp("usersLightIcon"));
                        config.setProp("userIcon", config.getProp("userLightIcon"));
                    } else {
                        config.setProp("color.theme", "custom");

                        // IMAGES
                        config.setProp("usersIcon", config.getProp("usersLightIcon"));
                        config.setProp("userIcon", config.getProp("userLightIcon"));
                    }
                    config.endTask("save_settings", trans.getProp("settings.saved"), LoadingAnimation.LEVEL_SUCCESS);
                    config.closeLoadingAndRestart();
                } catch (Exception e1) {
                    config.endTask("save_settings", trans.getProp("settings.save.error"), LoadingAnimation.LEVEL_ERROR);
                    config.closeLoadingAndRestart();
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

        resetButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                config.resetConfig();
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
