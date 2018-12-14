package org.tetawex.ecf.util

/**
 * Created by tetawex on 08.08.17.
 */
interface ConsumerListener<T> {
    fun call(arg: T)
}
