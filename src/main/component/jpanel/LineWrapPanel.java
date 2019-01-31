package main.component.jpanel;

import org.jdesktop.swingx.JXPanel;
import services.ConfigService;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by btor on 24/09/2016.
 */
public class LineWrapPanel extends JXPanel {
    private ConfigService config = ConfigService.getInstance();

    public LineWrapPanel() {
        super();
        Border ALLBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Border messageBorder = BorderFactory.createLineBorder(new Color(Integer.decode(config.getProp("color.table.secondary"))));

        setBackground(new Color(Integer.decode(config.getProp("color.table.primary"))));
        setOpaque(true);
        setLayout(new FlowLayout(FlowLayout.LEFT));
        //setBorder(BorderFactory.createCompoundBorder(messageBorder, BorderFactory.createEmptyBorder(10, 10, 10, 10)));


    }

    public void setText(String text) {
        String[] textTable = text.split("\\s");

        for (String word : textTable) {
            if(!word.equals("")){
                JLabel wordLabel = new JLabel(word);
                int db = (int) (Math.random() * 255);
                wordLabel.setBackground(new Color(0xEE81B2));
                wordLabel.setOpaque(true);
                add(wordLabel);
            }


        }
    }
}
