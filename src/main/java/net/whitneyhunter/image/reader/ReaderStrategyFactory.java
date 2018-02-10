package net.whitneyhunter.image.reader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.whitneyhunter.util.FileUtil;

public abstract class ReaderStrategyFactory {

    private final static Map<String, ReaderStrategy> readerStrategyMap = new HashMap<>();

    static {
        readerStrategyMap.put("NEF", new NefReaderStrategy());
        ReaderStrategy jpegReaderStrategy = new JpegReaderStrategy();
        readerStrategyMap.put("JPEG", jpegReaderStrategy);
        readerStrategyMap.put("JPG", jpegReaderStrategy);
        ReaderStrategy videoReaderStrategy = new VideoReaderStrategy();
        readerStrategyMap.put("MP4", videoReaderStrategy);
        readerStrategyMap.put("M4V", videoReaderStrategy);
        readerStrategyMap.put("AVI", videoReaderStrategy);
        readerStrategyMap.put("MOV", videoReaderStrategy);
        readerStrategyMap.put("3GP", videoReaderStrategy);
    }

    public static ReaderStrategy getFor(File file) {
        String suffix = FileUtil.getNormalizedSuffix(file);
        return readerStrategyMap.get(suffix);
    }

}
