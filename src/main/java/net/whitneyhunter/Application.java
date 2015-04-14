package net.whitneyhunter;

import java.io.File;

import net.whitneyhunter.image.ImageMetadata;
import net.whitneyhunter.image.reader.ReaderStrategy;
import net.whitneyhunter.image.reader.ReaderStrategySupport;
import net.whitneyhunter.image.writer.WriterStrategySupport;

public class Application {

    public static void main(String[] args) throws Exception {
        new Application().run(args);
    }

    private void processFile(File inputBaseDir, File outputBaseDir)
            throws Exception {
        ReaderStrategy strategy = ReaderStrategySupport
                .getInstance(inputBaseDir);
        if (strategy != null) {
            ImageMetadata imageMetadata = strategy.read(inputBaseDir);
            WriterStrategySupport.getInstance(inputBaseDir).write(
                    outputBaseDir, imageMetadata);
        }
    }

    private void processFileOrDirectory(File inputBaseDir, File outputBaseDir)
            throws Exception {
        if (inputBaseDir.isDirectory()) {
            for (String contents : inputBaseDir.list()) {
                File item = new File(inputBaseDir.getAbsolutePath()
                        + File.separator + contents);
                processFileOrDirectory(item, outputBaseDir);
            }
        } else {
            processFile(inputBaseDir, outputBaseDir);
        }
    }

    private void run(String[] args) throws Exception {
        String inputBaseString = args[0];
        File inputBaseDir = new File(inputBaseString);
        String outputBaseString = args[1];
        File outputBaseDir = new File(outputBaseString);
        processFileOrDirectory(inputBaseDir, outputBaseDir);
    }

}
