package net.whitneyhunter.image.writer;

import java.io.File;

public abstract class WriterStrategySupport implements WriterStrategy {

    private static WriterStrategy writerStrategy = new StandardWriterStrategy();

    public static WriterStrategy getInstance(File file) {
        return writerStrategy;
    }

}
