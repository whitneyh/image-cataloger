package net.whitneyhunter.image;

import org.junit.Test;

import static org.apache.commons.imaging.common.ImageMetadata.ImageMetadataItem;
import static org.apache.commons.imaging.formats.tiff.TiffImageMetadata.TiffMetadataItem;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileUtilTest {

    @Test
    public void testDoGetOriginalDate_happy() {
        // Setup the fixture
        TiffMetadataItem tiffMetadataItem = mock(TiffMetadataItem.class);
        when(tiffMetadataItem.getKeyword()).thenReturn("DateTimeOriginal");
        when(tiffMetadataItem.getText()).thenReturn("\"abc\"");

        // Execute the SUT
        String result = FileUtil.doGetOriginalDate(tiffMetadataItem);

        // Validation
        assertThat(result, equalTo("abc"));
    }

    @Test
    public void testDoGetOriginalDate_invalidType() {
        // Setup the fixture
        ImageMetadataItem tiffMetadataItem = mock(ImageMetadataItem.class);

        // Execute the SUT
        String result = FileUtil.doGetOriginalDate(tiffMetadataItem);

        // Validation
        assertThat(result, equalTo(null));
    }

    @Test
    public void testDoGetOriginalDate_wrongKeyword() {
        // Setup the fixture
        TiffMetadataItem tiffMetadataItem = mock(TiffMetadataItem.class);
        when(tiffMetadataItem.getKeyword()).thenReturn("WrongKeyword");

        // Execute the SUT
        String result = FileUtil.doGetOriginalDate(tiffMetadataItem);

        // Validation
        assertThat(result, equalTo(null));
    }

}
