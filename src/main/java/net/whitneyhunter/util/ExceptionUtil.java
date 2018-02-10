package net.whitneyhunter.util;

import java.util.function.Function;

public final class ExceptionUtil {

    private ExceptionUtil() {
    }

    public static <T, R, E extends Exception> Function<T, R> wrapper(FunctionWithException<T, R , E> fe) {
        return arg -> {
            try {
                return fe.apply(arg);
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

}
