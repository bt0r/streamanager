package chat;

import services.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import java.awt.*;
import java.util.logging.Logger;

/**
 * Created by btor on 19/08/2016.
 */
public class EventPanel extends JPanel {
    private ConfigService    config    = ConfigService.getInstance();
    private TwitchAPIService twitchAPI = TwitchAPIService.getInstance();
    private DatabaseService  db        = DatabaseService.getInstance();
    private LogService       chatLog   = LogService.getInstance();
    private Logger           logger    = Logger.getLogger("streaManager");
    private LanguageService  trans     = LanguageService.getInstance();
    private String           eventIcon = config.getProp("eventIcon");

    public EventPanel() {
        /*
         * Layout
		 */
        setBorder(BorderFactory.createEmptyBorder());
        setMinimumSize(new Dimension(150, 0));
        setLayout(new BorderLayout());


        // TITLE
        ImageIcon usersIcon  = new ImageIcon(getClass().getResource(eventIcon));
        JLabel    eventTitle = new JLabel(trans.getProp("chatTab.events"));
        eventTitle.setIcon(usersIcon);
        eventTitle.setHorizontalAlignment(SwingConstants.CENTER);
        eventTitle.setVerticalAlignment(SwingConstants.CENTER);
        eventTitle.setBackground(new Color(Integer.decode(config.getProp("color.secondary"))));
        eventTitle.setForeground(new Color(Integer.decode(config.getProp("color.text.secondary"))));

        eventTitle.setOpaque(true);

        // Event List Panel
        JScrollPane sc        = new JScrollPane();
        JPanel      eventList = new JPanel();
        sc.getViewport().add(eventList);
        sc.setBorder(BorderFactory.createEmptyBorder());
        sc.setVisible(true);

        // EVENT LIST
        eventList.setLayout(new BoxLayout(eventList, BoxLayout.Y_AXIS));
        eventList.setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        eventList.setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));


        // HARDWARE PANEL
        HardwarePanel hardwarePanel = new HardwarePanel();

        // TOP PANEL
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder());
        topPanel.add(eventTitle, BorderLayout.NORTH);
        topPanel.add(sc, BorderLayout.CENTER);

        // JSCROLLBAR
        JScrollPane sp = new JScrollPane(hardwarePanel);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setVisible(true);

        // SPLITPANE
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, sp);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(splitPane.getHeight() - 25);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setResizeWeight(0.9d);

        // DIVIDER OF SPLITPANE
        BasicSplitPaneDivider div = (BasicSplitPaneDivider) splitPane.getComponent(2);
        div.setBackground(new Color(Integer.decode(config.getProp("color.secondary"))));
        div.setBorder(null);

        add(splitPane, BorderLayout.CENTER);


        // CRONTASK
        new EventService(eventList);
        CronEvent cronTask = new CronEvent();
        cronTask.setInterval(10000);
        cronTask.start();

    }


}
