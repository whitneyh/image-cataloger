package net.whitneyhunter.image.writer;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class StandardWriterStrategyTest {

    @Test
    public void testCalculateFileBase() {
        // Setup the fixture
        LocalDateTime localDateTime = LocalDateTime.parse("1966-05-16T00:00:00");

        StandardWriterStrategy standardWriterStrategy = new StandardWriterStrategy();

        // Execute the SUT
        String result = standardWriterStrategy.calculateFileBase(localDateTime);

        // Validation
        assertThat(result, equalTo("19660516"));
    }

}
