package main.component.jxtable.renderer;

import services.ConfigService;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CommandNameRenderer extends JTextArea implements TableCellRenderer {


    private final Color         evenColor = new Color(240, 240, 240);
    private       ConfigService config    = ConfigService.getInstance();

    public CommandNameRenderer() {
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
            //setForeground(table.getForeground());
            setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
            setBackground((row % 2 == 0) ? new Color(Integer.decode(config.getProp("color.primary"))) : new Color(Integer.decode(config.getProp("color.secondary"))));
        }
        setForeground(new Color(Integer.decode(config.getProp("color.table.text"))));

        setFont(table.getFont());
        setText((value == null) ? "" : value.toString());
        setEditable(false);
        return this;
    }


}