package com.refinery408;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.Dimension;

public class TabPanel extends JPanel {
    private static final Config config = ConfigFactory.defaultApplication();
    private JPanel tablePanel;

    public TabPanel(CSVParser parser) {
        ConfigPanel configPanel = new ConfigPanel(this, parser);
        configPanel.setMaximumSize(new Dimension(config.getInt("config.width"),
                                                 config.getInt("config.height")));

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(configPanel);
        this.tablePanel = new JPanel();
        this.add(this.tablePanel);
    }

    public void replaceTablePanel(JPanel newTablePanel) {
        this.remove(tablePanel);
        this.add(newTablePanel);
        tablePanel = newTablePanel;
        revalidate();
        repaint();
    }
}
