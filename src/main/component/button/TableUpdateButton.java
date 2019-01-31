package main.component.button;

import listeners.TableUpdateButtonListener;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.Vector;

/**
 * Created by btor on 16/06/2016.
 */
public class TableUpdateButton extends CommonButton implements TableCellRenderer, TableCellEditor {
    private int selectedRow;
    private int selectedColumn;
    Vector<TableUpdateButtonListener> listener;

    public TableUpdateButton(String text) {
        super(text);
        listener = new Vector<TableUpdateButtonListener>();
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (TableUpdateButtonListener l : listener) {
                    l.tableUpdateButtonClicked(selectedRow, selectedColumn);
                }
            }
        });
    }

    public void addTableButtonListener(TableUpdateButtonListener l) {
        listener.add(l);
    }

    public void removeTableButtonListener(TableUpdateButtonListener l) {
        listener.remove(l);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        return this;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
        selectedRow = row;
        selectedColumn = col;
        return this;
    }

    @Override
    public void addCellEditorListener(CellEditorListener arg0) {
    }

    @Override
    public void cancelCellEditing() {
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }

    @Override
    public boolean isCellEditable(EventObject arg0) {
        return false;
    }

    @Override
    public void removeCellEditorListener(CellEditorListener arg0) {
    }

    @Override
    public boolean shouldSelectCell(EventObject arg0) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        return true;
    }
}
