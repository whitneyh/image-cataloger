package net.whitneyhunter.image;

import java.io.File;
import java.time.LocalDateTime;

public class ImageMetadata {

    private File file;

    private LocalDateTime originalDate;

    public ImageMetadata(File file, LocalDateTime originalDate) {
        this.file = file;
        this.originalDate = originalDate;
    }

    public File getFile() {
        return file;
    }

    public LocalDateTime getOriginalDate() {
        return originalDate;
    }

}
