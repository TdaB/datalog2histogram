package com.refinery408;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

        this.setLayout(new GridBagLayout());
        this.addXRow();
        this.addYRow();
        this.addZRow();
        this.addMinHitsRow();
        this.addButton();
    }

    private void addXRow() {
        double xMin = config.getDouble("table.axis.xmin");
        double xMax = config.getDouble("table.axis.xmax");
        double xWidth = config.getDouble("table.axis.xwidth");

        JLabel xAxisLabel = new JLabel("Select X axis:");
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 0;
        constraints.weightx = 0;
        constraints.insets = new Insets(5, 0, 5, 5);
        this.add(xAxisLabel, constraints);

        this.xCombo = new JComboBox<>(new DefaultComboBoxModel<>(this.parser.getColumnNames().toArray(new String[0])));
        this.xCombo.setSelectedIndex(0);
        this.xCombo.addActionListener(new ComboListener());
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weighty = 0;
        constraints.weightx = 0;
        constraints.insets = new Insets(5, 0, 5, 10);
        this.add(this.xCombo, constraints);

        JLabel xMinLabel = new JLabel("X min:");
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.weighty = 0;
        constraints.weightx = 0;
        constraints.insets = new Insets(5, 0, 5, 5);
        this.add(xMinLabel, constraints);

        this.xMinText = new JTextField(String.valueOf(xMin), 32);
        this.xMinText.addKeyListener(new TextFieldListener());
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.weighty = 0;
        constraints.weightx = .5;
        constraints.insets = new Insets(5, 0, 5, 10);
        this.add(this.xMinText, constraints);

        JLabel xMaxLabel = new JLabel("X max:");
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 4;
        constraints.gridy = 0;
        constraints.weighty = 0;
        constraints.weightx = 0;
        constraints.insets = new Insets(5, 0, 5, 5);
        this.add(xMaxLabel, constraints);

        this.xMaxText = new JTextField(String.valueOf(xMax), 32);
        this.xMaxText.addKeyListener(new TextFieldListener());
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 5;
        constraints.gridy = 0;
        constraints.weighty = 0;
        constraints.weightx = .5;
        constraints.insets = new Insets(5, 0, 5, 10);
        this.add(this.xMaxText, constraints);

        JLabel xSpacingLabel = new JLabel("X spacing:");
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 6;
        constraints.gridy = 0;
        constraints.weighty = 0;
        constraints.weightx = 0;
        constraints.insets = new Insets(5, 0, 5, 5);
        this.add(xSpacingLabel, constraints);

        this.xSpacingText = new JTextField(String.valueOf(xWidth), 32);
        this.xSpacingText.addKeyListener(new TextFieldListener());
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 7;
        constraints.gridy = 0;
        constraints.weighty = 0;
        constraints.weightx = .5;
        constraints.insets = new Insets(5, 0, 5, 0);
        this.add(this.xSpacingText, constraints);
    }

    private void addYRow() {
        double yMin = config.getDouble("table.axis.ymin");
        double yMax = config.getDouble("table.axis.ymax");
        double yWidth = config.getDouble("table.axis.ywidth");

        JLabel yAxisLabel = new JLabel("Select Y axis:");
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weighty = 0;
        constraints.weightx = 0;
        constraints.insets = new Insets(0, 0, 5, 5);
        this.add(yAxisLabel, constraints);

        this.yCombo = new JComboBox<>(new DefaultComboBoxModel<>(this.parser.getColumnNames().toArray(new String[0])));
        this.yCombo.setSelectedIndex(1);
        this.yCombo.addActionListener(new ComboListener());
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.weighty = 0;
        constraints.weightx = 0;
        constraints.insets = new Insets(0, 0, 5, 10);
        this.add(this.yCombo, constraints);

        JLabel yMinLabel = new JLabel("Y min:");
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.weighty = 0;
        constraints.weightx = 0;
        constraints.insets = new Insets(0, 0, 5, 5);
        this.add(yMinLabel, constraints);

        this.yMinText = new JTextField(String.valueOf(yMin), 32);
        this.yMinText.addKeyListener(new TextFieldListener());
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 3;
        constraints.gridy = 1;
        constraints.weighty = 0;
        constraints.weightx = .5;
        constraints.insets = new Insets(0, 0, 5, 10);
        this.add(this.yMinText, constraints);

        JLabel yMaxLabel = new JLabel("Y max:");
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 4;
        constraints.gridy = 1;
        constraints.weighty = 0;
        constraints.weightx = 0;
        constraints.insets = new Insets(0, 0, 5, 5);
        this.add(yMaxLabel, constraints);

        this.yMaxText = new JTextField(String.valueOf(yMax), 32);
        this.yMaxText.addKeyListener(new TextFieldListener());
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 5;
        constraints.gridy = 1;
        constraints.weighty = 0;
        constraints.weightx = .5;
        constraints.insets = new Insets(0, 0, 5, 10);
        this.add(this.yMaxText, constraints);

        JLabel ySpacingLabel = new JLabel("Y spacing:");
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 6;
        constraints.gridy = 1;
        constraints.weighty = 0;
        constraints.weightx = 0;
        constraints.insets = new Insets(0, 0, 5, 5);
        this.add(ySpacingLabel, constraints);

        this.ySpacingText = new JTextField(String.valueOf(yWidth), 32);
        this.ySpacingText.addKeyListener(new TextFieldListener());
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 7;
        constraints.gridy = 1;
        constraints.weighty = 0;
        constraints.weightx = .5;
        constraints.insets = new Insets(0, 0, 5, 0);
        this.add(this.ySpacingText, constraints);
    }

    private void addZRow() {
        JLabel zAxisLabel = new JLabel("Select Z axis:");
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weighty = 0;
        constraints.weightx = 0;
        constraints.insets = new Insets(0, 0, 5, 5);
        this.add(zAxisLabel, constraints);

        this.zCombo = new JComboBox<>(new DefaultComboBoxModel<>(this.parser.getColumnNames().toArray(new String[0])));
        this.zCombo.setSelectedIndex(2);
        this.zCombo.addActionListener(new ComboListener());
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.weighty = 0;
        constraints.weightx = 0;
        constraints.insets = new Insets(0, 0, 5, 10);
        this.add(this.zCombo, constraints);

        JLabel zNeutralLabel = new JLabel("Z neutral:");
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.weighty = 0;
        constraints.weightx = 0;
        constraints.insets = new Insets(0, 0, 5, 5);
        this.add(zNeutralLabel, constraints);

        this.neutralText = new JTextField("0", 32);
        this.neutralText.addKeyListener(new TextFieldListener());
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 3;
        constraints.gridy = 2;
        constraints.weighty = 0;
        constraints.weightx = .5;
        constraints.insets = new Insets(0, 0, 5, 10);
        this.add(this.neutralText, constraints);

        JLabel zHighLabel = new JLabel("Z high:");
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 4;
        constraints.gridy = 2;
        constraints.weighty = 0;
        constraints.weightx = 0;
        constraints.insets = new Insets(0, 0, 5, 5);
        this.add(zHighLabel, constraints);

        this.highText = new JTextField(32);
        this.highText.addKeyListener(new TextFieldListener());
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 5;
        constraints.gridy = 2;
        constraints.weighty = 0;
        constraints.weightx = .5;
        constraints.insets = new Insets(0, 0, 5, 10);
        this.add(this.highText, constraints);

        JLabel zLowLabel = new JLabel("Z low:");
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 6;
        constraints.gridy = 2;
        constraints.weighty = 0;
        constraints.weightx = 0;
        constraints.insets = new Insets(0, 0, 5, 5);
        this.add(zLowLabel, constraints);

        this.lowText = new JTextField(32);
        this.lowText.addKeyListener(new TextFieldListener());
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 7;
        constraints.gridy = 2;
        constraints.weighty = 0;
        constraints.weightx = .5;
        constraints.insets = new Insets(0, 0, 5, 0);
        this.add(this.lowText, constraints);
    }

    private void addMinHitsRow() {
        JLabel label = new JLabel("Show cells with hits >");
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 4;
        constraints.weighty = 0;
        constraints.weightx = .5;
        constraints.insets = new Insets(0, 0, 5, 5);
        this.add(label, constraints);

        this.minHitsText = new JTextField("0", 32);
        this.minHitsText.addKeyListener(new TextFieldListener());
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 4;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.weighty = 0;
        constraints.weightx = .5;
        constraints.insets = new Insets(0, 0, 5, 10);
        this.add(this.minHitsText, constraints);
    }

    private void addButton() {
        this.button = new JButton("Generate Histogram");
        this.button.setMnemonic(KeyEvent.VK_G);
        this.button.addActionListener(new GenerateButtonListener());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 8;
        constraints.weighty = 0;
        constraints.weightx = 0;
        this.add(this.button, constraints);
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
