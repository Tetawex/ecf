package org.tetawex.ecf.core

import org.tetawex.ecf.model.LevelData
import org.tetawex.ecf.util.BasicListener

/**
 * Created by Tetawex on 12.06.2017.
 */
class ActionResolverAdapter : ActionResolver {
    override fun setBackPressedListener(listener: () -> Unit) = Unit
    override fun externalStorageAccessible() = false
    override fun openUrl(url: String) = Unit
}
