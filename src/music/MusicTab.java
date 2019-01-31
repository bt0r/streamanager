package music;

import main.component.jpanel.CommonPanel;

import javax.swing.*;
import java.awt.*;

public class MusicTab extends CommonPanel {

    public MusicTab() {
        setBackground(new Color(255, 255, 255));

        BorderLayout mainLayout = new BorderLayout();
        setLayout(mainLayout);

        JScrollPane leftPanel = new JScrollPane();
        leftPanel.getViewport().add(new MusicLeftPanel());
        JPanel rightPanel = new MusicRightPanel();


        JSplitPane mainSplitPane = new JSplitPane();

		/*leftPanel.setBackground(new Color(12,144,14));
		leftPanel.setMinimumSize(new Dimension(500,0));
		leftPanel.setPreferredSize(new Dimension(1000,100));*/

        //rightPanel.setBackground(new Color(0,0,0));
        rightPanel.setMinimumSize(new Dimension(200, 0));
        rightPanel.setPreferredSize(new Dimension(200, 0));


        mainSplitPane.setLeftComponent(leftPanel);
        mainSplitPane.setRightComponent(rightPanel);
        mainSplitPane.setResizeWeight(1);

        add(mainSplitPane, BorderLayout.CENTER);
        setVisible(true);


    }


}
