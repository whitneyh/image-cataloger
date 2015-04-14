package net.whitneyhunter.image;

import java.io.File;

public class FileUtil {

    private FileUtil() {

    }

    public static String getSuffix(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

}
