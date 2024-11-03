package main.java.com.tagextractor;

import java.io.*;
import java.nio.file.*;

public class FileUtils {
    public static void saveToFile(String content, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(content);
        }
    }

    public static String readFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    public static String chooseFile() {
        java.awt.FileDialog fd = new java.awt.FileDialog((java.awt.Frame) null, "Choose a file", java.awt.FileDialog.LOAD);
        fd.setVisible(true);
        if (fd.getFile() != null) {
            return fd.getDirectory() + fd.getFile();
        }
        return null;
    }

    public static String chooseSaveFile() {
        java.awt.FileDialog fd = new java.awt.FileDialog((java.awt.Frame) null, "Save file", java.awt.FileDialog.SAVE);
        fd.setVisible(true);
        if (fd.getFile() != null) {
            return fd.getDirectory() + fd.getFile();
        }
        return null;
    }

    public static boolean fileExists(String filename) {
        return Files.exists(Paths.get(filename));
    }
}
