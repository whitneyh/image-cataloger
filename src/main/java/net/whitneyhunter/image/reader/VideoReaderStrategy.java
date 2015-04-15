package net.whitneyhunter.image.reader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;

import net.whitneyhunter.image.ImageMetadata;

public class VideoReaderStrategy implements ReaderStrategy {

    @Override
    public ImageMetadata read(File file) throws Exception {
        FileTime creation = (FileTime) Files.getAttribute(file.toPath(),
                "lastModifiedTime");
        return new ImageMetadata(file, LocalDateTime.ofInstant(
                creation.toInstant(), ZoneId.systemDefault()));
    }

}
