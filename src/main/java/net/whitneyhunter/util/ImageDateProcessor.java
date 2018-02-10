package net.whitneyhunter.util;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.apache.commons.imaging.common.ImageMetadata.ImageMetadataItem;
import static org.apache.commons.imaging.formats.tiff.TiffImageMetadata.TiffMetadataItem;

public class ImageDateProcessor {

    private static final String CREATION_TIME_ATTRIBUTE = "creationTime";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
    private static final String DATE_TIME_ORIGINAL_KEY = "DateTimeOriginal";
    private static final String LAST_MODIFIED_TIME_ATTRIBUTE = "lastModifiedTime";

    /**
     * Static method only
     */
    private ImageDateProcessor() {
    }

    /**
     * Predicate that determines if the given {@link TiffMetadataItem} as a key or {@link #CREATION_TIME_ATTRIBUTE}
     */
    static final Predicate<TiffMetadataItem> IS_DATE_TIME_ORIGINAL = tiffMetadataItem ->
            DATE_TIME_ORIGINAL_KEY.equals(tiffMetadataItem.getKeyword());

    static final Function<TiffMetadataItem, Optional<String>> METADATA_ITEM_TEXT =
            tiffMetadataItem -> Optional.ofNullable(tiffMetadataItem)
                    .map(TiffMetadataItem::getText)
                    .map(date -> date.substring(1, date.length() - 1));

    static final Function<List<? extends ImageMetadataItem>, List<TiffMetadataItem>> FILTER_TIFF_METADATA_ITEMS =
            items -> items.stream()
                    .filter(item -> item instanceof TiffMetadataItem)
                    .map(TiffMetadataItem.class::cast)
                    .collect(Collectors.toList());

    static final Function<List<TiffMetadataItem>, Optional<String>> DATE_TIME_ORIGINAL_AS_STRING =
            items -> items.stream()
                    .filter(IS_DATE_TIME_ORIGINAL)
                    .findFirst()
                    .map(METADATA_ITEM_TEXT)
                    .orElse(Optional.empty());

    static final Function<File, Optional<LocalDateTime>> DATE_TIME_ORIGINAL =
            image -> Optional.ofNullable(image)
                    .map(ExceptionUtil.wrapper(Imaging::getMetadata))
                    .map(ImageMetadata::getItems)
                    .map(FILTER_TIFF_METADATA_ITEMS)
                    .flatMap(DATE_TIME_ORIGINAL_AS_STRING)
                    .map(date -> LocalDateTime.parse(date, DATE_TIME_FORMATTER));

    static final Function<File, Optional<LocalDateTime>> EARLIER_CREATION_OR_LAST_MODIFIED =
            image -> {
                try {
                    FileTime created = (FileTime) Files.getAttribute(image.toPath(), CREATION_TIME_ATTRIBUTE);
                    FileTime modified = (FileTime) Files.getAttribute(image.toPath(), LAST_MODIFIED_TIME_ATTRIBUTE);
                    FileTime toUse = created.compareTo(modified) < 0 ? created : modified;
                    return Optional.of(LocalDateTime.ofInstant(toUse.toInstant(), ZoneId.systemDefault()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };

    public static class ImageTagTimeStampFunction implements Function<File, LocalDateTime> {

        private final Function<File, Optional<LocalDateTime>> dateTimeOriginal;

        private final Function<File, Optional<LocalDateTime>> earlierCreationOrLastModified;

        public ImageTagTimeStampFunction(Function<File, Optional<LocalDateTime>> dateTimeOriginal,
                                         Function<File, Optional<LocalDateTime>> earlierCreationOrLastModified) {
            this.dateTimeOriginal = dateTimeOriginal;
            this.earlierCreationOrLastModified = earlierCreationOrLastModified;
        }

        @Override
        public LocalDateTime apply(File image) {
            return dateTimeOriginal.apply(image)
                    .orElseGet(() -> earlierCreationOrLastModified.apply(image)
                            .orElseThrow(() -> new RuntimeException("Unable to determine date")));
        }

    }

}
