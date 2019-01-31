package main.component.jxtable.renderer;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MessageHLRenderer extends JTextArea implements TableCellRenderer {


    private final Color evenColor = new Color(240, 240, 240);

    public MessageHLRenderer() {
        super();
        setLineWrap(true);
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        setLineWrap(true);
        setWrapStyleWord(true);


        String content       = (String) table.getModel().getValueAt(row, 3);
        if (isSelected) {
            //table.setRowHeight(row, res + 20);
            setToolTipText(content);
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(new Color(0xC86058));

        }
        setFont(table.getFont());
        setText((value == null) ? "" : value.toString());
        return this;
    }


}