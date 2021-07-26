package org.tetawex.ecf.core

class ActionResolverAdapter : ActionResolver {
    override fun setBackPressedListener(listener: () -> Boolean) =Unit
    override fun externalStorageAccessible() = false
    override fun openUrl(url: String) = Unit
}
