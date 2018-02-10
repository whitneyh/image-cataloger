package net.whitneyhunter.util;

import org.junit.Test;

import java.util.Optional;

import static org.apache.commons.imaging.formats.tiff.TiffImageMetadata.TiffMetadataItem;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ImageDateProcessorTest {

    @Test
    public void test_IS_DATE_TIME_ORIGINAL_true() {
        // Setup the fixture
        TiffMetadataItem tiffMetadataItem = mock(TiffMetadataItem.class);
        when(tiffMetadataItem.getKeyword()).thenReturn("DateTimeOriginal");

        // Execute the SUT
        boolean result = ImageDateProcessor.IS_DATE_TIME_ORIGINAL.test(tiffMetadataItem);

        // Validation
        assertTrue(result);
    }

    @Test
    public void test_IS_DATE_TIME_ORIGINAL_false() {
        // Setup the fixture
        TiffMetadataItem tiffMetadataItem = mock(TiffMetadataItem.class);
        when(tiffMetadataItem.getKeyword()).thenReturn("Other");

        // Execute the SUT
        boolean result = ImageDateProcessor.IS_DATE_TIME_ORIGINAL.test(tiffMetadataItem);

        // Validation
        assertFalse(result);
    }

    @Test
    public void test_METADATA_ITEM_TEXT() {
        // Setup the fixture
        TiffMetadataItem tiffMetadataItem = mock(TiffMetadataItem.class);
        when(tiffMetadataItem.getText()).thenReturn("\"item\"");

        // Execute the SUT
        Optional<String> result = ImageDateProcessor.METADATA_ITEM_TEXT.apply(tiffMetadataItem);

        // Validation
        assertTrue(result.isPresent());
        assertThat(result.get(), equalTo("item"));
    }

}
