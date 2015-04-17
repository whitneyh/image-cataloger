package net.whitneyhunter.image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class FileUtil {

    public static LocalDateTime getEarlierDate(File file) throws IOException {
        FileTime created = (FileTime) Files.getAttribute(file.toPath(),
                "creationTime");
        FileTime modified = (FileTime) Files.getAttribute(file.toPath(),
                "lastModifiedTime");
        FileTime toUse = created.compareTo(modified) < 0 ? created : modified;
        return LocalDateTime.ofInstant(toUse.toInstant(),
                ZoneId.systemDefault());
    }

    public static String getNormalizedSuffix(File file) {
        return getSuffix(file).toUpperCase();
    }

    public static String getSuffix(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    private FileUtil() {
        // static methods only
    }

}
