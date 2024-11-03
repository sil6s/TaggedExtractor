package main.java.com.tagextractor;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class TagExtractorGUI extends JFrame {
    private JTextField inputFileField;
    private JTextField stopWordsFileField;
    private JTextArea resultArea;
    private JButton extractButton;
    private JButton saveButton;
    private JButton browseInputButton;
    private JButton browseStopWordsButton;
    private TagExtractor tagExtractor;

    public TagExtractorGUI() {
        tagExtractor = new TagExtractor();
        setTitle("Silas's Tag/Keyword Extractor");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        inputFileField = new JTextField(20);
        stopWordsFileField = new JTextField(20);
        resultArea = new JTextArea(20, 50);
        extractButton = new JButton("Extract Tags");
        saveButton = new JButton("Save Results");
        browseInputButton = new JButton("Browse");
        browseStopWordsButton = new JButton("Browse");

        // Layout components
        setLayout(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createTitledBorder("File Selection"));
        topPanel.add(new JLabel("Input File:"));
        topPanel.add(inputFileField);
        topPanel.add(browseInputButton);
        topPanel.add(new JLabel("Stop Words File:"));
        topPanel.add(stopWordsFileField);
        topPanel.add(browseStopWordsButton);
        add(topPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Extracted Tags"));
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(extractButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        browseInputButton.addActionListener(e -> inputFileField.setText(FileUtils.chooseFile()));
        browseStopWordsButton.addActionListener(e -> stopWordsFileField.setText(FileUtils.chooseFile()));
        extractButton.addActionListener(e -> extractTags());
        saveButton.addActionListener(e -> saveResults());
    }

    private void extractTags() {
        String inputFile = inputFileField.getText();
        String stopWordsFile = stopWordsFileField.getText();

        try {
            if (!inputFile.isEmpty() && FileUtils.fileExists(inputFile) && !stopWordsFile.isEmpty() && FileUtils.fileExists(stopWordsFile)) {
                tagExtractor.loadStopWords(stopWordsFile);
                tagExtractor.extractTags(inputFile);
                displayTags();
            } else {
                JOptionPane.showMessageDialog(this, "Please select valid input and stop words files.", "File Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading files: " + ex.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayTags() {
        Map<String, Integer> tags = tagExtractor.getTagFrequency();
        String result = tags.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("\n"));
        resultArea.setText(result);
    }

    private void saveResults() {
        String savePath = FileUtils.chooseSaveFile();
        if (savePath != null) {
            try {
                FileUtils.saveToFile(resultArea.getText(), savePath);
                JOptionPane.showMessageDialog(this, "Results saved successfully.", "Save Results", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TagExtractorGUI().setVisible(true));
    }
}
