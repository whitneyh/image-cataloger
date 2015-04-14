package net.whitneyhunter.image.reader;

import java.io.File;

import net.whitneyhunter.image.ImageMetadata;

public class NefReaderStrategy extends ReaderStrategySupport {

    @Override
    public ImageMetadata read(File file) throws Exception {
        return new ImageMetadata(file, getOriginalDate(file));
    }

}
