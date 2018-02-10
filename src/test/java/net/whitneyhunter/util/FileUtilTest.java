package net.whitneyhunter.util;

import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.apache.commons.imaging.common.ImageMetadata.ImageMetadataItem;
import static org.apache.commons.imaging.formats.tiff.TiffImageMetadata.TiffMetadataItem;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileUtilTest {

    @Test
    public void testGetOriginalDate() {
        // Setup the fixture


        // Execute the SUT

        // Validation
    }

    @Test
    public void testGetDateTimeOriginal_happy() {
        // Setup the fixture
        TiffMetadataItem tiffMetadataItem = mock(TiffMetadataItem.class);
        when(tiffMetadataItem.getKeyword()).thenReturn("DateTimeOriginal");
        when(tiffMetadataItem.getText()).thenReturn("\"abc\"");

        // Execute the SUT
        Optional<String> result = FileUtil.getDateTimeOriginal(tiffMetadataItem);

        // Validation
        assertTrue(result.isPresent());
        assertThat(result.get(), equalTo("abc"));
    }

    @Test
    public void testGetDateTimeOriginal_invalidType() {
        // Setup the fixture
        ImageMetadataItem tiffMetadataItem = mock(ImageMetadataItem.class);

        // Execute the SUT
        Optional<String> result = FileUtil.getDateTimeOriginal(tiffMetadataItem);

        // Validation
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetDateTimeOriginal_wrongKeyword() {
        // Setup the fixture
        TiffMetadataItem tiffMetadataItem = mock(TiffMetadataItem.class);
        when(tiffMetadataItem.getKeyword()).thenReturn("WrongKeyword");

        // Execute the SUT
        Optional<String> result = FileUtil.getDateTimeOriginal(tiffMetadataItem);

        // Validation
        assertFalse(result.isPresent());
    }

}
