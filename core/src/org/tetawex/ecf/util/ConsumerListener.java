package org.tetawex.ecf.util;

/**
 * Created by tetawex on 08.08.17.
 */
public interface ConsumerListener<T> {
    void call(T arg);
}
