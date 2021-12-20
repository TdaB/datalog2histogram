package com.refinery408;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.AbstractListModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class TabPanel extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(TabPanel.class);
    private static final Config config = ConfigFactory.defaultApplication();
    private CSVParser parser;
    private JComboBox<String> xCombo;
    private JComboBox<String> yCombo;
    private JComboBox<String> zCombo;
    private JButton button;
    private JPanel mainPanel;
    private JTextField neutralText;
    private JTextField highText;
    private JTextField lowText;
    private JPanel configPanel;
    private JTextField xMinText;
    private JTextField xMaxText;
    private JTextField xSpacingText;
    private JTextField yMinText;
    private JTextField yMaxText;
    private JTextField ySpacingText;
    private JPanel tablePanel;

    public TabPanel(CSVParser parser) {
        this.parser = parser;

        this.button = new JButton("Generate Histogram");
        this.button.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.button.setMnemonic(KeyEvent.VK_G);
        this.button.addActionListener(new GenerateButtonListener());
        this.button.setEnabled(false);

        int configRowSpacing = config.getInt("config.spacing.row");
        this.configPanel = new JPanel();
        this.configPanel.setLayout(new BoxLayout(this.configPanel, BoxLayout.PAGE_AXIS));
        this.configPanel.setMaximumSize(new Dimension(config.getInt("config.width"),
                                                      config.getInt("config.height")));
        this.configPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.configPanel.add(this.getXPanel());
        this.configPanel.add(Box.createRigidArea(new Dimension(0, configRowSpacing)));
        this.configPanel.add(this.getYPanel());
        this.configPanel.add(Box.createRigidArea(new Dimension(0, configRowSpacing)));
        this.configPanel.add(this.getZPanel());
        this.configPanel.add(Box.createRigidArea(new Dimension(0, configRowSpacing)));
        this.configPanel.add(this.button);

        this.initConfig();

        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.PAGE_AXIS));
        this.mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        this.mainPanel.add(this.configPanel);
        this.mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(this.mainPanel);
    }

    private JPanel getXPanel() {
        double xMin = config.getDouble("table.axis.xmin");
        double xMax = config.getDouble("table.axis.xmax");
        double xWidth = config.getDouble("table.axis.xwidth");

        JPanel xPanel = new JPanel();
        xPanel.setLayout(new BoxLayout(xPanel, BoxLayout.LINE_AXIS));
        JLabel xAxisLabel = new JLabel("Select x axis:");
        this.xCombo = new JComboBox<>();
        this.xCombo.setSelectedIndex(-1);
        this.xCombo.addActionListener(new ComboListener());
        JLabel xMinLabel = new JLabel("X min:");
        JLabel xMaxLabel = new JLabel("X max:");
        JLabel xSpacingLabel = new JLabel("X spacing:");
        this.xMinText = new JTextField(String.valueOf(xMin), 32);
        this.xMinText.addKeyListener(new TextFieldListener());
        this.xMaxText = new JTextField(String.valueOf(xMax), 32);
        this.xMaxText.addKeyListener(new TextFieldListener());
        this.xSpacingText = new JTextField(String.valueOf(xWidth), 32);
        this.xSpacingText.addKeyListener(new TextFieldListener());
        xPanel.add(xAxisLabel);
        xPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        xPanel.add(this.xCombo);
        xPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        xPanel.add(xMinLabel);
        xPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        xPanel.add(this.xMinText);
        xPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        xPanel.add(xMaxLabel);
        xPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        xPanel.add(this.xMaxText);
        xPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        xPanel.add(xSpacingLabel);
        xPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        xPanel.add(this.xSpacingText);
        return xPanel;
    }

    private JPanel getYPanel() {
        double yMin = config.getDouble("table.axis.ymin");
        double yMax = config.getDouble("table.axis.ymax");
        double yWidth = config.getDouble("table.axis.ywidth");

        JPanel yPanel = new JPanel();
        yPanel.setLayout(new BoxLayout(yPanel, BoxLayout.LINE_AXIS));
        JLabel yAxisLabel = new JLabel("Select y axis:");
        this.yCombo = new JComboBox<>();
        this.yCombo.setSelectedIndex(-1);
        this.yCombo.addActionListener(new ComboListener());
        JLabel yMinLabel = new JLabel("Y min:");
        JLabel yMaxLabel = new JLabel("Y max:");
        JLabel ySpacingLabel = new JLabel("Y spacing:");
        this.yMinText = new JTextField(String.valueOf(yMin), 32);
        this.yMinText.addKeyListener(new TextFieldListener());
        this.yMaxText = new JTextField(String.valueOf(yMax), 32);
        this.yMaxText.addKeyListener(new TextFieldListener());
        this.ySpacingText = new JTextField(String.valueOf(yWidth), 32);
        this.ySpacingText.addKeyListener(new TextFieldListener());
        yPanel.add(yAxisLabel);
        yPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        yPanel.add(this.yCombo);
        yPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        yPanel.add(yMinLabel);
        yPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        yPanel.add(this.yMinText);
        yPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        yPanel.add(yMaxLabel);
        yPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        yPanel.add(this.yMaxText);
        yPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        yPanel.add(ySpacingLabel);
        yPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        yPanel.add(this.ySpacingText);
        return yPanel;
    }

    private JPanel getZPanel() {
        JPanel zPanel = new JPanel();
        zPanel.setLayout(new BoxLayout(zPanel, BoxLayout.LINE_AXIS));
        JLabel zLabel = new JLabel("Select z axis:");
        this.zCombo = new JComboBox<>();
        this.zCombo.setSelectedIndex(-1);
        this.zCombo.addActionListener(new ComboListener());
        zPanel.add(zLabel);
        zPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        zPanel.add(this.zCombo);
        zPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        zPanel.add(new JLabel("Z neutral:"));
        zPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        this.neutralText = new JTextField("0", 32);
        this.neutralText.addKeyListener(new TextFieldListener());
        zPanel.add(this.neutralText);
        zPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        zPanel.add(new JLabel("Z high:"));
        zPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        this.highText = new JTextField(32);
        this.highText.addKeyListener(new TextFieldListener());
        zPanel.add(this.highText);
        zPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        zPanel.add(new JLabel("Z low:"));
        zPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        this.lowText = new JTextField(32);
        this.lowText.addKeyListener(new TextFieldListener());
        zPanel.add(this.lowText);
        return zPanel;
    }

    private void initConfig() {
        this.xCombo.setModel(new DefaultComboBoxModel<>(this.parser.getColumnNames().toArray(new String[0])));
        this.xCombo.setSelectedIndex(0);
        this.yCombo.setModel(new DefaultComboBoxModel<>(this.parser.getColumnNames().toArray(new String[0])));
        this.yCombo.setSelectedIndex(1);
        this.zCombo.setModel(new DefaultComboBoxModel<>(this.parser.getColumnNames().toArray(new String[0])));
        this.zCombo.setSelectedIndex(2);
        this.neutralText.setText("0");
        this.highText.setText("");
        this.lowText.setText("");
    }

    private JPanel generateTablePanel(String xAxis,
                                      String yAxis,
                                      String zAxis,
                                      Double neutral,
                                      Double high,
                                      Double low,
                                      Double xMin,
                                      Double xMax,
                                      Double xSteps,
                                      Double yMin,
                                      Double yMax,
                                      Double ySteps) throws Exception {
        this.parser.parseCsv(xAxis, yAxis, zAxis, neutral, high, low, xMin, xMax, xSteps, yMin, yMax, ySteps);

        HistogramTableModel tableModel = new HistogramTableModel(parser.getData(), parser.getxValues(), parser.getyValues());

        JTable table = new JTable(tableModel) {
            @Override
            public String getToolTipText(MouseEvent e) {
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                int realColumnIndex = convertColumnIndexToModel(colIndex);
                if (realColumnIndex < 0) return null;

                String hitAccuracy = tableModel.getHitPercentageAt(rowIndex, realColumnIndex) == null ?
                                     "" :
                                     String.format("Hit accuracy: %.1f%%",
                                                   tableModel.getHitPercentageAt(rowIndex, realColumnIndex) * 100);

                return String.format("<html>%s: %.0f, %s: %.0f<br>Hits: %d, %s",
                                     xAxis,
                                     parser.getxValues().toArray(new Double[0])[realColumnIndex],
                                     yAxis,
                                     parser.getyValues().toArray(new Double[0])[rowIndex],
                                     tableModel.getHitsAt(rowIndex, realColumnIndex),
                                     hitAccuracy);
            }
        };
        table.setRowHeight(32);
        ColumnCellRenderer cellRenderer = new ColumnCellRenderer(tableModel.getColors());
        for (Enumeration<TableColumn> e = table.getColumnModel().getColumns(); e.hasMoreElements(); ) {
            e.nextElement().setCellRenderer(cellRenderer);
        }
        table.setFont(new Font("Monospaced", Font.BOLD, 20));
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setRowHeaderView(buildRowHeader(table));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private boolean readyToGenerate() {
        if (this.xCombo.getSelectedIndex() == -1 ||
            this.yCombo.getSelectedIndex() == -1 ||
            this.zCombo.getSelectedIndex() == -1) {
            return false;
        }
        try {
            double xMin = Double.parseDouble(this.xMinText.getText());
            double xMax = Double.parseDouble(this.xMaxText.getText());
            double xSpacing = Double.parseDouble(this.xSpacingText.getText());
            double yMin = Double.parseDouble(this.yMinText.getText());
            double yMax = Double.parseDouble(this.yMaxText.getText());
            double ySpacing = Double.parseDouble(this.ySpacingText.getText());

            if (xMin >= xMax || yMin >= yMax || xSpacing <= 0 || xSpacing > xMax || ySpacing <= 0 || ySpacing > yMax) {
                return false;
            }

            Double neutral = this.neutralText.getText().isEmpty() ? null : Double.valueOf(this.neutralText.getText());
            Double high = this.highText.getText().isEmpty() ? null : Double.valueOf(this.highText.getText());
            Double low = this.lowText.getText().isEmpty() ? null : Double.valueOf(this.lowText.getText());

            if (high != null) {
                if (neutral == null || high <= neutral) {
                    return false;
                }
            }
            if (low != null) {
                if (neutral == null || low >= neutral) {
                    return false;
                }
            }
        } catch (Exception ex) {
            return false;
        }
        Set<String> selected = new HashSet<>();
        selected.add((String) this.xCombo.getSelectedItem());
        selected.add((String) this.yCombo.getSelectedItem());
        selected.add((String) this.zCombo.getSelectedItem());
        return selected.size() == 3;
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

    class ComboListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            button.setEnabled(readyToGenerate());
        }
    }

    class TextFieldListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent keyEvent) {
        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
            button.setEnabled(readyToGenerate());
        }
    }

    class GenerateButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // We know all input has been checked
            JPanel newTablePanel;
            try {
                Double neutral = neutralText.getText().isEmpty() ? null : Double.valueOf(neutralText.getText());
                Double high = highText.getText().isEmpty() ? null : Double.valueOf(highText.getText());
                Double low = lowText.getText().isEmpty() ? null : Double.valueOf(lowText.getText());
                newTablePanel = generateTablePanel(
                        (String) xCombo.getSelectedItem(),
                        (String) yCombo.getSelectedItem(),
                        (String) zCombo.getSelectedItem(),
                        neutral,
                        high,
                        low,
                        Double.parseDouble(xMinText.getText()),
                        Double.parseDouble(xMaxText.getText()),
                        Double.parseDouble(xSpacingText.getText()),
                        Double.parseDouble(yMinText.getText()),
                        Double.parseDouble(yMaxText.getText()),
                        Double.parseDouble(ySpacingText.getText()));
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            if (tablePanel != null) {
                mainPanel.remove(tablePanel);
            }
            mainPanel.add(newTablePanel);
            tablePanel = newTablePanel;
            mainPanel.revalidate();
            mainPanel.repaint();
        }
    }
}
