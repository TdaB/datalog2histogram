package com.refinery408;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class ConfigPanel extends JPanel {
    private static final Config config = ConfigFactory.defaultApplication();
    private final TabPanel tabPanel;
    private final CSVParser parser;
    private JComboBox<String> xCombo;
    private JComboBox<String> yCombo;
    private JComboBox<String> zCombo;
    private JTextField xMinText;
    private JTextField xMaxText;
    private JTextField xSpacingText;
    private JTextField yMinText;
    private JTextField yMaxText;
    private JTextField ySpacingText;
    private JTextField neutralText;
    private JTextField highText;
    private JTextField lowText;
    private JTextField minHitsText;
    private JButton button;

    public ConfigPanel(TabPanel tabPanel, CSVParser parser) {
        this.tabPanel = tabPanel;
        this.parser = parser;
        
        this.button = new JButton("Generate Histogram");
        this.button.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.button.setMnemonic(KeyEvent.VK_G);
        this.button.addActionListener(new GenerateButtonListener());

        int configRowSpacing = config.getInt("config.spacing.row");
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setPreferredSize(new Dimension(config.getInt("config.width"),
                                                        config.getInt("config.height")));
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(this.getXPanel());
        this.add(Box.createRigidArea(new Dimension(0, configRowSpacing)));
        this.add(this.getYPanel());
        this.add(Box.createRigidArea(new Dimension(0, configRowSpacing)));
        this.add(this.getZPanel());
        this.add(Box.createRigidArea(new Dimension(0, configRowSpacing)));
        this.add(this.getMinHitsPanel());
        this.add(Box.createRigidArea(new Dimension(0, configRowSpacing)));
        this.add(this.button);
    }

    private JPanel getXPanel() {
        double xMin = config.getDouble("table.axis.xmin");
        double xMax = config.getDouble("table.axis.xmax");
        double xWidth = config.getDouble("table.axis.xwidth");

        JPanel xPanel = new JPanel();
        xPanel.setLayout(new BoxLayout(xPanel, BoxLayout.LINE_AXIS));
        JLabel xAxisLabel = new JLabel("Select X axis:");
        this.xCombo = new JComboBox<>(new DefaultComboBoxModel<>(this.parser.getColumnNames().toArray(new String[0])));
        this.xCombo.setSelectedIndex(0);
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
        JLabel yAxisLabel = new JLabel("Select Y axis:");
        this.yCombo = new JComboBox<>(new DefaultComboBoxModel<>(this.parser.getColumnNames().toArray(new String[0])));
        this.yCombo.setSelectedIndex(1);
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
        JLabel zLabel = new JLabel("Select Z axis:");
        this.zCombo = new JComboBox<>(new DefaultComboBoxModel<>(this.parser.getColumnNames().toArray(new String[0])));
        this.zCombo.setSelectedIndex(2);
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

    private JPanel getMinHitsPanel() {
        JPanel minHitsPanel = new JPanel();
        minHitsPanel.setLayout(new BoxLayout(minHitsPanel, BoxLayout.LINE_AXIS));
        JLabel label = new JLabel("Show cells with hits >");
        minHitsPanel.add(label);
        minHitsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        this.minHitsText = new JTextField("0", 32);
        this.minHitsText.addKeyListener(new TextFieldListener());
        minHitsPanel.add(this.minHitsText);
        return minHitsPanel;
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
            Integer.parseInt(this.minHitsText.getText());

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

                TableConfig tableConfig = new TableConfig((String) xCombo.getSelectedItem(),
                                                          Double.parseDouble(xMinText.getText()),
                                                          Double.parseDouble(xMaxText.getText()),
                                                          Double.parseDouble(xSpacingText.getText()),
                                                          (String) yCombo.getSelectedItem(),
                                                          Double.parseDouble(yMinText.getText()),
                                                          Double.parseDouble(yMaxText.getText()),
                                                          Double.parseDouble(ySpacingText.getText()),
                                                          (String) zCombo.getSelectedItem(),
                                                          neutral,
                                                          high,
                                                          low,
                                                          Integer.parseInt(minHitsText.getText()));
                tabPanel.replaceTablePanel(new TablePanel(parser, tableConfig));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
