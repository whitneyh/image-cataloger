package net.whitneyhunter;

import java.io.File;

import net.whitneyhunter.image.ImageMetadata;
import net.whitneyhunter.image.reader.ReaderStrategy;
import net.whitneyhunter.image.reader.ReaderStrategySupport;
import net.whitneyhunter.image.writer.WriterStrategySupport;

public class Application {

    public static void main(String[] args) {
        new Application().run(args);
    }

    private void processFile(File inputFile, File outputBaseDir) {
        try {
            ReaderStrategy strategy = ReaderStrategySupport.getInstance(inputFile);
            if (strategy != null) {
                ImageMetadata imageMetadata = strategy.read(inputFile);
                WriterStrategySupport.getInstance(inputFile).write(outputBaseDir, imageMetadata);
            } else {
                System.out.println("No strategy found for: " + inputFile);
            }
        } catch (Exception e) {
            System.err.println("Failed to load: " + inputFile);
            e.printStackTrace();
        }
    }

    private void processFileOrDirectory(File input, File outputBaseDir) {
        if (input.isDirectory()) {
            for (String contents : input.list()) {
                File item = new File(input.getAbsolutePath() + File.separator + contents);
                processFileOrDirectory(item, outputBaseDir);
            }
        } else {
            processFile(input, outputBaseDir);
        }
    }

    private void run(String[] args) {
        String inputBaseString = args[0];
        File inputBaseDir = new File(inputBaseString);
        String outputBaseString = args[1];
        File outputBaseDir = new File(outputBaseString);
        processFileOrDirectory(inputBaseDir, outputBaseDir);
    }

}
