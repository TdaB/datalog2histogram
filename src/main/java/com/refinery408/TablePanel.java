package com.refinery408;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

public class TablePanel extends JPanel {
    private static final Config config = ConfigFactory.defaultApplication();
    private final CSVParser parser;

    public TablePanel(CSVParser parser, TableConfig tableConfig) throws Exception {
        this.parser = parser;
        this.parser.parseCsv(tableConfig);

        HistogramTableModel tableModel = new HistogramTableModel(parser.getData(),
                                                                 parser.getxValues(),
                                                                 parser.getyValues(),
                                                                 tableConfig.getMinHits());

        JTable table = new JTable(tableModel) {
            @Override
            public String getToolTipText(MouseEvent e) {
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                int realColumnIndex = convertColumnIndexToModel(colIndex);
                if (realColumnIndex < 0) return null;

                String data = tableModel.getValueAt(rowIndex, realColumnIndex) == null ?
                              "" :
                              String.format("<br>Value: %s<br>Hits: %d, Hit accuracy: %.1f%%",
                                            tableModel.getValueAt(rowIndex, realColumnIndex),
                                            tableModel.getHitsAt(rowIndex, realColumnIndex),
                                            tableModel.getHitPercentageAt(rowIndex, realColumnIndex) * 100);

                return String.format("<html>%s: %.0f, %s: %.0f%s",
                                     tableConfig.getXLabel(),
                                     parser.getxValues().toArray(new Double[0])[realColumnIndex],
                                     tableConfig.getYLabel(),
                                     parser.getyValues().toArray(new Double[0])[rowIndex],
                                     data);
            }
        };
        table.setRowHeight(config.getInt("table.row.height"));
        ColumnCellRenderer cellRenderer = new ColumnCellRenderer(tableModel.getColors());
        for (Enumeration<TableColumn> e = table.getColumnModel().getColumns(); e.hasMoreElements(); ) {
            e.nextElement().setCellRenderer(cellRenderer);
        }
        table.setFont(new Font("Monospaced", Font.BOLD, 20));
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setRowHeaderView(buildRowHeader(table));

        this.setLayout(new GridLayout(1, 1));
        this.add(scrollPane);
    }

    private JList<Double> buildRowHeader(final JTable table) {
        final JList<Double> rowHeader = new JList<>(new AbstractListModel<Double>() {
            public int getSize() {
                return parser.getyValues().size();
            }

            public Double getElementAt(int index) {
                return parser.getyValues().toArray(new Double[0])[index];
            }
        });
        rowHeader.setOpaque(false);
        rowHeader.setFixedCellWidth(64);
        rowHeader.setCellRenderer(new RowHeaderRenderer(table));
        rowHeader.setBackground(table.getBackground());
        rowHeader.setForeground(table.getForeground());
        return rowHeader;
    }
}
