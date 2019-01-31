package commands;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import database.Command;
import listeners.TableCellListener;
import main.component.border.CommonTitleBorder;
import main.component.button.CommonButton;
import main.component.jpanel.CommonPanel;
import main.component.jxlabel.CommonTextField;
import main.component.jxtable.renderer.ButtonColumnRenderer;
import org.jdesktop.swingx.JXTable;
import services.ConfigService;
import services.DatabaseService;
import services.LanguageService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by btor on 24/08/2016.
 */
public class CustomPanel extends CommonPanel {
    private ConfigService   config  = ConfigService.getInstance();
    private LanguageService trans   = LanguageService.getInstance();
    private DatabaseService db      = DatabaseService.getInstance();
    private ImageIcon       addIcon = new ImageIcon(getClass().getResource(config.getProp("addIcon")));
    private Logger          logger  = Logger.getLogger("streaManager");

    public CustomPanel() {

        // COMMAND PANEL
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // COMMAND JTABLE
        final CommandJTable commandJtable = new CommandJTable();


        // COMMAND ADD PANEL
        CommonPanel commandNamePanel = new CommonPanel();

        Label                 commandNameLabel  = new Label(trans.getProp("command.name"));
        final CommonTextField customCommandName = new CommonTextField();
        customCommandName.setPreferredSize(new Dimension(150, 20));
        commandNamePanel.add(commandNameLabel);
        commandNamePanel.add(customCommandName);


        CommonPanel           commandContentPanel  = new CommonPanel();
        Label                 commandContentLabel  = new Label(trans.getProp("command.content"));
        final CommonTextField customCommandContent = new CommonTextField();
        customCommandContent.setPreferredSize(new Dimension(470, 20));
        commandContentPanel.add(commandContentLabel);
        commandContentPanel.add(customCommandContent);


        CommonPanel                comboboxPanel      = new CommonPanel();
        final DefaultComboBoxModel customCommandModel = new DefaultComboBoxModel();
        customCommandModel.addElement(trans.getProp("common.enable"));
        customCommandModel.addElement(trans.getProp("common.disable"));
        final JComboBox customCommandState = new JComboBox(customCommandModel);
        comboboxPanel.add(customCommandState);


        CommonButton addCommandBtn = new CommonButton();
        addCommandBtn.setText(trans.getProp("common.add"));
        addCommandBtn.setIcon(addIcon);

        // ADD COMMAND PANEL
        CommonPanel addCommandPanel = new CommonPanel();
        Border      addCommandTitle = new CommonTitleBorder(trans.getProp("command.new.title"));
        addCommandPanel.setBorder(addCommandTitle);
        addCommandPanel.setLayout(new BoxLayout(addCommandPanel, BoxLayout.X_AXIS));
        addCommandPanel.add(commandNamePanel);
        addCommandPanel.add(commandContentPanel);
        addCommandPanel.add(comboboxPanel);

        Action delete = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JXTable           table       = (JXTable) e.getSource();
                int               modelRow    = Integer.valueOf(e.getActionCommand());
                DefaultTableModel model       = ((DefaultTableModel) table.getModel());
                String            commandName = (String) model.getValueAt(modelRow, 0);
                // DELETING COMMAND
                Dao<Command, ?> commandDAO = null;
                try {
                    commandDAO = DaoManager.createDao(db.getConnectionSource(), Command.class);
                    PreparedQuery<Command> qb          = commandDAO.queryBuilder().where().eq("name", commandName).prepare();
                    List<Command>          commandList = commandDAO.query(qb);
                    Iterator<Command>      commandIt   = commandList.iterator();
                    while (commandIt.hasNext()) {
                        Command command =  commandIt.next();
                        commandDAO.delete(command);
                        logger.severe(command.getName() + ": " + trans.getProp("command.deleted"));
                    }
                } catch (SQLException e1) {
                    logger.severe(trans.getProp("err.database.dao"));
                }
                model.removeRow(modelRow);
            }
        };
        Action cellAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
                if (tcl.getColumn() == 1) {
                    // COMMAND CONTENT CHANGED
                    Dao<Command, ?> commandDAO = null;
                    try {
                        String commandName = (String) commandJtable.getModel().getValueAt(tcl.getRow(), 0);
                        commandDAO = DaoManager.createDao(db.getConnectionSource(), Command.class);
                        PreparedQuery<Command> qb          = commandDAO.queryBuilder().where().eq("name", commandName).prepare();
                        List<Command>          commandList = commandDAO.query(qb);
                        Iterator<Command>      commandIt   = commandList.iterator();
                        while (commandIt.hasNext()) {
                            Command command = commandIt.next();
                            command.setMessage((String) tcl.getNewValue());
                            commandDAO.update(command);
                        }
                    } catch (SQLException e1) {
                        logger.severe(trans.getProp("err.database.dao"));
                    }
                }
            }
        };

        TableCellListener tcl = new TableCellListener(commandJtable, cellAction);

        addCommandPanel.add(addCommandBtn);
        ButtonColumnRenderer btnColumn = new ButtonColumnRenderer(commandJtable, delete, 3);


        JScrollPane commandScrollTable = new JScrollPane();
        commandScrollTable.getViewport().add(commandJtable);
        commandScrollTable.setBorder(BorderFactory.createEmptyBorder());
        commandScrollTable.setVisible(true);

        add(commandScrollTable);
        add(addCommandPanel);
        setVisible(true);

        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {}

            @Override
            public void componentMoved(ComponentEvent e) {}

            @Override
            public void componentShown(ComponentEvent e) {
                refreshTable(commandJtable);
            }

            @Override
            public void componentHidden(ComponentEvent e) {}
        });

        addCommandBtn.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!customCommandContent.getText().isEmpty() && !customCommandName.getText().isEmpty()) {
                    Dao<Command, ?> commandDAO = null;
                    try {
                        commandDAO = DaoManager.createDao(db.getConnectionSource(), Command.class);
                    } catch (SQLException e1) {
                        logger.severe(trans.getProp("err.database.dao"));
                    }
                    boolean isEnable = true;
                    if (customCommandModel.getIndexOf(customCommandModel.getSelectedItem()) == 1) {
                        isEnable = false;
                    }
                    Command command = new Command();
                    command.setIsEnable(isEnable);
                    command.setMessage(customCommandContent.getText());
                    command.setName(customCommandName.getText());
                    try {
                        commandDAO.create(command);
                        refreshTable(commandJtable);
                        customCommandContent.setText("");
                        customCommandName.setText("");
                    } catch (SQLException e1) {
                        logger.severe(trans.getProp("err.command.create"));
                    }
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });


    }

    private void refreshTable(CommandJTable commandJTable) {
        DefaultTableModel commandTableModel = (DefaultTableModel) commandJTable.getModel();
        int               totalRows         = commandTableModel.getRowCount();
        // REMOVE ALL ROWS
        for (int i = 0; i < totalRows; i++) {
            commandTableModel.removeRow(0);
        }

        // RETRIEVE DATA FROM DATABASE
        Dao<Command, ?> commandDAO = null;
        try {
            commandDAO = DaoManager.createDao(db.getConnectionSource(), Command.class);
        } catch (SQLException e1) {
            logger.severe(trans.getProp("err.database.dao"));
        }
        try {
            PreparedQuery<Command> qb           = commandDAO.queryBuilder().prepare();
            List<Command>          commands     = commandDAO.query(qb);
            Iterator               commandsList = commands.iterator();
            while (commandsList.hasNext()) {
                Command  command = (Command) commandsList.next();
                String   state   = command.getIsEnable() ? "enable" : "disable";
                Object[] row     = {command.getName(), command.getMessage(), trans.getProp("common." + state), trans.getProp("common.delete")};
                commandTableModel.addRow(row);
            }
        } catch (SQLException e) {
            logger.severe(trans.getProp("err.database.retrieve" + " commands !"));
        }
        // PUT DATA ON JXTABLE


    }


}
