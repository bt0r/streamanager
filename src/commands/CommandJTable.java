package commands;

import main.component.jxtable.renderer.CellRenderer;
import main.component.jxtable.renderer.CommandNameRenderer;
import org.jdesktop.swingx.JXTable;
import services.ConfigService;
import services.LanguageService;

import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class CommandJTable extends JXTable {
    private LanguageService trans  = LanguageService.getInstance();
    private ConfigService   config = ConfigService.getInstance();

    public CommandJTable() {
        super();
        Object[] columnModel = {
                trans.getProp("command.name"),
                trans.getProp("command.content"),
                trans.getProp("command.state"),
                trans.getProp("command.action")
        };
        DefaultTableModel model = new DefaultTableModel(null, columnModel);
        setModel(model);
        setBackground(new Color(Integer.decode(config.getProp("color.primary"))));
        setForeground(new Color(Integer.decode(config.getProp("color.text.primary"))));
        getColumnModel().getColumn(0).setMinWidth(50);
        getColumnModel().getColumn(1).setMinWidth(400);
        getColumnModel().getColumn(2).setMinWidth(80);
        getColumnModel().getColumn(3).setMinWidth(80);

        setCellSelectionEnabled(true);
        setShowGrid(false);
        setSortable(true);
        getTableHeader().setReorderingAllowed(false);

        getColumnModel().getColumn(0).setCellRenderer(new CommandNameRenderer());
        getColumnModel().getColumn(1).setCellRenderer(new CellRenderer());
        getColumnModel().getColumn(2).setCellRenderer(new CellRenderer());

    }


    public boolean isCellEditable(int row, int column) {
        boolean value = true;
        if (column == 0 || column == 2) {
            value = false;
        }
        return value;
    }
}
