package net.whitneyhunter.util;

import edu.emory.mathcs.backport.java.util.Collections;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.imaging.common.ImageMetadata.ImageMetadataItem;
import static org.apache.commons.imaging.formats.tiff.TiffImageMetadata.TiffMetadataItem;

/**
 * Provides static utility methods for handling {@link File}s
 */
public final class FileUtil {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");

    /**
     * Constructor. Never called. Static methods only.
     */
    private FileUtil() {
    }

    /**
     * Determine which is the earlier date between the files "creationTime" and its "lastModifiedTime".
     *
     * @param file The file from which the times should be taken.
     * @return The earlier of the two times.
     * @throws IOException Unable to read one or both times from the file.
     */
    public static LocalDateTime getEarlierDate(File file) throws IOException {
        FileTime created = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
        FileTime modified = (FileTime) Files.getAttribute(file.toPath(), "lastModifiedTime");
        FileTime toUse = created.compareTo(modified) < 0 ? created : modified;
        return LocalDateTime.ofInstant(toUse.toInstant(), ZoneId.systemDefault());
    }

    public static String getNormalizedSuffix(File file) {
        return getSuffix(file).toUpperCase();
    }

    public static String getSuffix(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    public static LocalDateTime getOriginalDate(File image) throws IOException, ImageReadException, ParseException {
        String dateString = null;

        ImageMetadata imageMetadata = Imaging.getMetadata(image);
        if (imageMetadata != null) {
            List<? extends ImageMetadataItem> metadataItems = imageMetadata.getItems();
            for (ImageMetadataItem metadataItem : metadataItems) {
                dateString = getDateTimeOriginal(metadataItem).orElse(null);
                if (dateString != null) {
                    break;
                }
            }
            if (dateString != null) {
                LocalDateTime originalDate = LocalDateTime.parse(dateString, DATE_TIME_FORMATTER);
                return originalDate;
            }
        } else {
            System.out.println("Unable to read EXIF for file: " + image);
        }
        LocalDateTime toUse = FileUtil.getEarlierDate(image);
        System.out.println("No EXIF date found. Using: " + toUse + " for file: " + image);
        return toUse;
    }

    static Optional<LocalDateTime> getDateTime(File image) throws IOException {
        List<ImageMetadataItem> items = Optional.ofNullable(image)
                .map(ExceptionUtil.wrapper(Imaging::getMetadata))
                .map(ImageMetadata::getItems)
                .orElse(Collections.emptyList());

        return items.stream()
                .map(FileUtil::getDateTimeOriginal)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .map(dateString -> LocalDateTime.parse(dateString, DATE_TIME_FORMATTER));
    }


    /**
     * Attempt to get the "DateTimeOriginal" value from an ImageMetadataItem. In order to do this, the ImageMetadataItem
     * must be a TiffMetadataItem. It also must be the "DateTimeOriginal" item.
     *
     * @param metadataItem The item to check
     * @return The original date/time if it can be determined.
     */
    static Optional<String> getDateTimeOriginal(ImageMetadataItem metadataItem) {
        return Optional.ofNullable(metadataItem)
                .filter(item -> item instanceof TiffMetadataItem)
                .map(TiffMetadataItem.class::cast)
                .filter(item -> "DateTimeOriginal".equals(item.getKeyword()))
                .map(TiffMetadataItem::getText)
                .map(date -> date.substring(1, date.length() - 1));
    }

}
