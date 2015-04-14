package net.whitneyhunter.image.writer;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

import net.whitneyhunter.image.FileUtil;
import net.whitneyhunter.image.ImageMetadata;

public class StandardWriterStrategy extends WriterStrategySupport {

    @Override
    public void write(File outputBaseDir, ImageMetadata imageMetadata) {
        LocalDateTime originalDate = imageMetadata.getOriginalDate();
        if (originalDate != null) {
            File outputPath = calculatePath(outputBaseDir, originalDate);
            String fileBaseName = calculateFileBase(originalDate);
            String fileSuffix = FileUtil.getSuffix(imageMetadata.getFile());
            int imageNumber = calculateImageNumber(outputPath, fileBaseName);
            File outputFilePath = calculateFilePath(outputPath, fileBaseName,
                    fileSuffix, imageNumber);
            copyFile(imageMetadata.getFile(), outputFilePath);
        } else {
            System.out.println("null value");
        }
    }

    private File calculatePath(File outputBaseDir, LocalDateTime originalDate) {
        int year = originalDate.getYear();
        String month = originalDate.getMonth().getDisplayName(TextStyle.FULL,
                Locale.US);
        String pathString = String.format("%4d%sFamily%s%s", year,
                File.separator, File.separator, month);
        return new File(outputBaseDir, pathString);
    }

    private String calculateFileBase(LocalDateTime originalDate) {
        int year = originalDate.getYear();
        int month = Integer.parseInt(originalDate.getMonth().getDisplayName(
                TextStyle.FULL_STANDALONE, Locale.US));
        int date = originalDate.getDayOfMonth();
        return String.format("%4d%02d%02d", year, month, date);
    }

    private int calculateImageNumber(File outputPath, String fileBaseName) {
        boolean isDirCreated = outputPath.mkdirs();
        int number = 1;
        String[] files = outputPath.list();
        for (String file : files) {
            if (file.startsWith(fileBaseName)) {
                number += 1;
            }
        }
        return number;
    }

    private File calculateFilePath(File outputPath, String fileBaseName,
            String fileSuffix, int imageNumber) {
        String fileName = String.format("%s_%04d.%s", fileBaseName,
                imageNumber, fileSuffix);
        return new File(outputPath, fileName);
    }

    private void copyFile(File inputFilePath, File outputFilePath) {
        System.out.println(inputFilePath + "\t" + outputFilePath);
//        Files.copy(inputFilePath, outputFilePath, REPLACE_EXISTING);
    }

}
