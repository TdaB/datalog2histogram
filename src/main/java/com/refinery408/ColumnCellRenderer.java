package com.refinery408;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;

public class ColumnCellRenderer extends DefaultTableCellRenderer {
    private Color[][] colors;

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

    private static Color invert(Color c) {
        if (c == null) return null;
        return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
    }
}
