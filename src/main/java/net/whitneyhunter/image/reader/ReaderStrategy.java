package net.whitneyhunter.image.reader;

import java.io.File;

import net.whitneyhunter.image.ImageMetadata;

public interface ReaderStrategy {

    ImageMetadata read(File file) throws Exception;

}
