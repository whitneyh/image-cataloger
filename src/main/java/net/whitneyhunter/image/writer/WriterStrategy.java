package net.whitneyhunter.image.writer;

import java.io.File;

import net.whitneyhunter.image.ImageMetadata;

public interface WriterStrategy {

    void write(File baseDir, ImageMetadata imageMetadata);

}
