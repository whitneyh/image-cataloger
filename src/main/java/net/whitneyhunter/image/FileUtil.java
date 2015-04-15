package net.whitneyhunter.image;

import java.io.File;

public class FileUtil {

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
