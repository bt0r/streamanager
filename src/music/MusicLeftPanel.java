package music;

import javax.swing.*;
import java.awt.*;

public class MusicLeftPanel extends JPanel {


    MusicLeftPanel() {
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);

        setBackground(new Color(50, 0, 50));
        setLayout(layout);

        for (int i = 0; i < 20; i++) {
            add(new MusicPlayerPanel());
        }


    }
}
