package main.component.jxtable.renderer;

import services.ConfigService;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CellRenderer extends JTextArea implements TableCellRenderer {


    private       ConfigService config    = ConfigService.getInstance();

    public CellRenderer() {
        super();
        setLineWrap(true);
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setBackground(new Color(Integer.decode(config.getProp("color.table.focus"))));
        } else {
            setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
            setBackground((row % 2 == 0) ? new Color(Integer.decode(config.getProp("color.primary"))) : new Color(Integer.decode(config.getProp("color.secondary"))));
        }
        setForeground(new Color(Integer.decode(config.getProp("color.table.text"))));
        setFont(table.getFont());
        setEditable(true);
        setText((value == null) ? "" : value.toString());
        return this;
    }
}