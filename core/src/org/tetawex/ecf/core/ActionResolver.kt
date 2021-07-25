package org.tetawex.ecf.core

/**
 * Created by Tetawex on 12.06.2017.
 */
interface ActionResolver {
    fun setBackPressedListener(listener: () -> Boolean)
    fun externalStorageAccessible(): Boolean
    fun openUrl(url: String)
}