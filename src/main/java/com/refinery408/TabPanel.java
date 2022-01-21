package com.refinery408;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class TabPanel extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(TabPanel.class);
    private static final Config config = ConfigFactory.defaultApplication();
    private final CSVParser parser;
    private JPanel tablePanel;

    public TabPanel(CSVParser parser) {
        this.parser = parser;
        ConfigPanel configPanel = new ConfigPanel(this, this.parser);

        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 0;
        constraints.weightx = 0;
        constraints.insets = new Insets(5, 0, 0, 0);
        this.add(configPanel, constraints);

        this.tablePanel = new JPanel();
        constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weighty = 1;
        constraints.weightx = 1;
        this.add(this.tablePanel, constraints);
    }

    public void replaceTablePanel(JPanel newTablePanel) {
        this.remove(tablePanel);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weighty = 1;
        constraints.weightx = 1;
        constraints.insets = new Insets(5, 5, 5, 5);
        add(newTablePanel, constraints);
        tablePanel = newTablePanel;
        revalidate();
        repaint();
    }
}
