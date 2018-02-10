package net.whitneyhunter.image.reader;

import java.io.File;

import net.whitneyhunter.util.FileUtil;
import net.whitneyhunter.image.ImageMetadata;

public class VideoReaderStrategy implements ReaderStrategy {

    @Override
    public ImageMetadata read(File file) throws Exception {
        return new ImageMetadata(file, FileUtil.getEarlierDate(file));
    }

}
