package main.component.jxtable.renderer;

import services.ConfigService;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MessageCellRenderer extends JTextArea implements TableCellRenderer {


    private final Color         evenColor = new Color(240, 240, 240);
    private       ConfigService config    = ConfigService.getInstance();

    public MessageCellRenderer() {
        super();
        setLineWrap(true);
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setLineWrap(true);
        setWrapStyleWord(true);
        String content = (String) table.getModel().getValueAt(row, 3);

        /*int    sizeOfContent = (int) (content.length() * (getFont().getSize() / 1.6));


        double width   = getSize().getWidth();
        double height  = table.getRowHeight(row);
        int    rowSize = 20;
        int    res     = 1;

        res = rowSize * ((int) (sizeOfContent / width));
        if (content.length() < 50) {
            table.setRowHeight(row, 20);
        } else if (content.length() >= 50 && content.length() < 200) {
            table.setRowHeight(row, 40);
        } else {
            table.setRowHeight(row, res);
        }*/
        if (isSelected) {
            //table.setRowHeight(row, res + 20);
            setToolTipText(content);
            setBackground(new Color(Integer.decode(config.getProp("color.table.focus"))));
        } else {
            setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
            setBackground((row % 2 == 0) ? evenColor : getBackground());
        }
        //System.out.println("res: "+res+" w:"+width+"");
        setFont(table.getFont());
        setText((value == null) ? "" : value.toString());
        return this;
    }


}