package giveAway;

import main.component.jpanel.CommonPanel;

import javax.swing.*;
import java.awt.*;


public class GiveAwayTab extends CommonPanel {
    public GiveAwayTab() {
        JLabel title = new JLabel("GiveAway");
        setBackground(new Color(0, 0, 0));
        add(title);
        setVisible(true);
    }

}
