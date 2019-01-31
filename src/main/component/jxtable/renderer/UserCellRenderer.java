package main.component.jxtable.renderer;

import services.ConfigService;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class UserCellRenderer extends JTextArea implements TableCellRenderer {


    private final Color         evenColor = new Color(240, 240, 240);
    private       ConfigService config    = ConfigService.getInstance();


    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {


        int[] rgbArray = {000000, 646464, 2041818, 941313, 2236145, 25222252, 23025252, 3717639, 9857168, 2552055, 1682585};

        setText((value == null) ? "" : value.toString());
        setFont(table.getFont().deriveFont(Font.BOLD));
        setForeground(Color.BLACK);
        String content = getText();
        Color  userColor;

        if (content.length() < 20 && content.length() > 10) {
            int res = content.length() - 10;
            userColor = new Color(rgbArray[res]);
            setForeground(userColor);
        } else if (content.length() <= 10) {

            userColor = new Color(rgbArray[content.length()]);
            setForeground(userColor);
            //System.out.println(userColor);
        } else {
            userColor = new Color(rgbArray[0]);
            setForeground(userColor);

        }
        if (isSelected) {
            //setForeground(table.getSelectionForeground());
            setBackground(new Color(Integer.decode(config.getProp("color.table.focus"))));
        } else {
            //setForeground(table.getForeground());
            setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
            setBackground((row % 2 == 0) ? evenColor : getBackground());
        }


        return this;
    }


}