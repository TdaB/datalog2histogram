package com.refinery408;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import java.awt.Component;
import java.awt.Dimension;

public class RowHeaderRenderer extends JLabel implements ListCellRenderer<Double> {
    private final JTable table;

    RowHeaderRenderer(JTable table) {
        this.table = table;
        JTableHeader header = this.table.getTableHeader();
        setOpaque(true);
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
        setHorizontalAlignment(CENTER);
        setForeground(header.getForeground());
        setBackground(header.getBackground());
        setFont(header.getFont());
        setDoubleBuffered(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Double> list,
                                                  Double value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
        setText((value == null) ? "" : value.toString());
        setPreferredSize(null);
        setPreferredSize(new Dimension((int) getPreferredSize().getWidth(), table.getRowHeight(index)));
        // Trick to force repaint on JList (set updateLayoutStateNeeded = true) on BasicListUI
        list.firePropertyChange("cellRenderer", 0, 1);
        return this;
    }
}
