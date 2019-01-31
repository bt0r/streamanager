package about;

import main.component.button.CommonButton;
import main.component.frame.mainFrame;
import main.component.jpanel.CommonPanel;
import main.component.jxlabel.Label;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AboutFrame extends mainFrame {
    private String meIconPath = config.getProp("meIcon");

    public AboutFrame() {
        setPreferredSize(new Dimension(400, 200));
        setAutoLocation();

        setResizable(false);
        setTitle(trans.getProp("menu.about"));

        CommonPanel  aboutPanel       = new CommonPanel();
        String       facebookIconPath = config.getProp("facebookIcon");
        ImageIcon    facebookIcon     = new ImageIcon(getClass().getResource(facebookIconPath));
        String       twitterIconPath  = config.getProp("twitterIcon");
        ImageIcon    twitterIcon      = new ImageIcon(getClass().getResource(twitterIconPath));
        ImageIcon    meIcon           = new ImageIcon(getClass().getResource(meIconPath));
        CommonPanel  rightPanel       = new CommonPanel();
        CommonPanel  leftPanel        = new CommonPanel();
        Label        descriptionLabel = new Label(trans.getProp("about.description"));
        CommonButton facebookButton   = new CommonButton(facebookIcon);
        CommonButton twitterButton    = new CommonButton(twitterIcon);
        CommonButton websiteButton    = new CommonButton(trans.getProp("about.website"));


        facebookButton.setOpaque(true);
        facebookButton.setBorderPainted(false);
        facebookButton.setContentAreaFilled(false);
        twitterButton.setOpaque(true);
        twitterButton.setBorderPainted(false);
        twitterButton.setContentAreaFilled(false);
        websiteButton.setOpaque(true);
        websiteButton.setBorderPainted(false);
        websiteButton.setContentAreaFilled(false);

        leftPanel.add(new JLabel(meIcon), BorderLayout.WEST);
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(descriptionLabel, BorderLayout.PAGE_START);
        rightPanel.add(facebookButton, BorderLayout.CENTER);
        rightPanel.add(twitterButton, BorderLayout.LINE_START);
        rightPanel.add(websiteButton, BorderLayout.LINE_END);

        aboutPanel.setLayout(new BorderLayout());
        aboutPanel.add(leftPanel, BorderLayout.WEST);
        aboutPanel.add(rightPanel, BorderLayout.CENTER);


        add(aboutPanel);
        setVisible(true);
        pack();

		/*
         * Listeners
		 */
        facebookButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Tools.Tools.openWebpage(trans.getProp("about.facebook"));
            }


        });
        twitterButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Tools.Tools.openWebpage(trans.getProp("about.twitter"));
            }


        });
        websiteButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Tools.Tools.openWebpage(trans.getProp("about.website"));
            }


        });


    }

}
