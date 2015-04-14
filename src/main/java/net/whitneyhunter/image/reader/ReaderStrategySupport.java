package net.whitneyhunter.image.reader;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.whitneyhunter.image.FileUtil;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.ImageMetadata.ImageMetadataItem;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;

public abstract class ReaderStrategySupport implements ReaderStrategy {

    private static Map<String, ReaderStrategy> readerStrategyMap = new HashMap<>();

    static {
        readerStrategyMap.put("NEF", new NefReaderStrategy());
        ReaderStrategy jpegReaderStrategy = new JpegReaderStrategy();
        readerStrategyMap.put("jpeg", jpegReaderStrategy);
        readerStrategyMap.put("jpg", jpegReaderStrategy);
    }

    public static ReaderStrategy getInstance(File file) {
        String suffix = FileUtil.getSuffix(file);
        return readerStrategyMap.get(suffix);
    }

    protected LocalDateTime getOriginalDate(File image) throws IOException,
            ImageReadException, ParseException {
        String dateString = null;

        ImageMetadata imageMetadata = Imaging.getMetadata(image);
        if (imageMetadata != null) {
            List<? extends ImageMetadataItem> metadataItems = imageMetadata
                    .getItems();
            for (ImageMetadataItem metadataItem : metadataItems) {
                dateString = doGetOriginalDate(metadataItem);
                if (dateString != null) {
                    break;
                }
            }
            if (dateString != null) {
                LocalDateTime originalDate = LocalDateTime.parse(dateString,
                        DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss"));
                return originalDate;
            }
        }
        return null;
    }

    private String doGetOriginalDate(ImageMetadataItem metadataItem) {
        TiffImageMetadata.TiffMetadataItem tiffMetadata = (TiffImageMetadata.TiffMetadataItem) metadataItem;
        if ("DateTimeOriginal".equals(tiffMetadata.getKeyword())) {
            String dateString = tiffMetadata.getText();
            dateString = dateString.substring(1, dateString.length() - 1);
            return dateString;
        }
        return null;
    }

}
