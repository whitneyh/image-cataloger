package net.whitneyhunter.image.writer;

import java.io.File;
import java.io.IOException;

import net.whitneyhunter.image.ImageMetadata;

public interface WriterStrategy {

    void write(File baseDir, ImageMetadata imageMetadata) throws IOException;

}
