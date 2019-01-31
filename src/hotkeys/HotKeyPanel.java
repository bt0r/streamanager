/*
 * Copyright © 2017 Streamanager.net all right reserved, for more informations contact us on streamanager.net
 */

package hotkeys;

import listeners.HotKeyMouseListener;
import main.component.border.CommonTitleBorder;
import main.component.button.CommonButton;
import main.component.jpanel.CommonPanel;
import main.component.jxlabel.Label;
import services.HotKeysService;
import services.LanguageService;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


/**
 * Created by btor on 08/02/2017.
 */
public class HotKeyPanel extends CommonPanel {
    private LanguageService trans         = LanguageService.getInstance();
    private Label           hotkeyLabel   = new Label();
    private Label           filenameLabel = new Label();

    public HotKeyPanel(int number) {

        setLayout(new BorderLayout());
        setName("hotkey_" + number);

        setBorder(new CommonTitleBorder(trans.getProp("hotkeys.panel") + number));

        // Header panel
        CommonPanel headerPanel = new CommonPanel();
        headerPanel.setLayout(new GridLayout(1, 0));


        hotkeyLabel.setName("hotkeyLabel");
        hotkeyLabel.setOpaque(true);
        hotkeyLabel.setIcon(new ImageIcon(getClass().getResource(config.getProp("keyIcon"))));


        filenameLabel.setName("filenameLabel");
        filenameLabel.setOpaque(true);
        filenameLabel.setIcon(new ImageIcon(getClass().getResource(config.getProp("soundIcon"))));

        headerPanel.add(hotkeyLabel);
        headerPanel.add(filenameLabel);


        // Center Panel
        Label messageBox = new Label();
        messageBox.setPreferredSize(new Dimension(400, 50));
        messageBox.setText(trans.getProp("hotkeys.edit"));
        messageBox.setHorizontalAlignment(SwingConstants.CENTER);


        // Footer Panel
        CommonPanel footerPanel = new CommonPanel();
        footerPanel.setLayout(new GridLayout(1, 0));

        CommonButton browseButton = new CommonButton();
        browseButton.setText(trans.getProp("component.browse"));
        browseButton.setIcon(new ImageIcon(getClass().getResource(config.getProp("browseIcon"))));

        CommonButton resetButton = new CommonButton();
        resetButton.setText(trans.getProp("component.reset"));
        resetButton.setIcon(new ImageIcon(getClass().getResource(config.getProp("trashIcon"))));

        footerPanel.add(browseButton);
        footerPanel.add(resetButton);


        add(headerPanel, BorderLayout.PAGE_START);
        add(messageBox, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.PAGE_END);

        setVisible(true);

        // Listeners
        addMouseListener(new HotKeyMouseListener(this, hotkeyLabel, messageBox));
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileFilter         audioFilter = new FileNameExtensionFilter("Audio", "mp3");
                final JFileChooser fc          = new JFileChooser();
                fc.addChoosableFileFilter(audioFilter);
                fc.setAcceptAllFileFilterUsed(false);
                int returnVal = fc.showOpenDialog(getRootPane());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    // File found
                    filenameLabel.setText(file.getName());
                    String filepath = file.getAbsolutePath();
                    System.out.println(filepath);
                    // Save to config file
                    config.setProp(getName() + "_path", filepath);

                    // Reload hotkeys
                    HotKeysService.load((HotKeysTab) getParent());

                }
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                try {
                    config.setProp(getName() + "_path", "");
                    config.setProp(getName() + "_keys", "");
                    filenameLabel.setText("");
                    hotkeyLabel.setText("");
                    HotKeysService.load((HotKeysTab) getParent());
                    JOptionPane.showMessageDialog(getRootPane(), trans.getProp("success.message"), trans.getProp("success.title"), 0, new ImageIcon(getClass().getResource(config.getProp("checkBigIcon"))));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(getRootPane(), trans.getProp("error.message"), trans.getProp("error.title"), 0, new ImageIcon(getClass().getResource(config.getProp("errorBigIcon"))));
                }
            }
        });
    }

    /**
     * Return Hotkey label
     * @return
     */
    public Label getHotkeyLabel() {
        return hotkeyLabel;
    }

    /**
     * Return filename label
     * @return
     */
    public Label getFilenameLabel() {
        return filenameLabel;
    }

}
