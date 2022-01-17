package com.refinery408;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);
    private static final Config config = ConfigFactory.defaultApplication();
    private JFrame frame;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JButton importButton;

    public App() {
        this.importButton = new JButton("Import CSV");
        this.importButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.importButton.setMnemonic(KeyEvent.VK_I);
        this.importButton.addActionListener(new ImportButtonListener());

        this.tabbedPane = new JTabbedPane();
        this.tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        this.mainPanel.add(this.importButton);
        this.mainPanel.add(this.tabbedPane);

        this.frame = new JFrame();
        this.frame.getContentPane().add(this.mainPanel);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.setTitle("Da Bomb Histogram");
        this.frame.pack();
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.frame.setVisible(true);
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

    class ImportButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
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
                    JOptionPane.showMessageDialog(frame,
                                                  "Failed to parse CSV header: " + ex.getMessage(),
                                                  "Import Error",
                                                  JOptionPane.ERROR_MESSAGE);
                }
            }
            tabbedPane.addTab(parser.getFileName().split("\\.(CSV|csv)")[0], new TabPanel(parser));
            int index = tabbedPane.getTabCount() - 1;
            tabbedPane.setTabComponentAt(index, new ButtonTabComponent(tabbedPane));
            tabbedPane.setSelectedIndex(index);
        }
    }

    public static void main(String[] args) {
        new App();
    }
}
