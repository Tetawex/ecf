package org.tetawex.ecf.util

import java.util.HashMap

/**
 * Created by tetawex on 28.01.17.
 */
class Bundle {
    private val items: MutableMap<String, Any>

    init {
        items = HashMap()
    }

    fun putItem(key: String, item: Any) {
        items[key] = item
    }

    fun <T> getItem(key: String, c: Class<T>): T? {
        return c.cast(items[key])
    }

    fun <T> getItem(key: String, c: Class<T>, defaultValue: T): T? {
        return if (items.containsKey(key)) c.cast(items[key]) else defaultValue
    }
}
