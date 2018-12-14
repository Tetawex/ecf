package org.tetawex.ecf.util

import com.google.gson.Gson

/**
 * Created by tetawex on 16.08.17.
 */
object JsonUtils {
    private var gson: Gson? = null

    val serializer: Gson
        get() {
            if (gson == null)
                gson = Gson()
            return gson!!
        }
}
