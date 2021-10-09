package com.refinery408;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;

import static com.refinery408.ColorUtils.invert;

public class ColumnCellRenderer extends DefaultTableCellRenderer {
    private final Color[][] colors;

    public ColumnCellRenderer(Color[][] colors) {
        this.colors = colors;
        this.setHorizontalAlignment(JLabel.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int col) {
        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        Color c = this.colors[row][col];
        if (c == null) {
            l.setBackground(Color.WHITE);
            l.setForeground(Color.BLACK);
        } else {
            l.setBackground(c);
            l.setForeground(invert(c));
        }
        return l;
    }
}
