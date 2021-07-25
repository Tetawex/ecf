package org.tetawex.ecf.core

/**
 * Created by Tetawex on 12.06.2017.
 */
class ActionResolverAdapter : ActionResolver {
    override fun setBackPressedListener(listener: (didHandle: Boolean) -> Unit) = Unit
    override fun externalStorageAccessible() = false
    override fun openUrl(url: String) = Unit
}
