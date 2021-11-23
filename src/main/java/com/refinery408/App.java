package com.refinery408;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.io.File;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static final Config config = ConfigFactory.defaultApplication();
    private JFrame frame;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;

    public App() {
        this.tabbedPane = new JTabbedPane();
        this.tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        this.mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(this.tabbedPane);

        this.frame = new JFrame();
        this.frame.setJMenuBar(this.getMenuBar());
        this.frame.getContentPane().add(mainPanel);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.setTitle("Da Bomb Histogram");
        this.frame.setPreferredSize(new Dimension(config.getInt("window.width"),
                                                  config.getInt("window.height")));
        this.frame.pack();
        this.frame.setVisible(true);
    }

    private JMenuBar getMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        JMenuItem menuItem = new JMenuItem("Import CSV", KeyEvent.VK_I);
        menuItem.addActionListener(e -> {
            CSVParser parser = null;
            boolean ok = false;
            while (!ok) {
                String filePath = getFilePath();
                if (filePath == null) {
                    return;
                }
                try {
                    parser = new CSVParser(filePath);
                    ok = true;
                } catch (Exception ex) {
                    log.error("Failed to parse CSV header: " + ex.getMessage());
                    JOptionPane.showMessageDialog(this.frame,
                                                  "Failed to parse CSV header: " + ex.getMessage(),
                                                  "Import Error",
                                                  JOptionPane.ERROR_MESSAGE);
                }
            }
            this.tabbedPane.addTab(parser.getFileName(), new TabPanel(parser));
        });
        menu.add(menuItem);
        menuBar.add(menu);
        return menuBar;
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

    public static void main(String[] args) {
        new App();
    }
}
