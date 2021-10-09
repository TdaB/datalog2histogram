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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.JTableHeader;
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
import java.io.File;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static final Config config = ConfigFactory.defaultApplication();
    private CSVParser parser;
    private JFrame frame;
    private JComboBox<String> xCombo;
    private JComboBox<String> yCombo;
    private JComboBox<String> zCombo;
    private JButton button;
    private JPanel tablePanel;
    private JPanel mainPanel;
    private JTextField neutralText;
    private JTextField highText;
    private JTextField lowText;
    private JPanel configPanel;

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

    public App() {
        this.configPanel = new JPanel();
        this.configPanel.setLayout(new BoxLayout(this.configPanel, BoxLayout.PAGE_AXIS));
        this.configPanel.setMaximumSize(new Dimension(config.getInt("config.width"),
                                                      config.getInt("config.height")));
        this.configPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel xPanel = new JPanel();
        xPanel.setLayout(new BoxLayout(xPanel, BoxLayout.LINE_AXIS));
        JLabel xLabel = new JLabel("Select x axis:");
        this.xCombo = new JComboBox<>();
        this.xCombo.setSelectedIndex(-1);
        this.xCombo.addActionListener(new ComboListener());
        xPanel.add(xLabel);
        xPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        xPanel.add(this.xCombo);

        JPanel yPanel = new JPanel();
        yPanel.setLayout(new BoxLayout(yPanel, BoxLayout.LINE_AXIS));
        JLabel yLabel = new JLabel("Select y axis:");
        this.yCombo = new JComboBox<>();
        this.yCombo.setSelectedIndex(-1);
        this.yCombo.addActionListener(new ComboListener());
        yPanel.add(yLabel);
        yPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        yPanel.add(this.yCombo);

        JPanel zPanel = new JPanel();
        zPanel.setLayout(new BoxLayout(zPanel, BoxLayout.LINE_AXIS));
        JLabel zLabel = new JLabel("Select z axis:");
        this.zCombo = new JComboBox<>();
        this.zCombo.setSelectedIndex(-1);
        this.zCombo.addActionListener(new ComboListener());
        zPanel.add(zLabel);
        zPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        zPanel.add(this.zCombo);

        JPanel neutralPanel = new JPanel();
        neutralPanel.setLayout(new BoxLayout(neutralPanel, BoxLayout.LINE_AXIS));
        neutralPanel.add(new JLabel("Neutral value:"));
        neutralPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        this.neutralText = new JTextField("0", 32);
        this.neutralText.addKeyListener(new TextFieldListener());
        neutralPanel.add(this.neutralText);

        JPanel highPanel = new JPanel();
        highPanel.setLayout(new BoxLayout(highPanel, BoxLayout.LINE_AXIS));
        highPanel.add(new JLabel("High value:"));
        highPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        this.highText = new JTextField(32);
        this.highText.addKeyListener(new TextFieldListener());
        highPanel.add(this.highText);

        JPanel lowPanel = new JPanel();
        lowPanel.setLayout(new BoxLayout(lowPanel, BoxLayout.LINE_AXIS));
        lowPanel.add(new JLabel("Low value:"));
        lowPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        this.lowText = new JTextField(32);
        this.lowText.addKeyListener(new TextFieldListener());
        lowPanel.add(this.lowText);

        this.button = new JButton("Generate Histogram");
        this.button.setMnemonic(KeyEvent.VK_G);
        this.button.addActionListener(e -> {
            JPanel newTablePanel;
            try {
                Double neutral = this.neutralText.getText().isEmpty() ? null : Double.valueOf(this.neutralText.getText());
                Double high = this.highText.getText().isEmpty() ? null : Double.valueOf(this.highText.getText());
                Double low = this.lowText.getText().isEmpty() ? null : Double.valueOf(this.lowText.getText());
                newTablePanel = this.generateTablePanel(
                        (String) this.xCombo.getSelectedItem(),
                        (String) this.yCombo.getSelectedItem(),
                        (String) this.zCombo.getSelectedItem(),
                        neutral,
                        high,
                        low);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            if (this.tablePanel != null) {
                this.mainPanel.remove(this.tablePanel);
            }
            this.mainPanel.add(newTablePanel);
            this.tablePanel = newTablePanel;
            this.mainPanel.revalidate();
            this.mainPanel.repaint();
        });
        this.button.setEnabled(false);

        this.configPanel.add(xPanel);
        this.configPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        this.configPanel.add(yPanel);
        this.configPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        this.configPanel.add(zPanel);
        this.configPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        this.configPanel.add(neutralPanel);
        this.configPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        this.configPanel.add(highPanel);
        this.configPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        this.configPanel.add(lowPanel);
        this.configPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        this.configPanel.add(button);
        this.configPanel.setVisible(false);

        this.mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(this.configPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        this.frame = new JFrame();
        this.frame.setJMenuBar(this.getMenuBar());
        this.frame.getContentPane().add(mainPanel);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.setTitle("Da Bomb Histogram");
        this.frame.setPreferredSize(new Dimension(config.getInt("window.width"),
                                                  config.getInt("window.height")));
        this.frame.setLocation(100, 100);
        this.frame.pack();
        this.frame.setVisible(true);
    }

    private JMenuBar getMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        JMenuItem menuItem = new JMenuItem("Import CSV", KeyEvent.VK_I);
        menuItem.addActionListener(e -> {
            boolean ok = false;
            while (!ok) {
                String filePath = getFilePath();
                if (filePath == null) {
                    return;
                }
                try {
                    this.parser = new CSVParser(filePath);
                    ok = true;
                } catch (Exception ex) {
                    log.error("Failed to parse CSV: " + ex.getMessage());
                    JOptionPane.showMessageDialog(this.frame,
                                                  "Failed to parse CSV: " + ex.getMessage(),
                                                  "Import Error",
                                                  JOptionPane.ERROR_MESSAGE);
                }
            }
            this.xCombo.setModel(new DefaultComboBoxModel<>(this.parser.getColumns().toArray(new String[0])));
            this.xCombo.setSelectedIndex(0);
            this.yCombo.setModel(new DefaultComboBoxModel<>(this.parser.getColumns().toArray(new String[0])));
            this.yCombo.setSelectedIndex(1);
            this.zCombo.setModel(new DefaultComboBoxModel<>(this.parser.getColumns().toArray(new String[0])));
            this.zCombo.setSelectedIndex(2);
            this.neutralText.setText("0");
            this.highText.setText("");
            this.lowText.setText("");

            this.configPanel.setVisible(true);

            if (this.tablePanel != null) {
                this.mainPanel.remove(this.tablePanel);
                this.tablePanel = null;
            }
            this.mainPanel.revalidate();
            this.mainPanel.repaint();
        });
        menu.add(menuItem);
        menuBar.add(menu);
        return menuBar;
    }

    private JPanel generateTablePanel(String xAxis,
                                      String yAxis,
                                      String zAxis,
                                      Double neutral,
                                      Double high,
                                      Double low) throws Exception {
        this.parser.parseCsv(xAxis, yAxis, zAxis, neutral, high, low);

        HistogramTableModel tableModel = new HistogramTableModel(parser.getData(), parser.getxValues(), parser.getyValues());

        JTable table = new JTable(tableModel) {
            @Override
            public String getToolTipText(MouseEvent e) {
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                int realColumnIndex = convertColumnIndexToModel(colIndex);

                if (realColumnIndex < 0) return null;

                StringJoiner joiner = new StringJoiner(", ");
                joiner.add(String.format("%s: %.0f",
                                         xAxis,
                                         parser.getxValues().toArray(new Double[0])[realColumnIndex]));
                joiner.add(String.format("%s: %.0f",
                                         yAxis,
                                         parser.getyValues().toArray(new Double[0])[rowIndex]));
                joiner.add("Hits: " + tableModel.getHitsAt(rowIndex, realColumnIndex));

                return joiner.toString();
            }
        };
        table.setRowHeight(30);
        ColumnCellRenderer cellRenderer = new ColumnCellRenderer(tableModel.getColors());
        for (Enumeration<TableColumn> e = table.getColumnModel().getColumns(); e.hasMoreElements();) {
            e.nextElement().setCellRenderer(cellRenderer);
        }
        table.setFont(new Font("Monospaced", Font.BOLD, 30));
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

    private static String getFilePath() {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setDialogTitle("Select CSV file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Comma-separated values (CSV)", "csv", "CSV");
        fileChooser.setFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }
        return null;
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
        rowHeader.setFixedCellWidth(60);
        rowHeader.setCellRenderer(new RowHeaderRenderer(table));
        rowHeader.setBackground(table.getBackground());
        rowHeader.setForeground(table.getForeground());
        return rowHeader;
    }

    static class RowHeaderRenderer extends JLabel implements ListCellRenderer<Double> {
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

    public static void main(String[] args) {
        new App();
    }
}
