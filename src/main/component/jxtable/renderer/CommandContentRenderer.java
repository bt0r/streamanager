package main.component.jxtable.renderer;


import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class CommandContentRenderer extends JTextArea implements TableCellRenderer {


    private final Color evenColor = new Color(240, 240, 240);

    public CommandContentRenderer() {
        super();
        setLineWrap(true);
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            //setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            //setForeground(table.getForeground());
            setBackground(table.getBackground());
            setBackground((row % 2 == 0) ? evenColor : getBackground());
        }
        setFont(table.getFont());
        setEditable(true);
        setText((value == null) ? "" : value.toString());
        return this;
    }



}
